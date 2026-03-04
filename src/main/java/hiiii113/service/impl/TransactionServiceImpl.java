package hiiii113.service.impl;

import hiiii113.dao.TransactionRecordDao;
import hiiii113.dao.UserDao;
import hiiii113.dao.impl.TransactionRecordDaoImpl;
import hiiii113.dao.impl.UserDaoImpl;
import hiiii113.entity.TransactionRecord;
import hiiii113.service.TransactionService;
import hiiii113.util.Result;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionServiceImpl implements TransactionService
{
    @Override
    public List<TransactionRecord> getTransactionRecord(Integer userId)
    {
        TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
        // 调用dao层方法获得list
        return transactionRecordDao.getTransactionRecord(userId);
    }

    @Override
    public Result deposit(Integer userId, BigDecimal amount)
    {
        try
        {
            UserDao userDao = new UserDaoImpl();
            TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
            // 获取账户余额
            BigDecimal previousBalance = userDao.getBalanceById(userId);
            // 计算出新的账户余额
            BigDecimal newBalance = previousBalance.add(amount);
            // 修改账户余额
            userDao.modifyBalanceById(userId, newBalance);
            // 添加存款流水记录
            transactionRecordDao.addTransaction(userId, 1, amount, null, newBalance);
            // 返回修改后的余额
            return Result.success(userDao.getBalanceById(userId));
        }
        catch (Exception e)
        {
            return Result.fail("数据库写入异常！");
        }
    }

    @Override
    public Result withdraw(Integer userId, BigDecimal amount)
    {
        try
        {
            UserDao userDao = new UserDaoImpl();
            TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
            // 获取账户余额
            BigDecimal previousBalance = userDao.getBalanceById(userId);
            // 计算出新的账户余额
            BigDecimal newBalance = previousBalance.subtract(amount);
            // 修改账户余额
            userDao.modifyBalanceById(userId, newBalance);
            // 添加存款流水记录
            transactionRecordDao.addTransaction(userId, 2, amount, null, newBalance);
            // 返回修改后的余额
            return Result.success(userDao.getBalanceById(userId));
        }
        catch (Exception e)
        {
            return Result.fail("数据库写入异常！");
        }
    }

    @Override
    public Result transfer(Integer userId, Integer targetUserId, BigDecimal amount)
    {
        try
        {
            UserDao userDao = new UserDaoImpl();
            TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
            // 获取双方账户余额
            BigDecimal userBalance = userDao.getBalanceById(userId);
            BigDecimal targetUserBalance = userDao.getBalanceById(targetUserId);
            // 计算出双方新的账户余额
            BigDecimal newUserBalance = userBalance.subtract(amount);
            BigDecimal newTargetUserBalance = targetUserBalance.add(amount);
            // 修改账户余额
            userDao.modifyBalanceById(userId, newUserBalance);
            userDao.modifyBalanceById(targetUserId, newTargetUserBalance);
            // 添加一条交易流水
            transactionRecordDao.addTransaction(userId, 3, amount, targetUserId, newUserBalance);
            transactionRecordDao.addTransaction(targetUserId, 4, amount, userId, newTargetUserBalance);
            // 返回修改后的余额
            return Result.success(userDao.getBalanceById(userId));
        }
        catch (Exception e)
        {
            return Result.fail("数据库写入异常！");
        }
    }

    @Override
    public String formatTransactionRecord(TransactionRecord transactionRecord)
    {
        // 时间记录
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = '[' + transactionRecord.getCreateTime().format(formatter) + ']';
        // 流水类型
        String type = "";
        switch (transactionRecord.getType())
        {
            case 1:
                type = "存款";
                break;
            case 2:
                type = "取款";
                break;
            case 3:
                type = "转出";
                break;
            case 4:
                type = "转入";
                break;
        }
        // 金额变化
        String amount = transactionRecord.getAmount().toString();
        if (transactionRecord.getType() == 1 || transactionRecord.getType() == 4)
        {
            amount = '+' + amount;
        }
        else
        {
            amount = '-' + amount;
        }
        // 余额
        String newBalance = transactionRecord.getNewBalance().toString();
        if (transactionRecord.getType() == 1 || transactionRecord.getType() == 2)
        {
            return createTime + ' ' + type + ' ' + amount + ' ' + "余额: " + newBalance;
        }
        else
        {
            // 对方名字
            UserDao userDao = new UserDaoImpl();
            String targetUsername = userDao.getUsernameById(transactionRecord.getTargetUserId());
            return createTime + ' ' + type + ' ' + amount + ' ' + "对方: " + targetUsername + ' ' + "余额: " + newBalance;
        }

    }
}

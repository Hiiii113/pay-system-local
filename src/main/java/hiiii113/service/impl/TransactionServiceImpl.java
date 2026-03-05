package hiiii113.service.impl;

import hiiii113.dao.TransactionRecordDao;
import hiiii113.dao.UserDao;
import hiiii113.dao.impl.TransactionRecordDaoImpl;
import hiiii113.dao.impl.UserDaoImpl;
import hiiii113.entity.TransactionRecord;
import hiiii113.entity.User;
import hiiii113.exception.BusinessException;
import hiiii113.service.TransactionService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class TransactionServiceImpl implements TransactionService
{
    @Override
    public List<TransactionRecord> getTransactionRecord(Integer userId) throws SQLException
    {
        TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
        // 调用dao层方法获得list
        return transactionRecordDao.getTransactionRecord(userId);
    }

    @Override
    public void deposit(Integer userId, BigDecimal amount) throws SQLException
    {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new BusinessException("金额必须大于0！");
        }
        UserDao userDao = new UserDaoImpl();
        TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
        // 获取用户对象
        User user = userDao.getUserById(userId);
        if (user == null)
        {
            throw new BusinessException("数据库出现问题!");
        }
        // 获取账户余额
        BigDecimal previousBalance = user.getBalance();
        // 计算出新的账户余额
        BigDecimal newBalance = previousBalance.add(amount);
        // 修改账户余额
        int balanceInfectedRows = userDao.modifyBalanceById(userId, newBalance);
        if (balanceInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
        // 添加存款流水记录
        int transactionInfectedRows = transactionRecordDao.addTransaction(userId, 1, amount, null, newBalance);
        if (transactionInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
    }

    @Override
    public void withdraw(Integer userId, BigDecimal amount) throws SQLException
    {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new BusinessException("金额必须大于0！");
        }
        UserDao userDao = new UserDaoImpl();
        TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
        // 获取用户对象
        User user = userDao.getUserById(userId);
        if (user == null)
        {
            throw new BusinessException("数据库出现问题!");
        }
        // 获取账户余额
        BigDecimal previousBalance = user.getBalance();
        // 计算出新的账户余额
        BigDecimal newBalance = previousBalance.subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException("余额不足！");
        }
        // 修改账户余额
        int balanceInfectedRows = userDao.modifyBalanceById(userId, newBalance);
        if (balanceInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
        // 添加存款流水记录
        int transactionInfectedRows = transactionRecordDao.addTransaction(userId, 2, amount, null, newBalance);
        if (transactionInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
    }

    @Override
    public void transfer(Integer userId, Integer targetUserId, BigDecimal amount) throws SQLException
    {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
        {
            throw new BusinessException("交易金额必须大于0！");
        }
        UserDao userDao = new UserDaoImpl();
        TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();
        // 获取双方用户对象
        User user = userDao.getUserById(userId);
        User targetUser = userDao.getUserById(targetUserId);
        if (user == null)
        {
            throw new BusinessException("数据库出现问题!");
        }
        else if (targetUser == null)
        {
            throw new BusinessException("对方账户不存在!");
        }
        else if (Objects.equals(user.getId(), targetUser.getId()))
        {
            throw new BusinessException("不能给自己转账！");
        }
        // 获取双方账户余额
        BigDecimal userBalance = user.getBalance();
        BigDecimal targetUserBalance = targetUser.getBalance();
        // 计算出双方新的账户余额
        BigDecimal newUserBalance = userBalance.subtract(amount);
        BigDecimal newTargetUserBalance = targetUserBalance.add(amount);
        if (newUserBalance.compareTo(BigDecimal.ZERO) < 0)
        {
            throw new BusinessException("余额不足！");
        }
        // 修改账户余额
        int userBalanceInfectedRows = userDao.modifyBalanceById(userId, newUserBalance);
        int targetUserBalanceInfectedRows = userDao.modifyBalanceById(targetUserId, newTargetUserBalance);
        if (userBalanceInfectedRows == 0 || targetUserBalanceInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
        // 添加一条交易流水
        int userTransactionInfectedRows = transactionRecordDao.addTransaction(userId, 3, amount, targetUserId, newUserBalance);
        int targetUserTransactionInfectedRows = transactionRecordDao.addTransaction(targetUserId, 4, amount, userId, newTargetUserBalance);
        if (userTransactionInfectedRows == 0 || targetUserTransactionInfectedRows == 0)
        {
            throw new BusinessException("数据库出现问题!");
        }
    }

    @Override
    public String formatTransactionRecord(TransactionRecord transactionRecord) throws SQLException
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
            String targetUsername = userDao.getUserById(transactionRecord.getTargetUserId()).getUsername();
            return createTime + ' ' + type + ' ' + amount + ' ' + "对方: " + targetUsername + ' ' + "余额: " + newBalance;
        }

    }
}

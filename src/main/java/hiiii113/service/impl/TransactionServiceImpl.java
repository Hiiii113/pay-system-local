package hiiii113.service.impl;

import hiiii113.dao.TransactionRecordDao;
import hiiii113.dao.UserDao;
import hiiii113.dao.impl.TransactionRecordDaoImpl;
import hiiii113.dao.impl.UserDaoImpl;
import hiiii113.entity.TransactionRecord;
import hiiii113.entity.User;
import hiiii113.exception.BusinessException;
import hiiii113.service.TransactionService;
import hiiii113.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class TransactionServiceImpl implements TransactionService
{
    private final UserDao userDao = new UserDaoImpl();
    private final TransactionRecordDao transactionRecordDao = new TransactionRecordDaoImpl();

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
        // 开启事务
        Connection conn = DBUtil.getConnection();
        try
        {
            // 设置自动提交为false
            conn.setAutoCommit(false);
            // 修改账户余额
            int balanceInfectedRows = userDao.modifyBalanceById(conn, userId, newBalance);
            if (balanceInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }
            // 添加存款流水记录
            int transactionInfectedRows = transactionRecordDao.addTransaction(conn, userId, 1, amount, null, newBalance);
            if (transactionInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }

            // 都成功，没有报错的，那就提交
            conn.commit();
        }
        catch (Exception e)
        {
            // 所有的可能导致事务执行失败的，都需要回滚
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException rollbackEx)
                {
                    rollbackEx.printStackTrace();
                }
            }
            // 重新抛出异常，让controller提示用户
            throw new RuntimeException("存款失败！", e);
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    // 恢复默认提交为true
                    conn.setAutoCommit(true);
                    // 关闭连接
                    conn.close();
                }
                catch (SQLException closeEx)
                {
                    closeEx.printStackTrace();
                }
            }
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
        // 开启事务
        Connection conn = DBUtil.getConnection();
        try
        {
            // 设置自动提交为false
            conn.setAutoCommit(false);
            // 修改账户余额
            int balanceInfectedRows = userDao.modifyBalanceById(conn, userId, newBalance);
            if (balanceInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }
            // 添加存款流水记录
            int transactionInfectedRows = transactionRecordDao.addTransaction(conn, userId, 2, amount, null, newBalance);
            if (transactionInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }
            // 没有报错则提交
            conn.commit();
        }
        catch (Exception e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException rollbackEx)
                {
                    rollbackEx.printStackTrace();
                }
            }
            // 重新抛出异常，让controller提示用户
            throw new RuntimeException("存款失败！", e);
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    // 恢复自动提交为true
                    conn.setAutoCommit(true);
                    // 关闭连接
                    conn.close();
                }
                catch (SQLException closeEx)
                {
                    closeEx.printStackTrace();
                }
            }
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
        Connection conn = DBUtil.getConnection();
        try
        {
            // 设置自动提交为false
            conn.setAutoCommit(false);
            // 修改账户余额
            int userBalanceInfectedRows = userDao.modifyBalanceById(conn, userId, newUserBalance);
            int targetUserBalanceInfectedRows = userDao.modifyBalanceById(conn, targetUserId, newTargetUserBalance);
            if (userBalanceInfectedRows == 0 || targetUserBalanceInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }
            // 添加一条交易流水
            int userTransactionInfectedRows = transactionRecordDao.addTransaction(conn, userId, 3, amount, targetUserId, newUserBalance);
            int targetUserTransactionInfectedRows = transactionRecordDao.addTransaction(conn, targetUserId, 4, amount, userId, newTargetUserBalance);
            if (userTransactionInfectedRows == 0 || targetUserTransactionInfectedRows == 0)
            {
                throw new BusinessException("数据库出现问题!");
            }

            // 没有报错则提交
            conn.commit();
        }
        catch (Exception e)
        {
            if (conn != null)
            {
                try
                {
                    // 回滚事务
                    conn.rollback();
                }
                catch (SQLException rollbackEx)
                {
                    rollbackEx.printStackTrace();
                }
            }
            // 重新抛出异常，让controller提示用户
            throw new RuntimeException("存款失败！", e);
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    // 恢复自动提交为true
                    conn.setAutoCommit(true);
                    // 关闭连接
                    conn.close();
                }
                catch (SQLException closeEx)
                {
                    closeEx.printStackTrace();
                }
            }
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

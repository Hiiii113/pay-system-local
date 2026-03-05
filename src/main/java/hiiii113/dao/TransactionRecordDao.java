package hiiii113.dao;

import hiiii113.entity.TransactionRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionRecordDao
{
    /**
     * dao层增加流水记录的方法
     *
     * @param conn         传入的连接，用于确保一个事务
     * @param userId       操作的用户id
     * @param type         交易类型(1. 存款 2. 取款 3. 转入 4. 转出)
     * @param amount       金额
     * @param targetUserId 目标id，存/取款为null
     */
    int addTransaction(Connection conn, Integer userId, Integer type, BigDecimal amount, Integer targetUserId, BigDecimal newBalance) throws SQLException;

    /**
     * 查找用户流水记录
     *
     * @param userId 需要查找的用户id
     * @return 获取一个list表，里面是TransactionRecord
     */
    List<TransactionRecord> getTransactionRecord(Integer userId) throws SQLException;
}

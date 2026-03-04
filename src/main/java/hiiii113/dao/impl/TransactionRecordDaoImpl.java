package hiiii113.dao.impl;

import hiiii113.dao.TransactionRecordDao;
import hiiii113.entity.TransactionRecord;
import hiiii113.exception.DataBaseWriteFailException;
import hiiii113.util.Result;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRecordDaoImpl implements TransactionRecordDao
{
    // 数据库相关信息
    private static final String URL = "jdbc:mysql://localhost:3306/qg_bank?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf8&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123";

    // 加载驱动
    static
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            throw new ExceptionInInitializerError("MySQL 驱动加载失败，请检查依赖！");
        }
    }

    // 添加一笔新的流水
    @Override
    public void addTransaction(Integer userId, Integer type, BigDecimal amount, Integer targetUserId, BigDecimal newBalance)
    {
        String sql = "insert into transaction_record(user_id,type,amount,target_user_id,newBalance) values(?,?,?,?,?)";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setInt(1, userId);
            ps.setInt(2, type);
            ps.setBigDecimal(3, amount);
            if (type == 3 || type == 4)
            {
                ps.setInt(4, targetUserId);
            }
            else
            {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setBigDecimal(5, newBalance);
            int infectedRows = ps.executeUpdate();
            if (infectedRows == 0)
            {
                throw new DataBaseWriteFailException("数据库写入失败！");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("交易失败，数据库异常", e);
        }
    }

    @Override
    public List<TransactionRecord> getTransactionRecord(Integer userId)
    {
        String sql = "select create_time, type, amount, target_user_id, newBalance from transaction_record where user_id = ?";
        List<TransactionRecord> transactionRecordList = new ArrayList<>();
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    TransactionRecord transactionRecord = new TransactionRecord();
                    transactionRecord.setCreateTime(rs.getTimestamp("create_time").toLocalDateTime());
                    transactionRecord.setType(rs.getInt("type"));
                    transactionRecord.setAmount(rs.getBigDecimal("amount"));
                    transactionRecord.setTargetUserId(rs.getInt("target_user_id"));
                    transactionRecord.setNewBalance(rs.getBigDecimal("newBalance"));
                    transactionRecordList.add(transactionRecord);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return transactionRecordList;
    }
}

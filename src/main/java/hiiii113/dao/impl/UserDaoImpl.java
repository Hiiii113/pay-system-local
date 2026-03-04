package hiiii113.dao.impl;

import hiiii113.dao.UserDao;
import hiiii113.entity.User;
import hiiii113.exception.DataBaseWriteFailException;
import hiiii113.util.Result;

import java.math.BigDecimal;
import java.sql.*;

public class UserDaoImpl implements UserDao
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

    // 添加用户（注册用）
    @Override
    public void addUser(User user)
    {
        // 设置sql命令
        String sql = "insert into user(username, password,balance) values(?,?,?)";
        // 连接数据库并预编译命令
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            // 设置值
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBigDecimal(3, user.getBalance());

            // 开始执行
            int infectedRows = ps.executeUpdate();
            if (infectedRows == 0)
            {
                throw new DataBaseWriteFailException("数据库写入异常！");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("注册失败，数据库异常", e);
        }
    }

    @Override
    public boolean findSameUser(String username)
    {
        String sql = "select count(*) as cnt from qg_bank.`user` where username = ?";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    int count = rs.getInt("cnt"); // 获取count(*)的结果
                    return count > 0;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("用户名查重失败：数据库异常", e);
        }
        return false;
    }

    @Override
    public Result loginConfirm(String username, String password)
    {
        // 设置sql命令
        String sql = "select count(*) as cnt, balance, id from qg_bank.`user` where username = ?  and password = ?";

        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    if (rs.getInt("cnt") == 1)
                    {
                        BigDecimal balance = rs.getBigDecimal("balance");
                        Integer id = rs.getInt("id");
                        User user = new User();
                        user.setBalance(balance);
                        user.setUsername(username);
                        user.setPassword(password);
                        user.setId(id);
                        return Result.success(user);
                    }
                    else
                    {
                        return Result.fail("用户名或密码错误！");
                    }
                }
            }
            return Result.fail();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("查询数据库失败，数据库异常", e);
        }
    }

    @Override
    public void modifyBalanceById(Integer userId, BigDecimal balance)
    {
        String sql = "update user set balance=? where id=? ";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setBigDecimal(1, balance);
            ps.setInt(2, userId);
            int infectedRows = ps.executeUpdate();
            if (infectedRows == 0)
            {
                throw new DataBaseWriteFailException("数据库写入异常！");
            }

        }
        catch (SQLException e)
        {
            throw new RuntimeException("修改账户余额失败，数据库异常");
        }
    }

    @Override
    public BigDecimal getBalanceById(Integer userId)
    {
        String sql = "select balance from user where id=?";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql)
        )
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getBigDecimal("balance");
                }
                else
                {
                    return null;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("查询余额失败，数据库异常", e);
        }
    }

    @Override
    public String getUsernameById(Integer id)
    {
        String sql = "select username from user where id=?";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql);
        )
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getString("username");
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

}

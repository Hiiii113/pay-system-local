package hiiii113.dao.impl;

import hiiii113.dao.UserDao;
import hiiii113.entity.User;

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
    public int addUser(User user) throws SQLException
    {
        // 设置sql命令
        String sql = "insert into user(username, password, balance) values(?,?,?)";
        // 连接数据库并预编译命令
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql))
        {
            // 设置值
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBigDecimal(3, user.getBalance());

            // 开始执行
            return ps.executeUpdate();
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException
    {
        String sql = "select id, username, password, balance from user where username = ?";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    int id = rs.getInt("id");
                    String password = rs.getString("password");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    return new User(id, username, password, balance);
                }
            }
        }
        // 未查询到的返回值
        return null;
    }

    @Override
    public int modifyBalanceById(Connection conn, Integer userId, BigDecimal balance) throws SQLException
    {
        String sql = "update user set balance = ? where id = ? ";
        try (PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setBigDecimal(1, balance);
            ps.setInt(2, userId);
            return ps.executeUpdate();
        }
    }

    @Override
    public User getUserById(Integer id) throws SQLException
    {
        String sql = "select id, username, password, balance from user where id = ?";
        try (
                Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                PreparedStatement ps = conn.prepareStatement(sql))
        {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    String username = rs.getString("username");
                    String password = rs.getString("password");
                    BigDecimal balance = rs.getBigDecimal("balance");
                    return new User(id, username, password, balance);
                }
            }
        }
        // 查询不到的返回结果
        return null;
    }

}

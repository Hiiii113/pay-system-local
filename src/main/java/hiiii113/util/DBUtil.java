package hiiii113.util;

import java.sql.*;

public class DBUtil
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

    public static Connection getConnection()
    {
        try
        {
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        catch (SQLException e)
        {
            throw new RuntimeException("获取数据库连接失败", e);
        }
    }
}
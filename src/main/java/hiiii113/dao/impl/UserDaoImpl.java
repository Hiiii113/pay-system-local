package hiiii113.dao.impl;

import hiiii113.dao.UserDao;
import hiiii113.entity.User;
import hiiii113.util.Result;

import java.sql.*;

public class UserDaoImpl implements UserDao
{
    private static final String url = "jdbc:mysql://localhost:3306/qg_bank?serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf8";
    private static final String username = "root";
    private static final String password = "123";

    @Override
    public Result addUser(User user)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try
        {
            // 获取数据库连接
            conn = DriverManager.getConnection(url, username, password);
            // 预编译sql命令
            String sql = "insert into qg_bank.user(username,password,balance) values(?,?,?)";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setBigDecimal(3, user.getBalance());

            // 开始执行
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0)
            {
                return Result.success();
            }
            else
            {
                return Result.fail();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("注册失败：数据库异常", e);
        }
        finally
        {
            try
            {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Result findSameUser(String username)
    {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try
        {
            // 连接数据库
            conn = DriverManager.getConnection(url, this.username, this.password);

            // 预编译sql命令
            String sql = "select count(*) from qg_bank.`user` where username = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username); // 传入要检查的用户名

            // 4. 执行查询，处理结果
            rs = pstmt.executeQuery();
            if (rs.next())
            {
                int count = rs.getInt(1); // 获取count(*)的结果
                if (count > 0)
                {
                    return Result.success();
                }
                else
                {
                    return Result.fail();
                }
            }
            return Result.fail();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("用户名查重失败：数据库异常", e);
        }
        finally
        {
            try
            {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}

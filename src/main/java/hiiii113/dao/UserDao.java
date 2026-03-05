package hiiii113.dao;

import hiiii113.entity.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public interface UserDao
{
    /**
     * 添加一条用户
     * @param user 用户相关信息
     */
    int addUser(User user) throws SQLException;

    /**
     * 根据用户名查找用户
     * @param username 需要查找的用户名
     * @return 返回查找到的用户对象
     */
    User getUserByUsername(String username) throws SQLException;

    /**
     * 用于修改用户的余额
     *
     * @param conn 传入的连接，用于确保一个事务
     * @param userId  用户的id
     * @param balance 修改完之后用户的余额
     */
    int modifyBalanceById(Connection conn, Integer userId, BigDecimal balance) throws SQLException;

    /**
     * 通过id查找用户
     *
     * @param id 需要查询的用户id
     * @return 返回用户对象
     */
    User getUserById(Integer id) throws SQLException;
}

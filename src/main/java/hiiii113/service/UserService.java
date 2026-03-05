package hiiii113.service;

import hiiii113.entity.User;

import java.sql.SQLException;

public interface UserService
{
    /**
     * 注册用函数
     * @param username 前端返回的用户名
     * @param password 前端返回的用户密码
     */
    void Register(String username, String password) throws SQLException;

    /**
     * 登录用函数
     *
     * @param username 前端返回的用户名
     * @param password 前端返回的用户密码
     * @return 返回是否登录成功和用户信息
     */
    boolean Login(String username, String password) throws SQLException;

    /**
     * 通过用户名获取用户对象
     * @param username 传入的用户名
     * @return 返回用户对象
     */
    User getUserByUsername(String username) throws SQLException;

    /**
     * 通过id获取用户对象
     * @param id 传入的用户id
     * @return 返回用户对象
     */
    User getUserById(int id) throws SQLException;
}

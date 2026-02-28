package hiiii113.service;

import hiiii113.util.Result;

public interface UserService
{
    /**
     * 注册用函数
     * @param username 前端返回的用户名
     * @param password 前端返回的用户密码
     * @return 返回是否注册成功的信息
     */
    public Result Register(String username, String password);

    /**
     * 登录用函数
     * @param username 前端返回的用户名
     * @param password 前端返回的用户密码
     * @return 返回是否登录成功和用户信息
     */
    public Result Login(String username, String password);

    /**
     * 查重用户名
     *
     * @param username 输入的用户名参数
     * @return 返回是否重复
     */
    public boolean findSameUsername(String username);
}

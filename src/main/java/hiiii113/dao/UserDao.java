package hiiii113.dao;

import hiiii113.entity.User;
import hiiii113.util.Result;

public interface UserDao
{
    /**
     * 添加一条用户
     * @param user 用户相关信息
     * @return 返回是否添加成功
     */
    public Result addUser(User user);

    /**
     * 查询user表中所有的
     * @param username
     * @return
     */
    public Result findSameUser(String username);
}

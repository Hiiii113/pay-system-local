package hiiii113.service.impl;

import hiiii113.dao.UserDao;
import hiiii113.dao.impl.UserDaoImpl;
import hiiii113.entity.User;
import hiiii113.service.UserService;
import hiiii113.util.Result;

public class UserServiceImpl implements UserService
{

    @Override
    public Result Register(String username, String password)
    {
        UserDao userDao = new UserDaoImpl();
        // 调用函数查找
        boolean haveSameUsername = userDao.findSameUser(username);
        if (haveSameUsername) // 找到了相应的用户名
        {
            return Result.fail("用户名已存在！"); // 注册失败
        }
        else
        {
            // 调用addUser方法
            userDao.addUser(new User(username, password));
            return Result.success("注册成功！");
        }
    }

    @Override
    public Result Login(String username, String password)
    {
        UserDao userDao = new UserDaoImpl();
        // 调用dao层方法查找是否存在对应的用户和密码
        return userDao.loginConfirm(username, password);
    }
}

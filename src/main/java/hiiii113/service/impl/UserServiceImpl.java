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
        boolean haveSameUsername = findSameUsername(username);
        if (haveSameUsername)
        {
            return Result.fail("用户名已存在！");
        }
        else
        {
            UserDao userDao = new UserDaoImpl();
            boolean addSuccess = userDao.addUser(new User(username, password)).isSuccess();
            if (addSuccess)
            {
                return Result.success("注册成功！");
            }
            else
            {
                return Result.fail("注册失败");
            }
        }
    }

    @Override
    public Result Login(String username, String password)
    {
        return null;
    }

    @Override
    public boolean findSameUsername(String username)
    {
        UserDao userDao = new UserDaoImpl();
        return userDao.findSameUser(username).isSuccess();
    }
}

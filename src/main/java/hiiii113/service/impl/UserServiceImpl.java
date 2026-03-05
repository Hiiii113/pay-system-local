package hiiii113.service.impl;

import hiiii113.dao.UserDao;
import hiiii113.dao.impl.UserDaoImpl;
import hiiii113.entity.User;
import hiiii113.exception.BusinessException;
import hiiii113.service.UserService;

import java.sql.SQLException;

public class UserServiceImpl implements UserService
{

    @Override
    public void Register(String username, String password) throws SQLException
    {
        UserDao userDao = new UserDaoImpl();
        // 调用函数查找
        User user = userDao.getUserByUsername(username);
        // 如果返回了对应的用户对象，就说明用户名重复了
        if (user != null)
        {
            throw new BusinessException("用户名已存在！");
        }
        else
        {
            int infectedRows = userDao.addUser(new User(username, password));
            if (infectedRows == 0)
            {
                throw new BusinessException("添加失败！");
            }
        }
    }

    @Override
    public boolean Login(String username, String password) throws SQLException
    {
        UserDao userDao = new UserDaoImpl();
        // 调用dao层方法获取用户对象并检查用户密码
        User user = userDao.getUserByUsername(username);
        if (user == null)
        {
            throw new SQLException("用户未注册！");
        }
        else
        {
            return user.getPassword().equals(password);
        }
    }

    @Override
    public User getUserByUsername(String username) throws SQLException
    {
        UserDao userDao = new UserDaoImpl();
        return userDao.getUserByUsername(username);
    }

    @Override
    public User getUserById(int id) throws SQLException
    {
        UserDao userDao = new UserDaoImpl();
        return userDao.getUserById(id);
    }
}

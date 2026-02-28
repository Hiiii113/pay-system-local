package hiiii113.dao.impl;

import hiiii113.dao.UserDao;
import hiiii113.entity.User;
import hiiii113.util.Result;
import org.junit.Test;

import java.math.BigDecimal;

public class UserDaoImplTest
{
    private UserDao userDao = new UserDaoImpl();

    /**
     * 测试添加用户功能(dao层)
     */
    @Test
    public void testAddUser()
    {
        // 新建一个User对象
        User testUser = new User();
        testUser.setUsername("hiiii113");
        testUser.setPassword("123456");
        testUser.setBalance(new BigDecimal("0.00"));

        // 调用DAO的addUser方法
        Result result = userDao.addUser(testUser);

        // 打印结果，判断逻辑是否正确
        System.out.println("DAO插入用户测试结果：");
        if (result.isSuccess())
        {
            System.out.println("✅ 插入成功！");
        }
        else
        {
            System.out.println("❌ 插入失败！");
        }
    }
}

package hiiii113.service.impl;

import hiiii113.entity.User;
import hiiii113.service.UserService;
import hiiii113.util.Result;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;

public class UserServiceImplTest
{
    @Test
    public void testAddUser()
    {
        UserService userService = new UserServiceImpl();
        String username = "hiiii113";
        String password = "123456";
        Result result = userService.Register(username, password);
        Assertions.assertTrue(result.isSuccess());
    }
}

package hiiii113.dao;

import hiiii113.entity.User;
import hiiii113.util.Result;

import java.math.BigDecimal;

public interface UserDao
{
    /**
     * 添加一条用户
     * @param user 用户相关信息
     */
    void addUser(User user);

    /**
     * 查询user表中是否存在该用户名
     * @param username 需要查找的用户名
     * @return 返回是否查找到
     */
    boolean findSameUser(String username);

    /**
     * 查找数据库中是否存在对应的用户名和密码
     * @param username 传入的用户名
     * @param password 传入的密码
     * @return 返回一个布尔值代表是否存在对应的账户
     */
    Result loginConfirm(String username, String password);

    /**
     * 用于修改用户的余额
     * @param userId 用户的id
     * @param balance 修改完之后用户的余额
     */
    void modifyBalanceById(Integer userId, BigDecimal balance);

    /**
     * 获取对应用户id的余额
     *
     * @param userId 传入的用户id
     * @return 返回的BigDecimal放在Result的data里面
     */
    BigDecimal getBalanceById(Integer userId);

    /**
     * 通过id获取用户名
     * @param id 需要查询的用户id
     * @return 返回用户名
     */
    String getUsernameById(Integer id);
}

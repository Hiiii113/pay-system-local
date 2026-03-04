package hiiii113.menu;

import java.math.BigDecimal;

public class Menu
{
    /**
     * 登录前菜单
     */
    public static void preLoginMenu()
    {
        System.out.println("=======  QG申必小金库 =======");
        System.out.println("1. 登录");
        System.out.println("2. 注册");
        System.out.print("请选择对应功能的序号: ");
    }

    /**
     * 主菜单
     */
    public static void mainMenu(String username, BigDecimal balance)
    {
        System.out.println("======= 💰QG申必小金库💰 =======");
        System.out.println("当前用户：" + username + "  余额：¥" + balance);
        System.out.println("--------------------------------");
        System.out.println("1. 存款（上交保护费预备）");
        System.out.println("2. 取款（卷钱跑路）");
        System.out.println("3. 转账（上交保护费）");
        System.out.println("4. 查看交易流水");
        System.out.println("5. 退出系统");
        System.out.print("请选择操作：");
    }
}

package hiiii113.controller;

import hiiii113.entity.TransactionRecord;
import hiiii113.entity.User;
import hiiii113.exception.BusinessException;
import hiiii113.menu.Menu;
import hiiii113.service.TransactionService;
import hiiii113.service.UserService;
import hiiii113.service.impl.TransactionServiceImpl;
import hiiii113.service.impl.UserServiceImpl;
import hiiii113.util.GenerateCaptcha;
import hiiii113.util.Input;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Controller
{
    public static void main(String[] args)
    {
        // 准备
        Scanner in = new Scanner(System.in);
        UserService userService = new UserServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl();

        // 记录当前用户信息
        User user;

        // 循环（登录前菜单）
        while (true)
        {

            // 登录前菜单
            Menu.preLoginMenu();
            int loginChoice = Integer.parseInt(in.nextLine());
            try
            {
                // 登录菜单
                if (loginChoice == 1)
                {
                    // 输出登录菜单并读入用户名，密码
                    System.out.println("=======  QG申必小金库 =======");
                    System.out.println("            登录            ");
                    System.out.println("请输入用户名: ");
                    String username = in.nextLine();
                    System.out.println("请输入密码：");
                    String password = in.nextLine();
                    boolean success = userService.Login(username, password);
                    if (success)
                    {
                        user = userService.getUserByUsername(username);
                        break;
                    }
                }
                else if (loginChoice == 2) // 注册菜单
                {
                    System.out.println("=======  QG申必小金库 =======");
                    System.out.println("            注册            ");
                    System.out.println("请输入用户名: ");
                    String username = in.nextLine();
                    System.out.println("请输入密码：");
                    String password = in.nextLine();
                    String Captcha = GenerateCaptcha.getCaptcha();
                    System.out.println("请输入验证码" + Captcha);
                    String inputCaptcha = in.nextLine();
                    if (inputCaptcha.equals(Captcha)) // 验证码输入成功
                    {
                        // 调用userService的Register函数检查并写入数据库
                        userService.Register(username, password);
                    }
                    else
                    {
                        System.out.println("验证码输入错误！");
                    }
                }
                else
                {
                    System.out.println("你输错了！");
                }
            }
            catch (BusinessException e)
            {
                System.out.println("操作失败: " + e.getMessage());
            }
            catch (SQLException e)
            {
                System.out.println("系统错误: 数据库操作失败! 请稍后再试! ");
            }
            catch (Exception e)
            {
                System.out.println("系统错误: 内部异常");
            }
        }

        MAIN:
        while (true)
        {
            // 主菜单
            Menu.mainMenu(user.getUsername(), user.getBalance());
            int choice = Integer.parseInt(in.nextLine());

            // 输出界面头
            System.out.println("======= 💰QG申必小金库💰 =======");
            System.out.println("当前用户：" + user.getUsername() + "  余额：¥" + user.getBalance());
            System.out.println("--------------------------------");

            try
            {
                switch (choice)
                {
                    case 1:
                        System.out.print("请输入你要存入的金额: ");
                        BigDecimal inputAmount = Input.readAmount();
                        // 调用存款函数
                        transactionService.deposit(user.getId(), inputAmount);
                        // 更新
                        user = userService.getUserById(user.getId());
                        break;
                    case 2:
                        System.out.print("请输入你要取出的金额: ");
                        BigDecimal outputAmount = Input.readAmount();
                        // 调用取款函数
                        transactionService.withdraw(user.getId(), outputAmount);
                        // 更新
                        user = userService.getUserById(user.getId());
                        break;
                    case 3:
                        System.out.println("请输入你要转给的用户id：");
                        Integer targetUserId = Integer.parseInt(in.nextLine());
                        System.out.println("请输入转账金额：");
                        BigDecimal giveAmount = Input.readAmount();
                        // 调用转账函数
                        transactionService.transfer(user.getId(), targetUserId, giveAmount);
                        // 更新
                        user = userService.getUserById(user.getId());
                        break;
                    case 4:
                        List<TransactionRecord> transactionRecordList = transactionService.getTransactionRecord(user.getId());
                        if (transactionRecordList.isEmpty())
                        {
                            System.out.println("当前用户没有流水记录");
                            break;
                        }
                        for (TransactionRecord transactionRecord : transactionRecordList)
                        {
                            System.out.println(transactionService.formatTransactionRecord(transactionRecord));
                        }
                        break;
                    case 5:
                        break MAIN;
                }
            }
            catch (BusinessException e)
            {
                System.out.println("操作失败: " + e.getMessage());
            }
            catch (SQLException e)
            {
                System.out.println("系统错误: 数据库操作失败! 请稍后再试! ");
            }
            catch (Exception e)
            {
                System.out.println("系统错误: 内部异常");
            }
        }
    }
}

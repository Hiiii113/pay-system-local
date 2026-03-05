package hiiii113.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

public class Input
{
    public static BigDecimal readAmount()
    {
        Scanner in = new Scanner(System.in);
        while (true)
        {
            try
            {
                String input = in.nextLine().trim();
                // 判空
                if (input.isEmpty())
                {
                    System.out.println("输入不能为空！");
                    continue;
                }
                // 正则表达式判断是否为数字
                if (!input.matches("-?\\d+(\\.\\d+)?"))
                {
                    System.out.println("只能输入纯数字！请重新输入：");
                    continue;
                }
                BigDecimal ans = new BigDecimal(input);
                //判负
                if (ans.compareTo(BigDecimal.ZERO) <= 0)
                {
                    System.out.println("金额必须大于0！");
                }
                // 保留两位
                ans = ans.setScale(2, RoundingMode.HALF_UP);
                return ans;
            }
            catch (Exception e)
            {
                System.out.print("只能输入纯数字！请重新输入：");
            }
        }
    }
}

package hiiii113.util;

import java.util.concurrent.ThreadLocalRandom;

public class GenerateCaptcha
{
    public String getCaptcha()
    {
        // 生成一个在[100000, 999999]之间的验证码
        int code = ThreadLocalRandom.current().nextInt(100000, 999999);
        // 字符串形式返回
        return String.valueOf(code);
    }
}

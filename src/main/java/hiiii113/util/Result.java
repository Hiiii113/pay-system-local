package hiiii113.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果类
 * 包含：是否成功 + 提示信息 + 可选数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result
{
    // 是否成功
    private boolean success;
    // 返回信息
    private String msg;
    // 返回数据（可选）
    private Object data;

    // 不带数据的成功返回方法
    public static Result success()
    {
        return new Result(true, null, null);
    }

    // 带返回消息的成功返回方法
    public static Result success(String msg)
    {
        return new Result(true, msg, null);
    }

    // 带返回消息和数据的成功返回方法
    public static Result success(String msg, Object data)
    {
        return new Result(true, msg, data);
    }

    // 只带数据的成功返回方法
    public static Result success(Object data)
    {
        return new Result(true, null, data);
    }

    // 不带数据的失败返回方法
    public static Result fail()
    {
        return new Result(false, null, null);
    }

    // 带返回消息的失败返回方法
    public static Result fail(String msg)
    {
        return new Result(false, msg, null);
    }

    // 带返回消息和数据的失败返回方法
    public static Result fail(String msg, Object data)
    {
        return new Result(false, msg, data);
    }

    // 只带数据的失败返回方法
    public static Result fail(Object data)
    {
        return new Result(false, null, data);
    }
}
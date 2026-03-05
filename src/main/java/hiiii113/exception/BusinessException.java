package hiiii113.exception;

import lombok.Getter;

/**
 * 自定义业务异常
 */
@Getter
public class BusinessException extends RuntimeException
{
    // 自定义错误码
    private final int code;

    // 仅传错误提示
    public BusinessException(String message)
    {
        super(message);
        this.code = 400;
    }

    // 传错误提示和自定义错误码
    public BusinessException(int code, String message)
    {
        super(message);
        this.code = code;
    }
}
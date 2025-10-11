package com.qzt.common.core.exception;


import com.qzt.common.core.constant.SysConstant;

/**
 * 统一异常基类
 *
 * @author cgw
 * @date 2017/11/9 23:02
 */
public abstract class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(Throwable ex) {
        super(ex);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable ex) {
        super(message, ex);
    }

    /**
     * 获取错误码
     *
     * @return ResultCodeEnum 错误码
     * @author cgw
     * @date 2017/11/10 09:38
     */
    public abstract SysConstant.ResultCodeEnum getCode();

}

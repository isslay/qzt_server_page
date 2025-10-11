package com.qzt.common.core.exception;

import com.qzt.common.core.constant.SysConstant;

/**
 * 登录异常类
 *
 * @author cgw
 * @date 2017/11/10 11:24
 */
public class LoginException extends BaseException {

    public LoginException() {
        super();
    }

    public LoginException(Throwable ex) {
        super(ex);
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public SysConstant.ResultCodeEnum getCode() {
        return SysConstant.ResultCodeEnum.LOGIN_FAIL;
    }
}

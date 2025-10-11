package com.qzt.common.core.exception;

import com.qzt.common.core.constant.SysConstant;

/**
 * 业务异常类
 *
 * @author cgw
 * @date 2017/11/10 11:24
 */
public class BusinessException extends BaseException {

    public BusinessException() {
        super();
    }

    public BusinessException(Throwable ex) {
        super(ex);
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public SysConstant.ResultCodeEnum getCode() {
        return SysConstant.ResultCodeEnum.INTERNAL_SERVER_ERROR;
    }
}

package com.qzt.common.web.handler;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.exception.BaseException;
import com.qzt.common.core.exception.BusinessException;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @author cgw
 * @date 2017/11/12 16:07
 */
@Slf4j
@RestControllerAdvice
public class SysExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(Exception.class)
    public ResultModel exceptionHandler(Exception e) {
        log.info(e.getMessage(), e);
        // 媒体类型
        if (e instanceof HttpMediaTypeNotSupportedException) {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.NO_SUPPORTED_MEDIATYPE);
        }
        // springboot参数验证框架如果验证失败则抛出MethodArgumentNotValidException异常
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) e;
            FieldError fieldError = methodArgumentNotValidException.getBindingResult().getFieldError();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.BAD_REQUEST, fieldError.getDefaultMessage());
        }
        // Shiro 安全校验失败
        if (e instanceof AuthorizationException) {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.UNAUTHORIZED, SysConstant.ResultCodeEnum.UNAUTHORIZED.getMessage());
        }
        if (e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            return ResultUtil.fail(baseException.getCode(), baseException.getMessage());
        }
        if (e instanceof RuntimeException && e.getMessage().contains(BusinessException.class.getName())) {
            String message = e.getMessage().substring(BusinessException.class.getName().length() + 1, e.getMessage().indexOf("\r\n")).trim();
            ;
            return ResultUtil.fail(SysConstant.ResultCodeEnum.INTERNAL_SERVER_ERROR, message);
        }
        return ResultUtil.fail(SysConstant.ResultCodeEnum.INTERNAL_SERVER_ERROR);
    }
}

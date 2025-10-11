package com.qzt.ump.server.annotation;

import com.qzt.common.core.constant.SysConstant;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author RickyWang
 * @date 2017/12/27 13:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogOpt {
    String value();
    String module()  default "";  //模块名称 系统管理-用户管理－列表页面
    String description()  default "";  //描述
    SysConstant.LogOptEnum operationType() default SysConstant.LogOptEnum.UNKNOW;//操作类型
}

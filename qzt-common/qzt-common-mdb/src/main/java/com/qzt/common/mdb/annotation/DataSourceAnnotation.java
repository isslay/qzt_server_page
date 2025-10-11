package com.qzt.common.mdb.annotation;


import com.qzt.common.core.constant.SysConstant;

import java.lang.annotation.*;

/**
 * 数据源注解
 *
 * @author cgw
 * @date 2017/11/17 13:56
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceAnnotation {
    SysConstant.DataSourceEnum value() default SysConstant.DataSourceEnum.MASTER;
}

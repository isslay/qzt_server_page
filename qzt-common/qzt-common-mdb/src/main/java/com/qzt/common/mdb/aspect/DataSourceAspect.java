package com.qzt.common.mdb.aspect;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.mdb.DbContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源切面处理类
 *
 * @author cgw
 * @date 2017/11/17 13:59
 */
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    @Pointcut("execution(* com.qzt..mapper.*.*(..))")
    public void dataSourcePointCut() {
    }

    @Before("dataSourcePointCut()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        if (methodName.startsWith(SysConstant.MAPPER_METHOD_STARTSWITH_SELECT) || methodName.startsWith(SysConstant.MAPPER_METHOD_STARTSWITH_FIND)|| methodName.startsWith(SysConstant.MAPPER_METHOD_STARTSWITH_QUERY)) {
            log.info("切换为从数据源：{}", SysConstant.DataSourceEnum.SLAVE.getName());
            // 切换从数据源
            DbContextHolder.setDbType(SysConstant.DataSourceEnum.SLAVE);
        } else {
            log.info("切换为主数据源：{}", SysConstant.DataSourceEnum.MASTER.getName());
            // 切换主数据源
            DbContextHolder.setDbType(SysConstant.DataSourceEnum.MASTER);
        }
    }

    @After("dataSourcePointCut()")
    public void doAfter() {
        log.info("---- 清空数据源 ----");
        // 清空数据源
        DbContextHolder.clearDbType();
    }

}

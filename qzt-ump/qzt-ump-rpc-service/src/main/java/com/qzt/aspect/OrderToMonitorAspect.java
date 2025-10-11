package com.qzt.aspect;

import com.qzt.annotation.OrderToMonitor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 订单切面
 *
 * @author Xiaofei
 * @date 2019-06-27
 */
@Slf4j
@Aspect//声明该类为切面bean
@Component//声明为组件，让spring 自动管理
public class OrderToMonitorAspect {

    @Pointcut("@annotation(com.qzt.annotation.OrderToMonitor)")
    public void annotationPoinCut() {
    }

    @After("annotationPoinCut()")
    public void after(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        OrderToMonitor action = method.getAnnotation(OrderToMonitor.class);
        System.out.println("注解式拦截 " + action.value());
    }

    /**
     * 订单回调AOP（切面）处理
     * 异常监听
     *
     * @param joinPoint
     * @return void
     * @author Xiaofei
     * @date 2019-06-27
     */
    /*@AfterThrowing(throwing = "ex", pointcut = "execution(* com.qzt.bus.rpc.service.impl.qztGorderServiceImpl.orderPayBack(..))")
    public void beforeOrderPayBack(JoinPoint joinPoint, Throwable ex) {
        IqztGorderService qztGorderService = (IqztGorderService) joinPoint.getTarget();
        String orderNo = (String) joinPoint.getArgs()[0];
        qztGorderService.abnormalOrders(orderNo, ex.getMessage());
        log.info("订单回调异常AOP：" + ex.getMessage());
    }*/

}


package com.qzt.ump.server.webApi.interceptor;

import com.alibaba.fastjson.JSON;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.mq.pushMq.MqConstant;
import com.qzt.common.mq.pushMq.MqMess;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by cgw on 2018/5/28.
 */
@Slf4j
@Component
public class InterceptorConfig implements HandlerInterceptor {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        log.info("---------------------开始进入请求地址拦截----------------------------");
//        String tokenId = request.getParameter("tokenId");
//        String userId = request.getParameter("userId");

        String tokenId = request.getHeader("x-dgd-tokenId");
        String userId = request.getHeader("x-dgd-userId");

        if (StringUtils.isEmpty(tokenId)) {//未传入TOKEN
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(returnUtil.returnMess(Constant.LOGIN_OUT)));
            return false;
        } else {
            //通过token来判断传入的token是否正确或者有效
            String tokenUserId = (String) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.MOBLIECODE.value() + tokenId);
            if (tokenUserId == null || userId == null || !userId.equals(tokenUserId)) {
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(JSON.toJSONString(returnUtil.returnMess(Constant.LOGIN_OUT)));
                return false;
            } else {
                CacheUtil.getCache().expire(SysConstant.CacheNamespaceEnum.CAPTCHA.value(), 30 * 24 * 60 * 60);
            }
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("--------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("---------------视图渲染之后的操作-------------------------0");
    }
}

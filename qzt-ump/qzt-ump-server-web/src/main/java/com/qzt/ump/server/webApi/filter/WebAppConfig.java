package com.qzt.ump.server.webApi.filter;

import com.qzt.ump.server.webApi.interceptor.InterceptorConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by cgw on 2018/5/28.
 */
@SpringBootConfiguration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private InterceptorConfig interceptorConfig;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(interceptorConfig).addPathPatterns("/webapi/**").excludePathPatterns("/webapi/webUser/regUser").excludePathPatterns("/webapi/sysDic");
    }
}

package com.qzt.ump.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author cgw
 * @date 2017/11/17 00:34
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages = {"com.qzt.ump.server", "com.qzt.common.web", "com.qzt.common.dsession", "com.qzt.common.log.web", "com.qzt.common.redis","com.qzt.common.pay","com.qzt.common.mq"})
@ServletComponentScan({"com.qzt.common.web.filter", "com.qzt.ump.server.druid"})
@ImportResource(value = {"classpath:dubbo/consumers.xml"})
public class ServerWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerWebApplication.class, args);
        log.info("========== ServerWebApplication启动成功 Web ==========");
    }
}

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
@ComponentScan(basePackages = {"com.qzt.ump.server","com.qzt.common.mq", "com.qzt.common.redis"})
@ImportResource(value = {"classpath:dubbo/consumers.xml"})
public class ServerScheduledApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServerScheduledApplication.class, args);
        log.info("========== ServerScheduledApplication启动成功 Scheduled ==========");
    }
}

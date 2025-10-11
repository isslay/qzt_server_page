package com.qzt.ump.rpc.service;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author cgw
 * @description: 启动类
 * @date 2017/11/17 00:34
 */
@Slf4j

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {"com.qzt.aspect","com.qzt.*.rpc.service.impl", "com.qzt.common.mdb", "com.qzt.common.db", "com.qzt.common.redis","com.qzt.common.mq"})
@ImportResource(value = {"classpath:dubbo/providers.xml"})
@MapperScan(basePackages = {"com.qzt.*.dao.mapper"})
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(ServiceApplication.class);
        springApplication.run(args);
        System.out.println("");
        log.info("========== ServiceApplication启动成功 ==========");
    }
}

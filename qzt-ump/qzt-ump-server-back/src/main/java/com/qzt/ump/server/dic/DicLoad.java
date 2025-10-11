package com.qzt.ump.server.dic;

import com.qzt.ump.rpc.api.SysAreaService;
import com.qzt.ump.rpc.api.SysDicService;
import com.qzt.ump.rpc.api.SysParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by cgw on 2018/6/1.
 */
@Component
@Slf4j
@Order(2)
public class DicLoad implements CommandLineRunner {

    @Autowired
    private SysDicService sysDicService;

    @Autowired
    private SysParamService sysParamService;

    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public void run(String... strings) throws Exception {
        log.info("======================开始加载字典===========================");
        this.sysDicService.initializeDic();

        log.info("======================开始加载系统参数===========================");
        this.sysParamService.initializeParam();

//        log.info("======================开始加载地区信息===========================");
//        this.sysAreaService.initializeArea();
    }

}

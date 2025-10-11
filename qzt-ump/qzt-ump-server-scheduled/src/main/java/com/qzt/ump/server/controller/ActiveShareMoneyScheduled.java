package com.qzt.ump.server.controller;


import com.qzt.bus.rpc.api.IQztUserCouponService;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


/**
 * 激活推广佣金
 */
@Slf4j
@Component
public class ActiveShareMoneyScheduled {

    @Autowired
    private IQztUserCouponService qztUserCouponService;

//    @Scheduled(cron = "00 01 00  * * ?")// 00 01 00  * * ? 每天零点1分执行将券转化成金额   0 * 14 * * ?
//    public void createDeductionMoney() {
//        log.info("start ActiveShareMoney "+ DateTime.getCurDate_yyyyMMddHHmmssRe());
//        Map<String, Object> params  = new HashMap<>();
//        params.put("validTime", DateTime.getCurDate_yyyy_MM_dd());
//        qztUserCouponService.updatePassCoupon(params);
//        log.info("end ActiveShareMoney "+ DateTime.getCurDate_yyyyMMddHHmmssRe());
//    }
}

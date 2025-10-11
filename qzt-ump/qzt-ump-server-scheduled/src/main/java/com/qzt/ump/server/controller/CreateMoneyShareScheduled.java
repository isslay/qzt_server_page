package com.qzt.ump.server.controller;


import com.qzt.bus.model.QztRecharge;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztAccountLogService;
import com.qzt.bus.rpc.api.IQztRechargeService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.tools.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 根据收入情况给各个服务站佣金
 */
@Slf4j
@Component
public class CreateMoneyShareScheduled {

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztRechargeService qztRechargeService;

    @Scheduled(cron = "00 03 00  * * ?")// 00 01 00  * * ? 每天零点1分执行将券转化成金额   0 * 14 * * ?
    public void createDeductionMoney() {
        log.info("start CreateMoneyShare "+ DateTime.getCurDate_yyyyMMddHHmmssRe());
        //首先查询所有服务站的当天收入
        //查询出所有的督导 计算出他们应该挣的钱 分两部分 一部分是直接收入  一部分是间接收入 20
        String [] typeAr = {"20","30","40"};
        for(String type : typeAr){
            Map<String,Object> typeParams = new HashMap();
            typeParams.put("parseType",type);
            List<QztUser> qztUserList = qztUserService.findListByType(typeParams);
            String typeMc = "健康引导师";
            if(type.equals("30")){
                typeMc = "健康大使";
            }else if(type.equals("40")){
                typeMc = "健康使者";
            }
            for(QztUser qztUser : qztUserList){
                String [] changeSource = {"30", "31", "32","33"};
                Map<String,Object> params = new HashMap<>();
                params.put("userId",qztUser.getUserId());
                params.put("changeSource",changeSource);
                params.put("logState",qztUser.getUserType());
                try {
                    params.put("createTime",DateTime.dateAdd("D",DateTime.getCurDate_yyyy_MM_dd(),-1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                params.put("type","down");
                long downAccount = qztAccountLogService.queryUserAccount(params);//级别小于自己的
                String [] rechangeSource = {"33"};
                params.put("changeSource",rechangeSource);
                params.put("type","up");
                long upAccount = qztAccountLogService.queryUserAccount(params); //大于等于自己
                long allAccount = (long) ((downAccount+upAccount)*0.2);
                if(allAccount>0){
                    QztRecharge qztRecharge =  new QztRecharge();
                    qztRecharge.setUserId(qztUser.getUserId());
                    qztRecharge.setTopUpMoney(allAccount);
                    qztRecharge.setAuditState("00");
                    BigDecimal bd = new BigDecimal(downAccount*0.002);
                    Double tem = bd.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();

                    BigDecimal bd1 = new BigDecimal(upAccount*0.002);
                    Double tem1 = bd1.setScale(2,BigDecimal.ROUND_FLOOR).doubleValue();

                    qztRecharge.setAuditRemark(params.get("createTime")+"晋升系统结算用户分润当前级别"+typeMc+",级别小于当前金额为："+tem+"；级别大于等于当前金额为："+tem1);
                    qztRecharge.setCreateTime(new Date());
                    qztRecharge.setUserName("系统自动生成");
                    //qztRecharge.setUserId(1l);
                    qztRechargeService.insert(qztRecharge);
                }
            }
            log.info("end CreateMoneyShare "+ DateTime.getCurDate_yyyyMMddHHmmssRe());
        }
    }

}

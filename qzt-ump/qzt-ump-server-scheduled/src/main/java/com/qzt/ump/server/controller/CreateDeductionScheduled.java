package com.qzt.ump.server.controller;


import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.bus.rpc.api.IQztAccountService;
import com.qzt.common.tools.DateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;


/**
 * 优惠券定时 过期
 */
@Slf4j
@Component
public class CreateDeductionScheduled {

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private IQztAccountService qztAccountService;

     @Scheduled(cron = "00 02 00  * * ?")// 00 01 00  * * ? 每天零点1分执行将券转化成金额   0 * 14 * * ?
    public void createDeductionMoney() {
        //查询所有需要转换的锁粉金额的人和金额
        log.info("start CreateRecommendMoney "+ DateTime.getCurDate_yyyyMMddHHmmssRe());
        Map<String,Object> param = new HashMap<>();
        try {
            param.put("createTime",DateTime.dateAdd("D",DateTime.getCurDate_yyyy_MM_dd(),-1));
            param.put("state",1);
            param.put("reType",1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Map> userRs = qztAccountRelogService.findAccountRelogByUserId(param);
        for(int i=0;i<userRs.size();i++){
            Map<String,Object> map = userRs.get(i);
            Long changeAllMoney = Long.parseLong(map.get("change_money").toString());//用户当日的剩余分享佣金
            Map<String,Object> params = new HashMap<>();
            params.put("userId",map.get("user_id").toString());
            params.put("reType","0");
            //计算出需要转换的推广佣金
            List<QztAccountRelog>  qztAccountRelogs = qztAccountRelogService.findCommendAllMoneyByUserId(params);
            List<QztAccountRelog> updateRs = new ArrayList();
            Long addAllMoney = 0l;
            StringBuffer activeMess= new StringBuffer();
            for(QztAccountRelog qztAccountRelog:qztAccountRelogs){
                Long changeMoney = qztAccountRelog.getReMoney()-qztAccountRelog.getChangeMoney()-qztAccountRelog.getGiveMoney();
                boolean  checkFlage = false;
                try{
                    if(changeAllMoney>changeMoney){
                        changeAllMoney = changeAllMoney-changeMoney;
                        qztAccountRelog.setChangeMoney(qztAccountRelog.getChangeMoney()+changeMoney);
                        addAllMoney +=changeMoney;
                        activeMess.append(qztAccountRelog.getId()+"激活"+changeMoney+";");
                    }else{
                        qztAccountRelog.setChangeMoney(qztAccountRelog.getChangeMoney()+changeAllMoney);
                        addAllMoney +=changeAllMoney;
                        checkFlage = true;
                        activeMess.append(qztAccountRelog.getId()+"激活"+changeAllMoney+";");
                    }
                }catch (Exception re){
                    re.printStackTrace();
                }
                updateRs.add(qztAccountRelog);
                if(checkFlage){
                    break;
                }
            }
            params.put("reType","1");
            try {
                param.put("createTime",DateTime.dateAdd("D",DateTime.getCurDate_yyyy_MM_dd(),-1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            qztAccountRelogs = qztAccountRelogService.findCommendAllMoneyByUserId(params);//计算所有的激活佣金
            long disMoney = addAllMoney;
            for(QztAccountRelog qztAccountRelog:qztAccountRelogs){
                Long changeMoney = qztAccountRelog.getReMoney()-qztAccountRelog.getChangeMoney()-qztAccountRelog.getGiveMoney();
                boolean  checkFlage = false;
                if(disMoney>changeMoney){
                    disMoney -=changeMoney;
                    qztAccountRelog.setChangeMoney(qztAccountRelog.getChangeMoney()+changeMoney);
                }else{
                    qztAccountRelog.setChangeMoney(qztAccountRelog.getChangeMoney()+disMoney);
                    checkFlage=true;
                }
                updateRs.add(qztAccountRelog);
                if(checkFlage){
                    break;
                }
            }
            //给用户激活推广佣金 和 保存到余额
            if(updateRs.size()>0 && qztAccountRelogService.updateBatchById(updateRs)){
                qztAccountService.updateQztAccount(map.get("user_id").toString()+DateTime.getCurDate_yyyyMMdd(),(Long)map.get("user_id"),addAllMoney,"32","01",activeMess.toString());
            }
            //修改激活记录
        }

         log.info("end CreateRecommendMoney "+ DateTime.getCurDate_yyyyMMddHHmmssRe());

    }

}

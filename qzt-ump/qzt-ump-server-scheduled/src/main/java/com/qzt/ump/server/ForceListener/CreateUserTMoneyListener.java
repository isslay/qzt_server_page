package com.qzt.ump.server.ForceListener;


import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.bus.rpc.api.IQztUserService;
import lombok.extern.log4j.Log4j;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * 用户注册给推广佣金
 */

@Log4j
@Component
public class CreateUserTMoneyListener {

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    /**
     * @param msg
     * @throws Exception
     */
    @JmsListener(destination = "Consumer.A.VirtualTopic.ShareMoneyTop")//监听添加原理
    @Async
    public void giveUserAndShareMoney(Message msg) throws Exception {
        //注册给 注册用户和 推荐用户各1000元推广佣金
        String busId =  msg.getStringProperty("busId") ;//业务用户ID
        //查询用户的基本信息
        QztUser qztUser = qztUserService.findDGUserById(Long.valueOf(busId));
        if(qztUser!=null){
            String[] userAr = null ;
            if(qztUser.getReferrerFirst()!=null){
                userAr = new String[2];
                userAr[1] = qztUser.getReferrerFirst().toString();
            }else{
                userAr = new String[1];
            }
            userAr[0] = busId;
            int index=0;
            for(String userId : userAr){
                HashMap<String,Object> params = new HashMap<>();
                params.put("busId",busId);
                params.put("userId",userId);
                QztAccountRelog klygAccountRelog = new QztAccountRelog();
                klygAccountRelog.setReMoney(100000l);
                klygAccountRelog.setChangeMoney(0l);
                klygAccountRelog.setGiveMoney(0l);
                klygAccountRelog.setReType(0);
                klygAccountRelog.setState(1);
                klygAccountRelog.setBusId(busId);
                if(index==0){
                    klygAccountRelog.setBusType("01");
                    klygAccountRelog.setRemark(busId+"用户注册获得推广佣金");
                    klygAccountRelog.setUserId(userId);
                    params.put("reType",0);
                }else{
                    klygAccountRelog.setBusType("02");
                    klygAccountRelog.setRemark(busId+"推荐用户注册获得推广佣金");
                    klygAccountRelog.setUserId(userId);
                    params.put("reType",0);
                }
                int rs =  qztAccountRelogService.findAccountByBusId(params);
                if(rs==0){
                    klygAccountRelog.setCreateTime(new Date());
                    qztAccountRelogService.add(klygAccountRelog);
                }
                index++;
            }

        }
    }
}

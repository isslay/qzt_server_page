package com.qzt.ump.server.ForceListener;


import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.model.QztServiceOrder;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.*;
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
public class CreateServiceTMoneyListener {

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private IQztServiceOrderService qztServiceOrderService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Autowired
    private IQztGorderService qztGorderService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztAccountService qztAccountService;


    /**
     * @param msg
     * @throws Exception
     */
    @JmsListener(destination = "Consumer.A.VirtualTopic.ServicesMoneyTop")//监听添加原理
    @Async
    public void giveUserAndShareMoney(Message msg) throws Exception {
        //服务订单
        String busId =  msg.getStringProperty("busId") ;//服务订单主键ID\
        log.info("服务订单分润"+busId);
        try{
            QztServiceOrder qztServiceOrder = qztServiceOrderService.queryById(Long.parseLong(busId));
            if(qztServiceOrder!=null && qztServiceOrder.getOrderState().equals("07")){
                //获得服务站id'
                long serviceId = qztBusinessService.queryById(qztServiceOrder.getBusniessId()).getUserId();
                //查询订单信息 获得 服务费用
                QztGorder qztGorder = qztGorderService.queryByOrederNo(qztServiceOrder.getOrderNo());
                if(qztGorder!=null && qztGorder.getServiceMoney()>0){
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("busId",busId);
                    params.put("userId",serviceId);
                    params.put("reType",1);
                    int rs =  qztAccountRelogService.findAccountByBusId(params);
                    if(rs==0){
                        QztAccountRelog klygAccountRelog = new QztAccountRelog();
                        klygAccountRelog.setReMoney(qztGorder.getServiceMoney());
                        klygAccountRelog.setChangeMoney(0l);
                        klygAccountRelog.setGiveMoney(0l);
                        klygAccountRelog.setReType(1);
                        klygAccountRelog.setState(1);
                        klygAccountRelog.setBusId(busId);
                        klygAccountRelog.setBusType("04");
                        klygAccountRelog.setRemark(qztServiceOrder.getOrderNo()+"完成服务订单获得分享佣金");
                        klygAccountRelog.setUserId(serviceId+"");
                        klygAccountRelog.setCreateTime(new Date());
                        qztAccountRelogService.add(klygAccountRelog);
                    }
                    //给余额
                    //到余额
                    HashMap<String,Object> newparams = new HashMap<>();
                    newparams.put("busId",busId);
                    newparams.put("userId",serviceId);
                    newparams.put("changeSource","31");
                    rs =  qztAccountLogService.findAccountByBusId(newparams);
                    if(rs==0){
                        qztAccountService.updateQztAccount(qztServiceOrder.getOrderNo(),serviceId,qztGorder.getServiceMoney(),"31","01","通过服务订单获得分润"+qztServiceOrder.getOrderNo());
                    }
                }
            }
        }catch (Exception re){
            re.printStackTrace();
        }

    }
}

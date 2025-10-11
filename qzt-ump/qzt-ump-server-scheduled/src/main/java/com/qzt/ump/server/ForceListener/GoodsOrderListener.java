package com.qzt.ump.server.ForceListener;


import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.tools.StringUtil;
import lombok.extern.log4j.Log4j;
import org.apache.activemq.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;


/**
 * 根据消费订单给用户分润
 */

@Log4j
@Component
public class GoodsOrderListener {

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private IQztGorderService qztGorderService;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    /**
     * 分润
     * @param msg
     * @throws Exception
     */
    @JmsListener(destination = "Consumer.A.VirtualTopic.GoodsOrderTop")//监听添加原理
    @Async
    public void GoodsOrderTop(Message msg) throws Exception {
        String busId =  msg.getStringProperty("busId") ;//订单编号
        log.info("订单分润"+busId);
        try {
            //查询订单信息
            QztGorder qztGorder =qztGorderService.queryByOrederNo(busId);
            //查询个人用户信息
            QztUser qztUser = qztUserService.findDGUserById(qztGorder.getUserId());
            if(qztGorder!=null && qztGorder.getOrderState().equals("09")){
                String giveUserId = "";
                boolean giveFlage = true;
                if(qztUser.getReferrerFirst()==null && qztGorder.getShareCode()==null){
                    giveFlage = false;
                }else{
                    giveUserId = qztUser.getReferrerFirst()==null?qztGorder.getShareCode():qztUser.getReferrerFirst().toString();
                }
                if(qztGorder.getShareMoney()!=null && qztGorder.getShareMoney()>0 &&  giveFlage){
                    //到激活佣金 给推荐人
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("busId",busId);
                    params.put("userId",giveUserId);
                    params.put("reType",1);
                    QztAccountRelog klygAccountRelog = new QztAccountRelog();
                    klygAccountRelog.setReMoney(qztGorder.getShareMoney());
                    klygAccountRelog.setGiveMoney(0l);
                    klygAccountRelog.setChangeMoney(0l);
                    klygAccountRelog.setReType(1);
                    klygAccountRelog.setState(1);
                    klygAccountRelog.setBusId(busId);
                    klygAccountRelog.setBusType("01");
                    klygAccountRelog.setRemark(busId+"推荐用户下单获得激活佣金");
                    klygAccountRelog.setUserId(giveUserId);
                    int rs =  qztAccountRelogService.findAccountByBusId(params);
                    if(rs==0){
                        klygAccountRelog.setCreateTime(new Date());
                        qztAccountRelogService.add(klygAccountRelog);
                    }
                }
                if(qztGorder.getRecommendMoney()!=null && qztGorder.getRecommendMoney()>0 && qztGorder.getShareCode()!=null){
                    //到余额
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("busId",busId);
                    params.put("userId",qztGorder.getShareCode());
                    params.put("changeSource","30");
                    int rs =  qztAccountLogService.findAccountByBusId(params);
                    if(rs==0){
                        qztAccountService.updateQztAccount(busId,Long.parseLong(qztGorder.getShareCode()),qztGorder.getRecommendMoney(),"30","01","推荐用户消费获得分润");
                    }
                }

                if(!StringUtil.isEmpty(qztGorder.getTheVerificationCode())){
                    long serviceId = qztBusinessService.queryById(qztGorder.getAddressId()).getUserId();
                    HashMap<String,Object> params = new HashMap<>();
                    params.put("busId",busId+"_S");
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
                        klygAccountRelog.setBusType("04");
                        klygAccountRelog.setBusId(busId+"_S");
                        klygAccountRelog.setRemark(qztGorder.getOrderNo()+"完成服务订单获得分享佣金");
                        klygAccountRelog.setUserId(serviceId+"");
                        klygAccountRelog.setCreateTime(new Date());
                        qztAccountRelogService.add(klygAccountRelog);
                    }
                    //给余额
                    //到余额
                    HashMap<String,Object> newparams = new HashMap<>();
                    newparams.put("busId",busId+"_S");
                    newparams.put("userId",serviceId);
                    newparams.put("changeSource","31");
                    rs =  qztAccountLogService.findAccountByBusId(newparams);
                    if(rs==0){
                        qztAccountService.updateQztAccount(qztGorder.getOrderNo()+"_S",serviceId,qztGorder.getServiceMoney(),"31","01","通过服务订单获得分润"+qztGorder.getOrderNo());
                    }

                }

            }
        }catch (Exception re){
            re.printStackTrace();
        }
    }
}

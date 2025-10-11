package com.qzt.ump.server.ForceListener;

import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztApplyBusorderService;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.bus.rpc.api.IQztUserRegService;
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
 * 通用MQ
 *
 * @author Xiaofei
 * @date 2019-11-24
 */
@Log4j
@Component
public class CommonMqListener {

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 处理用户升级MQ
     *
     * @param msg
     * @return void
     * @author Xiaofei
     * @date 2019-11-24
     */
    @Async
    @JmsListener(destination = "Consumer.A.VirtualTopic.UserUpgradeTop")//监听添加原理
    public void userUpgradeTop(Message msg) throws Exception {
        String busId = msg.getStringProperty("busId");//业务用户ID
        this.qztUserService.userUpgrade(Long.valueOf(busId));
    }

}

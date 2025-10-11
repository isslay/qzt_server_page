package com.qzt.bus.rpc.service.impl;

import com.qzt.bus.dao.mapper.QztGorderMapper;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.rpc.api.IQztGorderService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.HashMap;
import java.util.Map;

/**
 * 自动取消订单监听  订单分类（商品订单01、、、、、）
 */
@Log4j
public class TopicMessageListener implements MessageListener {

    @Autowired
    private IQztGorderService qztGorderService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            byte[] body = message.getBody();// 请使用valueSerializer
            String itemValue = new String(body);
            String orderNo = itemValue.substring(itemValue.indexOf("^") + 1, itemValue.lastIndexOf("^"));
            String orderType = itemValue.substring(itemValue.lastIndexOf("^") + 1);
            if ("01".equals(orderType)) {//商品订单
                log.info("自动取消订单Start");
                this.qztGorderService.cancellationOfOrder(new QztGorder(0L, orderNo));
                log.info("自动取消订单End");
            }
        } catch (Exception e) {
            log.info("redis倒计时error：" + e.getMessage());
            e.printStackTrace();
        }
    }

}

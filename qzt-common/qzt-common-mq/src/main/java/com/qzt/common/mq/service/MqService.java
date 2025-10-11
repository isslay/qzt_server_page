package com.qzt.common.mq.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.Message;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import java.util.Map;

/**
 * MQ服务类
 *
 * @author Ricky Wang
 * @date 17/11/24 15:16:50
 */
@Service("mqService")
@Slf4j
public class MqService {
    @Autowired()
    /**
     * 也可以注入JmsTemplate，JmsMessagingTemplate对JmsTemplate进行了封装
     *
     * @author RickyWang
     * @date 18/1/1 15:19:22
     */
    private JmsMessagingTemplate jmsMessagingTemplate;

    /**
     * 发送消息，destination是发送到的队列，message是待发送的消息
     *
     * @author RickyWang
     * @date 18/1/1 15:19:41
     */
    public void send(String queueName, final Object object) {
        send(new ActiveMQQueue(queueName), object);
    }

    public void send(Destination destination, final Object object) {
        String message = object instanceof String ? object.toString() : JSONObject.toJSONString(object);
        try {
            jmsMessagingTemplate.convertAndSend(destination, message);
        } catch (MessagingException e) {
            log.error(e.toString());
        }
    }

    public void sendToVTopic(Map<String, String> data,String vTopic) {
        ActiveMQTopic mqVTopic = new ActiveMQTopic(vTopic);
        ActiveMQMessage msg = new ActiveMQMessage();
        try {
            for(Map.Entry<String, String> entry: data.entrySet()){
                msg.setStringProperty(entry.getKey(),entry.getValue());
            }
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sendMsg(mqVTopic, msg);
    }
    public void sendMsg(Destination destination, Message msg) {
        jmsMessagingTemplate.convertAndSend(destination, msg);
    }
}

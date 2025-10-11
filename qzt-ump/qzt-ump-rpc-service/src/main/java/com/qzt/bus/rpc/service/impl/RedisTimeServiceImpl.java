package com.qzt.bus.rpc.service.impl;

import com.qzt.bus.rpc.api.IRedisTimeService;
import com.qzt.common.core.constant.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service("redisTimeService")
public class RedisTimeServiceImpl implements IRedisTimeService {

    @Autowired
    public RedisTemplate redisTemplate;

    @Override
    public Boolean creatRedis(String orderId, String orderType, String timeType, Long time) {
        try {
            TimeUnit timeUnit = TimeUnit.MINUTES;
            if ("D".equals(timeType)) {
                timeUnit = TimeUnit.DAYS;
            } else if ("H".equals(timeType)) {
                timeUnit = TimeUnit.HOURS;
            } else if ("S".equals(timeType)) {
                timeUnit = TimeUnit.SECONDS;
            }
            this.redisTemplate.opsForValue().set(SysConstant.CacheNamespaceEnum.COUNTDOWN.value() + "OrderID^" + orderId + "^" + orderType, orderId, time, timeUnit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

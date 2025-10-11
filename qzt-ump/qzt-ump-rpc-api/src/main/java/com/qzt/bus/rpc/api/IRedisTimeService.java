package com.qzt.bus.rpc.api;

/**
 * redis计时
 */
public interface IRedisTimeService {
    /**
     * @param orderId   订单主键ID
     * @param orderType 订单分类（商品订单01、、、、、）
     * @param timeType  时间类型（D：天，H：小时，M：分钟，S：秒钟（注意大写，默认分钟））
     * @param time      时间长度
     * @return
     */
    Boolean creatRedis(String orderId, String orderType, String timeType, Long time);

}

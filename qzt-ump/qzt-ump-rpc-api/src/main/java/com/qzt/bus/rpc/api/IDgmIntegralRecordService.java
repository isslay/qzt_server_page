package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmIntegralRecord;
import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.common.core.base.BaseService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
public interface IDgmIntegralRecordService extends BaseService<DgmIntegralRecord> {

    Page<DgmIntegralRecord> find(Page<DgmIntegralRecord> pageModel);

    /**
     * 用户获取积分
     * 用户id,1下单2分享下单,说明,积分
     */
    void creatNew(int id, int type, String remark, int value);
    /**
     * 用户消耗积分
     * 用户id,21积分兑换,说明,积分
     */
    int consumeNew(int id, int type, String remark, int value);

    /**
     * 过期积分
     * 用户id,99过期,说明,积分
     */
    void overNew();
}

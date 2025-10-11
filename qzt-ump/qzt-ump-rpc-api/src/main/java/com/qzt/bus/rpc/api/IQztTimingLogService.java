package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztTimingLog;
import com.qzt.common.core.base.BaseService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztTimingLogService extends BaseService<QztTimingLog> {

    Page<QztTimingLog> find(Page<QztTimingLog> pageModel);

    /**
     * 添加定时任务日志
     *
     * @param busType           定时业务分类（01：优惠券）
     * @param busId             业务记录ID
     * @param resultEnforcement 执行结果（成功：Y、失败：N）
     * @param busRemark         备注
     * @return void
     * @author Xiaofei
     * @date 2019-11-12
     */
    void addTimingLog(String busType, String busId, String resultEnforcement, String busRemark);

}

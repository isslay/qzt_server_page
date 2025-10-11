package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztBusLog;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztBusLogService extends BaseService<QztBusLog> {

    Page<QztBusLog> find(Page<QztBusLog> pageModel);

    /**
     * 添加业务日志
     *
     * @param businessType 业务分类（商品订单相关01、提现相关03、申请服务站订单05、试用订单07、申请服务订单09）
     * @param operNode     操作节点
     * @param businessId   业务记录ID
     * @param busRemark    备注
     * @param operatorId   操作人ID
     * @return void
     * @author Xiaofei
     * @date 2019-11-12
     */
    void addBusLog(String businessType, String operNode, String businessId, String busRemark, Long operatorId);

    /**
     * 根据业务日志busid查询日志列表
     *
     * @param businessType 业务分类（商品订单相关01、提现相关03、申请服务站订单05、试用订单07、申请服务订单09）
     * @param businessId   业务单号
     * @return java.util.List
     * @author Xiaofei
     * @date 2019-11-23
     */
    List<QztBusLog> findByBusinessId(String businessType, String businessId);

}

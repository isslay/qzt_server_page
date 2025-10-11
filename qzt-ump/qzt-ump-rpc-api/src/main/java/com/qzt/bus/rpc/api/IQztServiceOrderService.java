package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztServiceOrder;
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
public interface IQztServiceOrderService extends BaseService<QztServiceOrder> {

    Page<QztServiceOrder> find(Page<QztServiceOrder> pageModel);

    /**
     * 服务订单导出数据集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Xiaofei
     * @date 2019-12-05
     */
    List<Map<String, Object>> soorderExcel(Map map);

}

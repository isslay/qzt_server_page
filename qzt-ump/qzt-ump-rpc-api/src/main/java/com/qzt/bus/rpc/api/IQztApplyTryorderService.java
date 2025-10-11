package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztApplyTryorder;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztApplyTryorderService extends BaseService<QztApplyTryorder> {

    Page<QztApplyTryorder> find(Page<QztApplyTryorder> pageModel);

    boolean updateTryOrder(Map<String, Object> params);

    List<QztApplyTryorder> queryOrderByCradId(String cardId, String disType);

    /**
     * 检测手机号和病是否已经下过单
     *
     * @param params
     * @return
     */
    boolean checkTryOrder(Map<String, Object> params);

    /**
     * 试用订单导出数据集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Xiaofei
     * @date 2019-12-05
     */
    List<Map<String, Object>> syorderExcel(Map map);

}

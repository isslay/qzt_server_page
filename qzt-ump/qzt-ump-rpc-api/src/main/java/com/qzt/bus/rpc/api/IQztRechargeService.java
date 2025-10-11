package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztRecharge;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-22
 */
public interface IQztRechargeService extends BaseService<QztRecharge> {

    Page<QztRecharge> find(Page <QztRecharge> pageModel);

    /**
     * 查询充值记录导出
     * @param map
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     * @author Xiaofei
     * @date 2020-04-02
     */
    List<Map<String, Object>> rechargeExcel(Map map);

}

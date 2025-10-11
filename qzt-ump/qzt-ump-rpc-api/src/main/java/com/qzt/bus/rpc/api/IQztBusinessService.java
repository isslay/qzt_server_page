package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.model.QztBusiness;
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
public interface IQztBusinessService extends BaseService<QztBusiness> {

    Page find(Page pageModel);

    /**
     * 新建服务站商家
     *
     * @param qztApplyBusorder
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-14
     */
    QztBusiness addBusiness(QztApplyBusorder qztApplyBusorder);

    List<QztBusiness> findAllBussiness(Map<String, Object> params);

    /**
     * 根据userId查询服务站信息
     *
     * @param userId
     * @return com.qzt.bus.model.QztBusiness
     * @author Xiaofei
     * @date 2019-11-17
     */
    QztBusiness queryByUserId(Long userId);

    /**
     * 查询指定区的服务站数量
     *
     * @param areaCode 区code
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-12-12
     */
    Long findAreaBussinessSize(String areaCode);

    /**
     * 查询指定范围内的服务站数量
     *
     * @param busLong   经度
     * @param busLat    纬度
     * @param kilometer 千米数
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-12-12
     */
    Integer findScopeBussinessSize(String busLong, String busLat, Long kilometer);

}

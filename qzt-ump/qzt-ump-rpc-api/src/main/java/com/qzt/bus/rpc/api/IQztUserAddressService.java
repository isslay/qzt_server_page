package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztUserAddress;
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
public interface IQztUserAddressService extends BaseService<QztUserAddress> {

    Page<QztUserAddress> find(Page<QztUserAddress> pageModel);

    /**
     * 查询收货地址列表
     *
     * @param conditionMap
     * @return java.util.List<com.qzt.bus.model.QztUserAddress>
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztUserAddress> findList(Map conditionMap);

    /**
     * 根据userId查询地址
     *
     * @param userId
     * @return java.util.List<com.qzt.bus.model.QztUserAddress>
     * @author Xiaofei
     * @date 2019-11-17
     */
    List<Map> findListByUserId(Long userId);

    /**
     * 修改默认地址
     *
     * @param entity
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-11
     */
    Integer defaultArea(QztUserAddress entity);

    /**
     * 获取默认地址
     *
     * @param userId
     * @return com.qzt.bus.model.QztUserAddress
     * @author Xiaofei
     * @date 2019-11-11
     */
    QztUserAddress selectDefaultaccAddress(Long userId);

    /**
     * 下单处理地址信息
     *
     * @param addressId
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map<String, String> disposeAddressInfo(Long addressId);

}

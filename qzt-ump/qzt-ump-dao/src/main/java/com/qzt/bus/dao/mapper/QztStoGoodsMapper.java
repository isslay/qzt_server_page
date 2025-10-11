package com.qzt.bus.dao.mapper;


import com.alibaba.fastjson.JSONArray;
import com.qzt.bus.model.QztStoGoods;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-12-24
 */
public interface QztStoGoodsMapper extends BaseMapper<QztStoGoods> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-12-24
     */
    List<QztStoGoods> find(Map<String, Object> map);

    /**
     * 根据商品ID查询用户购物车商品
     *
     * @param qztStoGoods
     * @return com.qzt.bus.model.QztStoGoods
     * @author Xiaofei
     * @date 2019-12-27
     */
    QztStoGoods queryByGoodsId(QztStoGoods qztStoGoods);

    /**
     * 批量更新购物车商品信息
     *
     * @param stoGoodsList
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-12-29
     */
    Integer updateStoGoodsList(List<QztStoGoods> stoGoodsList);

    /**
     * 批量更新购物车商品为未选中
     *
     * @param userId
     * @return java.lang.Integer
     * @author
     * @date
     */
    Integer updateStoGoodsList1(@Param("userId") Long userId);

}

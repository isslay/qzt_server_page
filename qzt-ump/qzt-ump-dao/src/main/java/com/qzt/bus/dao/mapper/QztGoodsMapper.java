package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztGoods;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author snow
 * @since 2019-11-05
 */
public interface QztGoodsMapper extends BaseMapper<QztGoods> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author snow
     * @date 2019-11-05
     */
    List<QztGoods> find(Map<String, Object> map);

    /**
     * 减少库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer reduceGoodsRepertory(@Param("goodsId") Long goodsId, @Param("buyNum") Integer buyNum);

    /**
     * 增加库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer addGoodsRepertory(@Param("goodsId") Long goodsId, @Param("buyNum") Integer buyNum);


    /**
     * 增加销量
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer addGoodsSalesVolume(@Param("goodsId") Long goodsId, @Param("buyNum") Integer buyNum);

}

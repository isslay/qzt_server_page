package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztGoods;
import com.qzt.common.core.base.BaseService;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author snow
 * @since 2019-11-05
 */
public interface IQztGoodsService extends BaseService<QztGoods> {

    Page<QztGoods> find(Page<QztGoods> pageModel);

    /**
     * 减少库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer reduceGoodsRepertory(Long goodsId, Integer buyNum);

    /**
     * 增加库存
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer addGoodsRepertory(Long goodsId, Integer buyNum);

    /**
     * 增加销量
     *
     * @param goodsId 商品id
     * @param buyNum  购买数量
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer addGoodsSalesVolume(Long goodsId, Integer buyNum);

}

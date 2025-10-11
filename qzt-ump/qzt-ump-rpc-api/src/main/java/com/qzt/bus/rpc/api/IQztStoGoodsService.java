package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztStoGoods;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-12-24
 */
public interface IQztStoGoodsService extends BaseService<QztStoGoods> {

    Page<QztStoGoods> find(Page<QztStoGoods> pageModel);

    /**
     * 查询购物车商品
     *
     * @param pmap
     * @return java.util.List<com.qzt.bus.model.QztStoGoods>
     * @author Xiaofei
     * @date 2019-12-27
     */
    List<QztStoGoods> findList(Map pmap);

    /**
     * 根据商品ID查询用户购物车商品
     *
     * @param userId
     * @param goodsId
     * @return com.qzt.bus.model.QztStoGoods
     * @author Xiaofei
     * @date 2019-12-27
     */
    QztStoGoods queryByGoodsId(Long userId, Long goodsId);

    /**
     * 批量更新购物车商品信息
     *
     * @param stoGoodsList
     * @return boolean
     * @author Xiaofei
     * @date 2019-12-29
     */
    boolean updateStoGoodsList(List<QztStoGoods> stoGoodsList);

    boolean updateStoGoodsList1(Long userId);

    /**
     * 添加商品到购物车
     *
     * @param qztStoGoods
     * @return boolean
     * @author Xiaofei
     * @date 2019-12-30
     */
    boolean addGoods(QztStoGoods qztStoGoods);

    QztStoGoods addGoods1(QztStoGoods qztStoGoods);

    /**
     * 查询用户购物车有效商品
     *
     * @param userId
     * @param isSingleCommodity 是否单商品购买
     * @param orderNo
     * @return java.util.List<com.qzt.bus.model.QztStoGoods>
     * @author Xiaofei
     * @date 2019-12-30
     */
    List<QztStoGoods> getSettlementGoodsList(Long userId, String isSingleCommodity, String orderNo);

    /**
     * 查询用户订单商品列表
     *
     * @param userId
     * @param orderNo
     * @return java.util.List<com.qzt.bus.model.QztStoGoods>
     * @author Xiaofei
     * @date 2019-12-30
     */
    List<QztStoGoods> getOrderGoodsList(Long userId, String orderNo);

}

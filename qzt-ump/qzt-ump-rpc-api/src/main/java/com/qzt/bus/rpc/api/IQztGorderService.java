package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztGorder;
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
public interface IQztGorderService extends BaseService<QztGorder> {

    Page find(Page<QztGorder> pageModel);

    /**
     * 查询订单列表
     *
     * @param pmap
     * @return java.util.List<com.qzt.bus.model.QztGorder>
     * @author
     * @date 2019-11-20
     */
    List<QztGorder> findList(Map pmap);

    /**
     * 提交商品订单
     *
     * @param qztGorder
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map subGorder(QztGorder qztGorder) throws Exception;

    /**
     * 商城订单支付
     *
     * @param qztGorder
     * @param payPwd
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map pay0rder(QztGorder qztGorder, String payPwd) throws Exception;

    /**
     * 根据订单编号与用户ID查询订单
     *
     * @param qztGorder
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztGorder queryByOrederNoUserId(QztGorder qztGorder);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztGorder queryByOrederNo(String orderNo);

    /**
     * 支付回调更新订单状态
     *
     * @param orderNo
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-12
     */
    boolean orderPayBack(String orderNo);

    /**
     * 取消订单
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author
     * @date
     */
    void cancellationOfOrder(QztGorder qztGorder) throws Exception;
    /**
     * 取消订单（已支付待发货）
     *
     * @param qztGorder
     * @return java.lang.Integer
     * @author
     * @date
     */
    int cancellationOfOrder1(QztGorder qztGorder) throws Exception;

    /**
     * 用户确认收货
     *
     * @param qztGorder
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-14
     */
    boolean confirmReceipt(QztGorder qztGorder) throws Exception;

    /**
     * 更新是否可申请服务状态
     *
     * @param orderNo    订单编号
     * @param isServe    是否可申请服务（是Y、否N）
     * @param operatorId 操作人ID
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-20
     */
    boolean updateIsServe(String orderNo, String isServe, Long operatorId) throws Exception;

    /**
     * 自提
     *
     * @param entity
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-23
     */
    boolean offlineDistribution(QztGorder entity);

    /**
     * 查询可申请服务订单
     *
     * @param pmap
     * @return java.util.List<com.qzt.bus.model.QztGorder>
     * @author Xiaofei
     * @date 2019-11-28
     */
    List<QztGorder> getCanServiceOrderList(Map pmap);

    /**
     * 商品订单导出数据集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Xiaofei
     * @date 2019-12-04
     */
    List<Map<String, Object>> gorderExcel(Map map);

    /**
     * 商家确认核销
     *
     * @param qztGorder
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-12-13
     */
    boolean confirmTheSettlement(QztGorder qztGorder);

    /**
     * 根据商家ID与核销码查询订单
     *
     * @param qztGorder
     * @return com.qzt.bus.model.QztGorder
     * @author Xiaofei
     * @date 2019-12-13
     */
    QztGorder findChargeOffOrderInfo(QztGorder qztGorder);

    /**
     * 再来一单，将该单商品添加购物车
     *
     * @param qztGorder
     * @return boolean
     * @author Xiaofei
     * @date 2020-01-07
     */
    boolean recurOrder(QztGorder qztGorder) throws Exception;

}

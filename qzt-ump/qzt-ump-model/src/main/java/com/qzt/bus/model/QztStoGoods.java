package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

import java.util.Date;

/**
 * <p>
 * 购物车/订单  商品关系表
 * </p>
 *
 * @author Xiaofei
 * @since 2019-12-24
 */
@TableName("dgm_qzt_sto_goods")
public class QztStoGoods extends BaseModel {

    private static final long serialVersionUID = 1L;


    public QztStoGoods(Long userId, Long goodsId, Integer buyNum) {
        this.userId = userId;
        this.goodsId = goodsId;
        this.buyNum = buyNum;
        this.setCreateBy(userId);
        this.setUpdateBy(userId);
    }

    public QztStoGoods(Long id, Long goodsId, Integer buyNum, String isPitchon) {
        this.setId(id);
        this.goodsId = goodsId;
        this.buyNum = buyNum;
        this.isPitchon = isPitchon;
    }

    public QztStoGoods(Long id, String orderNo, String isCart, Long shopId, String goodsName, String goodsRemark, String goodsPic, Long goodsPrice, Long postage, Long totalPrice, String couponStatus, String isServe,String goodsSpec) {
        this.setId(id);
        this.setUpdateTime(new Date());
        this.orderNo = orderNo;
        this.isCart = isCart;
        this.shopId = shopId;
        this.goodsName = goodsName;
        this.goodsRemark = goodsRemark;
        this.goodsPic = goodsPic;
        this.goodsPrice = goodsPrice;
        this.postage = postage;
        this.totalPrice = totalPrice;
        this.couponStatus = couponStatus;
        this.isServe = isServe;
        this.goodsSpec = goodsSpec;
    }

    public QztStoGoods() {

    }

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 是否单商品购买（否N、是Y）
     */
    @TableField("is_single_commodity")
    private String isSingleCommodity;
    /**
     * 是否处于购物车中（否N、是Y）
     */
    @TableField("is_cart")
    private String isCart;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;

    @TableField("goods_spec")
    private String goodsSpec;
    /**
     * 商品的说明
     */
    @TableField("goods_remark")
    private String goodsRemark;
    /**
     * 商品缩略图
     */
    @TableField("goods_pic")
    private String goodsPic;
    /**
     * 商品单价（单位：分）
     */
    @TableField("goods_price")
    private Long goodsPrice;
    /**
     * 邮费（单位：分）
     */
    private Long postage;
    /**
     * 购买数量
     */
    @TableField("buy_num")
    private Integer buyNum;
    /**
     * 商品总金额RMB（单位：分）
     */
    @TableField("total_price")
    private Long totalPrice;
    /**
     * 是否禁止使用抵扣券(是Y、否N)
     */
    @TableField("coupon_status")
    private String couponStatus;
    /**
     * 是否可申请服务（是Y、否N）
     */
    @TableField("is_serve")
    private String isServe;
    /**
     * 是否已删除（否N、是Y）
     */
    @TableField("is_del")
    private String isDel;
    /**
     * 是否已选中（否N、是Y）
     */
    @TableField("is_pitchon")
    private String isPitchon;

    public String getGoodsSpec() {
        return goodsSpec;
    }

    public void setGoodsSpec(String goodsSpec) {
        this.goodsSpec = goodsSpec;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIsSingleCommodity() {
        return isSingleCommodity;
    }

    public void setIsSingleCommodity(String isSingleCommodity) {
        this.isSingleCommodity = isSingleCommodity;
    }

    public String getIsCart() {
        return isCart;
    }

    public void setIsCart(String isCart) {
        this.isCart = isCart;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public Long getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Long goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getPostage() {
        return postage;
    }

    public void setPostage(Long postage) {
        this.postage = postage;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getIsServe() {
        return isServe;
    }

    public void setIsServe(String isServe) {
        this.isServe = isServe;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getIsPitchon() {
        return isPitchon;
    }

    public void setIsPitchon(String isPitchon) {
        this.isPitchon = isPitchon;
    }

    @Override
    public String toString() {
        return "QztStoGoods{" +
                ", userId=" + userId +
                ", isSingleCommodity=" + isSingleCommodity +
                ", isCart=" + isCart +
                ", orderNo=" + orderNo +
                ", goodsId=" + goodsId +
                ", shopId=" + shopId +
                ", goodsName=" + goodsName +
                ", goodsRemark=" + goodsRemark +
                ", goodsPic=" + goodsPic +
                ", goodsPrice=" + goodsPrice +
                ", postage=" + postage +
                ", buyNum=" + buyNum +
                ", totalPrice=" + totalPrice +
                ", couponStatus=" + couponStatus +
                ", isServe=" + isServe +
                ", isDel=" + isDel +
                ", isPitchon=" + isPitchon +
                ", goodsSpec=" + goodsSpec +
                "}";
    }
}

package com.qzt.bus.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 *
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@TableName("dgm_qzt_user_coupon")
public class QztUserCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztUserCoupon(String state, String orderNo, Long cashCouponId, Long userId) {
        this.state = state;
        this.orderNo = orderNo;
        this.setId(cashCouponId);
        this.setUpdateBy(userId);
        this.setUpdateTime(new Date());
    }

    public QztUserCoupon(Long cashCouponId, Long userId) {
        this.setId(cashCouponId);
        this.setUpdateBy(userId);
        this.setUpdateTime(new Date());
    }

    public QztUserCoupon(Long couponId, Long userId,String state) {
        this.setCouponId(couponId);
        this.setUserId(userId);
        this.setState(state);
    }

    public QztUserCoupon(Long couponId, Long userId,String dateToday,int i) {
        this.setCouponId(couponId);
        this.setUserId(userId);
        this.setDateToday(dateToday);
    }

    public QztUserCoupon() {

    }

    /**
     * 使用状态（0待激活 1待使用  2 已使用  3已过期）
     */
    private String state;
    /**
     * 券金额（单位：分）
     */
    @TableField("coupon_money")
    private Long couponMoney;

    /**
     *
     */
    @TableField("coupon_id")
    private Long couponId;

    /**
     *
     */
    @TableField("target_money")
    private Long targetMoney;
    /**
     * 激活日期
     */
    @TableField("active_time")
    private Date activeTime;
    /**
     * 使用日期
     */
    @TableField("used_time")
    private Date usedTime;
    /**
     * 到期日期
     */
    @TableField("over_time")
    private Date overTime;
    /**
     * 使用商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 使用商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 适用范围（满减01，新手券02，商品券03、活动券04、积分券05、）
     */
    @TableField("goods_type")
    private String goodsType;
    /**
     * 对应订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 领券人ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 赠送用户
     */
    @TableField("give_user_id")
    private Long giveUserId;

    private String dateToday;

    private String couponRemark;

    public String getCouponRemark() {
        return couponRemark;
    }

    public void setCouponRemark(String couponRemark) {
        this.couponRemark = couponRemark;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getDateToday() {
        return dateToday;
    }

    public void setDateToday(String dateToday) {
        this.dateToday = dateToday;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(Long couponMoney) {
        this.couponMoney = couponMoney;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Date usedTime) {
        this.usedTime = usedTime;
    }

    public Date getOverTime() {
        return overTime;
    }

    public void setOverTime(Date overTime) {
        this.overTime = overTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGiveUserId() {
        return giveUserId;
    }

    public void setGiveUserId(Long giveUserId) {
        this.giveUserId = giveUserId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public Long getTargetMoney() {
        return targetMoney;
    }

    public void setTargetMoney(Long targetMoney) {
        this.targetMoney = targetMoney;
    }

    @Override
    public String toString() {
        return "QztUserCoupon{" +
                ", state=" + state +
                ", couponMoney=" + couponMoney +
                ", targetMoney=" + targetMoney +
                ", couponId=" + couponId +
                ", activeTime=" + activeTime +
                ", usedTime=" + usedTime +
                ", overTime=" + overTime +
                ", goodsId=" + goodsId +
                ", goodsType=" + goodsType +
                ", orderNo=" + orderNo +
                ", userId=" + userId +
                ", giveUserId=" + giveUserId +
                ", goodsName=" + goodsName +
                ", couponRemark=" + couponRemark +
                "}";
    }
}

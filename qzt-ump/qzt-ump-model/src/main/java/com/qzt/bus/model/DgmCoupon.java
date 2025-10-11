package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
@TableName("dgm_coupon")
public class DgmCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 券金额（单位：分）
     */
	@TableField("coupon_money")
	private Long couponMoney;
    /**
     * 目标金额（分）
     */
	@TableField("target_money")
	private Long targetMoney;
    /**
     * 可以领取的数量
     */
	private Integer number;
    /**
     * 状态(0开启 1关闭)
     */
	private Integer status;
	/**
	 * 分类(1登录,2新用户,3商品,4节日)
	 */
	private Integer couponType;

	private String couponRemark;
	/**
	 * 药品ERP id
	 */
	@TableField("target_id")
	private String targetId;
	/**
	 * 商城商品id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 开始日期(yyyy-MM-dd)
	 */
	@TableField("coupon_begin")
	private String couponBegin;

	/**
	 * 结束日期(yyyy-MM-dd)
	 */
	@TableField("coupon_end")
	private String couponEnd;

	public String getCouponBegin() {
		return couponBegin;
	}

	public void setCouponBegin(String couponBegin) {
		this.couponBegin = couponBegin;
	}

	public String getCouponEnd() {
		return couponEnd;
	}

	public void setCouponEnd(String couponEnd) {
		this.couponEnd = couponEnd;
	}

	public String getTargetId() {
		return targetId;
	}

	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getCouponRemark() {
		return couponRemark;
	}

	public void setCouponRemark(String couponRemark) {
		this.couponRemark = couponRemark;
	}

	public Integer getCouponType() {
		return couponType;
	}

	public void setCouponType(Integer couponType) {
		this.couponType = couponType;
	}

	public Long getCouponMoney() {
		return couponMoney;
	}

	public void setCouponMoney(Long couponMoney) {
		this.couponMoney = couponMoney;
	}

	public Long getTargetMoney() {
		return targetMoney;
	}

	public void setTargetMoney(Long targetMoney) {
		this.targetMoney = targetMoney;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "DgmCoupon{" +
			", couponMoney=" + couponMoney +
			", targetMoney=" + targetMoney +
			", number=" + number +
			", status=" + status +
			", couponType=" + couponType +
			", couponRemark=" + couponRemark +
			", targetId=" + targetId +
			", goodsId=" + goodsId +
			", couponBegin=" + couponBegin +
			", couponEnd=" + couponEnd +
			"}";
	}
}

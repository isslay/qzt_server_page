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
 * @since 2023-10-18
 */
@TableName("dgm_goods_coupon")
public class DgmGoodsCoupon extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 单据ID
     */
	@TableField("bill_no")
	private String billNo;
    /**
     * 零售单明细序号
     */
	@TableField("bill_sn")
	private String billSn;
    /**
     * 商品ID
     */
	@TableField("goods_id")
	private String goodsId;
    /**
     * 商品名称
     */
	@TableField("goods_name")
	private String goodsName;
    /**
     * 用户真实姓名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 用户手机号码
     */
	@TableField("user_mobile")
	private String userMobile;
    /**
     * 优惠券id
     */
	@TableField("coupon_id")
	private Long couponId;
    /**
     * 状态(0初始1完毕)
     */
	@TableField("gc_status")
	private String gcStatus;


	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getBillSn() {
		return billSn;
	}

	public void setBillSn(String billSn) {
		this.billSn = billSn;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getGcStatus() {
		return gcStatus;
	}

	public void setGcStatus(String gcStatus) {
		this.gcStatus = gcStatus;
	}

	@Override
	public String toString() {
		return "DgmGoodsCoupon{" +
			", billNo=" + billNo +
			", billSn=" + billSn +
			", goodsId=" + goodsId +
			", goodsName=" + goodsName +
			", userName=" + userName +
			", userMobile=" + userMobile +
			", couponId=" + couponId +
			", gcStatus=" + gcStatus +
			"}";
	}
}

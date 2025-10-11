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
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_service_order")
public class QztServiceOrder extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 来源 (PC：1、IOS：2、Android：3、H5：4、小程序：5)
     */
	@TableField("source_")
	private String source;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 用户手机号
     */
	@TableField("user_tel")
	private String userTel;
    /**
     * 服务站ID
     */
	@TableField("busniess_id")
	private Long busniessId;

	@TableField(exist = false)
	private String busniessName;
    /**
     * 订单编号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 订单状态（01待确认、03待服务、05已到店、07已完成、90已驳回、96异常订单）
     */
	@TableField("order_state")
	private String orderState;
    /**
     * 联系人
     */
	@TableField("contacts_name")
	private String contactsName;
    /**
     * 联系电话
     */
	@TableField("contacts_tel")
	private String contactsTel;
    /**
     * 收入金额rmb
     */
	@TableField("share_money")
	private Long shareMoney;
    /**
     * 完成时间
     */
	@TableField("finish_time")
	private Date finishTime;
	/**
	 * 疾病名称
	 */
	@TableField("disease")
	private String disease;

	public String getBusniessName() {
		return busniessName;
	}

	public void setBusniessName(String busniessName) {
		this.busniessName = busniessName;
	}

	public String getDisease() {
		return disease;
	}

	public void setDisease(String disease) {
		this.disease = disease;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserTel() {
		return userTel;
	}

	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}

	public Long getBusniessId() {
		return busniessId;
	}

	public void setBusniessId(Long busniessId) {
		this.busniessId = busniessId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}

	public String getContactsTel() {
		return contactsTel;
	}

	public void setContactsTel(String contactsTel) {
		this.contactsTel = contactsTel;
	}

	public Long getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(Long shareMoney) {
		this.shareMoney = shareMoney;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	@Override
	public String toString() {
		return "QztServiceOrder{" +
			", source=" + source +
			", userId=" + userId +
			", userTel=" + userTel +
			", busniessId=" + busniessId +
			", orderNo=" + orderNo +
			", orderState=" + orderState +
			", contactsName=" + contactsName +
			", contactsTel=" + contactsTel +
			", shareMoney=" + shareMoney +
			", finishTime=" + finishTime +
			", disease=" + disease +
			", busniessName=" + busniessName +
			"}";
	}
}

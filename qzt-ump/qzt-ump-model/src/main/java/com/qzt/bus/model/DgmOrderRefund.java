package com.qzt.bus.model;

import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 订单--退款处理_
 * </p>
 *
 * @author snow
 * @since 2023-11-06
 */
@TableName("dgm_order_refund")
public class DgmOrderRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 订单ID
     */
	@TableField("order_id")
	private String orderId;
    /**
     * 商户退款单号
     */
	@TableField("out_refund_id")
	private String outRefundId;
    /**
     * 用户ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 用户姓名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 退款原因
     */
	@TableField("refund_reason")
	private String refundReason;
    /**
     * 需要退回的款项
     */
	@TableField("pay_money")
	private Integer payMoney;
    /**
     * 协商的需要扣除的手续费
     */
	@TableField("service_cost")
	private Integer serviceCost;
    /**
     * 卖家处理时间
     */
	@TableField("handle_time")
	private Date handleTime;
    /**
     * 退款成功时间
     */
	@TableField("finish_time")
	private Date finishTime;
    /**
     * 状态
     */
	@TableField("curr_status")
	private Integer currStatus;
    /**
     * 微信退款ID
     */
	@TableField("refund_id")
	private String refundId;
    /**
     * 异步通知报文
     */
	@TableField("notify_packet")
	private String notifyPacket;
    /**
     * 退款状态
     */
	@TableField("refund_status")
	private String refundStatus;
    /**
     * 退款入账账户 取当前退款单的退款入账方
     */
	@TableField("user_received_account")
	private String userReceivedAccount;
	@TableField("transaction_id")
	private String transactionId;
    /**
     * 申请时间
     */
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOutRefundId() {
		return outRefundId;
	}

	public void setOutRefundId(String outRefundId) {
		this.outRefundId = outRefundId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public Integer getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Integer payMoney) {
		this.payMoney = payMoney;
	}

	public Integer getServiceCost() {
		return serviceCost;
	}

	public void setServiceCost(Integer serviceCost) {
		this.serviceCost = serviceCost;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Integer getCurrStatus() {
		return currStatus;
	}

	public void setCurrStatus(Integer currStatus) {
		this.currStatus = currStatus;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getNotifyPacket() {
		return notifyPacket;
	}

	public void setNotifyPacket(String notifyPacket) {
		this.notifyPacket = notifyPacket;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getUserReceivedAccount() {
		return userReceivedAccount;
	}

	public void setUserReceivedAccount(String userReceivedAccount) {
		this.userReceivedAccount = userReceivedAccount;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "DgmOrderRefund{" +
			", id=" + id +
			", orderId=" + orderId +
			", outRefundId=" + outRefundId +
			", userId=" + userId +
			", userName=" + userName +
			", refundReason=" + refundReason +
			", payMoney=" + payMoney +
			", serviceCost=" + serviceCost +
			", handleTime=" + handleTime +
			", finishTime=" + finishTime +
			", currStatus=" + currStatus +
			", refundId=" + refundId +
			", notifyPacket=" + notifyPacket +
			", refundStatus=" + refundStatus +
			", userReceivedAccount=" + userReceivedAccount +
			", transactionId=" + transactionId +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			"}";
	}
}

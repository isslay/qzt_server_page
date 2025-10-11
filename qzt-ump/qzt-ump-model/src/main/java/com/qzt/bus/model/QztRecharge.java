package com.qzt.bus.model;

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
 * @since 2019-11-22
 */
@TableName("dgm_qzt_recharge")
public class QztRecharge extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 充值用户id
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 充值手机号
     */
	@TableField("user_tel")
	private String userTel;
    /**
     * 充值用户姓名
     */
	@TableField("user_name")
	private String userName;
    /**
     * 充值金额
     */
	@TableField("top_up_money")
	private Long topUpMoney;
    /**
     * 操作人id
     */
	@TableField("audit_user_id")
	private Long auditUserId;
    /**
     * 操作人名称
     */
	@TableField("audit_user_name")
	private String auditUserName;
    /**
     * 操作时间
     */
	@TableField("audit_time")
	private Date auditTime;
    /**
     * 操作状态（00 待提交 10:待确认、20:待完成、01:二审 驳回、11:三审驳回 90已完成）
     */
	@TableField("audit_state")
	private String auditState;
    /**
     * 操作描述
     */
	@TableField("audit_remark")
	private String auditRemark;


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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTopUpMoney() {
		return topUpMoney;
	}

	public void setTopUpMoney(Long topUpMoney) {
		this.topUpMoney = topUpMoney;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getAuditState() {
		return auditState;
	}

	public void setAuditState(String auditState) {
		this.auditState = auditState;
	}

	public String getAuditRemark() {
		return auditRemark;
	}

	public void setAuditRemark(String auditRemark) {
		this.auditRemark = auditRemark;
	}

	@Override
	public String toString() {
		return "QztRecharge{" +
			", userId=" + userId +
			", userTel=" + userTel +
			", userName=" + userName +
			", topUpMoney=" + topUpMoney +
			", auditUserId=" + auditUserId +
			", auditUserName=" + auditUserName +
			", auditTime=" + auditTime +
			", auditState=" + auditState +
			", auditRemark=" + auditRemark +
			"}";
	}
}

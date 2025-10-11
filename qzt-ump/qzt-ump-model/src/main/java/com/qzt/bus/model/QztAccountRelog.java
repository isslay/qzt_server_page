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
 * @since 2019-11-11
 */
@TableName("dgm_qzt_account_relog")
public class QztAccountRelog extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 获得金额
     */
	@TableField("re_money")
	private Long reMoney;
    /**
     * 类型（0推广金额  1分享金额）
     */
	@TableField("re_type")
	private Integer reType;
    /**
     * 状态（0 待激活 1有效 2无效）
     */
	private Integer state;

	/**
	 * 赠送金额
	 */
	@TableField("give_money")
	private Long giveMoney;

    /**
     * 转换金额
     */
	@TableField("change_money")
	private Long changeMoney;
    /**
     * 操作时间
     */
	@TableField("control_date")
	private Date controlDate;
    /**
     * 业务订单
     */
	@TableField("bus_id")
	private String busId;
    /**
     * 业务类型 01注册  02 推荐注册  03订单推荐  04 服务订单
     */
	@TableField("bus_type")
	private String busType;

	/**
	 * 用户ID
	 */
	@TableField("user_id")
	private String userId;
    /**
     * 描述
     */
	private String remark;


	public Long getReMoney() {
		return reMoney;
	}

	public void setReMoney(Long reMoney) {
		this.reMoney = reMoney;
	}

	public Integer getReType() {
		return reType;
	}

	public void setReType(Integer reType) {
		this.reType = reType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(Long changeMoney) {
		this.changeMoney = changeMoney;
	}

	public Date getControlDate() {
		return controlDate;
	}

	public void setControlDate(Date controlDate) {
		this.controlDate = controlDate;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getGiveMoney() {
		return giveMoney;
	}

	public void setGiveMoney(Long giveMoney) {
		this.giveMoney = giveMoney;
	}

	@Override
	public String toString() {
		return "QztAccountRelog{" +
			", reMoney=" + reMoney +
			", reType=" + reType +
			", state=" + state +
			", changeMoney=" + changeMoney +
			", controlDate=" + controlDate +
			", busId=" + busId +
			", busType=" + busType +
			", remark=" + remark +
			"}";
	}
}

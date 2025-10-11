package com.qzt.bus.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 积分记录表
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@TableName("dgm_integral_record")
public class DgmIntegralRecord extends BaseModel {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id_", type = IdType.AUTO)
	private Long id;

    /**
     * 用户ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 积分值
     */
	@TableField("int_value")
	private Integer intValue;
    /**
     * 激活日期
     */
	@TableField("active_time")
	private Date activeTime;
    /**
     * 到期日期
     */
	@TableField("over_time")
	private Date overTime;
    /**
     * 类别(1下单2分享下单21积分兑换99过期)
     */
	@TableField("int_type")
	private Integer intType;
    /**
     * 说明
     */
	@TableField("int_remark")
	private String intRemark;
    /**
     * 是否删除
     */
	@TableField("is_del")
	private String isDel;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public Integer getIntType() {
		return intType;
	}

	public void setIntType(Integer intType) {
		this.intType = intType;
	}

	public String getIntRemark() {
		return intRemark;
	}

	public void setIntRemark(String intRemark) {
		this.intRemark = intRemark;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "DgmIntegralRecord{" +
			", userId=" + userId +
			", intValue=" + intValue +
			", activeTime=" + activeTime +
			", overTime=" + overTime +
			", intType=" + intType +
			", intRemark=" + intRemark +
			", isDel=" + isDel +
			"}";
	}
}

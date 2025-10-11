package com.qzt.bus.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 扣减积分详情表
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@TableName("dgm_reduce_integral_detail")
public class DgmReduceIntegralDetail extends BaseModel {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id_", type = IdType.AUTO)
	private Long id;

    /**
     * 用户ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 扣减积分记录ID
     */
	@TableField("record_id")
	private Integer recordId;
    /**
     * 新增积分记录ID
     */
	@TableField("new_id")
	private Integer newId;
    /**
     * 扣减积分
     */
	@TableField("red_value")
	private Integer redValue;
    /**
     * 到期日期
     */
	@TableField("over_time")
	private Date overTime;
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

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getNewId() {
		return newId;
	}

	public void setNewId(Integer newId) {
		this.newId = newId;
	}

	public Integer getRedValue() {
		return redValue;
	}

	public void setRedValue(Integer redValue) {
		this.redValue = redValue;
	}

	public Date getOverTime() {
		return overTime;
	}

	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "DgmReduceIntegralDetail{" +
			", userId=" + userId +
			", recordId=" + recordId +
			", newId=" + newId +
			", redValue=" + redValue +
			", overTime=" + overTime +
			", isDel=" + isDel +
			"}";
	}
}

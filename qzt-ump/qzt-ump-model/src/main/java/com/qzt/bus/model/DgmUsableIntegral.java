package com.qzt.bus.model;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 可用积分表
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@TableName("dgm_usable_integral")
public class DgmUsableIntegral extends BaseModel {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id_", type = IdType.AUTO)
	private Long id;

	@TableField("record_id")
	private Integer recordId;
	@TableField("user_id")
	private Integer userId;
    /**
     * 积分值
     */
	@TableField("use_value")
	private Integer useValue;
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

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getUseValue() {
		return useValue;
	}

	public void setUseValue(Integer useValue) {
		this.useValue = useValue;
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
		return "DgmUsableIntegral{" +
			", recordId=" + recordId +
			", userId=" + userId +
			", useValue=" + useValue +
			", overTime=" + overTime +
			", isDel=" + isDel +
			"}";
	}
}

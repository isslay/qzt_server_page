package com.qzt.ump.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
@TableName("dgm_sys_dic")
public class SysDic extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 字典类型
     */
	@TableField("dic_type")
	private String dicType;
    /**
     * 字典name
     */
	@TableField("dic_name")
	private String dicName;
    /**
     * 备注
     */
	private String remark;
    /**
     * 启用状态（0启用，1关闭）
     */
	@TableField("enable_")
	private String enable;
	/**
     * 是否删除（0正常，1删除）
     */
	@TableField("is_del")
	private String isDel;



	public String getDicType() {
		return dicType;
	}

	public void setDicType(String dicType) {
		this.dicType = dicType;
	}

	public String getDicName() {
		return dicName;
	}

	public void setDicName(String dicName) {
		this.dicName = dicName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "SysDic{" +
			", dicType=" + dicType +
			", dicName=" + dicName +
			", remark=" + remark +
			", enable=" + enable +
			", isDel=" + isDel +
			"}";
	}
}

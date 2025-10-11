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
@TableName("dgm_sys_dic_sublist")
public class SysDicSublist extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 父级id
     */
	@TableField("parent_id")
	private String parentId;
    /**
     * 关键字
     */
	@TableField("code_")
	private String code;
    /**
     * 关键字名称
     */
	@TableField("code_text")
	private String codeText;
    /**
     * 备注
     */
	@TableField("remark")
	private String remark;
    /**
     * 排序
     */
	@TableField("sort_no")
	private Integer sortNo;
    /**
     * 启用状态（0启用，1关闭）
     */
	@TableField("enable_")
	private String enable;



	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeText() {
		return codeText;
	}

	public void setCodeText(String codeText) {
		this.codeText = codeText;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public String getEnable() {
		return enable;
	}

	public void setEnable(String enable) {
		this.enable = enable;
	}

	@Override
	public String toString() {
		return "SysDicsSublist{" +
			", parentId=" + parentId +
			", code=" + code +
			", codeText=" + codeText +
			", remark=" + remark +
			", sortNo=" + sortNo +
			", enable=" + enable +
			"}";
	}
}

package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 商品分类
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
@TableName("dgm_goods_class")
public class DgmGoodsClass extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 分类名称
     */
	private String name;
    /**
     * 上级部门编号
     */
	@TableField("parent_id")
	private Long parentId;
    /**
     * 排序号
     */
	@TableField("sort_no")
	private Integer sortNo;
    /**
     * 叶子节点(0:树枝节点;1:叶子节点)
     */
	@TableField("leaf_")
	private Integer leaf;
    /**
     * 是否删除(0否；1是)
     */
	@TableField("is_del")
	private Integer isDel;

	@TableField("remark_")
	private String remark;

	@TableField("pic_url")
	private String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Integer getLeaf() {
		return leaf;
	}

	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	@Override
	public String toString() {
		return "DgmGoodsClass{" +
			", name=" + name +
			", parentId=" + parentId +
			", sortNo=" + sortNo +
			", leaf=" + leaf +
			", isDel=" + isDel +
			", remark=" + remark +
			", picUrl=" + picUrl +
			"}";
	}
}

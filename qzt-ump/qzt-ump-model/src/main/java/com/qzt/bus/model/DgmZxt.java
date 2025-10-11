package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 专孝堂内容
 * </p>
 *
 * @author snow
 * @since 2023-09-27
 */
@TableName("dgm_zxt")
public class DgmZxt extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
	@TableField("zxt_name")
	private String zxtName;
    /**
     * 分类id
     */
	@TableField("class_id")
	private Long classId;
    /**
     * 分类名称
     */
	@TableField("class_name")
	private String className;
    /**
     * 详情
     */
	private String content;
    /**
     * 商品状态（0待上架，1上架，2下架,-1删除）
     */
	private String state;


	public String getZxtName() {
		return zxtName;
	}

	public void setZxtName(String zxtName) {
		this.zxtName = zxtName;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "DgmZxt{" +
			", zxtName=" + zxtName +
			", classId=" + classId +
			", className=" + className +
			", content=" + content +
			", state=" + state +
			"}";
	}
}

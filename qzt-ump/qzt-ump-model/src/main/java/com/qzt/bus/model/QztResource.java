package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-26
 */
@TableName("qzt_resource")
public class QztResource extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 类型（1音频2视频）
     */
	private String type;
    /**
     * 标题
     */
	private String title;
    /**
     * 地址
     */
	private String url;
    /**
     * 备注
     */
	private String remark;


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "QztResource{" +
			", type=" + type +
			", title=" + title +
			", url=" + url +
			", remark=" + remark +
			"}";
	}
}

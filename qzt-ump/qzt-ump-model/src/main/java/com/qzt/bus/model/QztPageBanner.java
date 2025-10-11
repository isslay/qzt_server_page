package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-25
 */
@TableName("dgm_qzt_page_banner")
public class QztPageBanner extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 类型（1：企业文化，2：健康课堂，3：创业平台，4商学院 5成功案例）
     */
	@TableField("banner_type")
	private String bannerType;
    /**
     * 类型名称
     */
	@TableField("type_name")
	private String typeName;
    /**
     * 图片地址
     */
	@TableField("pic_url")
	private String picUrl;
    /**
     * 跳转地址
     */
	@TableField("link_url")
	private String linkUrl;
    /**
     * 备注
     */
	private String remark;


	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "QztPageBanner{" +
			", bannerType=" + bannerType +
			", typeName=" + typeName +
			", picUrl=" + picUrl +
			", linkUrl=" + linkUrl +
			", remark=" + remark +
			"}";
	}
}

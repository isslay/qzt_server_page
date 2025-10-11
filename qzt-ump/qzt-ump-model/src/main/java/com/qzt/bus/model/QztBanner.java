package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author snow
 * @since 2019-11-07
 */
@TableName("dgm_qzt_banner")
public class QztBanner extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 类型（1：首页，2：明细）
     */
	@TableField("banner_type")
	private String bannerType;
    /**
     * WX,IOS,AN
     */
	@TableField("banner_source")
	private String bannerSource;
    /**
     * 顺序名称
     */
	@TableField("banner_num")
	private Integer bannerNum;
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
    /**
     * 状态（01：上架；02 下架）
     */
	private String state;

	/**
	 * 说明明细
	 */
	@TableField("content")
	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBannerType() {
		return bannerType;
	}

	public void setBannerType(String bannerType) {
		this.bannerType = bannerType;
	}

	public String getBannerSource() {
		return bannerSource;
	}

	public void setBannerSource(String bannerSource) {
		this.bannerSource = bannerSource;
	}

	public Integer getBannerNum() {
		return bannerNum;
	}

	public void setBannerNum(Integer bannerNum) {
		this.bannerNum = bannerNum;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "QztBanner{" +
			", bannerType=" + bannerType +
			", bannerSource=" + bannerSource +
			", bannerNum=" + bannerNum +
			", picUrl=" + picUrl +
			", linkUrl=" + linkUrl +
			", remark=" + remark +
			", state=" + state +
			", content=" + content +
			"}";
	}
}

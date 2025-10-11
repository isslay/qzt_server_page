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
@TableName("dgm_qzt_health_info")
public class QztHealthInfo extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 课堂名称
     */
	@TableField("info_title")
	private String infoTitle;
    /**
     * 课堂图片
     */
	@TableField("info_pic")
	private String infoPic;
    /**
     * 说明
     */
	@TableField("info_remark")
	private String infoRemark;
    /**
     * 外链商品ID
     */
	@TableField("link_goods_id")
	private Long linkGoodsId;
    /**
     * 状态（0待上线，1上线，2下线）
     */
	private String state;

	/**
	 * 排序
	 */
	@TableField("order_num")
	private Long orderNum;

	/**
	 * 类型（1：企业文化，2：健康课堂，3：创业平台，4商学院 5成功案例）
	 */
	@TableField("info_type")
	private Long infoType;

	@TableField("info_pic1")
	private String infoPic1;

	@TableField("info_pic2")
	private String infoPic2;

	/**
	 * 副标题
	 */
	@TableField("info_title1")
	private String infoTitle1;

	public String getInfoTitle1() {
		return infoTitle1;
	}

	public void setInfoTitle1(String infoTitle1) {
		this.infoTitle1 = infoTitle1;
	}

	public Long getInfoType() {
		return infoType;
	}

	public void setInfoType(Long infoType) {
		this.infoType = infoType;
	}

	public String getInfoPic1() {
		return infoPic1;
	}

	public void setInfoPic1(String infoPic1) {
		this.infoPic1 = infoPic1;
	}

	public String getInfoPic2() {
		return infoPic2;
	}

	public void setInfoPic2(String infoPic2) {
		this.infoPic2 = infoPic2;
	}

	public Long getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Long orderNum) {
		this.orderNum = orderNum;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getInfoPic() {
		return infoPic;
	}

	public void setInfoPic(String infoPic) {
		this.infoPic = infoPic;
	}

	public String getInfoRemark() {
		return infoRemark;
	}

	public void setInfoRemark(String infoRemark) {
		this.infoRemark = infoRemark;
	}

	public Long getLinkGoodsId() {
		return linkGoodsId;
	}

	public void setLinkGoodsId(Long linkGoodsId) {
		this.linkGoodsId = linkGoodsId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "QztHealthInfo{" +
			", infoTitle=" + infoTitle +
			", infoTitle1=" + infoTitle1 +
			", infoPic=" + infoPic +
			", infoPic1=" + infoPic1 +
			", infoPic2=" + infoPic2 +
			", infoRemark=" + infoRemark +
			", linkGoodsId=" + linkGoodsId +
			", state=" + state +
			", orderNum=" + orderNum +
			", infoType=" + infoType +
			"}";
	}
}

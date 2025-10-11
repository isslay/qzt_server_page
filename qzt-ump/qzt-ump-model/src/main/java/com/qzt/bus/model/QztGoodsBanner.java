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
 * @since 2019-11-06
 */
@TableName("dgm_qzt_goods_banner")
public class QztGoodsBanner extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 业务ID
     */
	@TableField("bus_id")
	private Long busId;
    /**
     * 名称
     */
	@TableField("banner_name")
	private String bannerName;
    /**
     * 图片地址
     */
	@TableField("banner_url")
	private String bannerUrl;
    /**
     * 排序
     */
	@TableField("order_num")
	private Integer orderNum;
    /**
     * 状态
     */
	private String state;


	public Long getBusId() {
		return busId;
	}

	public void setBusId(Long busId) {
		this.busId = busId;
	}

	public String getBannerName() {
		return bannerName;
	}

	public void setBannerName(String bannerName) {
		this.bannerName = bannerName;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "QztGoodsBanner{" +
			", busId=" + busId +
			", bannerName=" + bannerName +
			", bannerUrl=" + bannerUrl +
			", orderNum=" + orderNum +
			", state=" + state +
			"}";
	}
}

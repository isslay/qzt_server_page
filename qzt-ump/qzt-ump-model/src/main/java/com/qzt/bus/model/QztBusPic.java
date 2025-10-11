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
 * @since 2019-11-11
 */
@TableName("dgm_qzt_bus_pic")
public class QztBusPic extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类别（01试用订单试用前图片  02试用订单试用后图片 03商家环境图）
     */
	@TableField("bus_type")
	private String busType;
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


	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

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
		return "QztBusPic{" +
			", busType=" + busType +
			", busId=" + busId +
			", bannerName=" + bannerName +
			", bannerUrl=" + bannerUrl +
			", orderNum=" + orderNum +
			", state=" + state +
			"}";
	}
}

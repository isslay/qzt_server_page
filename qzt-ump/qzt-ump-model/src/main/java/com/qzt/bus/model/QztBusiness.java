package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since
 */
@TableName("dgm_qzt_business")
public class QztBusiness extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztBusiness(Long applyUserId, String busAddress, String contactTel, String busRecommender, String province, String city, String area, String contactName) {
        this.userId = applyUserId;
        this.busAddress = busAddress;
        this.busTel = contactTel;
        this.busRecommender = busRecommender;
        this.province = province;
        this.city = city;
        this.area = area;
        this.contacts = contactName;
        this.contactsTel = contactTel;
        this.busState = "1";
        this.setCreateBy(applyUserId);
        this.setUpdateBy(applyUserId);
    }

    public QztBusiness() {

    }

    public QztBusiness(Long id, String busRecommender, Long updateBy) {
        this.setId(id);
        this.busRecommender = busRecommender;
        this.setUpdateBy(updateBy);
    }

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 商家名称
     */
    @TableField("bus_name")
    private String busName;
    /**
     * 商家地址
     */
    @TableField("bus_address")
    private String busAddress;
    /**
     * 经度
     */
    @TableField("bus_long")
    private String busLong;
    /**
     * 纬度
     */
    @TableField("bus_lat")
    private String busLat;
    /**
     * 电话
     */
    @TableField("bus_tel")
    private String busTel;
    /**
     * 列表图片
     */
    @TableField("pic_url")
    private String picUrl;
    /**
     * 营业开始时间
     */
    @TableField("bus_time_start")
    private String busTimeStart;
    /**
     * 营业截止时间
     */
    @TableField("bus_time_end")
    private String busTimeEnd;
    /**
     * 商家详情
     */
    @TableField("bus_detail")
    private String busDetail;
    /**
     * 推荐人
     */
    @TableField("bus_recommender")
    private String busRecommender;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 联系人
     */
    private String contacts;
    /**
     * 联系电话
     */
    @TableField("contacts_tel")
    private String contactsTel;
    /**
     * 服务站状态（0正常 1歇业 9下线）
     */
    @TableField("bus_state")
    private String busState;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusAddress() {
        return busAddress;
    }

    public void setBusAddress(String busAddress) {
        this.busAddress = busAddress;
    }

    public String getBusLong() {
        return busLong;
    }

    public void setBusLong(String busLong) {
        this.busLong = busLong;
    }

    public String getBusLat() {
        return busLat;
    }

    public void setBusLat(String busLat) {
        this.busLat = busLat;
    }

    public String getBusTel() {
        return busTel;
    }

    public void setBusTel(String busTel) {
        this.busTel = busTel;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getBusTimeStart() {
        return busTimeStart;
    }

    public void setBusTimeStart(String busTimeStart) {
        this.busTimeStart = busTimeStart;
    }

    public String getBusTimeEnd() {
        return busTimeEnd;
    }

    public void setBusTimeEnd(String busTimeEnd) {
        this.busTimeEnd = busTimeEnd;
    }

    public String getBusDetail() {
        return busDetail;
    }

    public void setBusDetail(String busDetail) {
        this.busDetail = busDetail;
    }

    public String getBusRecommender() {
        return busRecommender;
    }

    public void setBusRecommender(String busRecommender) {
        this.busRecommender = busRecommender;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getContactsTel() {
        return contactsTel;
    }

    public void setContactsTel(String contactsTel) {
        this.contactsTel = contactsTel;
    }

    public String getBusState() {
        return busState;
    }

    public void setBusState(String busState) {
        this.busState = busState;
    }

    @Override
    public String toString() {
        return "QztBusiness{" +
                ", userId=" + userId +
                ", busName=" + busName +
                ", busAddress=" + busAddress +
                ", busLong=" + busLong +
                ", busLat=" + busLat +
                ", busTel=" + busTel +
                ", picUrl=" + picUrl +
                ", busTimeStart=" + busTimeStart +
                ", busTimeEnd=" + busTimeEnd +
                ", busDetail=" + busDetail +
                ", busRecommender=" + busRecommender +
                ", province=" + province +
                ", city=" + city +
                ", area=" + area +
                ", contacts=" + contacts +
                ", contactsTel=" + contactsTel +
                ", busState=" + busState +
                "}";
    }
}

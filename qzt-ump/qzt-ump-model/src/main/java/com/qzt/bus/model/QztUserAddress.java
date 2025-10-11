package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 收货地址实体类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_user_address")
public class QztUserAddress extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", hidden = false, required = true)
    @TableField("user_id")
    private Long userId;
    /**
     * 省
     */
    @ApiModelProperty(value = "省", hidden = true)
    @TableField("province")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value = "市", hidden = true)
    @TableField("city")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value = "区", hidden = true)
    @TableField("area")
    private String area;
    /**
     * 邮编
     */
    @ApiModelProperty(value = "邮编", hidden = false, required = true)
    @TableField("zip_code")
    private String zipCode;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址", hidden = false, required = true)
    @TableField("detail_address")
    private String detailAddress;

    /**
     * 详细地址（全部）
     */
    @ApiModelProperty(value = "详细地址（全部）", hidden = false, required = true)
    @TableField("all_address")
    private String allAddress;

    @ApiModelProperty(value = "经", hidden = false, required = true)
    @TableField("longitude")
    private String longitude;

    @ApiModelProperty(value = "纬", hidden = false, required = true)
    @TableField("latitude")
    private String latitude;

    /**
     * 姓名
     */
    @ApiModelProperty(value = "姓名", hidden = false, required = true)
    @TableField("recipient_name")
    private String recipientName;
    /**
     * 联系电话
     */
    @ApiModelProperty(value = "电话号码", hidden = false, required = true)
    @TableField("phone")
    private String phone;
    /**
     * 是否默认地址（是Y/ 否N）
     */
    @ApiModelProperty(value = "是否默认地址(是Y、否N)", hidden = false, required = true)
    @TableField("is_default")
    private String isDefault;
    /**
     * 是否删除（是Y/ 否N）
     */
    @ApiModelProperty(value = "是否删除(Y是、N否)", hidden = true)
    @TableField("is_del")
    private String isDel;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getAllAddress() {
        return allAddress;
    }

    public void setAllAddress(String allAddress) {
        this.allAddress = allAddress;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "QztUserAddress{" +
                ", userId=" + userId +
                ", province=" + province +
                ", city=" + city +
                ", area=" + area +
                ", zipCode=" + zipCode +
                ", detailAddress=" + detailAddress +
                ", recipientName=" + recipientName +
                ", phone=" + phone +
                ", isDefault=" + isDefault +
                ", isDel=" + isDel +
                ", allAddress=" + allAddress +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                "}";
    }
}

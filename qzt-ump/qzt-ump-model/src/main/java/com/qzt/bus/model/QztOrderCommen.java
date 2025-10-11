package com.qzt.bus.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 评论实体类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_order_commen")
public class QztOrderCommen extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 来源 (PC：1、IOS：2、Android：3、H5：4、小程序：5)
     */
    @TableField("source_")
    private String source;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 订单类型(01订单 02 服务订单 03 试用订单)
     */
    @TableField("order_type")
    private String orderType;
    /**
     * 业务ID
     */
    @TableField("bus_id")
    private Long busId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 商家ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 是否匿名（N否、Y是）
     */
    @TableField("is_anonymity")
    private String isAnonymity;
    /**
     * 评论内容
     */
    private String content;
    /**
     * 评论图片集合（英文逗号分隔，限制4张图）
     */
    private String pictures;
    /**
     * 评论星级（1 - 5）
     */
    @TableField("star_level")
    private Double starLevel;
    /**
     * 商家回复内容
     */
    @TableField("reply_message")
    private String replyMessage;
    /**
     * 商家回复时间
     */
    @TableField("revert_time")
    private Date revertTime;
    /**
     * 评论人电话
     */
    private String mobile;
    /**
     * 评论人头像
     */
    @TableField("head_img")
    private String headImg;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public String getIsAnonymity() {
        return isAnonymity;
    }

    public void setIsAnonymity(String isAnonymity) {
        this.isAnonymity = isAnonymity;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public Double getStarLevel() {
        return starLevel;
    }

    public void setStarLevel(Double starLevel) {
        this.starLevel = starLevel;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }

    public Date getRevertTime() {
        return revertTime;
    }

    public void setRevertTime(Date revertTime) {
        this.revertTime = revertTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    @Override
    public String toString() {
        return "QztOrderCommen{" +
                ", source=" + source +
                ", orderNo=" + orderNo +
                ", orderType=" + orderType +
                ", busId=" + busId +
                ", userId=" + userId +
                ", shopId=" + shopId +
                ", isAnonymity=" + isAnonymity +
                ", content=" + content +
                ", pictures=" + pictures +
                ", starLevel=" + starLevel +
                ", replyMessage=" + replyMessage +
                ", revertTime=" + revertTime +
                ", mobile=" + mobile +
                ", headImg=" + headImg +
                "}";
    }
}

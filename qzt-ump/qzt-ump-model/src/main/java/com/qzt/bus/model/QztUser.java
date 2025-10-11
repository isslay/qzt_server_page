package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.qzt.common.core.base.BaseModel;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@TableName("dgm_qzt_user")
public class QztUser extends BaseModel {

    private static final long serialVersionUID = 1L;

//    public QztUser(Long userId, Long bussId, String referrerSecond, Integer userType) {
//        this.userId = userId;
//        this.bussId = bussId;
//        this.referrerSecond = referrerSecond;
//        this.userType = userType;
//        this.setUpdateTime(new Date());
//    }

    public QztUser() {

    }

    @TableId(value = "id_", type = IdType.AUTO)
    private Long id;

    private Long userId;

    /**
     * 我的推荐码
     */
    @TableField("referrer_code")
    private String referrerCode;
    /**
     * 服务站商家ID
     */
    @TableField("buss_id")
    private Long bussId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 登录密码
     */
    private String pwd;
    /**
     * 支付密码
     */
    @TableField("pay_pwd")
    private String payPwd;
    /**
     * 注册来源 1：IOS 2：AND 3：小程序
     */
    @TableField("reg_source")
    private String regSource;
    /**
     * 微信名称
     */
    @TableField("wx_nick_name")
    private String wxNickName;
    /**
     * 微信openId
     */
    @TableField("wx_open_id")
    private String wxOpenId;
    /**
     * 微信头像
     */
    @TableField("wx_head_image")
    private String wxHeadImage;
    /**
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;
    /**
     * 状态：0正常，1删除，2拉黑
     */
    private String state;
    /**
     * 用户身份（服务站10、督导20、合伙人30、股东40）默认 0
     */
    @TableField("user_type")
    private Integer userType;
    /**
     * 是否参与降级（是Y、否N）
     */
    @TableField("is_demotion")
    private String isDemotion;
    /**
     * 是否参与升级（是Y、否N）
     */
    @TableField("is_upgrade")
    private String isUpgrade;
    /**
     * 用户注册推荐人
     */
    @TableField("referrer_first")
    private Integer referrerFirst;
    /**
     * 推荐人姓名
     */
    @TableField("referrer_second")
    private String referrerSecond;
    /**
     * 推荐人userId
     */
    @TableField("invitation_one")
    private String invitationOne;
    /**
     *
     */
    @TableField("invitation_two")
    private String invitationTwo;
    /**
     * 收款方式
     */
    @TableField("gathering_type")
    private Integer gatheringType;
    /**
     * 收款账号
     */
    @TableField("gathering_code")
    private String gatheringCode;
    /**
     * 收款图片
     */
    @TableField("gathering_img")
    private String gatheringImg;
    /**
     * 部门编号
     */
    @TableField("pid")
    private Integer pid;
    /**
     * 部门名称
     */
    @TableField("ph_short_name")
    private String phShortName;

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getPhShortName() {
        return phShortName;
    }

    public void setPhShortName(String phShortName) {
        this.phShortName = phShortName;
    }

    public String getReferrerCode() {
        return referrerCode;
    }

    public void setReferrerCode(String referrerCode) {
        this.referrerCode = referrerCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Long getBussId() {
        return bussId;
    }

    public void setBussId(Long bussId) {
        this.bussId = bussId;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getRegSource() {
        return regSource;
    }

    public void setRegSource(String regSource) {
        this.regSource = regSource;
    }

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getWxHeadImage() {
        return wxHeadImage;
    }

    public void setWxHeadImage(String wxHeadImage) {
        this.wxHeadImage = wxHeadImage;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getIsDemotion() {
        return isDemotion;
    }

    public void setIsDemotion(String isDemotion) {
        this.isDemotion = isDemotion;
    }

    public String getIsUpgrade() {
        return isUpgrade;
    }

    public void setIsUpgrade(String isUpgrade) {
        this.isUpgrade = isUpgrade;
    }

    public Integer getReferrerFirst() {
        return referrerFirst;
    }

    public void setReferrerFirst(Integer referrerFirst) {
        this.referrerFirst = referrerFirst;
    }

    public String getReferrerSecond() {
        return referrerSecond;
    }

    public void setReferrerSecond(String referrerSecond) {
        this.referrerSecond = referrerSecond;
    }

    public String getInvitationOne() {
        return invitationOne;
    }

    public void setInvitationOne(String invitationOne) {
        this.invitationOne = invitationOne;
    }

    public String getInvitationTwo() {
        return invitationTwo;
    }

    public void setInvitationTwo(String invitationTwo) {
        this.invitationTwo = invitationTwo;
    }

    public Integer getGatheringType() {
        return gatheringType;
    }

    public void setGatheringType(Integer gatheringType) {
        this.gatheringType = gatheringType;
    }

    public String getGatheringCode() {
        return gatheringCode;
    }

    public void setGatheringCode(String gatheringCode) {
        this.gatheringCode = gatheringCode;
    }

    public String getGatheringImg() {
        return gatheringImg;
    }

    public void setGatheringImg(String gatheringImg) {
        this.gatheringImg = gatheringImg;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "QztUser{" +
                ", referrerCode=" + referrerCode +
                ", mobile=" + mobile +
                ", bussId=" + bussId +
                ", pwd=" + pwd +
                ", payPwd=" + payPwd +
                ", regSource=" + regSource +
                ", wxNickName=" + wxNickName +
                ", wxOpenId=" + wxOpenId +
                ", wxHeadImage=" + wxHeadImage +
                ", realName=" + realName +
                ", idCard=" + idCard +
                ", state=" + state +
                ", userType=" + userType +
                ", isDemotion=" + isDemotion +
                ", isUpgrade=" + isUpgrade +
                ", referrerFirst=" + referrerFirst +
                ", referrerSecond=" + referrerSecond +
                ", invitationOne=" + invitationOne +
                ", invitationTwo=" + invitationTwo +
                ", gatheringType=" + gatheringType +
                ", gatheringCode=" + gatheringCode +
                ", gatheringImg=" + gatheringImg +
                ", pid=" + pid +
                ", phShortName=" + phShortName +
                "}";
    }
}

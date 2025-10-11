package com.qzt.bus.model;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 *
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-12
 */
@TableName("dgm_qzt_withdraw")
public class QztWithdraw extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztWithdraw(String cno, Long userId, BigDecimal withdrawMoney, BigDecimal serviceCharge, Long cardId) {
        this.source = cno;
        this.userId = userId;

        withdrawMoney = withdrawMoney == null || withdrawMoney.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : withdrawMoney;
        withdrawMoney = withdrawMoney.multiply(BigDecimal.valueOf(100)).setScale(0);
        this.withdrawMoney = Long.valueOf(withdrawMoney.toString());

        serviceCharge = serviceCharge == null || serviceCharge.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : serviceCharge;
        serviceCharge = serviceCharge.multiply(BigDecimal.valueOf(100)).setScale(0);
        this.serviceCharge = Long.valueOf(serviceCharge.toString());

        this.cardId = cardId;
        this.cardType = cardType;
        this.withdrawMoneyType = withdrawMoneyType == null || "".equals(withdrawMoneyType) ? "01" : withdrawMoneyType;
        this.setCreateBy(userId);
        this.setUpdateBy(userId);
    }

    public QztWithdraw() {

    }

    /**
     * 来源 (PC：1、IOS：2、Android：3、H5：4、小程序：5)
     */
    @TableField("source_")
    private String source;
    /**
     * 提现用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 用户手机号
     */
    @TableField("user_tel")
    private String userTel;
    /**
     * 提现金额类型（余额01）
     */
    @TableField("withdraw_money_type")
    private String withdrawMoneyType;
    /**
     * 提现金额（单位：分）
     */
    @TableField("withdraw_money")
    private Long withdrawMoney;
    /**
     * 免手续费金额（单位：分）
     */
    @TableField("free_of_fee_money")
    private Long freeOfFeeMoney;
    /**
     * 提现手续费（单位：分）
     */
    @TableField("service_charge")
    private Long serviceCharge;
    /**
     * 提现手续费比例（单位：.00）
     */
    @TableField("service_ratio")
    private BigDecimal serviceRatio;
    /**
     * 实际到账金额（单位：分）
     */
    @TableField("arrival_amount")
    private Long arrivalAmount;
    /**
     * 收款账号类型（01银行卡 02支付宝）
     */
    @TableField("card_type")
    private String cardType;
    /**
     * 收款账号ID
     */
    @TableField("card_id")
    private Long cardId;
    /**
     * 银行卡号
     */
    @TableField("card_code")
    private String cardCode;
    /**
     * 持卡人姓名
     */
    @TableField("real_name")
    private String realName;
    /**
     * 银行名称
     */
    @TableField("bank_name")
    private String bankName;
    /**
     * 开户行信息
     */
    @TableField("opening_bank")
    private String openingBank;
    /**
     * 审核人
     */
    @TableField("audit_user_id")
    private Long auditUserId;
    /**
     * 审核人名称
     */
    @TableField("audit_user_name")
    private String auditUserName;
    /**
     * 审核时间
     */
    @TableField("audit_time")
    private Date auditTime;
    /**
     * 审核状态（待审核00、已受理01、初审驳回11、已确认03、二审驳回13、已完成 20、三审驳回15）
     */
    @TableField("audit_state")
    private String auditState;
    /**
     * 审核描述
     */
    @TableField("audit_remark")
    private String auditRemark;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getWithdrawMoneyType() {
        return withdrawMoneyType;
    }

    public void setWithdrawMoneyType(String withdrawMoneyType) {
        this.withdrawMoneyType = withdrawMoneyType;
    }

    public Long getWithdrawMoney() {
        return withdrawMoney;
    }

    public void setWithdrawMoney(Long withdrawMoney) {
        this.withdrawMoney = withdrawMoney;
    }

    public Long getFreeOfFeeMoney() {
        return freeOfFeeMoney;
    }

    public void setFreeOfFeeMoney(Long freeOfFeeMoney) {
        this.freeOfFeeMoney = freeOfFeeMoney;
    }

    public Long getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Long serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getServiceRatio() {
        return serviceRatio;
    }

    public void setServiceRatio(BigDecimal serviceRatio) {
        this.serviceRatio = serviceRatio;
    }

    public Long getArrivalAmount() {
        return arrivalAmount;
    }

    public void setArrivalAmount(Long arrivalAmount) {
        this.arrivalAmount = arrivalAmount;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public Long getAuditUserId() {
        return auditUserId;
    }

    public void setAuditUserId(Long auditUserId) {
        this.auditUserId = auditUserId;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public String getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
    }

    @Override
    public String toString() {
        return "QztWithdraw{" +
                ", source=" + source +
                ", userId=" + userId +
                ", userTel=" + userTel +
                ", withdrawMoneyType=" + withdrawMoneyType +
                ", withdrawMoney=" + withdrawMoney +
                ", freeOfFeeMoney=" + freeOfFeeMoney +
                ", serviceCharge=" + serviceCharge +
                ", serviceRatio=" + serviceRatio +
                ", arrivalAmount=" + arrivalAmount +
                ", cardType=" + cardType +
                ", cardId=" + cardId +
                ", cardCode=" + cardCode +
                ", realName=" + realName +
                ", bankName=" + bankName +
                ", openingBank=" + openingBank +
                ", auditUserId=" + auditUserId +
                ", auditUserName=" + auditUserName +
                ", auditTime=" + auditTime +
                ", auditState=" + auditState +
                ", auditRemark=" + auditRemark +
                "}";
    }
}

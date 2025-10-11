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
 * @since 2019-11-12
 */
@TableName("dgm_qzt_account_log")
public class QztAccountLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztAccountLog(String busId, Long userId, Long accountId, String changeType, Long changeMoney, String changeSource, Long moneyBalanceEnd, Long moneyBalance, String remark,int logState) {
        this.busId = busId;
        this.userId = userId;
        this.accountId = accountId;
        this.changeType = changeType;
        this.changeNum = changeMoney;
        this.changeSource = changeSource;
        this.moneyBalanceEnd = moneyBalanceEnd;
        this.moneyBalance = moneyBalance;
        this.remark = remark;
        this.logState = logState;
        this.setCreateBy(userId);
        this.setUpdateBy(userId);
    }

    public QztAccountLog() {

    }

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 资产ID（0 平台 1：公益池 2：分红池）
     */
    @TableField("account_id")
    private Long accountId;
    /**
     * 业务订单ID
     */
    @TableField("bus_id")
    private String busId;
    /**
     * 变动类别（增加余额01、减少余额11）
     */
    @TableField("change_type")
    private String changeType;
    /**
     * 变动来源（余额： 购买商品消费01、服务站申请03、余额提现05、商品售出21、取消商品订单23、余额提现驳回 30购买商品消费分润 31服务订单分润 32 推广佣金激活 33下级服务金额）
     */
    @TableField("change_source")
    private String changeSource;
    /**
     * 变动金额（单位：分）
     */
    @TableField("change_num")
    private Long changeNum;
    /**
     * 当前余额
     */
    @TableField("money_balance")
    private Long moneyBalance;
    /**
     * 变动后余额
     */
    @TableField("money_balance_end")
    private Long moneyBalanceEnd;
    /**
     * 描述
     */
    private String remark;
    /**
     * 排序
     */
    @TableField("sort_index")
    private Long sortIndex;
    /**
     * 用户身份
     */
    @TableField("log_state")
    private Integer logState;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeSource() {
        return changeSource;
    }

    public void setChangeSource(String changeSource) {
        this.changeSource = changeSource;
    }

    public Long getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(Long changeNum) {
        this.changeNum = changeNum;
    }

    public Long getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(Long moneyBalance) {
        this.moneyBalance = moneyBalance;
    }

    public Long getMoneyBalanceEnd() {
        return moneyBalanceEnd;
    }

    public void setMoneyBalanceEnd(Long moneyBalanceEnd) {
        this.moneyBalanceEnd = moneyBalanceEnd;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Long sortIndex) {
        this.sortIndex = sortIndex;
    }

    public Integer getLogState() {
        return logState;
    }

    public void setLogState(Integer logState) {
        this.logState = logState;
    }

    @Override
    public String toString() {
        return "QztAccountLog{" +
                ", userId=" + userId +
                ", accountId=" + accountId +
                ", busId=" + busId +
                ", changeType=" + changeType +
                ", changeSource=" + changeSource +
                ", changeNum=" + changeNum +
                ", moneyBalance=" + moneyBalance +
                ", moneyBalanceEnd=" + moneyBalanceEnd +
                ", remark=" + remark +
                ", sortIndex=" + sortIndex +
                ", logState=" + logState +
                "}";
    }
}

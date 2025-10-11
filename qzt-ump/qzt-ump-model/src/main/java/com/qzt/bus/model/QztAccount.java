package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@TableName("dgm_qzt_account")
public class QztAccount extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
	@TableField("user_id")
	private Long userId;
    /**
     * 用户类型（1：前端用户）
     */
	@TableField("user_type")
	private String userType;
    /**
     * 账户余额
     */
	@TableField("account_money")
	private Long accountMoney;
    /**
     * 可用余额
     */
	@TableField("used_money")
	private Long usedMoney;
    /**
     * 冻结余额
     */
	@TableField("frozen_money")
	private Long frozenMoney;
    /**
     * 锁粉金额
     */
	@TableField("recommend_money")
	private Long recommendMoney;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Long getAccountMoney() {
		return accountMoney;
	}

	public void setAccountMoney(Long accountMoney) {
		this.accountMoney = accountMoney;
	}

	public Long getUsedMoney() {
		return usedMoney;
	}

	public void setUsedMoney(Long usedMoney) {
		this.usedMoney = usedMoney;
	}

	public Long getFrozenMoney() {
		return frozenMoney;
	}

	public void setFrozenMoney(Long frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public Long getRecommendMoney() {
		return recommendMoney;
	}

	public void setRecommendMoney(Long recommendMoney) {
		this.recommendMoney = recommendMoney;
	}

	@Override
	public String toString() {
		return "QztAccount{" +
			", userId=" + userId +
			", userType=" + userType +
			", accountMoney=" + accountMoney +
			", usedMoney=" + usedMoney +
			", frozenMoney=" + frozenMoney +
			", recommendMoney=" + recommendMoney +
			"}";
	}
}

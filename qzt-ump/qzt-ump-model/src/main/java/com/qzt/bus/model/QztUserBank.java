package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>
 *  用户收款账号实体类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_user_bank")
public class QztUserBank extends BaseModel {

    private static final long serialVersionUID = 1L;
	public QztUserBank(Long id, Long userId, String isDefault, String isDel) {
		this.setId(id);
		this.setUpdateBy(userId);
		this.setUpdateTime(new Date());
		this.userId = userId;
		this.isDefault = isDefault;
		this.isDel = isDel;
	}

	public QztUserBank(Long id, Long userId) {
		this.setId(id);
		this.userId = userId;
	}

	public QztUserBank() {

	}

    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID", hidden = false, required = true)
	@TableField("user_id")
	private Long userId;
    /**
     * 账号类型（01银行卡 02支付宝）
     */
	@ApiModelProperty(value = "账号类型（01银行卡 02支付宝）", hidden = false, required = true)
	@TableField("bank_type")
	private String bankType;
    /**
     * 银行信息表ID
     */
	@ApiModelProperty(value = "银行ID", hidden = false, required = false)
	@TableField("bank_id")
	private Long bankId;
    /**
     * 银行名称
     */
	@ApiModelProperty(value = "银行名称", hidden = true)
	@TableField("bank_name")
	private String bankName;
    /**
     * 银行logo
     */
	@ApiModelProperty(value = "银行logo", hidden = true)
	@TableField("bank_pic")
	private String bankPic;
    /**
     * 持卡人姓名
     */
	@ApiModelProperty(value = "持卡人姓名", hidden = false, required = true)
	@TableField("real_name")
	private String realName;
    /**
     * 绑定手机号
     */
	@ApiModelProperty(value = "绑定手机号", hidden = false, required = true)
	@TableField("binding_tel")
	private String bindingTel;
    /**
     * 银行卡号/支付宝账号
     */
	@ApiModelProperty(value = "银行卡号/支付宝账号", hidden = false, required = true)
	@TableField("card_num")
	private String cardNum;
    /**
     * 开户行信息
     */
	@ApiModelProperty(value = "开户行信息", hidden = false, required = false)
	@TableField("bank_branch_name")
	private String bankBranchName;
    /**
     * 是否默认银行卡(是Y、否N)
     */
	@ApiModelProperty(value = "是否默认银行卡(是Y、否N)", hidden = false, required = false)
	@TableField("is_default")
	private String isDefault;
    /**
     * 是否删除
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

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public Long getBankId() {
		return bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankPic() {
		return bankPic;
	}

	public void setBankPic(String bankPic) {
		this.bankPic = bankPic;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBindingTel() {
		return bindingTel;
	}

	public void setBindingTel(String bindingTel) {
		this.bindingTel = bindingTel;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
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

	@Override
	public String toString() {
		return "QztUserBank{" +
			", userId=" + userId +
			", bankType=" + bankType +
			", bankId=" + bankId +
			", bankName=" + bankName +
			", bankPic=" + bankPic +
			", realName=" + realName +
			", bindingTel=" + bindingTel +
			", cardNum=" + cardNum +
			", bankBranchName=" + bankBranchName +
			", isDefault=" + isDefault +
			", isDel=" + isDel +
			"}";
	}
}

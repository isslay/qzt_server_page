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
@TableName("dgm_qzt_apply_tryorder")
public class QztApplyTryorder extends BaseModel {

    private static final long serialVersionUID = 1L;
	/**
	 * 订单编号
	 */
	@TableField("order_no")
    private String orderNo;

    /**
     * 联系人
     */
	@TableField("store_type")
	private String storeType;
    /**
     * 联系电话
     */
	@TableField("store_name")
	private String storeName;
    /**
     * 疾病类别
     */
	@TableField("disease_type")
	private String diseaseType;
    /**
     * 疾病名称
     */
	@TableField("disease_name")
	private String diseaseName;
    /**
     * 选择服务站
     */
	@TableField("bus_id")
	private String busId;
    /**
     * 服务站名称
     */
	@TableField("bus_name")
	private String busName;
    /**
     * 申请人
     */
	@TableField("apply_user_id")
	private String applyUserId;
    /**
     * 订单状态（01 待确认，03待到店，05 已服务，07已完成 ，90已拒绝 99已取消）
     */
	@TableField("order_state")
	private String orderState;
    /**
     * 身份证
     */
	@TableField("id_cardno")
	private String idCardno;
    /**
     * 推荐人ID（无用）
     */
	@TableField("referrer_user_id")
	private Long referrerUserId;


	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getDiseaseType() {
		return diseaseType;
	}

	public void setDiseaseType(String diseaseType) {
		this.diseaseType = diseaseType;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getBusName() {
		return busName;
	}

	public void setBusName(String busName) {
		this.busName = busName;
	}

	public String getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(String applyUserId) {
		this.applyUserId = applyUserId;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public String getIdCardno() {
		return idCardno;
	}

	public void setIdCardno(String idCardno) {
		this.idCardno = idCardno;
	}

	public Long getReferrerUserId() {
		return referrerUserId;
	}

	public void setReferrerUserId(Long referrerUserId) {
		this.referrerUserId = referrerUserId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public String toString() {
		return "QztApplyTryorder{" +
			", storeType=" + storeType +
			", storeName=" + storeName +
			", diseaseType=" + diseaseType +
			", diseaseName=" + diseaseName +
			", busId=" + busId +
			", busName=" + busName +
			", applyUserId=" + applyUserId +
			", orderState=" + orderState +
			", idCardno=" + idCardno +
			", referrerUserId=" + referrerUserId +
			"}";
	}
}

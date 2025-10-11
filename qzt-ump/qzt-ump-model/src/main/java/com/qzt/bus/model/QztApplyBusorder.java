package com.qzt.bus.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since
 */
@Data
@TableName("dgm_qzt_apply_busorder")
public class QztApplyBusorder extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztApplyBusorder(Long userId, String contactName, String contactTel, String context, String stateMark, Long referrerUserId) {
        this.setCreateBy(userId);
        this.setUpdateBy(userId);
        this.applyUserId = userId;
        this.contactName = contactName;
        this.contactTel = contactTel;
        this.context = context;
        this.stateMark = stateMark;
        this.consigneeName = contactName;
        this.consigneeTel = contactTel;
        this.referrerUserId = referrerUserId;
    }

    public QztApplyBusorder() {

    }

    /**
     * 申请人姓名
     */
    @TableField("contact_name")
    private String contactName;
    /**
     * 联系电话
     */
    @TableField("contact_tel")
    private String contactTel;
    /**
     * 企业名称 （该字段废弃）
     */
    @TableField("company_name")
    private String companyName;
    /**
     * 城市CODE
     */
    private String context;
    /**
     * 详细地址
     */
    @TableField("state_mark")
    private String stateMark;
    /**
     * 用户ID
     */
    @TableField("apply_user_id")
    private Long applyUserId;
    /**
     * 推荐人ID
     */
    @TableField("referrer_user_id")
    private Long referrerUserId;
    /**
     * 推荐人账号
     */
    @TableField("referrer_tel")
    private String referrerTel;
    /**
     * 订单金额（单位：分）
     */
    @TableField("order_money")
    private Long orderMoney;
    /**
     * 余额支付金额（单位：分）
     */
    @TableField("balance_money")
    private Long balanceMoney;
    /**
     * 三方支付金额（单位：分）
     */
    @TableField("three_sides_money")
    private Long threeSidesMoney;
    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 订单状态（01：待审核，03：待发货，05：待收货，09：已完成，11：已驳回，96：异常订单）
     */
    @TableField("order_state")
    private String orderState;
    /**
     * 支付方式（支付宝z、微信w、余额y）
     */
    @TableField("pay_type")
    private String payType;
    /**
     * 支付状态（待付款00、已支付11、失败12、已取消99）
     */
    @TableField("pay_state")
    private String payState;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 收货地址ID
     */
    @TableField("address_id")
    private Long addressId;
    /**
     * 收货人名称
     */
    @TableField("consignee_name")
    private String consigneeName;
    /**
     * 收货人电话
     */
    @TableField("consignee_tel")
    private String consigneeTel;
    /**
     * 收货地址
     */
    @TableField("consignee_address")
    private String consigneeAddress;
    /**
     * 快递公司名称
     */
    @TableField("courier_company")
    private String courierCompany;
    /**
     * 快递公司code
     */
    @TableField("courier_code")
    private String courierCode;
    /**
     * 快递单号
     */
    @TableField("courier_no")
    private String courierNo;
    /**
     * 快递备注
     */
    @TableField("courier_remarks")
    private String courierRemarks;
    /**
     * 发货时间
     */
    @TableField("shipments_time")
    private Date shipmentsTime;
    /**
     * 签收时间
     */
    @TableField("signfor_time")
    private Date signforTime;
    /**
     * 完成时间
     */
    @TableField("finsh_time")
    private Date finshTime;

    @Override
    public String toString() {
        return "QztApplyBusorder{" +
                ", contactName=" + contactName +
                ", contactTel=" + contactTel +
                ", companyName=" + companyName +
                ", context=" + context +
                ", stateMark=" + stateMark +
                ", applyUserId=" + applyUserId +
                ", referrerUserId=" + referrerUserId +
                ", referrerTel=" + referrerTel +
                ", orderMoney=" + orderMoney +
                ", balanceMoney=" + balanceMoney +
                ", threeSidesMoney=" + threeSidesMoney +
                ", orderNo=" + orderNo +
                ", orderState=" + orderState +
                ", payType=" + payType +
                ", payState=" + payState +
                ", payTime=" + payTime +
                ", addressId=" + addressId +
                ", consigneeName=" + consigneeName +
                ", consigneeTel=" + consigneeTel +
                ", consigneeAddress=" + consigneeAddress +
                ", courierCompany=" + courierCompany +
                ", courierCode=" + courierCode +
                ", courierNo=" + courierNo +
                ", courierRemarks=" + courierRemarks +
                ", shipmentsTime=" + shipmentsTime +
                ", signforTime=" + signforTime +
                ", finshTime=" + finshTime +
                "}";
    }
}

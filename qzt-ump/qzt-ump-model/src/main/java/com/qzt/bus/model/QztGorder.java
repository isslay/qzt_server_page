package com.qzt.bus.model;

import java.math.BigDecimal;
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
 * @author Xiaofei
 * @since 2019-11-12
 */
@Data
@TableName("dgm_qzt_gorder")
public class QztGorder extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztGorder(String orderSource, Long userId, String pickupWay, Long addressId, Long goodsId, Integer buyNum, Long cashCouponId,Long storeCouponId,Long goodsCouponId,Long activityCouponId, String shareCode, String remarks, String userTel, String orderType, String orderState, String payState) {
        this.orderSource = orderSource;
        this.userId = userId;
        this.userTel = userTel;
        this.pickupWay = pickupWay;
        this.addressId = addressId;
        this.goodsId = goodsId;
        this.buyNum = buyNum == null || buyNum < 0 ? 0 : buyNum;
        this.cashCouponId = cashCouponId;
        this.storeCouponId = storeCouponId;
        this.goodsCouponId = goodsCouponId;
        this.activityCouponId = activityCouponId;
//        this.shareCode = userId.toString().equals(shareCode) ? null : shareCode;
        this.shareCode = shareCode;
//        this.orderSource = shareUserId;
        this.remarks = remarks;
        this.orderType = orderType;
        this.orderState = orderState;
        this.payState = payState;
        this.setCreateBy(userId);
        this.setUserId(userId);
    }

    public QztGorder(Long userId, String orderNo, String payType, BigDecimal balanceMoney) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.payType = payType;
        balanceMoney = balanceMoney == null || balanceMoney.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : balanceMoney;
        balanceMoney = balanceMoney.multiply(BigDecimal.valueOf(100)).setScale(0);
        this.balanceMoney = Long.valueOf(balanceMoney.toString());
    }

    public QztGorder(Long userId, String orderNo) {
        this.userId = userId;
        this.orderNo = orderNo;
        this.setUpdateTime(new Date());
    }

    public QztGorder(String orderNo, String isServe, Long operatorId) {
        this.setUpdateTime(new Date());
        this.setUpdateBy(operatorId);
        this.orderNo = orderNo;
        this.isServe = isServe;
    }

    public QztGorder() {

    }

    /**
     * 订单编号
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 用户电话
     */
    @TableField("user_tel")
    private String userTel;
    /**
     * 商品ID
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 商品名称
     */
    @TableField("goods_name")
    private String goodsName;
    /**
     * 商品缩略图
     */
    @TableField("goods_pic")
    private String goodsPic;
    /**
     * 商品单价（单位：分）
     */
    @TableField("goods_price")
    private Long goodsPrice;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 分享佣金（单位：分）
     */
    @TableField("share_money")
    private Long shareMoney;
    /**
     * 推广佣金（单位：分）
     */
    @TableField("recommend_money")
    private Long recommendMoney;
    /**
     * 服务佣金（单位：分）
     */
    @TableField("service_money")
    private Long serviceMoney;
    /**
     * 员工推荐码
     */
    @TableField("share_code")
    private String shareCode;
    /**
     * 推荐人id
     */
    @TableField("order_source")
    private String orderSource;
    /**
     * 订单类型（商品01、、、、）
     */
    @TableField("order_type")
    private String orderType;
    /**
     * 订单状态（01：待付款，02：待到店，03：待发货，04：待核销，05：待收货，09：已完成，11：已取消，96：异常订单）
     */
    @TableField("order_state")
    private String orderState;
    /**
     * 邮费（单位：分） 商品邮费合计
     */
    private Long postage;
    /**
     * 订单金额RMB（单位：分） 商品价格合计
     */
    @TableField("total_price")
    private Long totalPrice;
    /**
     * 成本金额RMB（单位：分） 商品成本合计
     */
    @TableField("cost_price")
    private Long costPrice;
    /**
     * 实际需支付金额Rmb（单位：分） 邮费 + 商品价格  -  抵付金额
     */
    @TableField("actua_payment")
    private Long actuaPayment;
    /**
     * 购买数量
     */
    @TableField("buy_num")
    private Integer buyNum;
    /**
     * 用户备注/留言
     */
    private String remarks;
    /**
     * 余额支付金额（单位：分）
     */
    @TableField("balance_money")
    private Long balanceMoney;
    /**
     * 是否需要服务（是Y、否N）
     */
    @TableField("is_service")
    private String isService;
    /**
     * 三方支付金额（单位：分）
     */
    @TableField("three_sides_money")
    private Long threeSidesMoney;
    /**
     * 券ID
     */
    @TableField("cash_coupon_id")
    private Long cashCouponId;
    /**
     * 抵扣券支付金额（单位：分）
     */
    @TableField("cash_coupon_money")
    private Long cashCouponMoney;
    /**
     * 商户券ID
     */
    @TableField("store_coupon_id")
    private Long storeCouponId;
    /**
     * 商户抵扣券支付金额（单位：分）
     */
    @TableField("store_coupon_money")
    private Long storeCouponMoney;
    /**
     * 商品券ID
     */
    @TableField("goods_coupon_id")
    private Long goodsCouponId;
    /**
     * 商户抵扣券支付金额（单位：分）
     */
    @TableField("goods_coupon_money")
    private Long goodsCouponMoney;
    /**
     * 活动券ID
     */
    @TableField("activity_coupon_id")
    private Long activityCouponId;
    /**
     * 活动抵扣券支付金额（单位：分）
     */
    @TableField("activity_coupon_money")
    private Long activityCouponMoney;
    /**
     * 支付方式（支付宝z、微信w、余额y）
     */
    @TableField("pay_type")
    private String payType;
    /**
     * 支付状态（00：待付款（全部） 01：待付款（三方） 11 ：已支付  12 ：失败  99：已取消）
     */
    @TableField("pay_state")
    private String payState;
    /**
     * 支付时间
     */
    @TableField("pay_time")
    private Date payTime;
    /**
     * 取货方式(自提0、快递1)
     */
    @TableField("pickup_way")
    private String pickupWay;
    /**
     * 核销码
     */
    @TableField("the_verification_code")
    private String theVerificationCode;
    /**
     * 收货地址ID/取货商家ID
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
    /**
     * 自动取消时间
     */
    @TableField("order_timing_time")
    private Date orderTimingTime;
    /**
     * 抵扣券总金额（单位：分）
     */
    @TableField("deduct_max")
    private Long deductMax;
    /**
     * 是否禁止使用抵扣券(是Y、否N)
     */
    @TableField("coupon_status")
    private String couponStatus;
    /**
     * 是否可申请服务（是Y、否N）
     */
    @TableField("is_serve")
    private String isServe;
    /**
     * 补货状态（未补货00、已补货02）
     */
    @TableField("replenish_status")
    private String replenishStatus;
    /**
     * 补货时间
     */
    @TableField("replenish_time")
    private Date replenishTime;
    /**
     * 补货-收货人名称
     */
    @TableField("replenish_cne_name")
    private String replenishCneName;
    /**
     * 补货-收货人电话
     */
    @TableField("replenish_cne_tel")
    private String replenishCneTel;
    /**
     * 补货-收货地址
     */
    @TableField("replenish_cne_address")
    private String replenishCneAddress;
    /**
     * 补货-快递公司名称
     */
    @TableField("replenish_csc")
    private String replenishCsc;
    /**
     * 补货-快递公司code
     */
    @TableField("replenish_csc_code")
    private String replenishCscCode;
    /**
     * 补货-快递单号
     */
    @TableField("replenish_csc_no")
    private String replenishCscNo;
    /**
     * 补货-快递备注
     */
    @TableField("replenish_csc_remarks")
    private String replenishCscRemarks;

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
     * 推荐人部门
     */
    @TableField("referrer_name")
    private String referrerName;



    @Override
    public String toString() {
        return "QztGorder{" +
                ", orderNo=" + orderNo +
                ", userId=" + userId +
                ", goodsId=" + goodsId +
                ", goodsName=" + goodsName +
                ", goodsPic=" + goodsPic +
                ", goodsPrice=" + goodsPrice +
                ", shopId=" + shopId +
                ", shareMoney=" + shareMoney +
                ", recommendMoney=" + recommendMoney +
                ", serviceMoney=" + serviceMoney +
                ", shareCode=" + shareCode +
                ", orderSource=" + orderSource +
                ", orderType=" + orderType +
                ", orderState=" + orderState +
                ", postage=" + postage +
                ", totalPrice=" + totalPrice +
                ", costPrice=" + costPrice +
                ", actuaPayment=" + actuaPayment +
                ", buyNum=" + buyNum +
                ", remarks=" + remarks +
                ", balanceMoney=" + balanceMoney +
                ", isService=" + isService +
                ", threeSidesMoney=" + threeSidesMoney +
                ", cashCouponId=" + cashCouponId +
                ", cashCouponMoney=" + cashCouponMoney +
                ", storeCouponId=" + storeCouponId +
                ", storeCouponMoney=" + storeCouponMoney +
                ", activityCouponId=" + activityCouponId +
                ", activityCouponMoney=" + activityCouponMoney +
                ", payType=" + payType +
                ", payState=" + payState +
                ", payTime=" + payTime +
                ", pickupWay=" + pickupWay +
                ", theVerificationCode=" + theVerificationCode +
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
                ", orderTimingTime=" + orderTimingTime +
                ", deductMax=" + deductMax +
                ", couponStatus=" + couponStatus +
                ", isServe=" + isServe +
                ", replenishStatus=" + replenishStatus +
                ", replenishTime=" + replenishTime +
                ", replenishCneName=" + replenishCneName +
                ", replenishCneTel=" + replenishCneTel +
                ", replenishCneAddress=" + replenishCneAddress +
                ", replenishCsc=" + replenishCsc +
                ", replenishCscCode=" + replenishCscCode +
                ", replenishCscNo=" + replenishCscNo +
                ", replenishCscRemarks=" + replenishCscRemarks +
                ", referrerFirst=" + referrerFirst +
                ", referrerSecond=" + referrerSecond +
                ", referrerName=" + referrerName +
                "}";
    }
}

package com.qzt.bus.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * 支付回调日志实体类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@TableName("dgm_qzt_pay_backlog")
public class QztPayBacklog extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztPayBacklog(String backSource, String orderNo, String payNo, String backPayNo, BigDecimal payMoney, String busType, String message) {
        this.backSource = backSource;
        this.orderNo = orderNo;
        this.payNo = payNo;
        this.backPayNo = backPayNo;
        this.payMoney = payMoney;
        this.busType = busType;
        this.message = message;
        this.setCreateBy(0L);
        this.setUpdateBy(0L);
    }

    public QztPayBacklog() {
    }

    /**
     * 回调来源（支付宝z、微信w）
     */
    @TableField("back_source")
    private String backSource;
    /**
     * 订单编号 （平台订单编号）
     */
    @TableField("order_no")
    private String orderNo;
    /**
     * 支付单号 （指点字符 + 订单编号）
     */
    @TableField("pay_no")
    private String payNo;
    /**
     * 支付金额
     */
    @TableField("pay_money")
    private BigDecimal payMoney;
    /**
     * 第三方支付单号
     */
    @TableField("back_pay_no")
    private String backPayNo;
    /**
     * 业务类型 （详见OrderUtil.OrderNoEnum.*）
     */
    @TableField("bus_type")
    private String busType;
    /**
     * 报文
     */
    @TableField("message_")
    private String message;

    public String getBackSource() {
        return backSource;
    }

    public void setBackSource(String backSource) {
        this.backSource = backSource;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getBackPayNo() {
        return backPayNo;
    }

    public void setBackPayNo(String backPayNo) {
        this.backPayNo = backPayNo;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "QztPayBacklog{" +
                ", backSource=" + backSource +
                ", orderNo=" + orderNo +
                ", payNo=" + payNo +
                ", payMoney=" + payMoney +
                ", backPayNo=" + backPayNo +
                ", busType=" + busType +
                ", message=" + message +
                "}";
    }
}

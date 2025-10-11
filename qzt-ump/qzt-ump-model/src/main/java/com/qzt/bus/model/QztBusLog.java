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
 * @since 2019-11-11
 */
@TableName("dgm_qzt_bus_log")
public class QztBusLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztBusLog(String businessType, String operNode, String businessId, String busRemark, Long operatorId) {
        this.businessType = businessType;
        this.operNode = operNode;
        this.businessId = businessId;
        this.busRemark = busRemark;
        this.setCreateBy(operatorId);
        this.setUpdateBy(operatorId);
    }

    public QztBusLog() {

    }

    /**
     * 业务分类（商品订单相关01、提现相关03、申请服务站订单05、试用订单07、申请服务订单09）
     */
    @TableField("business_type")
    private String businessType;
    /**
     * 操作节点
     */
    @TableField("oper_node")
    private String operNode;
    /**
     * 业务记录ID
     */
    @TableField("business_id")
    private String businessId;
    /**
     * 排序
     */
    @TableField("sort_index")
    private Long sortIndex;
    /**
     * 备注
     */
    @TableField("bus_remark")
    private String busRemark;

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getOperNode() {
        return operNode;
    }

    public void setOperNode(String operNode) {
        this.operNode = operNode;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Long getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(Long sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getBusRemark() {
        return busRemark;
    }

    public void setBusRemark(String busRemark) {
        this.busRemark = busRemark;
    }

    @Override
    public String toString() {
        return "QztBusLog{" +
                ", businessType=" + businessType +
                ", operNode=" + operNode +
                ", businessId=" + businessId +
                ", sortIndex=" + sortIndex +
                ", busRemark=" + busRemark +
                "}";
    }
}

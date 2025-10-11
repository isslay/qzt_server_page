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
@TableName("dgm_qzt_timing_log")
public class QztTimingLog extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztTimingLog(String busType, String busId, String resultEnforcement, String busRemark) {
        this.setCreateBy(0L);
        this.setUpdateBy(0L);
        this.busType = busType;
        this.busId = busId;
        this.resultEnforcement = resultEnforcement;
        this.busRemark = busRemark;
    }

    public QztTimingLog() {
    }

    /**
     * 定时业务分类（01：优惠券）
     */
    @TableField("bus_type")
    private String busType;
    /**
     * 业务记录ID
     */
    @TableField("bus_id")
    private String busId;
    /**
     * 执行结果（成功：Y、失败：N）
     */
    @TableField("result_enforcement")
    private String resultEnforcement;
    /**
     * 备注
     */
    @TableField("bus_remark")
    private String busRemark;

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getResultEnforcement() {
        return resultEnforcement;
    }

    public void setResultEnforcement(String resultEnforcement) {
        this.resultEnforcement = resultEnforcement;
    }

    public String getBusRemark() {
        return busRemark;
    }

    public void setBusRemark(String busRemark) {
        this.busRemark = busRemark;
    }

    @Override
    public String toString() {
        return "QztTimingLog{" +
                ", busType=" + busType +
                ", busId=" + busId +
                ", resultEnforcement=" + resultEnforcement +
                ", busRemark=" + busRemark +
                "}";
    }
}

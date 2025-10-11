package com.qzt.ump.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Xiaofei
 * @since 2018-10-17
 */
@TableName("dgm_sys_area")
public class SysArea extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * 父类ID
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 地区名称
     */
    @TableField("area_name")
    private String areaName;
    /**
     * 地区名称全拼
     */
    @TableField("area_name_spell")
    private String areaNameSpell;
    /**
     * 地区备注(现存邮编)
     */
    @TableField("area_remark")
    private String areaRemark;
    /**
     * 状态
     */
    @TableField("area_status")
    private String areaStatus;
    /**
     * 编码
     */
    @TableField("area_code")
    private String areaCode;
    /**
     * 高德地图市code
     */
    @TableField("autonavi_city_code")
    private String autonaviCityCode;
    /**
     * 高德地图区code
     */
    @TableField("autonavi_area_code")
    private String autonaviAreaCode;


    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaNameSpell() {
        return areaNameSpell;
    }

    public void setAreaNameSpell(String areaNameSpell) {
        this.areaNameSpell = areaNameSpell;
    }

    public String getAreaRemark() {
        return areaRemark;
    }

    public void setAreaRemark(String areaRemark) {
        this.areaRemark = areaRemark;
    }

    public String getAreaStatus() {
        return areaStatus;
    }

    public void setAreaStatus(String areaStatus) {
        this.areaStatus = areaStatus;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAutonaviCityCode() {
        return autonaviCityCode;
    }

    public void setAutonaviCityCode(String autonaviCityCode) {
        this.autonaviCityCode = autonaviCityCode;
    }

    public String getAutonaviAreaCode() {
        return autonaviAreaCode;
    }

    public void setAutonaviAreaCode(String autonaviAreaCode) {
        this.autonaviAreaCode = autonaviAreaCode;
    }

    @Override
    public String toString() {
        return "qztArea{" +
                ", parentId=" + parentId +
                ", areaName=" + areaName +
                ", areaNameSpell=" + areaNameSpell +
                ", areaRemark=" + areaRemark +
                ", areaStatus=" + areaStatus +
                ", areaCode=" + areaCode +
                ", autonaviCityCode=" + autonaviCityCode +
                ", autonaviAreaCode=" + autonaviAreaCode +
                "}";
    }
}

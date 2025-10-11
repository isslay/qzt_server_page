package com.qzt.bus.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@TableName("dgm_qzt_user_reg")
public class QztUserReg extends BaseModel {

    private static final long serialVersionUID = 1L;

    public QztUserReg(String progressType, Integer type, Long parentUserId, Long regUserId) {
        this.progressType = progressType;
        this.type = type;
        this.parentUserId = parentUserId;
        this.regUserId = regUserId;
        this.setCreateTime(new Date());
    }

    public QztUserReg(Integer type, Long parentUserId, Long regUserId, int num) {
        this.type = type;
        this.parentUserId = parentUserId;
        this.regUserId = regUserId;
        this.num = num;
        Date date = new Date();
        this.setCreateBy(regUserId);
        this.setCreateTime(date);
        this.setUpdateBy(regUserId);
        this.setUpdateTime(date);
    }

    public QztUserReg(Long parentUserId, Long regUserId, Integer type) {
        this.type = type;
        this.parentUserId = parentUserId;
        this.regUserId = regUserId;
    }

    public QztUserReg() {

    }

    /**
     * 关系类型（0用户 2服务站）
     */
    private Integer type;
    /**
     * 父级ID
     */
    @TableField("parent_user_id")
    private Long parentUserId;
    /**
     * 子级ID
     */
    @TableField("reg_user_id")
    private Long regUserId;
    /**
     * 层级关系
     */
    @TableField("num")
    private Integer num;
    /**
     * 发展类型（正向01、逆向03）
     */
    @TableField(exist = false)
    private String progressType;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public Long getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(Long regUserId) {
        this.regUserId = regUserId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getProgressType() {
        return progressType;
    }

    public void setProgressType(String progressType) {
        this.progressType = progressType;
    }

    @Override
    public String toString() {
        return "QztUserReg{" +
                ", type=" + type +
                ", parentUserId=" + parentUserId +
                ", regUserId=" + regUserId +
                ", num=" + num +
                "}";
    }
}

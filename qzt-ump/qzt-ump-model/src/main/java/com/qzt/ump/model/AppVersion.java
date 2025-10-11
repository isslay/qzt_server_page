package com.qzt.ump.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.qzt.common.core.base.BaseModel;

/**
 * <p>
 *  版本管理实体
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-23
 */
@TableName("dgm_app_version")
public class AppVersion extends BaseModel {

    private static final long serialVersionUID = 1L;

    /**
     * app来源（IOS 2，Android 3）
     */
    @TableField("source_mode")
    private String sourceMode;
    /**
     * 版本号
     */
    @TableField("version_no")
    private String versionNo;
    /**
     * 下载地址
     */
    @TableField("download_url")
    private String downloadUrl;
    /**
     * 是否强制更新（是Y、否N）
     */
    @TableField("is_forced_update")
    private String isForcedUpdate;
    /**
     * 备注
     */
    @TableField("remark_")
    private String remark;


    public String getSourceMode() {
        return sourceMode;
    }

    public void setSourceMode(String sourceMode) {
        this.sourceMode = sourceMode;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(String isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "qztAppVersion{" +
                ", sourceMode=" + sourceMode +
                ", versionNo=" + versionNo +
                ", downloadUrl=" + downloadUrl +
                ", isForcedUpdate=" + isForcedUpdate +
                ", remark=" + remark +
                "}";
    }
}

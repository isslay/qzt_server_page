package com.qzt.common.core.base;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据模型基类
 *
 * @author cgw
 * @date 2017/11/10 12:02
 */
@Data
public abstract class BaseModel implements Serializable {
    private static final long serialVersionUID = 7258436689721815928L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键id", hidden = true)
    @TableId(value = "id_", type = IdType.ID_WORKER)
    private Long id;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", hidden = true)
    @TableField("create_by")
    private Long createBy;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间", hidden = true)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @ApiModelProperty(value = "更新人", hidden = true)
    @TableField("update_by")
    private Long updateBy;

    // 明确声明setter，避免在某些环境下Lombok未生效导致编译失败
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getId() {
        return this.id;
    }
}

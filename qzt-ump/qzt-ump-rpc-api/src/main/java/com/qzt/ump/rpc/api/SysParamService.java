package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysParamModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 全局参数表 服务类
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
public interface SysParamService extends BaseService<SysParamModel> {

    /**
     * 分页查询参数配置明细
     *
     * @param page
     * @return Page<SysParamModel>
     * @author shadj
     * @date 2017/12/24 14:45
     */
    Page<SysParamModel> queryListPage(Page<SysParamModel> page);

    /**
     * 查询所有参数
     *
     * @param map
     * @return java.util.List<com.qzt.ump.model.SysParamModel>
     * @author Xiaofei
     * @date 2019-07-31
     */
    List<SysParamModel> findLinst(Map map);

    /**
     * 系统参数初始化
     *
     * @return void
     * @author Xiaofei
     * @date 2019-07-31
     */
    void initializeParam();
}

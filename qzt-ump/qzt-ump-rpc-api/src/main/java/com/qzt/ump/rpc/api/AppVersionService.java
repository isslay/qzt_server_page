package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.AppVersion;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-26
 */
public interface AppVersionService extends BaseService<AppVersion> {

    Page<AppVersion> find(Page<AppVersion> pageModel);

    /**
     * 更新版本信息
     *
     * @param appVersion
     * @return void
     * @author Xiaofei
     * @date 2019-06-23
     */
    void updateVersionCache(AppVersion appVersion);

    /**
     * 查询版本信息
     *
     * @param sourceMode 客户端
     * @param versionNo  版本号
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-06-26
     */
    Map findVersionCache(String sourceMode, String versionNo);

    /**
     * 获取最新版本信息
     *
     * @param sourceMode
     * @return com.qzt.ump.model.AppVersion
     * @author Xiaofei
     * @date 2019-06-26
     */
    AppVersion findMaxVersion(String sourceMode);
}

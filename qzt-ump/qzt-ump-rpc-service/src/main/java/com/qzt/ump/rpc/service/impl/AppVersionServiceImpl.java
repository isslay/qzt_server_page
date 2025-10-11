package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.ump.rpc.api.AppVersionService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.dao.mapper.AppVersionMapper;
import com.qzt.ump.model.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-23
 */
@Service("appVersionService")
public class AppVersionServiceImpl extends BaseServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Autowired
    private AppVersionMapper appVersionMapper;

    @Override
    public Page<AppVersion> find(Page<AppVersion> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<AppVersion> rb = this.appVersionMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void updateVersionCache(AppVersion appVersion) {
        Map map = new HashMap<>();
        map.put("sourceMode", appVersion.getSourceMode());
        List<AppVersion> appVersionList = this.appVersionMapper.find(map);//查询最新上传时间版本
        if (appVersionList == null || appVersionList.size() < 2) {
            return;
        }
        Date forcedUpdatingDate = null;//最新强制更新时间
        AppVersion forcedUpdatingVersion = null;//最新强制更新版本信息
        Date unForcedUpdateDate = null;//最新非强制更新时间
        AppVersion unForcedUpdateVersion = null;//最新非强制更新版本信息
        //获取最新强制更新与非强制更新版本信息，获取到直接跳出
        for (AppVersion appVersiony : appVersionList) {
            if (forcedUpdatingDate == null && "Y".equals(appVersiony.getIsForcedUpdate())) {//强制更新
                forcedUpdatingDate = appVersiony.getCreateTime();
                forcedUpdatingVersion = appVersiony;
                continue;
            }
            if (unForcedUpdateDate == null && "N".equals(appVersiony.getIsForcedUpdate())) {//非强制更新
                unForcedUpdateDate = appVersiony.getCreateTime();
                unForcedUpdateVersion = appVersiony;
                continue;
            }
            if (forcedUpdatingDate != null && unForcedUpdateDate != null) {
                break;
            }
        }

        /*Date data = DateUtil.stringDateToDate("2019-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        forcedUpdatingDate = forcedUpdatingDate == null ? data : forcedUpdatingDate;
        unForcedUpdateDate = unForcedUpdateDate == null ? data : unForcedUpdateDate;*/
        //比较强制更新时间和非强制更新时间大小,如最新版本为强制更新，所有版本都更新为强制更新
        if (forcedUpdatingDate != null && unForcedUpdateDate != null && forcedUpdatingDate.compareTo(unForcedUpdateDate) > -1) {
            for (AppVersion appVersiony : appVersionList) {
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersiony.getSourceMode() + ":" + appVersiony.getVersionNo(), appVersion.getVersionNo() + "_Y_" + forcedUpdatingVersion.getDownloadUrl() + "_" + forcedUpdatingVersion.getRemark());
            }
        } else {//强制更新在非强制更新之前，根据每一版本信息更新历史版本信息
            for (AppVersion appVersiony : appVersionList) {
                if (forcedUpdatingVersion == null) {//如没有强制更新版本信息，则将该版本信息更新为最新的非强制更新版本信息
                    CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersiony.getSourceMode() + ":" + appVersiony.getVersionNo(), appVersion.getVersionNo() + "_N_" + unForcedUpdateVersion.getDownloadUrl() + "_" + unForcedUpdateVersion.getRemark());
                } else {
                    //如果当前版本信息时间小于最新强制版本更新时间，则将该版本信息更新为最新版本的非强制版本信息
                    if (appVersiony.getCreateTime().compareTo(forcedUpdatingVersion.getCreateTime()) == -1) {
                        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersiony.getSourceMode() + ":" + appVersiony.getVersionNo(), appVersion.getVersionNo() + "_Y_" + unForcedUpdateVersion.getDownloadUrl() + "_" + unForcedUpdateVersion.getRemark());
                    } else {
                        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersiony.getSourceMode() + ":" + appVersiony.getVersionNo(), appVersion.getVersionNo() + "_N_" + unForcedUpdateVersion.getDownloadUrl() + "_" + unForcedUpdateVersion.getRemark());
                    }
                }
            }
        }
    }

    @Override
    public Map findVersionCache(String sourceMode, String versionNo) {
        Map remap = new HashMap<>();
        Object obj = CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.APPVERSION.value() + sourceMode + ":" + versionNo);
        if (obj != null && !"".equals(obj)) {
            String[] qzt = obj.toString().split("_");
            remap.put("versionNo", qzt[0]);
            remap.put("isForcedUpdate", qzt[1]);
            remap.put("downloadUrl", qzt[2]);
            remap.put("remark", qzt[3]);
        } else {
            remap.put("versionNo", versionNo);
            remap.put("isForcedUpdate", "N");
            remap.put("downloadUrl", "");
            remap.put("remark", "");
        }
        return remap;
    }

    @Override
    public AppVersion findMaxVersion(String sourceMode) {
        return this.appVersionMapper.findMaxVersion(sourceMode);
    }

}

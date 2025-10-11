package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.ump.common.UmpConstants;
import com.qzt.ump.dao.mapper.SysParamMapper;
import com.qzt.ump.model.SysParamModel;
import com.qzt.ump.rpc.api.SysParamService;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 全局参数表 服务实现类
 * </p>
 *
 * @author shadj
 * @since 2017-12-24
 */
@Service("sysParamService")
@CacheConfig(cacheNames = UmpConstants.UmpCacheName.PARAM)
public class SysParamServiceImpl extends BaseServiceImpl<SysParamMapper, SysParamModel> implements SysParamService {

    @Override
    public Page<SysParamModel> queryListPage(Page<SysParamModel> page) {
        SysParamModel sysParamModel = new SysParamModel();
        sysParamModel.setIsDel(0);
        EntityWrapper<SysParamModel> entityWrapper = new EntityWrapper<>(sysParamModel);
        if (ObjectUtil.isNotNull(page.getCondition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<String, Object> paramMap = page.getCondition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' AND ");
                }
            });
            entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        page.setCondition(null);
        return super.selectPage(page, entityWrapper);
    }

    @Override
    public List<SysParamModel> findLinst(Map paramMap) {
        SysParamModel sysParamModel = new SysParamModel();
        sysParamModel.setIsDel(0);
        EntityWrapper<SysParamModel> entityWrapper = new EntityWrapper<>(sysParamModel);
        if (ObjectUtil.isNotNull(paramMap)) {
            StringBuilder conditionSql = new StringBuilder();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' AND ");
                }
            });
            entityWrapper.and(StrUtil.removeSuffix(conditionSql.toString(), "AND "));
        }
        return super.selectList(entityWrapper);
    }

    @Override
    public void initializeParam() {
        List<SysParamModel> sysParamModelList = this.findLinst(new HashMap());
        CacheUtil.getCache().delAll(SysConstant.CacheNamespaceEnum.PARAMES.value() + "*");
        for (SysParamModel spm : sysParamModelList) {
            if (0 == spm.getIsDel() && 1 == spm.getEnable()) {
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.PARAMES.value() + spm.getParamKey(), spm);
            }
        }
    }

    @Override
    @CacheEvict(value = UmpConstants.UmpCacheName.PARAM, allEntries = true)
    public SysParamModel add(SysParamModel paramModel) {
        return super.add(paramModel);
    }

    @Override
    @CacheEvict(value = UmpConstants.UmpCacheName.PARAM, allEntries = true)
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        List<SysParamModel> sysParamModelList = new ArrayList<SysParamModel>();
        idList.forEach(id -> {
            SysParamModel entity = new SysParamModel();
            entity.setId((Long) id);
            entity.setIsDel(1);
            entity.setUpdateTime(new Date());
            sysParamModelList.add(entity);
        });
        return super.updateBatchById(sysParamModelList);
    }
}

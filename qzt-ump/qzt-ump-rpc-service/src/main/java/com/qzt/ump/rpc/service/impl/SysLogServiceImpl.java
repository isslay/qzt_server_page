package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.dao.mapper.SysLogMapper;
import com.qzt.ump.model.SysLogModel;
import com.qzt.ump.rpc.api.SysLogService;
import com.xiaoleilu.hutool.util.ObjectUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统日志 服务实现类
 * </p>
 *
 * @author RickyWang
 * @since 2017-12-26
 */
@Service("sysLogService")
@Slf4j
public class SysLogServiceImpl extends BaseServiceImpl<SysLogMapper, SysLogModel> implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public Page<SysLogModel> queryListPage(Page<SysLogModel> page) {
        EntityWrapper<SysLogModel> entityWrapper = new EntityWrapper<>();
        if (ObjectUtil.isNotNull(page.getCondition())) {
            StringBuilder conditionSql = new StringBuilder();
            Map<String, Object> paramMap = page.getCondition();
            paramMap.forEach((k, v) -> {
                if (StrUtil.isNotBlank(v + "")) {
                    conditionSql.append(k + " like '%" + v + "%' OR ");
                }
            });
            if(StrUtil.isNotBlank(conditionSql)){
                entityWrapper.where(StrUtil.removeSuffix(conditionSql.toString(), "OR "));
            }
        }
        entityWrapper.orderBy("create_time", false);
        page.setCondition(null);
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<SysLogModel> rb = super.selectPage(page, entityWrapper).getRecords();
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }
}

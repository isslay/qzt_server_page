package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmUsableIntegralMapper;
import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.bus.rpc.api.IDgmUsableIntegralService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateTime;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@Service("dgmUsableIntegralService")
public class DgmUsableIntegralServiceImpl extends BaseServiceImpl<DgmUsableIntegralMapper, DgmUsableIntegral> implements IDgmUsableIntegralService {

    @Autowired
    private DgmUsableIntegralMapper dgmUsableIntegralMapper;

    @Override
    public Page<DgmUsableIntegral> find(Page<DgmUsableIntegral> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<DgmUsableIntegral> rb = this.dgmUsableIntegralMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public DgmUsableIntegral findById(Long id) {
        return this.dgmUsableIntegralMapper.findById(id);
    }

    @Override
    public List<DgmUsableIntegral> findList(Map<String, Object> map) {
        return this.dgmUsableIntegralMapper.find(map);
    }

    @Override
    public int sumAll(int userId) {
        return this.dgmUsableIntegralMapper.sumAll(userId);
    }

    @Override
    public int monthOver(int userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String ss = sdf.format(new Date());
        String lastDate = DateTime.getMonthLastDay(ss) + " 23:59:59";
        return this.dgmUsableIntegralMapper.monthOver(userId, lastDate);
    }
}

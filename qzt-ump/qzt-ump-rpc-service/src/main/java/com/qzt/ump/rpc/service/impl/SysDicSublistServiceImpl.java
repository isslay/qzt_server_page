package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.dao.mapper.SysDicSublistMapper;
import com.qzt.ump.model.SysDicSublist;
import com.qzt.ump.rpc.api.SysDicSublistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
@Service("sysDicsSublistService")
public class SysDicSublistServiceImpl extends BaseServiceImpl<SysDicSublistMapper, SysDicSublist> implements SysDicSublistService {

    @Autowired
    private SysDicSublistMapper rhsDicsSublistMapper;

    @Override
    public Page<SysDicSublist> find(Page<SysDicSublist> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<SysDicSublist> rb = rhsDicsSublistMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public List selectDicListByDicType(String dicType) {
        Map map = new HashMap();
        map.put("dicType", dicType);
        List byDicType = rhsDicsSublistMapper.findByDicType(map);
        if(byDicType.size() > 0){
            return byDicType;
        }
        return null;
    }
}

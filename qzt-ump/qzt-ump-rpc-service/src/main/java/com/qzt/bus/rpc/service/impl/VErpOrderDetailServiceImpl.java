package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.VErpOrderDetailMapper;
import com.qzt.bus.model.VErpOrderDetail;
import com.qzt.bus.rpc.api.IVErpOrderDetailService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author snow
 * @since 2023-10-23
 */
@Service("vErpOrderDetailService")
public class VErpOrderDetailServiceImpl extends BaseServiceImpl<VErpOrderDetailMapper, VErpOrderDetail> implements IVErpOrderDetailService {

    @Autowired
    private VErpOrderDetailMapper vErpOrderDetailMapper;

    @Override
    public Page<VErpOrderDetail> find(Page<VErpOrderDetail> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<VErpOrderDetail> rb = this.vErpOrderDetailMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public List<VErpOrderDetail> findList(Map<String, Object> map) {
        return this.vErpOrderDetailMapper.find(map);
    }
}

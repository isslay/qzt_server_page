package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmGoodsCouponMapper;
import com.qzt.bus.model.DgmGoodsCoupon;
import com.qzt.bus.rpc.api.IDgmGoodsCouponService;
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
 * @since 2023-10-18
 */
@Service("dgmGoodsCouponService")
public class DgmGoodsCouponServiceImpl extends BaseServiceImpl<DgmGoodsCouponMapper, DgmGoodsCoupon> implements IDgmGoodsCouponService {

    @Autowired
    private DgmGoodsCouponMapper dgmGoodsCouponMapper;

    @Override
    public Page<DgmGoodsCoupon> find(Page<DgmGoodsCoupon> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<DgmGoodsCoupon> rb = this.dgmGoodsCouponMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public List<DgmGoodsCoupon> findList(Map<String, Object> map) {
        return this.dgmGoodsCouponMapper.find(map);
    }

}

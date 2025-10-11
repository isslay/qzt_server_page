package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmCouponIntegralMapper;
import com.qzt.bus.dao.mapper.QztUserCouponMapper;
import com.qzt.bus.model.DgmCouponIntegral;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.bus.rpc.api.IDgmCouponIntegralService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
@Service("dgmCouponIntegralService")
public class DgmCouponIntegralServiceImpl extends BaseServiceImpl<DgmCouponIntegralMapper, DgmCouponIntegral> implements IDgmCouponIntegralService {

    @Autowired
    private DgmCouponIntegralMapper dgmCouponMapper;
    @Autowired
    private QztUserCouponMapper qztUserCouponMapper;

    @Override
    public Page<DgmCouponIntegral> find(Page<DgmCouponIntegral> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<DgmCouponIntegral> rb = this.dgmCouponMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<DgmCouponIntegral> findList(Map<String, Object> map) {
        return this.dgmCouponMapper.find(map);
    }

    @Override
    public void creatCouponIntegral(Long userId, Long couponId, int num) {
        DgmCouponIntegral dc = dgmCouponMapper.selectById(couponId);
        try {
            Date now = new Date();
            Date nowEnd = DateUtil.getTimingDay(now, 90);
            for (int i = 0; i < num; i++) {
                QztUserCoupon coupon = new QztUserCoupon();
                coupon.setState("1");
                coupon.setGoodsType("05");//积分券
                coupon.setCouponId(dc.getId());
                coupon.setCouponMoney(dc.getCouponMoney());
                coupon.setCouponRemark(dc.getCouponRemark());
                coupon.setTargetMoney(dc.getTargetMoney());
                coupon.setActiveTime(now);
                coupon.setOverTime(nowEnd);
                coupon.setUserId(userId);
                coupon.setCreateTime(now);
                coupon.setCreateBy(userId);
                this.qztUserCouponMapper.insert(coupon);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}

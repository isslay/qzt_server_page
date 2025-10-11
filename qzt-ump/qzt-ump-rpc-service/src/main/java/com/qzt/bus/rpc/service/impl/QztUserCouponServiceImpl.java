package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztAccountRelogMapper;
import com.qzt.bus.dao.mapper.QztUserCouponMapper;
import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.bus.rpc.api.IQztUserCouponService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztUserCouponService")
public class QztUserCouponServiceImpl extends BaseServiceImpl<QztUserCouponMapper, QztUserCoupon> implements IQztUserCouponService {

    @Autowired
    private QztUserCouponMapper qztUserCouponMapper;

    @Autowired
    private QztAccountRelogMapper qztAccountRelogMapper;

    @Override
    public Page<QztUserCoupon> find(Page<QztUserCoupon> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztUserCoupon> rb = this.qztUserCouponMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztUserCoupon> findList(Map<String, Object> map) {
        return this.qztUserCouponMapper.find(map);
    }

    @Override
    public QztUserCoupon queryEffectiveVouchers(QztUserCoupon qztUserCoupon) {
        return this.qztUserCouponMapper.queryEffectiveVouchers(qztUserCoupon);
    }

    @Override
    public boolean kockStampsUpdate(String state, String orderNo, Long cashCouponId, Long userId) {
        return this.qztUserCouponMapper.kockStampsUpdate(new QztUserCoupon(state, orderNo, cashCouponId, userId)) == 1;
    }

    @Override
    public List<Map> queryCouponByUserId(Long userId) {
        Map pmap = new HashMap();
//        pmap.put("goodsType", "01");
//        pmap.put("validTime", new Date());
        pmap.put("state", "1");
        pmap.put("goodsType", "01");
        pmap.put("userId", userId);
        List<QztUserCoupon> couponList = this.qztUserCouponMapper.find(pmap);
        List<Map> dataList = new ArrayList<>();
        for (QztUserCoupon qztUserCoupon : couponList) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
//            maps.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getActiveTime()));//开始时间
//            maps.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getOverTime()));//结束时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额
            dataList.add(maps);
        }
        return dataList;
    }

    @Override
    public List<Map> queryCouponByUserId(Long userId, Long num) {
        Map pmap = new HashMap();
        pmap.put("state", "1");
        pmap.put("goodsType", "01");
        pmap.put("userId", userId);
        pmap.put("targetMoney", num);
        List<QztUserCoupon> couponList = this.qztUserCouponMapper.find(pmap);
        List<Map> dataList = new ArrayList<>();
        for (QztUserCoupon qztUserCoupon : couponList) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
////            maps.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getActiveTime()));//开始时间
////            maps.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getOverTime()));//结束时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额

//            maps.put("targetMoney", qztUserCoupon.getTargetMoney() / 100);
//            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);
//            maps.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getActiveTime()));
//            maps.put("usedTime", qztUserCoupon.getUsedTime()==null?"":DateUtil.chineseDateFormat.format(qztUserCoupon.getUsedTime()));
//            maps.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getOverTime()));
//            maps.put("id",qztUserCoupon.getId());
            maps.put("couponRemark",qztUserCoupon.getCouponRemark());
            maps.put("couponType",qztUserCoupon.getGoodsType());

            dataList.add(maps);
        }

        Map pmap1 = new HashMap();
        pmap1.put("state", "1");
        pmap1.put("goodsType", "04");
        pmap1.put("userId", userId);
        pmap1.put("targetMoney", num);
        List<QztUserCoupon> couponList1 = this.qztUserCouponMapper.find(pmap1);
        for (QztUserCoupon qztUserCoupon : couponList1) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额

            maps.put("couponRemark",qztUserCoupon.getCouponRemark());
            maps.put("couponType","01");

            dataList.add(maps);
        }

        Map pmap2 = new HashMap();
        pmap2.put("state", "1");
        pmap2.put("goodsType", "05");
        pmap2.put("userId", userId);
        pmap2.put("targetMoney", num);
        List<QztUserCoupon> couponList2 = this.qztUserCouponMapper.find(pmap2);
        for (QztUserCoupon qztUserCoupon : couponList2) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额

            maps.put("couponRemark",qztUserCoupon.getCouponRemark());
            maps.put("couponType","01");

            dataList.add(maps);
        }
        return dataList;
    }

    public List<QztUserCoupon> queryStorePromotion(Long userId) {
        Map pmap = new HashMap();
        pmap.put("state", "1");
        pmap.put("userId", userId);
        pmap.put("goodsType", "02");
        List<QztUserCoupon> couponList = this.qztUserCouponMapper.find(pmap);
        return couponList;
    }

    public List<Map> queryGoodsPromotion(Long userId,String goodsIds) {
        Map pmap = new HashMap();
        pmap.put("state", "1");
        pmap.put("goodsType", "03");
        pmap.put("userId", userId);
        pmap.put("goodsIds", goodsIds.split(","));
        List<QztUserCoupon> couponList = this.qztUserCouponMapper.find(pmap);
        List<Map> dataList = new ArrayList<>();
        for (QztUserCoupon qztUserCoupon : couponList) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额

//            maps.put("targetMoney", qztUserCoupon.getTargetMoney() / 100);
//            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);
//            maps.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getActiveTime()));
//            maps.put("usedTime", qztUserCoupon.getUsedTime()==null?"":DateUtil.chineseDateFormat.format(qztUserCoupon.getUsedTime()));
//            maps.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getOverTime()));
//            maps.put("id",qztUserCoupon.getId());
            maps.put("couponRemark",qztUserCoupon.getCouponRemark());
            maps.put("couponType",qztUserCoupon.getGoodsType());
            dataList.add(maps);
        }
        return dataList;
    }

    @Override
    public List<Map> queryActivityPromotion(Long userId) {
        Map pmap = new HashMap();
        pmap.put("state", "1");
        pmap.put("goodsType", "04");
        pmap.put("userId", userId);
        List<QztUserCoupon> couponList = this.qztUserCouponMapper.find(pmap);
        List<Map> dataList = new ArrayList<>();
        for (QztUserCoupon qztUserCoupon : couponList) {
            Map maps = new HashMap();
            maps.put("couponId", qztUserCoupon.getId());//券ID
            maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
            maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
            maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间
            maps.put("fullAmountReduction", qztUserCoupon.getTargetMoney() / 100);//满减金额
            maps.put("couponRemark",qztUserCoupon.getCouponRemark());
            maps.put("couponType",qztUserCoupon.getGoodsType());
            dataList.add(maps);
        }
        return dataList;
    }

    @Transactional
    @Override
    public String saveUserCoupon(Map<String, Object> params) throws Exception {
        try {
            List<QztAccountRelog> allRelogs = qztAccountRelogMapper.findHaveAccount(params);
            Long allAccount = qztAccountRelogMapper.findCountHaveAccount(params);
            if (allAccount < Long.parseLong(params.get("price").toString())) {
                return "0001";
            }
            long allPrice = Long.parseLong(params.get("price").toString());
            List<QztAccountRelog> updateRs = new ArrayList<>();
            for (QztAccountRelog qztAccountRelog : allRelogs) {
                long syPrice = allPrice - qztAccountRelog.getReMoney() + qztAccountRelog.getChangeMoney() + qztAccountRelog.getGiveMoney();
                if (syPrice > 0) {
                    qztAccountRelog.setGiveMoney(qztAccountRelog.getReMoney() - qztAccountRelog.getChangeMoney());
                    updateRs.add(qztAccountRelog);
                } else {
                    qztAccountRelog.setGiveMoney(qztAccountRelog.getGiveMoney() + allPrice);
                    updateRs.add(qztAccountRelog);
                    break;
                }
            }
            if (allRelogs.size() > 0) {
                //循环修改金额
                for (QztAccountRelog qztAccountRelog : allRelogs) {
                    qztAccountRelog.setUpdateTime(new Date());
                    int rs = this.qztAccountRelogMapper.updateById(qztAccountRelog);
                    if (rs == 0) {
                        return "0002";
                    }
                }
                //创建券
                QztUserCoupon qztUserCoupon = new QztUserCoupon();
                qztUserCoupon.setGiveUserId((Long) params.get("userId"));
                qztUserCoupon.setState("0");
                qztUserCoupon.setGoodsType("01");
                qztUserCoupon.setCreateTime(new Date());
                qztUserCoupon.setCreateBy((Long) params.get("userId"));
                qztUserCoupon.setCouponMoney(allPrice);
                qztUserCoupon.setOverTime(DateUtil.addYear(qztUserCoupon.getCreateTime(), 1));

                this.qztUserCouponMapper.insert(qztUserCoupon);

                return qztUserCoupon.getId().toString();

            } else {
                return null;
            }

        } catch (Exception re) {
            re.printStackTrace();
            new Exception("保存失败");
        }
        return null;
    }

    @Override
    public boolean activeCoupon(Map<String, Object> params) {
        int rs = this.qztUserCouponMapper.updateActiveCoupon(params);
        if (rs > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int updatePassCoupon(Map<String, Object> params) {
        return this.qztUserCouponMapper.updatePassCoupon(params);
    }


}

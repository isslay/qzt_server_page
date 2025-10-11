package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmCouponMapper;
import com.qzt.bus.dao.mapper.QztUserCouponMapper;
import com.qzt.bus.model.DgmCoupon;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.bus.rpc.api.IDgmCouponService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.DateUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
@Service("dgmCouponService")
public class DgmCouponServiceImpl extends BaseServiceImpl<DgmCouponMapper, DgmCoupon> implements IDgmCouponService {

    @Autowired
    private DgmCouponMapper dgmCouponMapper;
    @Autowired
    private QztUserCouponMapper qztUserCouponMapper;

    @Override
    public Page<DgmCoupon> find(Page<DgmCoupon> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<DgmCoupon> rb = this.dgmCouponMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<DgmCoupon> findList(Map<String, Object> map) {
        return this.dgmCouponMapper.find(map);
    }

    @Override
    public Object creatUserCoupon(Long userId) {
        List list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("couponType", 1);
        List<DgmCoupon> rb = this.dgmCouponMapper.find(map);
        if (rb.size() > 0) {
            try {
                Date now = new Date();
                Date nowEnd = DateUtil.getTimingDay(now, 60);
                for (DgmCoupon dc : rb) {
                    List<QztUserCoupon> qztUserCouponList = this.qztUserCouponMapper.queryEffectiveVouchersList(new QztUserCoupon(dc.getId(), userId, "1"));
//                    List<QztUserCoupon> qztUserCouponListToday = this.qztUserCouponMapper.queryEffectiveVouchersListToday(new QztUserCoupon(dc.getId(), userId, DateUtil.chineseDateFormat.format(now), 1));
//                    if (qztUserCouponList.size() < dc.getNumber() && qztUserCouponListToday.size() == 0) {
                    if (qztUserCouponList.size() < dc.getNumber()) {

                        QztUserCoupon coupon = new QztUserCoupon();
                        coupon.setState("1");
                        coupon.setGoodsType("01");//登录
                        coupon.setCouponId(dc.getId());
                        coupon.setCouponMoney(dc.getCouponMoney());
                        coupon.setCouponRemark(dc.getCouponRemark());
                        coupon.setTargetMoney(dc.getTargetMoney());
                        coupon.setActiveTime(now);
//                        coupon.setUsedTime(now);
                        coupon.setOverTime(nowEnd);
                        coupon.setUserId(userId);
                        coupon.setCreateTime(now);
                        coupon.setCreateBy(userId);
                        this.qztUserCouponMapper.insert(coupon);

                        Map dataMap = new HashMap();
                        dataMap.put("targetMoney", dc.getTargetMoney() / 100);
                        dataMap.put("couponMoney", dc.getCouponMoney() / 100);
                        dataMap.put("usedTime", DateUtil.chineseDateFormat.format(now));
                        dataMap.put("overTime", DateUtil.chineseDateFormat.format(nowEnd));

                        list.add(dataMap);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public void creatNewUserCoupon(Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("couponType", 2);
        List<DgmCoupon> rb = this.dgmCouponMapper.find(map);
        if (rb.size() > 0) {
            try {
                Date now = new Date();
                Date nowEnd = DateUtil.getTimingDay(now, 365);
                for (DgmCoupon dc : rb) {
                    List<QztUserCoupon> qztUserCouponList = this.qztUserCouponMapper.queryEffectiveVouchersList(new QztUserCoupon(dc.getId(), userId, "1"));
                    List<QztUserCoupon> qztUserCouponListToday = this.qztUserCouponMapper.queryEffectiveVouchersListToday(new QztUserCoupon(dc.getId(), userId, DateUtil.chineseDateFormat.format(now), 1));
                    if (qztUserCouponList.size() < dc.getNumber() && qztUserCouponListToday.size() == 0) {

                        QztUserCoupon coupon = new QztUserCoupon();
                        coupon.setState("1");
                        coupon.setGoodsType("02");//新用户
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
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void creatGoodsUserCoupon(Long userId, Long couponId) {
        DgmCoupon dc = this.dgmCouponMapper.selectById(couponId);
        if (dc != null && dc.getStatus() == 0) {
            try {
                Date now = new Date();
                Date nowEnd = DateUtil.getTimingDay(now, 90);

                QztUserCoupon coupon = new QztUserCoupon();
                coupon.setState("1");
                coupon.setGoodsType("03");//商品券
                coupon.setCouponId(dc.getId());
                coupon.setCouponMoney(dc.getCouponMoney());
                coupon.setCouponRemark(dc.getCouponRemark());
                coupon.setTargetMoney(dc.getTargetMoney());
                coupon.setGoodsId(dc.getGoodsId());//商品编号
                coupon.setActiveTime(now);
                coupon.setOverTime(nowEnd);
                coupon.setUserId(userId);
                coupon.setCreateTime(now);
                coupon.setCreateBy(userId);
                this.qztUserCouponMapper.insert(coupon);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 活动券
     *
     * @param userId
     * @return
     */
    @Override
    public Object creatActivityCoupon(Long userId) {
        List list = new ArrayList();
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String ss = sdf.format(new Date());
        map.put("status", 0);
        map.put("couponType", 4);
        map.put("couponBegin", ss);
        map.put("couponEnd", ss);
        List<DgmCoupon> rb = this.dgmCouponMapper.find(map);
        if (rb.size() > 0) {
            try {
                Date now = new Date();
                for (DgmCoupon dc : rb) {
                    List<QztUserCoupon> qztUserCouponList = this.qztUserCouponMapper.queryEffectiveVouchersListAll(new QztUserCoupon(dc.getId(), userId, ""));
                    if (qztUserCouponList.size() == 0) {//没有获取过活动券
                        QztUserCoupon coupon = new QztUserCoupon();
                        coupon.setState("1");
                        coupon.setGoodsType("04");//活动
                        coupon.setCouponId(dc.getId());
                        coupon.setCouponMoney(dc.getCouponMoney());
                        coupon.setCouponRemark(dc.getCouponRemark());
                        coupon.setTargetMoney(dc.getTargetMoney());
                        coupon.setActiveTime(sdf.parse(dc.getCouponBegin()));
                        coupon.setOverTime(sdf1.parse(dc.getCouponEnd() + " 23:59:59"));
                        coupon.setUserId(userId);
                        coupon.setCreateTime(now);
                        coupon.setCreateBy(userId);
                        this.qztUserCouponMapper.insert(coupon);

                        Map dataMap = new HashMap();
                        dataMap.put("targetMoney", dc.getTargetMoney() / 100);
                        dataMap.put("couponMoney", dc.getCouponMoney() / 100);
                        dataMap.put("usedTime", dc.getCouponBegin());
                        dataMap.put("overTime", dc.getCouponEnd());

                        list.add(dataMap);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}

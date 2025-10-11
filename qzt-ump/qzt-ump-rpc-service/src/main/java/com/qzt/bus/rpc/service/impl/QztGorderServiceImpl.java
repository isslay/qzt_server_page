package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztGorderMapper;
import com.qzt.bus.model.*;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.mq.pushMq.MqConstant;
import com.qzt.common.mq.pushMq.MqMess;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.*;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.rpc.api.SysAreaService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@Log4j
@Service("qztGorderService")
public class QztGorderServiceImpl extends BaseServiceImpl<QztGorderMapper, QztGorder> implements IQztGorderService {

    private final int AMOUNT = 10;//页面显示延长分钟数
    private final Long TIME = 10L;//时间长度
    private final String TIMETYPE = "M";//时间类型，D为天，H为小时，M为分钟，S为秒钟。（注意大写，默认分钟）

    @Autowired
    private QztGorderMapper qztGorderMapper;

    @Autowired
    private IQztGoodsService qztGoodsService;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private IQztUserCouponService qztUserCouponService;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private RedisTimeServiceImpl redisTimeService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Autowired
    private IQztStoGoodsService qztStoGoodsService;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private IDgmIntegralRecordService integralRecordService;

    @Override
    public Page find(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztGorder> rb = this.qztGorderMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<QztGorder> findList(Map pmap) {
        return this.qztGorderMapper.find(pmap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map subGorder(QztGorder qztGorder) throws Exception {
        Map remap = new HashMap();
        QztUser qztUser = this.qztUserService.findDGUserById(qztGorder.getUserId());
        if (qztUser == null) {
            remap.put("code", "0003");
            return remap;
        }
        //处理商品相关信息
        String isSingleCommodity = "N";
        qztGorder.setOrderNo(OrderUtil.generateOrderNo(OrderUtil.OrderNoEnum.GOODS.value()));//生成订单编号
        if (qztGorder.getGoodsId() != null) {//单商品购买处理
            QztStoGoods qztStoGoods = new QztStoGoods(qztGorder.getUserId(), qztGorder.getGoodsId(), qztGorder.getBuyNum());
            isSingleCommodity = "Y";
            qztStoGoods.setIsCart("N");
            qztStoGoods.setOrderNo(qztGorder.getOrderNo());
            qztStoGoods.setIsSingleCommodity(isSingleCommodity);
            QztStoGoods add = this.qztStoGoodsService.add(qztStoGoods);
            if (add == null) {
                remap.put("code", "0003");
                return remap;
            }
        }
        if ("0".equals(qztGorder.getPickupWay())) {//自提 0
//            QztBusiness qztBusiness = this.qztBusinessService.queryById(qztGorder.getAddressId());
//            if (qztBusiness == null || !"0".equals(qztBusiness.getBusState())) {
//                throw new Exception("6025");
//            }
//            //处理商家补货地址
//            String replenishCneName = "";//补货-收货人名称
//            String replenishCneTel = "";//补货-收货人电话
//            String replenishCneAddress = "";//补货-收货地址
//            QztUserAddress healthyUserAddress = this.qztUserAddressService.selectDefaultaccAddress(qztBusiness.getUserId());
//            if (healthyUserAddress == null) {
//                replenishCneName = qztBusiness.getBusName();
//                replenishCneTel = qztBusiness.getBusTel();
//                replenishCneAddress = qztBusiness.getBusAddress();
//            } else {
//                String areaName = this.sysAreaService.selectAreaName(healthyUserAddress.getProvince(), healthyUserAddress.getCity(), healthyUserAddress.getArea());
//                replenishCneName = healthyUserAddress.getRecipientName();
//                replenishCneTel = healthyUserAddress.getPhone();
//                replenishCneAddress = areaName + healthyUserAddress.getDetailAddress();
//            }
//            qztGorder.setReplenishCneName(replenishCneName);
//            qztGorder.setReplenishCneTel(replenishCneTel);
//            qztGorder.setReplenishCneAddress(replenishCneAddress);
//
//            qztGorder.setConsigneeName(qztBusiness.getBusName());//商家姓名
//            qztGorder.setConsigneeTel(qztBusiness.getBusTel());//商家电话
//            qztGorder.setConsigneeAddress(qztBusiness.getBusAddress());//商家地址
//            qztGorder.setTheVerificationCode(OrderUtil.generateChargeOff());//生成核销码
        } else {//快递 1
            //处理地址相关信息
            Map<String, String> addressInfo = this.qztUserAddressService.disposeAddressInfo(qztGorder.getAddressId());
            if (addressInfo == null) {
                throw new Exception("6002");
            }
            qztGorder.setConsigneeName(addressInfo.get("name"));
            qztGorder.setConsigneeTel(addressInfo.get("tel"));
            qztGorder.setConsigneeAddress(addressInfo.get("address"));
        }

        Long couponMoney = 0L;//折扣券抵扣金额
        Long couponMoneyTarget = 0L;//折扣券抵扣最低设置
        Long storeCouponMoney = 0L;//新人券抵扣金额
        Long goodsCouponMoney = 0L;//商品券抵扣金额
        Long activityCouponMoney = 0L;//活动券抵扣金额
        Long totalPostage = 0L;//邮费
        Long goodsTotalPrices = 0L;//商品总价
        Long goodsTotalCostPrices = 0L;//商品成本总价
        Long tShareMoney = 0L;//分享佣金
        Long tRecommendMoney = 0L;//推广佣金
        Long tServiceMoney = 0L;//服务佣金
        //使用抵扣券处理
        if (qztGorder.getCashCouponId() != null) {
            QztUserCoupon qztUserCoupon = this.qztUserCouponService.queryEffectiveVouchers(new QztUserCoupon(qztGorder.getCashCouponId(), qztGorder.getUserId()));
            if (qztUserCoupon == null) {
                throw new Exception("6021");
            }
            //锁券处理
            boolean resulrbo = this.qztUserCouponService.kockStampsUpdate("2", qztGorder.getOrderNo(), qztGorder.getCashCouponId(), qztGorder.getUserId());//锁券
            if (!resulrbo) {
                throw new Exception("6021");
            }
            couponMoney = qztUserCoupon.getCouponMoney();
            couponMoneyTarget = qztUserCoupon.getTargetMoney();
        }

        //使用新人券处理
        if (qztGorder.getStoreCouponId() != null) {
            QztUserCoupon qztUserCoupon = this.qztUserCouponService.queryEffectiveVouchers(new QztUserCoupon(qztGorder.getStoreCouponId(), qztGorder.getUserId()));
            if (qztUserCoupon == null) {
                throw new Exception("6021");
            }
            //锁券处理
            boolean resulrbo = this.qztUserCouponService.kockStampsUpdate("2", qztGorder.getOrderNo(), qztGorder.getStoreCouponId(), qztGorder.getUserId());//锁券
            if (!resulrbo) {
                throw new Exception("6021");
            }
            storeCouponMoney = qztUserCoupon.getCouponMoney();
        }

        //使用商品券处理
        if (qztGorder.getGoodsCouponId() != null) {
            QztUserCoupon qztUserCoupon = this.qztUserCouponService.queryEffectiveVouchers(new QztUserCoupon(qztGorder.getGoodsCouponId(), qztGorder.getUserId()));
            if (qztUserCoupon == null) {
                throw new Exception("6021");
            }
            //锁券处理
            boolean resulrbo = this.qztUserCouponService.kockStampsUpdate("2", qztGorder.getOrderNo(), qztGorder.getGoodsCouponId(), qztGorder.getUserId());//锁券
            if (!resulrbo) {
                throw new Exception("6021");
            }
            goodsCouponMoney = qztUserCoupon.getCouponMoney();
        }

        //使用活动券处理
        if (qztGorder.getActivityCouponId() != null) {
            QztUserCoupon qztUserCoupon = this.qztUserCouponService.queryEffectiveVouchers(new QztUserCoupon(qztGorder.getActivityCouponId(), qztGorder.getUserId()));
            if (qztUserCoupon == null) {
                throw new Exception("6021");
            }
            //锁券处理
            boolean resulrbo = this.qztUserCouponService.kockStampsUpdate("2", qztGorder.getOrderNo(), qztGorder.getActivityCouponId(), qztGorder.getUserId());//锁券
            if (!resulrbo) {
                throw new Exception("6021");
            }
//            activityCouponMoney = qztUserCoupon.getCouponMoney();

            //当成折扣卷判定
            couponMoney = qztUserCoupon.getCouponMoney();
            couponMoneyTarget = qztUserCoupon.getTargetMoney();
        }


        List<QztStoGoods> upQztStoGoodsList = new ArrayList<>();
        List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getSettlementGoodsList(qztGorder.getUserId(), isSingleCommodity, qztGorder.getOrderNo());

        //去得最大分享佣金比例
        Map<Long, QztGoods> qztGoodsMap = new HashMap<>();
//        Long maxRatioGoodsId = 0L;//分享佣金比例最高商品ID
//        Long gShareRatio = 0L;//分享佣金比例
        for (int i = 0; i < qztStoGoodsList.size(); i++) {
            QztStoGoods qztStoGoods = qztStoGoodsList.get(i);
            QztGoods qztGoods = this.qztGoodsService.queryById(qztStoGoods.getGoodsId());
//            if (gShareRatio < qztGoods.getShareMoney()) {
//                gShareRatio = qztGoods.getShareMoney();
//                maxRatioGoodsId = qztGoods.getId();
//            }
            qztGoodsMap.put(qztGoods.getId(), qztGoods);
        }

        for (QztStoGoods qztStoGoods : qztStoGoodsList) {
            QztGoods qztGoods = qztGoodsMap.get(qztStoGoods.getGoodsId());
            if (!this.verifyProductStatus(qztGoods)) {
                throw new Exception("6003");//不存在或已下架
            }
            if ("1".equals(qztGorder.getPickupWay())) {//快递
                //减少库存
                if (qztGorder.getBuyNum() > qztGoods.getGoodsNum() || this.qztGoodsService.reduceGoodsRepertory(qztStoGoods.getGoodsId(), qztStoGoods.getBuyNum()) != 1) {
                    throw new Exception("6005");//库存不足
                }
            }
            Long freight = qztGoods.getFreight() == null || qztGoods.getFreight() < 0 ? 0 : qztGoods.getFreight();//单商品邮费总额
            Long tpostage = freight * qztStoGoods.getBuyNum();//单商品邮费总额
            Long goodstPrices = qztGoods.getGoodsPrice() == null || qztGoods.getGoodsPrice() < 0 ? 0 : qztGoods.getGoodsPrice() * qztStoGoods.getBuyNum();//单商品总价
            Long goodstCostPrices = qztGoods.getGoodsCostPrice() == null || qztGoods.getGoodsCostPrice() < 0 ? 0 : qztGoods.getGoodsCostPrice() * qztStoGoods.getBuyNum();//单商品总价
            totalPostage += tpostage;
            goodsTotalPrices += goodstPrices;
            goodsTotalCostPrices += goodstCostPrices;


            tShareMoney += BigDecimalUtil.longPercentage(goodstPrices, qztGoods.getShareMoney());//分享佣金
            tRecommendMoney += BigDecimalUtil.longPercentage(goodstPrices, qztGoods.getRecommendMoney());//推广佣金
            tServiceMoney += BigDecimalUtil.longPercentage(goodstPrices, qztGoods.getServiceMoney());//服务佣金

            String isService = "0".equals(qztGoods.getIsService()) ? "Y" : "N";
            String couponStatus = qztGoods.getCouponStatus() != null && qztGoods.getCouponStatus() == 0 ? "Y" : "N";
            upQztStoGoodsList.add(new QztStoGoods(qztStoGoods.getId(), qztGorder.getOrderNo(), "N", null, qztGoods.getGoodsName(), qztGoods.getGoodsRemark(), qztGoods.getThumbnail(), qztGoods.getGoodsPrice(), freight, tpostage + goodstPrices, couponStatus, isService, qztGoods.getGoodsSpec()));
        }

        if (storeCouponMoney > 0 || goodsCouponMoney > 0 || activityCouponMoney > 0) {//使用商户券或者商品券、活动券，则判定折扣券是否可以使用
            if (goodsTotalPrices - storeCouponMoney - goodsCouponMoney - activityCouponMoney < couponMoneyTarget) {
                throw new Exception("6023");
            }
        }

        Long act = goodsTotalPrices - couponMoney - storeCouponMoney - goodsCouponMoney - activityCouponMoney + totalPostage;

        if (act < Constant.ORDER_LOW_NUM) {//支付金额小于订单支付限制
            throw new Exception("1234");
        }

        totalPostage = "0".equals(qztGorder.getPickupWay()) ? 0L : totalPostage;//自提无需邮费
//        if (couponMoney > 0 && goodsTotalPrices < 10000L) {
//            throw new Exception("6023");
//        }
        qztGorder.setShareMoney(tShareMoney);//分享佣金
        qztGorder.setRecommendMoney(tRecommendMoney);//推广佣金
        qztGorder.setServiceMoney(tServiceMoney);//服务佣金
        qztGorder.setCashCouponMoney(couponMoney);//抵扣券抵扣金额
        qztGorder.setStoreCouponMoney(storeCouponMoney);//商户券抵扣券抵扣金额
        qztGorder.setGoodsCouponMoney(goodsCouponMoney);//商品券抵扣券抵扣金额
        qztGorder.setActivityCouponMoney(activityCouponMoney);//活动券抵扣金额
        qztGorder.setPostage(totalPostage);//邮费
        qztGorder.setTotalPrice(goodsTotalPrices);//订单金额
        qztGorder.setCostPrice(goodsTotalCostPrices);//订单成本金额

        qztGorder.setActuaPayment(act < 0 ? 1L : act);//实际支付金额
        qztGorder.setDeductMax(couponMoney + storeCouponMoney + goodsCouponMoney);//抵扣券金额
        qztGorder.setIsService("Y");//是否需要服务
        qztGorder.setCouponStatus("N");//是否禁止使用抵扣券
        if (StringUtil.isEmpty(qztGorder.getUserTel())) {//如无联系方式默认为用户信息电话
//            QztUser qztUser = this.qztUserService.findDGUserById(qztGorder.getUserId());
            qztGorder.setUserTel(qztUser.getMobile());
        }
        Date date = new Date();
        Date nextMinutesTime = DateUtil.getTimingTimeMinute(date, AMOUNT);
        qztGorder.setOrderTimingTime(nextMinutesTime);
        qztGorder.setReferrerFirst(qztUser.getReferrerFirst() == null ? null : qztUser.getReferrerFirst());//推荐人ID
        qztGorder.setReferrerSecond(qztUser.getReferrerSecond() == null ? null : qztUser.getReferrerSecond());//推荐人姓名
        qztGorder.setReferrerName(qztUser.getPhShortName() == null ? null : qztUser.getPhShortName());//推荐人部门
        //添加redis倒计时取消订单
        this.redisTimeService.creatRedis(qztGorder.getOrderNo(), "01", this.TIMETYPE, this.TIME);

        QztGorder qztGorders = this.add(qztGorder);
        if (qztGorders == null) {
            throw new Exception("添加商品订单失败");
        }
        this.qztStoGoodsService.updateStoGoodsList(upQztStoGoodsList);
        remap.put("code", "200");
        remap.put("data", qztGorders.getOrderNo());
        return remap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map pay0rder(QztGorder qztGordery, String payPwd) throws Exception {
        Map remap = new HashMap();
        QztGorder qztGorder = this.qztGorderMapper.queryByOrederNoUserId(qztGordery);
        if (qztGorder == null) {
            remap.put("code", "6017");
            return remap;
        } else if (!"01".equals(qztGorder.getOrderState())) {
            remap.put("code", "6018");
            return remap;
        }
        Map payMess = new HashMap();
        qztGordery.setUpdateTime(new Date());
        //第一次支付
        if ("00".equals(qztGorder.getPayState())) {
            long actuaPayment = qztGorder.getActuaPayment();//订单需支付金额
            long threeSidesMoney = actuaPayment;//三方支付金额默认为订单需支付金额
//            if (qztGordery.getBalanceMoney() > 0) {
//                //验证支付密码
//                Long result = this.qztUserService.verifyPayPwd(qztGorder.getUserId(), payPwd);
//                if (result == -1) {
//                    remap.put("code", "6009");
//                    return remap;
//                } else if (result != 1) {
//                    remap.put("code", "6012");
//                    return remap;
//                }
//                qztGordery.setBalanceMoney(qztGordery.getBalanceMoney() > threeSidesMoney ? threeSidesMoney : qztGordery.getBalanceMoney());
//                threeSidesMoney = threeSidesMoney - qztGordery.getBalanceMoney();
//                if (threeSidesMoney == 0) {//三方为0，支付方式为余额支付
//                    qztGordery.setPayType("y");
//                }
//            }
            //第三方需要支付则判定
            if (threeSidesMoney > 0 && !"w".equals(qztGordery.getPayType()) && !"z".equals(qztGordery.getPayType())) {
                remap.put("code", "6019");
                return remap;
            }
            //余额支付处理
//            if (qztGordery.getBalanceMoney() > 0) {
//                boolean accounreuslt = this.qztAccountService.updateQztAccount(qztGorder.getOrderNo(), qztGorder.getUserId(), qztGordery.getBalanceMoney(), "01", "11", "商品订单支付消费余额【" + qztGorder.getOrderNo() + "】");
//                if (!accounreuslt) {
//                    remap.put("code", "6008");
//                    return remap;
//                }
//            }
            qztGordery.setThreeSidesMoney(threeSidesMoney);
            qztGordery.setPayState("01");//待支付三方
            Integer opresult = this.qztGorderMapper.orderPayUpdate(qztGordery);
            if (opresult != 1) {
                throw new Exception("商品订单支付更新失败");
            }
            //订单日志
            this.qztBusLogService.addBusLog("01", "订单支付", qztGorder.getOrderNo(), "商品订单支付，更新订单信息", qztGorder.getUserId());

            //三方支付如为0交易结束
            if (qztGordery.getThreeSidesMoney() == 0) {
                boolean backret = this.orderPayBack(qztGorder.getOrderNo());
                if (!backret) {
                    throw new Exception("无需三方支付回调失败");
                }
            }
        } else if ("01".equals(qztGorder.getPayState())) {//继续支付
            //判断支付方式正确性 必须是上次三方的支付方式
            if (!qztGorder.getPayType().equals(qztGordery.getPayType())) {
                remap.put("code", "6019");
                return remap;
            }
            qztGordery.setThreeSidesMoney(qztGorder.getThreeSidesMoney());
            //订单日志
            this.qztBusLogService.addBusLog("01", "继续支付", qztGorder.getOrderNo(), "商品订单继续支付", qztGorder.getUserId());
        } else {
            remap.put("code", "6018");
            return remap;
        }
        payMess.put("payType", qztGordery.getPayType());//如为0  1 则为对应三方支付平台 其他状态则支付成功
        payMess.put("orderNo", qztGordery.getOrderNo());
        payMess.put("orderType", OrderUtil.OrderNoEnum.GOODS.value());
        payMess.put("threeSidesMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGordery.getThreeSidesMoney()));
        remap.put("data", payMess);
        remap.put("code", "200");
        return remap;
    }

    @Override
    public QztGorder queryByOrederNoUserId(QztGorder qztGorder) {
        return this.qztGorderMapper.queryByOrederNoUserId(qztGorder);
    }

    @Override
    public QztGorder queryByOrederNo(String orderNo) {
        return this.qztGorderMapper.queryByOrederNo(orderNo);
    }

    @Override
    public boolean orderPayBack(String orderNo) {
        boolean resultb = true;
        QztGorder qztGorder = this.qztGorderMapper.queryByOrederNo(orderNo);
        //各订单回调后处理
        qztGorder.setPayState("11");//支付状态 已支付
        if ("0".equals(qztGorder.getPickupWay())) {//自提
            qztGorder.setOrderState("02");//订单状态 待到店
        } else {
            qztGorder.setOrderState("03");//订单状态 待发货
        }
        qztGorder.setUpdateTime(new Date());
        Integer opbresult = this.qztGorderMapper.orderPayBack(qztGorder);
        if (opbresult != 1) {
            resultb = false;
            QztGorder qztGordery = new QztGorder();
            qztGordery.setId(qztGorder.getId());
            qztGordery.setPayState("11");//支付状态 已支付
            qztGordery.setOrderState("96");
            this.modifyById(qztGordery);
        }
        //增加商品销量
        if (qztGorder.getGoodsId() != null && qztGorder.getBuyNum() != null) {
            this.qztGoodsService.addGoodsSalesVolume(qztGorder.getGoodsId(), qztGorder.getBuyNum());
        } else {
            List<QztStoGoods> qztStoGoodsList = qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), orderNo);
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                this.qztGoodsService.addGoodsSalesVolume(qztStoGoods.getGoodsId(), qztStoGoods.getBuyNum());
            }
        }

        //订单日志
        this.qztBusLogService.addBusLog("01", resultb ? "已支付" : "异常订单", qztGorder.getOrderNo(), "支付回调,更新订单" + (resultb ? "状态成功" : "出现异常"), qztGorder.getUserId());
        return resultb;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancellationOfOrder(QztGorder qztGordery) throws Exception {
        QztGorder qztGorder = this.qztGorderMapper.queryByOrederNo(qztGordery.getOrderNo());
        Integer resultcount = this.qztGorderMapper.cancellationOfOrder(qztGordery);
        if (resultcount != 1) {
            throw new Exception("取消订单失败");
        }
        //余额处理,增加对应余额
//        if (qztGorder.getBalanceMoney() != null && qztGorder.getBalanceMoney() > 0) {
//            boolean accounreuslt = this.qztAccountService.updateQztAccount(qztGorder.getOrderNo(), qztGorder.getUserId(), qztGorder.getBalanceMoney(), "01", "01", "取消商品订单返还余额【" + qztGorder.getOrderNo() + "】");
//            if (!accounreuslt) {
//                throw new Exception("用户：" + qztGorder.getUserId() + "增加余额失败【" + qztGorder.getOrderNo() + "】");
//            }
//        }
        if ("1".equals(qztGorder.getPickupWay())) {//快递方式
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                //恢复库存
                Integer result = this.qztGoodsService.addGoodsRepertory(qztStoGoods.getGoodsId(), qztStoGoods.getBuyNum());
                if (result != 1) {
                    log.info("取消订单恢复库存失败");
                    throw new Exception("取消订单失败");
                }
            }
        }
        //解锁抵扣券
        if (qztGorder.getCashCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getCashCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getStoreCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getStoreCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getGoodsCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getGoodsCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getActivityCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getActivityCouponId(), qztGorder.getUserId());
        }
        //订单日志
        this.qztBusLogService.addBusLog("01", "已取消", qztGorder.getOrderNo(), "取消订单", qztGorder.getUserId());
    }

    @Override
    public int cancellationOfOrder1(QztGorder qztGordery) throws Exception {

        Integer resultcount = this.qztGorderMapper.cancellationOfOrder1(qztGordery);
        if (resultcount != 1) {
            return 0;
        }
        QztGorder qztGorder = this.qztGorderMapper.queryByOrederNo(qztGordery.getOrderNo());

        if ("1".equals(qztGorder.getPickupWay())) {//快递方式
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                //恢复库存
                this.qztGoodsService.addGoodsRepertory(qztStoGoods.getGoodsId(), qztStoGoods.getBuyNum());
            }
        }
        //解锁抵扣券
        if (qztGorder.getCashCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getCashCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getStoreCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getStoreCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getGoodsCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getGoodsCouponId(), qztGorder.getUserId());
        }
        if (qztGorder.getActivityCouponId() != null) {
            this.qztUserCouponService.kockStampsUpdate("1", qztGorder.getOrderNo(), qztGorder.getActivityCouponId(), qztGorder.getUserId());
        }
        //订单日志
        this.qztBusLogService.addBusLog("01", "已取消", qztGorder.getOrderNo(), "取消订单", qztGorder.getUserId());
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmReceipt(QztGorder qztGorder) throws Exception {
        boolean ber = false;
        qztGorder = this.qztGorderMapper.queryByOrederNoUserId(qztGorder);
        Integer result = this.qztGorderMapper.confirmReceipt(qztGorder);
        if (result == 1) {
            //分润Mq
//            new MqMess(MqConstant.GOODS_ORDER_TOP, qztGorder.getOrderNo()).sendTop();
//            log.info("确认收货，发送分润Mq");
            //订单日志
            this.qztBusLogService.addBusLog("01", "待收货", qztGorder.getOrderNo(), "用户确认收货,更新订单状态成功", qztGorder.getUserId());
            ber = true;
            int inte = (int) (qztGorder.getActuaPayment() / 100);
            //积分
            if (inte > 0) {
                integralRecordService.creatNew(qztGorder.getUserId().intValue(), 1, "订单积分", inte);
                if (qztGorder.getShareCode() != null && !qztGorder.getShareCode().equals("")) {
                    integralRecordService.creatNew(Integer.valueOf(qztGorder.getShareCode()), 2, "推荐人订单积分", inte);
                }
            }
        }
        return ber;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean offlineDistribution(QztGorder qztGorder) {
        boolean ber = false;
        Integer result = this.qztGorderMapper.offlineDistribution(qztGorder);
        if (result == 1) {
            //分润Mq
            new MqMess(MqConstant.GOODS_ORDER_TOP, qztGorder.getOrderNo()).sendTop();
            log.info("自提，发送分润Mq");
            //订单日志
            this.qztBusLogService.addBusLog("01", "待发货", qztGorder.getOrderNo(), "自提,更新订单状态成功", qztGorder.getUserId());
            ber = true;
        }
        return ber;
    }

    @Override
    public List<QztGorder> getCanServiceOrderList(Map pmap) {
        return this.qztGorderMapper.getCanServiceOrderList(pmap);
    }

    @Override
    public boolean updateIsServe(String orderNo, String isServe, Long operatorId) throws Exception {
        QztGorder qztGorder = new QztGorder(orderNo, "Y".equals(isServe) ? "Y" : "N", operatorId);
        return this.qztGorderMapper.updateIsServe(qztGorder) == 1;
    }

    /**
     * 验证商品状态是否可购买
     *
     * @param qztGoods
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-12
     */
    private boolean verifyProductStatus(QztGoods qztGoods) {
        boolean b = true;
        if (qztGoods == null || qztGoods.getState() == null || !"1".equals(qztGoods.getState())) {
            b = false;
        }
        return b;
    }

    @Override
    public List<Map<String, Object>> gorderExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztGorder> rb = this.qztGorderMapper.find(map);
        String pickupWay = map.get("pickupWay").toString();
        for (QztGorder qztGorder : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("orderNo", qztGorder.getOrderNo());
            linkmaps.put("createTime", DateUtil.dateTimeFormat.format(qztGorder.getCreateTime()));
            linkmaps.put("userId", qztGorder.getUserId());
            linkmaps.put("reId", qztGorder.getReferrerSecond());//推荐人
            linkmaps.put("reName", qztGorder.getReferrerName());//推荐人部门
            String goodsName = "";
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                goodsName += "；" + qztStoGoods.getGoodsName() + ", x " + qztStoGoods.getBuyNum();
            }
            goodsName = goodsName.substring(1);
            linkmaps.put("goodsName", goodsName);
            String orderStateMc = "";
            if ("0".equals(qztGorder.getPickupWay()) && "09".equals(qztGorder.getOrderState())) {//自提
                orderStateMc = "已核销";
            } else {
                orderStateMc = qztGorder.getOrderState() == null ? "" : DicParamUtil.getDicCodeByType("ORDER_STATE", qztGorder.getOrderState());
            }
            linkmaps.put("orderStateMc", orderStateMc);//订单状态

//            linkmaps.put("balanceMoney", PriceUtil.moneyLongToDecimal(qztGorder.getBalanceMoney()));//余额支付
            linkmaps.put("balanceMoney", PriceUtil.moneyLongToDecimal(qztGorder.getTotalPrice()));//订单金额
            linkmaps.put("costMoney", PriceUtil.moneyLongToDecimal(qztGorder.getCostPrice()));//成本金额

//            linkmaps.put("cashCouponMoney", PriceUtil.moneyLongToDecimal(qztGorder.getCashCouponMoney()));//券支付
            linkmaps.put("cashCouponMoney", PriceUtil.moneyLongToDecimal(qztGorder.getDeductMax()));//券支付

//            linkmaps.put("threeSidesMoney", PriceUtil.moneyLongToDecimal(qztGorder.getThreeSidesMoney()));//三方支付


//            linkmaps.put("payType", qztGorder.getPayType() == null ? "" : DicParamUtil.getDicCodeByType("PAY_TYPE", qztGorder.getPayType()));
            linkmaps.put("actuaPayMoney", PriceUtil.moneyLongToDecimal(qztGorder.getActuaPayment()));//实际支付

            linkmaps.put("payState", qztGorder.getPayState() == null ? "" : DicParamUtil.getDicCodeByType("PAY_STATE", qztGorder.getPayState()));
            linkmaps.put("payTime", qztGorder.getPayTime() == null ? "" : DateUtil.dateTimeFormat.format(qztGorder.getPayTime()));
            if ("0".equals(pickupWay)) {//自提
                linkmaps.put("theVerificationCode", qztGorder.getTheVerificationCode());
            }
            linkmaps.put("consigneeName", qztGorder.getConsigneeName());
            linkmaps.put("consigneeTel", qztGorder.getConsigneeTel());
            linkmaps.put("consigneeAddress", qztGorder.getConsigneeAddress());
            if ("1".equals(pickupWay)) {//快递
                linkmaps.put("courierCompany", qztGorder.getCourierCompany());
                linkmaps.put("courierNo", qztGorder.getCourierNo());
                linkmaps.put("courierRemarks", qztGorder.getCourierRemarks());
                linkmaps.put("shipmentsTime", qztGorder.getShipmentsTime() == null ? "" : DateUtil.dateTimeFormat.format(qztGorder.getShipmentsTime()));
                linkmaps.put("signforTime", qztGorder.getSignforTime() == null ? "" : DateUtil.dateTimeFormat.format(qztGorder.getSignforTime()));
            } else if ("0".equals(pickupWay)) {//自提
                linkmaps.put("finshTime", qztGorder.getFinshTime() == null ? "" : DateUtil.dateTimeFormat.format(qztGorder.getFinshTime()));
                linkmaps.put("replenishStatus", qztGorder.getReplenishStatus() == null ? "" : DicParamUtil.getDicCodeByType("REPLENISH_STATUS", qztGorder.getReplenishStatus()));
            }
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean confirmTheSettlement(QztGorder qztGordery) {
        boolean ber = false;
        QztGorder qztGorder = this.qztGorderMapper.queryByOrederNoUserId(qztGordery);
        qztGorder.setUpdateBy(qztGordery.getUpdateBy());
        Integer result = this.qztGorderMapper.confirmTheSettlement(qztGorder);
        if (result == 1) {
            //分润Mq
            new MqMess(MqConstant.GOODS_ORDER_TOP, qztGorder.getOrderNo()).sendTop();
            log.info("确认核销，发送分润Mq");
            //订单日志
            this.qztBusLogService.addBusLog("01", "待核销", qztGorder.getOrderNo(), "商家核销完成订单,更新订单状态成功", qztGorder.getUserId());
            ber = true;
        }
        return ber;
    }

    @Override
    public QztGorder findChargeOffOrderInfo(QztGorder qztGorder) {
        return this.qztGorderMapper.findChargeOffOrderInfo(qztGorder);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recurOrder(QztGorder qztGorder) throws Exception {
        int count = 0;
        List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
        for (QztStoGoods qztStoGoods : qztStoGoodsList) {
            QztStoGoods qztStoGoodsAdd = new QztStoGoods(qztStoGoods.getUserId(), qztStoGoods.getGoodsId(), qztStoGoods.getBuyNum());
            qztStoGoodsAdd.setIsPitchon("Y");
            boolean result = this.qztStoGoodsService.addGoods(qztStoGoodsAdd);
            if (result) {
                count++;
            }
        }
        return count > 0;
    }

}

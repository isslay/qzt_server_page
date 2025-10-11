package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztApplyBusorderMapper;
import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztGorder;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.mq.pushMq.MqConstant;
import com.qzt.common.mq.pushMq.MqMess;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.rpc.api.SysAreaService;
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
@Service("qztApplyBusorderService")
public class QztApplyBusorderServiceImpl extends BaseServiceImpl<QztApplyBusorderMapper, QztApplyBusorder> implements IQztApplyBusorderService {

    @Autowired
    private QztApplyBusorderMapper qztApplyBusorderMapper;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Autowired
    private IQztUserRegService qztUserRegService;

    @Autowired
    private SysAreaService sysAreaService;

    @Override
    public Page find(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<QztApplyBusorder> rb = this.qztApplyBusorderMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public QztApplyBusorder queryByUserId(Long userId) {
        return this.qztApplyBusorderMapper.queryByUserId(userId);
    }

    @Override
    public QztApplyBusorder queryByOrederNo(String orderNo) {
        return this.qztApplyBusorderMapper.queryByOrederNo(orderNo);
    }

    @Override
    public QztApplyBusorder queryByOrederNoUserId(String orderNo, Long userId) {
        return this.qztApplyBusorderMapper.queryByOrederNoUserId(orderNo, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map payApplay0rder(String orderNo, Long addressId, String payType, long balanceMoney, String payPwd, Long userId) throws Exception {
        Map remap = new HashMap();
        QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderMapper.queryByOrederNoUserId(orderNo, userId);
        if (qztApplyBusorder == null) {
            remap.put("code", "6017");
            return remap;
        } else if (!"01".equals(qztApplyBusorder.getOrderState())) {
            remap.put("code", "6018");
            return remap;
        }
        Map<String, String> addressInfo = this.qztUserAddressService.disposeAddressInfo(addressId);
        if (addressInfo == null) {
            remap.put("code", "6002");
            return remap;
        }
        qztApplyBusorder.setAddressId(addressId);
        qztApplyBusorder.setConsigneeName(addressInfo.get("name"));
        qztApplyBusorder.setConsigneeTel(addressInfo.get("tel"));
        qztApplyBusorder.setConsigneeAddress(addressInfo.get("address"));

        Map payMess = new HashMap();
        qztApplyBusorder.setUpdateTime(new Date());
        //第一次支付
        if (qztApplyBusorder.getBalanceMoney() == null || qztApplyBusorder.getBalanceMoney() == 0) {
            long orderMoney = qztApplyBusorder.getOrderMoney();//订单金额
            long threeSidesMoney = orderMoney;//三方支付金额默认为订单金额
            if (balanceMoney > 0) {
                //验证支付密码
                Long result = this.qztUserService.verifyPayPwd(userId, payPwd);
                if (result == -1) {
                    remap.put("code", "6009");
                    return remap;
                } else if (result != 1) {
                    remap.put("code", "6012");
                    return remap;
                }
                threeSidesMoney = threeSidesMoney - balanceMoney;
            }
            //第三方需要支付则判定
            if (threeSidesMoney > 0 && !"w".equals(payType) && !"z".equals(payType)) {
                remap.put("code", "6019");
                return remap;
            }
            //余额支付处理
            if (balanceMoney > 0) {
                boolean accounreuslt = this.qztAccountService.updateQztAccount(qztApplyBusorder.getOrderNo(), qztApplyBusorder.getApplyUserId(), qztApplyBusorder.getBalanceMoney(), "03", "11", "服务站申请订单支付消费余额【" + qztApplyBusorder.getOrderNo() + "】");
                if (!accounreuslt) {
                    remap.put("code", "6008");
                    return remap;
                }
            }
            qztApplyBusorder.setBalanceMoney(balanceMoney);
            qztApplyBusorder.setThreeSidesMoney(threeSidesMoney);
            qztApplyBusorder.setPayType(payType);//支付方式
            Integer opresult = this.qztApplyBusorderMapper.applyOrderPayUpdate(qztApplyBusorder);
            if (opresult != 1) {
                throw new Exception("支付更新订单信息失败");
            }
            //订单日志
            this.qztBusLogService.addBusLog("05", "待支付", qztApplyBusorder.getOrderNo(), "订单支付，更新申请服务站订单信息", qztApplyBusorder.getApplyUserId());
            //三方支付如为0交易结束
            if (threeSidesMoney == 0) {
                boolean backret = this.applyOrderPayBack(qztApplyBusorder.getOrderNo());
                if (!backret) {
                    throw new Exception("无需三方支付回调失败");
                }
                qztApplyBusorder.setOrderState("03");//订单状态 待发货
            }

        } else if (qztApplyBusorder.getBalanceMoney() != null) {//继续支付
            //判断支付方式正确性 必须是上次三方的支付方式
            if (!qztApplyBusorder.getPayType().equals(payType)) {
                remap.put("code", "6019");
                return remap;
            }
            qztApplyBusorder.setPayType(payType);//支付方式处理
            Integer opresult = this.qztApplyBusorderMapper.applyOrderPayUpdate(qztApplyBusorder);
            if (opresult != 1) {
                throw new Exception("继续支付更新订单信息失败");
            }
            //订单日志
            this.qztBusLogService.addBusLog("05", "继续支付", qztApplyBusorder.getOrderNo(), "申请服务站订单继续支付，更新支付方式", qztApplyBusorder.getApplyUserId());
        } else {
            remap.put("code", "6018");
            return remap;
        }
        payMess.put("payType", qztApplyBusorder.getPayType());//如为0  1 则为对应三方支付平台 其他状态则支付成功
        payMess.put("orderNo", qztApplyBusorder.getOrderNo());
        payMess.put("orderType", OrderUtil.OrderNoEnum.BUSORDER.value());
        payMess.put("threeSidesMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getThreeSidesMoney()));
        remap.put("data", payMess);
        remap.put("code", "200");
        return remap;
    }

    @Override
    public boolean applyOrderPayBack(String orderNo) {
        boolean resultb = true;
        String logmess = "成功";
        QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderMapper.queryByOrederNo(orderNo);
        //各订单回调后处理
        qztApplyBusorder.setPayState("11");//支付状态 已支付
        qztApplyBusorder.setOrderState("03");//订单状态 待发货
        qztApplyBusorder.setUpdateTime(new Date());
        Integer opbresult = this.qztApplyBusorderMapper.applyOrderPayBack(qztApplyBusorder);
        if (opbresult != 1) {
            resultb = false;
            logmess = "失败";
            qztApplyBusorder.setOrderState("96");
            this.modifyById(qztApplyBusorder);
        } else {
            try {
                this.coherentProcessing(qztApplyBusorder);
            } catch (Exception e) {
                e.printStackTrace();
                String message = e.getMessage();
                if ("1".equals(message)) {//创建商家失败
                    logmess = "后，创建商家失败！";
                } else if ("3".equals(message)) {//反写用户信息失败
                    logmess = "后，反写用户信息失败！";
                } else if ("5".equals(message)) {//创建关系树失败
                    logmess = "后，创建关系树失败！";
                } else {
                    logmess = "后，ERROR错误！";
                }
                resultb = false;
                qztApplyBusorder.setOrderState("96");
                this.modifyById(qztApplyBusorder);
            }
        }
        //订单日志
        this.qztBusLogService.addBusLog("05", resultb ? "待审核" : "异常订单", qztApplyBusorder.getOrderNo(),
                "审核通过,更新申请服务站订单信息" + logmess, qztApplyBusorder.getApplyUserId());
//                "三方支付回调,更新申请服务站订单信息" + logmess, qztApplyBusorder.getApplyUserId());
        return resultb;
    }

    /**
     * 服务订单回调后相关处理
     *
     * @return void
     * @author Xiaofei
     * @date 2019-11-24
     */
    @Transactional(rollbackFor = Exception.class)
    public void coherentProcessing(QztApplyBusorder qztApplyBusorder) throws Exception {
        throw new Exception("1");
//        //创建服务站商家
//        QztBusiness qztBusiness = this.qztBusinessService.addBusiness(qztApplyBusorder);
//        if (qztBusiness == null) {
//            throw new Exception("1");
//        } else {
//            //反写用户信息
//            boolean resultin = this.qztUserService.modifyUserInfo(new QztUser(qztBusiness.getUserId(), qztBusiness.getId(),
//                    StringUtil.isEmpty(qztBusiness.getBusRecommender()) ? null : qztBusiness.getBusRecommender(), 10));
//            if (!resultin) {
//                throw new Exception("3");
//            }
//            //创建关系树并处理关系
//            if (qztApplyBusorder.getReferrerUserId() != null) {
//                //创建关系树
//                boolean resultcrt = this.qztUserRegService.createRelationshipTree("01", 2, qztApplyBusorder.getApplyUserId(), qztApplyBusorder.getReferrerUserId());
//                if (!resultcrt) {
//                    throw new Exception("5");
//                }
//                //处理用户升级Mq 防止并发因此发Mq处理
//                new MqMess(MqConstant.USER_UPGRADE_TOP, qztBusiness.getUserId().toString()).sendTop();
//            }
//        }
    }

    @Override
    public boolean offlineDistribution(QztApplyBusorder entity) {
        boolean ber = false;
        Integer result = this.qztApplyBusorderMapper.offlineDistribution(entity);
        if (result == 1) {
            //订单日志
            this.qztBusLogService.addBusLog("01", "待发货", entity.getOrderNo(), "自提,更新订单状态成功", entity.getApplyUserId());
            ber = true;
        }
        return ber;
    }

    @Override
    public List<Map<String, Object>> saorderExcel(Map map) {
        List<Map<String, Object>> retmaplist = new ArrayList();
        List<QztApplyBusorder> rb = this.qztApplyBusorderMapper.find(map);
        for (QztApplyBusorder qztApplyBusorder : rb) {
            Map<String, Object> linkmaps = new LinkedHashMap();
            linkmaps.put("orderNo", qztApplyBusorder.getOrderNo());
            linkmaps.put("createTime", DateUtil.dateTimeFormat.format(qztApplyBusorder.getCreateTime()));
            linkmaps.put("applyUserId", qztApplyBusorder.getApplyUserId());
            linkmaps.put("contactName", qztApplyBusorder.getContactName());
            linkmaps.put("contactTel", qztApplyBusorder.getContactTel());
            String areaName = this.sysAreaService.selectAreaName(qztApplyBusorder.getContext().substring(0, 2), qztApplyBusorder.getContext().substring(2, 7), qztApplyBusorder.getContext().substring(7, 12));
            linkmaps.put("stateMark", areaName + qztApplyBusorder.getStateMark());
            linkmaps.put("referrerUserId", qztApplyBusorder.getReferrerUserId());
            linkmaps.put("orderState", qztApplyBusorder.getOrderState() == null ? "" : DicParamUtil.getDicCodeByType("ORDER_STATE", qztApplyBusorder.getOrderState()));
            linkmaps.put("threeSidesMoney", PriceUtil.moneyLongToDecimal(qztApplyBusorder.getThreeSidesMoney()));
            linkmaps.put("payType", qztApplyBusorder.getPayType() == null ? "" : DicParamUtil.getDicCodeByType("PAY_TYPE", qztApplyBusorder.getPayType()));
            linkmaps.put("payState", qztApplyBusorder.getPayState() == null ? "" : DicParamUtil.getDicCodeByType("PAY_STATE", qztApplyBusorder.getPayState()));
            linkmaps.put("payTime", qztApplyBusorder.getPayTime() == null ? "" : DateUtil.dateTimeFormat.format(qztApplyBusorder.getPayTime()));
            linkmaps.put("consigneeName", qztApplyBusorder.getConsigneeName());
            linkmaps.put("consigneeTel", qztApplyBusorder.getConsigneeTel());
            linkmaps.put("consigneeAddress", qztApplyBusorder.getConsigneeAddress());
            linkmaps.put("courierCompany", qztApplyBusorder.getCourierCompany());
            linkmaps.put("courierNo", qztApplyBusorder.getCourierNo());
            linkmaps.put("courierRemarks", qztApplyBusorder.getCourierRemarks());
            linkmaps.put("shipmentsTime", qztApplyBusorder.getShipmentsTime() == null ? "" : DateUtil.dateTimeFormat.format(qztApplyBusorder.getShipmentsTime()));
            linkmaps.put("signforTime", qztApplyBusorder.getSignforTime() == null ? "" : DateUtil.dateTimeFormat.format(qztApplyBusorder.getSignforTime()));
            retmaplist.add(linkmaps);
        }
        return retmaplist;
    }

}

package com.qzt.ump.server.controllerBack;

import com.qzt.bus.model.*;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.BeanTools;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztGorder")
@Api(value = "QztGorderController", description = "QztGorderController")
public class QztGorderController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztGorderService service;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private IQztPayBacklogService qztPayBacklogService;

    @Autowired
    private IQztStoGoodsService qztStoGoodsService;

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-28
     */
    @GetMapping("/query/{id}")
    public Map<String, Object> query(@PathVariable Long id) {
        try {
            Map remap = new HashMap();
            QztGorder qztGorder = service.selectById(id);

            //商品信息
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            List<Map> stoGoodsList = new ArrayList<>();
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                Map mapsto = new HashMap();
                mapsto.put("goodsId", qztStoGoods.getGoodsId());//商品ID
                mapsto.put("buyNum", qztStoGoods.getBuyNum());
                mapsto.put("goodsName", qztStoGoods.getGoodsName());
                mapsto.put("goodsPic", qztStoGoods.getGoodsPic());
                mapsto.put("goodsRemark", qztStoGoods.getGoodsRemark());
                mapsto.put("goodsPrice", PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));
                stoGoodsList.add(mapsto);
            }

            Map orderMess = new HashMap();//订单信息
            orderMess.put("orderNo", qztGorder.getOrderNo());//订单编号
            orderMess.put("buyNum", qztGorder.getBuyNum());//购买数量
            orderMess.put("createTime", qztGorder.getCreateTime());//下单时间
            orderMess.put("userId", qztGorder.getUserId());//用户ID
            orderMess.put("orderState", qztGorder.getOrderState());//订单状态
            orderMess.put("remarks", qztGorder.getRemarks());//订单状态
            String orderStateMc = "";
            if ("0".equals(qztGorder.getPickupWay()) && "09".equals(qztGorder.getOrderState())) {//自提
                orderStateMc = "已核销";
            } else {
                orderStateMc = DicParamUtil.getDicCodeByType("ORDER_STATE", qztGorder.getOrderState());
            }
            orderMess.put("orderStateMc", orderStateMc);//订单状态Mc
            String totalPrice = "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getTotalPrice());

            orderMess.put("totalPrice", totalPrice);//订单金额
//            orderMess.put("balanceMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getBalanceMoney()));//余额支付金额
            orderMess.put("cashCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getDeductMax()));//抵扣券支付金额
//            orderMess.put("threeSidesMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getThreeSidesMoney()));//三方支付金额
            orderMess.put("zkCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getCashCouponMoney()));//折扣券
            orderMess.put("xsCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getStoreCouponMoney()));//新手券
            orderMess.put("spCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getGoodsCouponMoney()));//商品券
            orderMess.put("acCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getActivityCouponMoney()));//活动券
            orderMess.put("actuaCouponMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getActuaPayment()));//实付金额

            orderMess.put("postage", qztGorder.getPostage());//邮费
            orderMess.put("payTime", qztGorder.getPayTime() == null ? "－" : qztGorder.getPayTime());//支付时间
            orderMess.put("payType", qztGorder.getPayType() == null ? "－" : qztGorder.getPayType());//支付方式
            orderMess.put("payTypeMc", qztGorder.getPayType() == null ? "－" : DicParamUtil.getDicCodeByType("PAY_TYPE", qztGorder.getPayType()));//支付方式Mc
            orderMess.put("consigneeName", qztGorder.getConsigneeName() == null ? "－" : qztGorder.getConsigneeName());//收货人
            orderMess.put("consigneeTel", qztGorder.getConsigneeTel() == null ? "－" : qztGorder.getConsigneeTel());//收货人电话
            orderMess.put("consigneeAddress", qztGorder.getConsigneeAddress() == null ? "－" : qztGorder.getConsigneeAddress());//收货地址
            orderMess.put("courierCompany", qztGorder.getCourierCompany() == null ? "－" : qztGorder.getCourierCompany());//快递公司
            orderMess.put("courierNo", qztGorder.getCourierNo() == null ? "－" : qztGorder.getCourierNo());//快递单号
            orderMess.put("courierCode", qztGorder.getCourierCode() == null ? "－" : qztGorder.getCourierCode());//快递code
            orderMess.put("courierRemarks", qztGorder.getCourierRemarks() == null ? "－" : qztGorder.getCourierRemarks());//快递备注
            //补货信息
            orderMess.put("replenishStatus", qztGorder.getReplenishStatus());//补货状态
            orderMess.put("replenishStatusMc", DicParamUtil.getDicCodeByType("REPLENISH_STATUS", qztGorder.getReplenishStatus()));//补货状态Mc
            orderMess.put("replenishCsc", qztGorder.getReplenishCsc() == null ? "－" : qztGorder.getReplenishCsc());//补货-快递公司
            orderMess.put("replenishCscNo", qztGorder.getReplenishCscNo() == null ? "－" : qztGorder.getReplenishCscNo());//补货-快递单号
            orderMess.put("replenishCscRemarks", qztGorder.getReplenishCscRemarks() == null ? "－" : qztGorder.getReplenishCscRemarks());//补货-快递备注
            orderMess.put("replenishCneName", qztGorder.getReplenishCneName() == null ? "－" : qztGorder.getReplenishCneName());//补货-收货人名称
            orderMess.put("replenishCneTel", qztGorder.getReplenishCneTel() == null ? "－" : qztGorder.getReplenishCneTel());//补货-收货人电话
            orderMess.put("replenishCneAddress", qztGorder.getReplenishCneAddress() == null ? "－" : qztGorder.getReplenishCneAddress());//补货-收货地址
            orderMess.put("replenishTime", qztGorder.getReplenishTime() == null ? "－" : qztGorder.getReplenishTime());//补货-发货时间

            List<QztBusLog> orderLogs = this.qztBusLogService.findByBusinessId("01", qztGorder.getOrderNo());
            List<Map> shareLogs = this.qztAccountLogService.findShareMapList(qztGorder.getOrderNo());
            List<Map> shareReLogs = this.qztAccountRelogService.findShareMapList(qztGorder.getOrderNo());
            List<Map<String, Object>> sharLogData = new ArrayList<>();


            for (Map dataMap : shareReLogs) {
                Map<String, Object> rsMap = new HashMap<>();
                rsMap.put("userId", dataMap.get("user_id"));
                rsMap.put("mobile", dataMap.get("mobile"));
                rsMap.put("giveNum", PriceUtil.exactlyTwoDecimalPlaces((Long) dataMap.get("re_money")));
                rsMap.put("changeNum", PriceUtil.exactlyTwoDecimalPlaces((Long) dataMap.get("change_money")));
                rsMap.put("remark", dataMap.get("remark"));
                rsMap.put("createDTime", dataMap.get("create_time"));//分润日期
                rsMap.put("updateDTime", dataMap.get("update_time"));//变更日期
                sharLogData.add(rsMap);
            }
            for (Map dataMap : shareLogs) {
                Map<String, Object> rsMap = new HashMap<>();
                rsMap.put("userId", dataMap.get("user_id"));
                rsMap.put("mobile", dataMap.get("mobile"));
                rsMap.put("giveNum", PriceUtil.exactlyTwoDecimalPlaces((Long) dataMap.get("change_num")));
                rsMap.put("changeNum", PriceUtil.exactlyTwoDecimalPlaces((Long) dataMap.get("change_num")));
                rsMap.put("remark", dataMap.get("remark"));
                rsMap.put("createDTime", dataMap.get("create_time"));//分润日期
                rsMap.put("updateDTime", dataMap.get("update_time"));//变更日期
                sharLogData.add(rsMap);
            }

            List<Map> payBackLogsma = new ArrayList<>();
            List<QztPayBacklog> payBackLogs = this.qztPayBacklogService.findByOrderNoType(OrderUtil.OrderNoEnum.GOODS.value(), qztGorder.getOrderNo());
            for (QztPayBacklog qztPayBacklog : payBackLogs) {
                Map map = new HashMap();
                map.put("payNo", qztPayBacklog.getPayNo());
                map.put("payMoney", qztPayBacklog.getPayMoney());
                map.put("backSource", DicParamUtil.getDicCodeByType("PAY_TYPE", qztPayBacklog.getBackSource()));
                map.put("backPayNo", qztPayBacklog.getBackPayNo());
                payBackLogsma.add(map);
            }
            remap.put("orderLogs", orderLogs);//订单日志
            remap.put("shareLogs", sharLogData);//分润流水
            remap.put("payBackLogs", payBackLogsma);//支付回调日志
            remap.put("stoGoodsList", stoGoodsList);//商品信息
            remap.put("orderMess", orderMess);//订单信息
            return this.returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 查询分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-11
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        try {
            Map condition = pageModel.getCondition();
            condition = condition == null ? new HashMap() : condition;
            if (condition.get("ssUserid") != null && !"".equals(condition.get("ssUserid").toString())) {
                condition.put("addressId", this.qztUserService.findDGUserById(Long.valueOf(condition.get("ssUserid").toString())).getBussId());
            }
            condition.put("errorOrder", "Y");
            pageModel = (PageModel) service.find(pageModel);
            List<Map> listordermap = new ArrayList<>();
            List<QztGorder> records = pageModel.getRecords();
            for (QztGorder qztGorder : records) {
                Map<String, Object> ordermap = BeanTools.beanToMap(qztGorder);
                String orderStateMc = "";
                if ("0".equals(qztGorder.getPickupWay()) && "09".equals(qztGorder.getOrderState())) {//自提
                    orderStateMc = "已核销";
                } else {
                    orderStateMc = DicParamUtil.getDicCodeByType("ORDER_STATE", qztGorder.getOrderState());
                }
                ordermap.put("orderStateMc", orderStateMc);
                ordermap.put("payTypeMc", DicParamUtil.getDicCodeByType("PAY_TYPE", qztGorder.getPayType()));
                ordermap.put("payStateMc", DicParamUtil.getDicCodeByType("PAY_STATE", qztGorder.getPayState()));
                ordermap.put("replenishStatusMc", DicParamUtil.getDicCodeByType("REPLENISH_STATUS", qztGorder.getReplenishStatus()));
                listordermap.add(ordermap);
            }
            pageModel.setRecords(listordermap);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 发货方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-28
     */
    @PostMapping("/goodsOrderShipments")
    public ResultModel goodsOrderShipments(@RequestBody QztGorder entity) {
        try {
            QztGorder qztGorder;
            Date date = new Date();
            String operNode = "发货";
            if ("1".equals(entity.getPickupWay())) {//发货
                entity.setUpdateBy(this.getCurrentUserId());
                entity.setShipmentsTime(date);
                entity.setOrderState("05");
                qztGorder = entity;
            } else {//自提-补货
                operNode = "补货";
                qztGorder = this.service.queryById(entity.getId());
                if (!"00".equals(qztGorder.getReplenishStatus())) {
                    return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE, "对不起，该订单未完成无法补货");
                }
                qztGorder.setId(entity.getId());
                qztGorder.setUpdateBy(this.getCurrentUserId());
                qztGorder.setReplenishCsc(entity.getCourierCompany());
                qztGorder.setReplenishCscCode(entity.getCourierCode());
                qztGorder.setReplenishCscNo(entity.getCourierNo());
                qztGorder.setReplenishCscRemarks(entity.getCourierRemarks());
                qztGorder.setReplenishStatus("02");
                qztGorder.setReplenishTime(date);
            }
            QztGorder old = service.selectById(entity.getId());
            if (old != null && "03".equals(old.getOrderState())) {
                QztGorder entityback = service.modifyById(qztGorder);
                if (entityback == null) {
                    return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
                } else {
                    //订单日志
                    this.qztBusLogService.addBusLog("01", "已" + operNode, qztGorder.getOrderNo(), operNode + ",更新订单状态成功", qztGorder.getUserId());
                    return ResultUtil.ok();
                }
            }else {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE, "该订单状态已变更");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 自提
     *
     * @param id
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-28
     */
    @PostMapping("/offlineDistribution/{id}")
    public ResultModel offlineDistribution(@PathVariable Long id) {
        try {
            QztGorder entity = this.service.queryById(id);
            Date date = new Date();
            entity.setId(id);
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(date);
            entity.setCourierRemarks("自提");
            boolean re = this.service.offlineDistribution(entity);
            if (re) {
                return ResultUtil.ok();
            } else {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, e.getMessage());
        }
    }


}


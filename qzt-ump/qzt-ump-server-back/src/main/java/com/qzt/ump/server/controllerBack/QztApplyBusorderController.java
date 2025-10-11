package com.qzt.ump.server.controllerBack;

import com.qzt.bus.model.QztBusLog;
import com.qzt.bus.model.QztPayBacklog;
import com.qzt.bus.rpc.api.IQztAccountLogService;
import com.qzt.bus.rpc.api.IQztBusLogService;
import com.qzt.bus.rpc.api.IQztPayBacklogService;
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
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.rpc.api.IQztApplyBusorderService;
import org.springframework.beans.factory.annotation.Autowired;

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
@RequestMapping("/back/qztApplyBusorder")
@Api(value = "QztApplyBusorderController", description = "QztApplyBusorderController")
public class QztApplyBusorderController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztApplyBusorderService service;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private IQztBusLogService qztBusLogService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztPayBacklogService qztPayBacklogService;

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
            QztApplyBusorder qztApplyBusorder = service.selectById(id);
            Map goodsMess = new HashMap();//商品信息
            goodsMess.put("goodsName", "七足堂服务站");
            goodsMess.put("goodsPic", "https://img.jlqizutang.com/servicelogo.png");
            Map orderMess = new HashMap();//订单信息
            orderMess.put("orderNo", qztApplyBusorder.getOrderNo());//订单编号
            orderMess.put("createTime", qztApplyBusorder.getCreateTime());//下单时间
            orderMess.put("userId", qztApplyBusorder.getApplyUserId());//用户ID
            orderMess.put("orderState", qztApplyBusorder.getOrderState());//订单状态
            orderMess.put("orderStateMc", DicParamUtil.getDicCodeByType("BUS_ORDER_STATE", qztApplyBusorder.getOrderState()));//订单状态Mc
            String totalPrice = "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getThreeSidesMoney());
            orderMess.put("totalPrice", totalPrice);//订单金额
            orderMess.put("threeSidesMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getThreeSidesMoney()));//三方支付金额
            orderMess.put("payTime", qztApplyBusorder.getPayTime() == null ? "－" : qztApplyBusorder.getPayTime());//支付时间
            orderMess.put("payType", qztApplyBusorder.getPayType() == null ? "－" : qztApplyBusorder.getPayType());//支付方式
            orderMess.put("payTypeMc", qztApplyBusorder.getPayType() == null ? "－" : DicParamUtil.getDicCodeByType("PAY_TYPE", qztApplyBusorder.getPayType()));//支付方式Mc
            orderMess.put("consigneeName", qztApplyBusorder.getConsigneeName() == null ? "－" : qztApplyBusorder.getConsigneeName());//收货人
            orderMess.put("consigneeTel", qztApplyBusorder.getConsigneeTel() == null ? "－" : qztApplyBusorder.getConsigneeTel());//收货人电话
            orderMess.put("consigneeAddress", qztApplyBusorder.getConsigneeAddress() == null ? "－" : qztApplyBusorder.getConsigneeAddress());//收货地址
            orderMess.put("courierCompany", qztApplyBusorder.getCourierCompany() == null ? "－" : qztApplyBusorder.getCourierCompany());//快递公司
            orderMess.put("courierNo", qztApplyBusorder.getCourierNo() == null ? "－" : qztApplyBusorder.getCourierNo());//快递单号
            orderMess.put("courierCode", qztApplyBusorder.getCourierCode() == null ? "－" : qztApplyBusorder.getCourierCode());//快递code
            orderMess.put("courierRemarks", qztApplyBusorder.getCourierRemarks() == null ? "－" : qztApplyBusorder.getCourierRemarks());//快递备注
            List<QztBusLog> orderLogs = this.qztBusLogService.findByBusinessId("05", qztApplyBusorder.getOrderNo());
            List<Map> payBackLogsma = new ArrayList<>();
            List<QztPayBacklog> payBackLogs = this.qztPayBacklogService.findByOrderNoType(OrderUtil.OrderNoEnum.BUSORDER.value(), qztApplyBusorder.getOrderNo());
            for (QztPayBacklog qztPayBacklog : payBackLogs) {
                Map map = new HashMap();
                map.put("payNo", qztPayBacklog.getPayNo());
                map.put("payMoney", qztPayBacklog.getPayMoney());
                map.put("backSource", DicParamUtil.getDicCodeByType("PAY_TYPE", qztPayBacklog.getBackSource()));
                map.put("backPayNo", qztPayBacklog.getBackPayNo());
                payBackLogsma.add(map);
            }
            remap.put("orderLogs", orderLogs);//订单日志
            remap.put("payBackLogs", payBackLogsma);//支付回调日志
            remap.put("goodsMess", goodsMess);//商品信息
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
            pageModel = (PageModel) service.find(pageModel);
            List<QztApplyBusorder> records = pageModel.getRecords();
            List<Map> datamap = new ArrayList<>();
            for (QztApplyBusorder qztApplyBusorder : records) {
                Map<String, Object> maps = BeanTools.beanToMap(qztApplyBusorder);
                String areaName = this.sysAreaService.selectAreaName(qztApplyBusorder.getContext().substring(0, 2), qztApplyBusorder.getContext().substring(2, 7), qztApplyBusorder.getContext().substring(7, 12));
                maps.put("stateMark", areaName + qztApplyBusorder.getStateMark());
                maps.put("orderStateMc", DicParamUtil.getDicCodeByType("BUS_ORDER_STATE", qztApplyBusorder.getOrderState()));
                maps.put("payStateMc", DicParamUtil.getDicCodeByType("PAY_STATE", qztApplyBusorder.getPayState()));
                maps.put("payTypeMc", DicParamUtil.getDicCodeByType("PAY_TYPE", qztApplyBusorder.getPayType()));
                datamap.add(maps);
            }
            pageModel.setRecords(datamap);

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
    public ResultModel goodsOrderShipments(@RequestBody QztApplyBusorder entity) {
        try {
            Date date = new Date();
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(date);
            entity.setShipmentsTime(date);
            entity.setOrderState("05");
            QztApplyBusorder entityback = service.modifyById(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                this.qztBusLogService.addBusLog("05", "待发货", entity.getOrderNo(),
                        "订单发货,更新申请服务站订单信息", this.getCurrentUserId());
                return ResultUtil.ok();
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
            QztApplyBusorder entity = this.service.queryById(id);
            Date date = new Date();
            entity.setId(id);
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(date);
            entity.setCourierRemarks("自提");
            boolean re = this.service.offlineDistribution(entity);
            if (re) {
                this.qztBusLogService.addBusLog("05", "待发货", entity.getOrderNo(),
                        "订单自提,更新申请服务站订单信息", this.getCurrentUserId());
                return ResultUtil.ok();
            } else {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, e.getMessage());
        }
    }

    /**
     * 审核方法
     *
     * @param id   主键ID
     * @param type 1通过、2驳回
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2020-03-27
     */
    @PostMapping("/audit")
    public Map<String, Object> audit(Long id, String type) {
        try {
            QztApplyBusorder qztApplyBusorder = this.service.queryById(id);
            if ("1".equals(type)) {//通过
                boolean boo = this.service.applyOrderPayBack(qztApplyBusorder.getOrderNo());
                if (boo) {
                    return this.returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
                } else {
                    return this.returnUtil.returnMess(Constant.OPERATION_FAILURE);
                }
            } else if ("2".equals(type)) {//驳回
                qztApplyBusorder.setUpdateBy(this.getCurrentUserId());
                qztApplyBusorder.setOrderState("11");
                QztApplyBusorder entityback = service.modifyById(qztApplyBusorder);
                if (entityback == null) {
                    return this.returnUtil.returnMess(Constant.OPERATION_FAILURE);
                } else {
                    this.qztBusLogService.addBusLog("05", "待审核", qztApplyBusorder.getOrderNo(),
                            "审核驳回,更新申请服务站订单信息", this.getCurrentUserId());
                    return this.returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
                }
            } else {
                return this.returnUtil.returnMess(Constant.OPERATION_FAILURE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }
}


package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.*;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.*;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztGorderController", description = "商品订单相关Api")
public class WebQztGorderController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztGorderService qztGorderService;

    @Autowired
    private IQztGoodsService qztGoodsService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztStoGoodsService qztStoGoodsService;

    /**
     * 提交商品订单
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "提交商品订单", notes = "提交商品订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址/服务站id", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pickupWay", value = "取货方式(自提0、快递1)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "buyNum", value = "购买数量", dataType = "Integer", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cashCouponId", value = "券ID", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "shareCode", value = "员工推荐码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "userTel", value = "联系电话", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "remarks", value = "用户备注/留言", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "storeCouponId", value = "新人券ID", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "goodsCouponId", value = "商品券ID", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "activityCouponId", value = "活动券ID", dataType = "Long", required = false, paramType = "query")
    })
    @PutMapping("/webapi/qztGorder/subGorder")
    public Map<String, Object> subGorder(String vno, String cno, String tokenId, Long userId, Long addressId, String pickupWay, Long goodsId, Integer buyNum, Long cashCouponId, String shareCode, String userTel, String remarks, Long storeCouponId, Long goodsCouponId, Long activityCouponId) {
        try {
//            pickupWay = !"0".equals(pickupWay) ? "1" : "0";
            pickupWay = "1";//默认快递
            QztUser shareUser = qztUserService.findDGUserById(userId);
            if (shareUser == null) {
                return returnUtil.returnMess("201","查无此用户信息，请核对！");
            }
            QztGorder qztGorder = new QztGorder(cno, userId, pickupWay, addressId, goodsId, buyNum, cashCouponId, storeCouponId, goodsCouponId, activityCouponId, shareUser.getInvitationOne(), remarks, userTel, "01", "01", "00");
            Map map = this.qztGorderService.subGorder(qztGorder);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("6021".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.PLEASE_SELECT_VALID_VOUCHER);
            } else if ("6023".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.TO_MEET_THE_100);
            } else if ("6003".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.GOODS_NOTHINGNESS);
            } else if ("6005".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.UNDERSTOCK);
            } else if ("6025".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OFFLINE_OR_OUT_OF_BUSINESS);
            } else if ("6002".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.ADDRESS_ERROR);
            } else if ("1234".equals(e.getMessage())) {
                return returnUtil.returnMess("201", "最低支付金额小于50");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 获取支付页信息
     *
     * @return Map
     * @author
     * @date
     */
    @ApiOperation(value = "获取支付页信息", notes = "获取支付页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/getPay0rderInfo")
    public Map<String, Object> getPay0rderInfo(String vno, String cno, String tokenId, Long userId, String orderNo) {
        try {
            Map remap = new HashMap();
            QztGorder qztGorder = this.qztGorderService.queryByOrederNoUserId(new QztGorder(userId, orderNo));
            if (qztGorder == null) {
                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
            } else if (!"01".equals(qztGorder.getOrderState())) {
                return returnUtil.returnMess(Constant.ORDER_OFF_THE_STOCKS);
            }
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(userId, orderNo);
            if ("1.0.0".equals(vno)) {
                Map goodsMess = new HashMap();//商品信息处理
                QztStoGoods qztStoGoods = qztStoGoodsList.get(0);
                goodsMess.put("goodsName", qztStoGoods.getGoodsName());//商品名称
                goodsMess.put("goodsExplain", qztStoGoods.getGoodsRemark());//商品说明
                goodsMess.put("goodsPic", qztStoGoods.getGoodsPic());//商品缩略图
                goodsMess.put("goodsPrice", PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));//商品单价
                remap.put("goodsMess", goodsMess);
            } else if ("1.0.1".equals(vno)) {
                List<Map> stoGoodsList = new ArrayList<>();
                for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                    Map map = new HashMap();
                    map.put("buyNum", qztStoGoods.getBuyNum());
                    map.put("goodsName", qztStoGoods.getGoodsName());
                    map.put("goodsPic", qztStoGoods.getGoodsPic());
                    map.put("goodsRemark", qztStoGoods.getGoodsRemark());
                    map.put("goodsPrice", PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));
                    stoGoodsList.add(map);
                }
                remap.put("stoGoodsList", stoGoodsList);
            }

            Map orderMess = new HashMap(); //订单信息
            String actuaPayment = PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getActuaPayment());
            orderMess.put("actuaPayment", actuaPayment);//订单支付金额
            orderMess.put("totalPriceMc", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getTotalPrice() + qztGorder.getPostage()));//订单总金额Mc 商品金额 + 邮费
            orderMess.put("orderNo", qztGorder.getOrderNo());//订单编号

            Map payMess = new HashMap();//订单支付信息
            payMess.put("balanceMoney", PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getBalanceMoney()));//余额抵付金额
            payMess.put("cashCouponMoney", PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getCashCouponMoney()));//券抵付金额
            payMess.put("threeSidesMoney", qztGorder.getBalanceMoney() != null && qztGorder.getBalanceMoney() > 0 ?
                    PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getThreeSidesMoney()) : actuaPayment);//三方支付金额
            payMess.put("payType", qztGorder.getPayType());//返回上次三方支付方式

            //获取用户资产
            Map userAssets = new HashMap();//用户资产信息
//            QztAccount qztAccount = this.qztAccountService.selectByUserId(qztGorder.getUserId());
//            QztUser qztUser = this.qztUserService.findDGUserById(userId);
//            userAssets.put("usedMoney", PriceUtil.exactlyTwoDecimalPlaces(qztAccount.getUsedMoney()));//可用余额
            userAssets.put("usedMoney", "");//可用余额
//            userAssets.put("isPayPwd", StringUtil.isEmpty(qztUser.getPayPwd()) ? "N" : "Y");//是否有支付密码(是Y、否N)
            userAssets.put("isPayPwd", "N");//是否有支付密码(是Y、否N)

//            remap.put("isContinuePay", "01".equals(qztGorder.getPayState()) ? "Y" : "N");//是否继续支付 (是Y、否N) 如为Y 页面显示抵付金额  N则正常显示
            remap.put("isContinuePay", "N");//是否继续支付 (是Y、否N) 如为Y 页面显示抵付金额  N则正常显示
            remap.put("orderMess", orderMess);
            remap.put("payMess", payMess);
            remap.put("userAssets", userAssets);
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 商品订单支付
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "商品订单支付", notes = "商品订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "三方平台支付方式(微信w、支付宝z)", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "balanceMoney", value = "余额抵付金额", dataType = "BigDecimal", required = false, paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码（md5加密）", dataType = "String", required = false, paramType = "query")
    })
    @PostMapping("/webapi/qztGorder/pay0rder")
    public Map<String, Object> pay0rder(String vno, String cno, String tokenId, Long userId, String orderNo, String payType, BigDecimal balanceMoney, String payPwd) {
        try {
            Map map = this.qztGorderService.pay0rder(new QztGorder(userId, orderNo, payType, balanceMoney), payPwd);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 取消商品订单
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "取消商品订单", notes = "取消商品订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztGorder/cancellationOfOrder")
    public Map<String, Object> cancellationOfOrder(String vno, String cno, String tokenId, Long userId, String orderNo) {
        try {
            this.qztGorderService.cancellationOfOrder(new QztGorder(userId, orderNo));
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 确认收货
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztGorder/confirmReceipt")
    public Map<String, Object> confirmReceipt(String vno, String cno, String tokenId, Long userId, String orderNo) {
        try {
            return returnUtil.returnMess(this.qztGorderService.confirmReceipt(new QztGorder(userId, orderNo)));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 核销码查询核销订单
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-12-13
     */
    @ApiOperation(value = "核销码查询核销订单", notes = "核销码查询核销订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "theVerificationCode", value = "核销码", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/findChargeOffOrderInfo")
    public Map<String, Object> findChargeOffOrderInfo(String vno, String cno, String tokenId, Long userId, Long bussId, String theVerificationCode) {
        try {
            QztGorder qztGorder = this.qztGorderService.findChargeOffOrderInfo(new QztGorder(bussId, theVerificationCode));
            if (qztGorder == null) {
                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
            }
            Map remap = new HashMap();
            remap.put("orderNo", qztGorder.getOrderNo());
            remap.put("userId", qztGorder.getUserId());
            List<Map> goodsList = new ArrayList<>();
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                Map map = new HashMap();
                map.put("goodsId", qztStoGoods.getGoodsId());
                map.put("goodsPrice", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));
                map.put("goodsPic", qztStoGoods.getGoodsPic());
                map.put("goodsName", qztStoGoods.getGoodsName());
                map.put("buyNum", qztStoGoods.getBuyNum());
                goodsList.add(map);
            }
            remap.put("goodsList", goodsList);
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 商家确认核销
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-12-13
     */
    @ApiOperation(value = "商家确认核销", notes = "商家确认核销")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderUserId", value = "订单用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztGorder/confirmTheSettlement")
    public Map<String, Object> confirmTheSettlement(String vno, String cno, String tokenId, Long userId, Long bussId, Long orderUserId, String orderNo) {
        try {
            QztGorder qztGorder = new QztGorder(orderUserId, orderNo);
            qztGorder.setUpdateBy(bussId);
            return returnUtil.returnMess(this.qztGorderService.confirmTheSettlement(qztGorder));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取物流信息
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "获取物流信息", notes = "获取物流信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/getLogisticsInformation")
    public Map getLogisticsInformation(String vno, String cno, String tokenId, Long userId, String orderNo) {
        return returnUtil.returnMess(Constant.DATA_ERROR);
//        try {
//            QztGorder qztGorder = this.qztGorderService.queryByOrederNo(orderNo);
//            if (qztGorder == null) {
//                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
//            }
//            if (StringUtil.isEmpty(qztGorder.getCourierNo()) || StringUtil.isEmpty(qztGorder.getCourierCode())) {
//                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "该订单无物流单号");
//            }
//            Map map = LogisticsRelated.selectCode(qztGorder.getCourierNo(), qztGorder.getCourierCode());
//            if (map.get("data") != null) {
//                Map data = (Map) map.get("data");
//                data.put("courierCompany", StringUtil.isEmpty(qztGorder.getCourierCompany()) ? "" : qztGorder.getCourierCompany());
//                data.put("courierNo", StringUtil.isEmpty(qztGorder.getCourierNo()) ? "" : qztGorder.getCourierNo());
//                map.put("data", data);
//            }
//            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
    }


    /**
     * 分页查询商品订单
     *
     * @return Map
     * @author
     * @date
     */
    @ApiOperation(value = "分页查询商品订单", notes = "分页查询商品订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderState", value = "订单状态(待支付01、待发货03、待收货05、已完成09、已取消11)", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "orderStateShop", value = "订单状态(待到店02、待确认04、已完成09)", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId, String orderState, Long bussId, String orderStateShop) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            if (bussId != null) {//商家订单
                conditionMap.put("orderState", orderStateShop);
                conditionMap.put("addressId", bussId);
                conditionMap.put("pickupWay", "0");
            } else {//用户订单
                if ("03".equals(orderState)) {
                    conditionMap.put("orderStateArr", new String[]{"02", "03"});
                } else {
                    conditionMap.put("orderState", orderState);
                }
                conditionMap.put("userId", userId);
            }
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.qztGorderService.find(pageModel);
            List<QztGorder> records = pageModel.getRecords();
            List<Map> datamaplist = new ArrayList<>();
            for (QztGorder qztGorder : records) {
                Map maps = new HashMap();
                Long time = DateUtil.computingTimeDifference(qztGorder.getOrderTimingTime());
                maps.put("timingTime", time);//自动取消订单时间S
                maps.put("orderNo", qztGorder.getOrderNo());//订单状态
                if ("02".equals(qztGorder.getOrderState())) {
                    qztGorder.setOrderState("03");
                }
                maps.put("orderState", qztGorder.getOrderState());//订单状态
                maps.put("orderStateMc", DicParamUtil.getDicCodeByType("ORDER_STATE", qztGorder.getOrderState()));//订单状态MC
                List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
                if ("1.0.0".equals(vno)) {
                    QztStoGoods qztStoGoods = qztStoGoodsList.get(0);
                    maps.put("goodsId", qztStoGoods.getGoodsId());//商品ID
                    maps.put("goodsName", qztStoGoods.getGoodsName());//商品名称
                    maps.put("goodsPic", qztStoGoods.getGoodsPic());//商品缩略图
                    maps.put("buyNum", qztStoGoods.getBuyNum());//购买数
                } else if ("1.0.1".equals(vno)) {
                    List<Map> stoGoodsList = new ArrayList<>();
                    for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                        Map mapsto = new HashMap();
                        mapsto.put("goodsId", qztStoGoods.getGoodsId());//商品ID
                        mapsto.put("buyNum", qztStoGoods.getBuyNum());
                        mapsto.put("goodsName", qztStoGoods.getGoodsName());
                        mapsto.put("goodsPic", qztStoGoods.getGoodsPic());
                        mapsto.put("goodsRemark", qztStoGoods.getGoodsRemark());
                        mapsto.put("goodsPrice", qztStoGoods.getGoodsPrice());
                        mapsto.put("goodsPriceStr", PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));
                        mapsto.put("goodsSpec", qztStoGoods.getGoodsSpec());
                        stoGoodsList.add(mapsto);
                    }
                    maps.put("stoGoodsList", stoGoodsList);
                }

                maps.put("payTypeMc", qztGorder.getPayType() != null ? DicParamUtil.getDicCodeByType("PAY_TYPE", qztGorder.getPayType()) : "");//支付方式MC
                maps.put("isServe", qztGorder.getIsServe());//是否可申请服务（是Y、否N）
                maps.put("pickupWay", qztGorder.getPickupWay());//取货方式(自提0、快递1)
                maps.put("theVerificationCode", bussId != null ? "" : qztGorder.getTheVerificationCode());//核销码 商家无权查看
                maps.put("totalPriceStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getTotalPrice()));//订单金额
                maps.put("totalPrice", qztGorder.getTotalPrice());//订单金额(分)
                maps.put("payPrice", qztGorder.getActuaPayment());//实际支付金额(分)
                datamaplist.add(maps);
            }
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", datamaplist);
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 分类查询商品订单数量
     *
     * @return Map
     * @author
     * @date
     */
    @ApiOperation(value = "分类查询商品订单数量", notes = "分类查询商品订单数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
//            @ApiImplicitParam(name = "orderState", value = "订单状态(待支付01、待发货03、待收货05、已完成09、已取消11)", dataType = "String", required = false, paramType = "query"),
//            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "Long", required = false, paramType = "query"),
//            @ApiImplicitParam(name = "orderStateShop", value = "订单状态(待到店02、待确认04、已完成09)", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/getListNum")
    public Map<String, Object> getListNum(String vno, String cno, String tokenId, Long userId) {
        Map map = new HashMap();
        Map conditionMap = new HashMap();
        if (userId != null) {
            conditionMap.put("userId", userId);
            conditionMap.put("orderState", "01");//待支付
            List<QztGorder> list01 = this.qztGorderService.findList(conditionMap);
            map.put("waitPayNum", list01.size());
            conditionMap.put("orderState", "03");//待发货
            List<QztGorder> list03 = this.qztGorderService.findList(conditionMap);
            map.put("waitGoodsNum", list03.size());
            conditionMap.put("orderState", "05");//待收货
            List<QztGorder> list05 = this.qztGorderService.findList(conditionMap);
            map.put("waitReceiveNum", list05.size());
        } else {
            map.put("waitPayNum", 0);
            map.put("waitGoodsNum", 0);
            map.put("waitReceiveNum", 0);
        }
        return returnUtil.returnMessMap(map);
    }

    /**
     * 查询商品订单详情
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "查询商品订单详情", notes = "查询商品订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztGorder/selectByOrderNo")
    public Map<String, Object> selectByOrderNo(String vno, String cno, String tokenId, Long userId, Long bussId, String orderNo) {
        try {
            QztGorder qztGorder;
            if (bussId != null) {//商家订单
                qztGorder = this.qztGorderService.queryByOrederNo(orderNo);
            } else {//用户订单
                qztGorder = this.qztGorderService.queryByOrederNoUserId(new QztGorder(userId, orderNo));
            }
            if (qztGorder == null) {
                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
            }
            Map remap = new HashMap();
            Map addressMess = new HashMap();
            addressMess.put("consigneeName", qztGorder.getConsigneeName());//收货人姓名
            addressMess.put("consigneeTel", qztGorder.getConsigneeTel());//收货人电话
            addressMess.put("consigneeAddress", qztGorder.getConsigneeAddress());//收货地址
            Map orderMess = new HashMap();
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getOrderGoodsList(qztGorder.getUserId(), qztGorder.getOrderNo());
            if ("1.0.0".equals(vno)) {
                QztStoGoods qztStoGoods = qztStoGoodsList.get(0);
                Map goodsMess = new HashMap();
                goodsMess.put("goodsId", qztStoGoods.getGoodsId());//商品ID
                goodsMess.put("goodsName", qztStoGoods.getGoodsName());//商品名称
                goodsMess.put("goodsPic", qztStoGoods.getGoodsPic());//商品图
                goodsMess.put("goodsPrice", qztStoGoods.getGoodsPrice());//商品单价
                goodsMess.put("goodsPriceStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));//商品单价
                orderMess.put("buyNum", qztStoGoods.getBuyNum());//购买数量
                remap.put("goodsMess", goodsMess);
            } else if ("1.0.1".equals(vno)) {
                List<Map> stoGoodsList = new ArrayList<>();
                for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                    Map mapsto = new HashMap();
                    mapsto.put("goodsId", qztStoGoods.getGoodsId());//商品ID
                    mapsto.put("buyNum", qztStoGoods.getBuyNum());
                    mapsto.put("goodsName", qztStoGoods.getGoodsName());
                    mapsto.put("goodsPic", qztStoGoods.getGoodsPic());
                    mapsto.put("goodsRemark", qztStoGoods.getGoodsRemark());
                    mapsto.put("goodsPrice", qztStoGoods.getGoodsPrice());
                    mapsto.put("goodsPriceStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztStoGoods.getGoodsPrice()));
                    mapsto.put("goodsSpec", qztStoGoods.getGoodsSpec());
                    stoGoodsList.add(mapsto);
                }
                remap.put("stoGoodsList", stoGoodsList);
            }

            if ("02".equals(qztGorder.getOrderState())) {
                qztGorder.setOrderState("03");
            }
            orderMess.put("payPrice", qztGorder.getActuaPayment());//实际支付金额(分)
            orderMess.put("totalPrice", qztGorder.getTotalPrice());//订单金额
            orderMess.put("totalPriceStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getTotalPrice()));//订单金额
            orderMess.put("orderState", qztGorder.getOrderState());//订单状态
            orderMess.put("orderStateMc", DicParamUtil.getDicCodeByType("ORDER_STATE", qztGorder.getOrderState()));//订单状态MC
            orderMess.put("payState", qztGorder.getPayState());//支付状态
            orderMess.put("payTypeMc", qztGorder.getPayType() != null ? DicParamUtil.getDicCodeByType("PAY_TYPE", qztGorder.getPayType()) : "—");//支付方式MC
            orderMess.put("createTime", qztGorder.getCreateTime() == null ? "—" : qztGorder.getCreateTime().getTime());//下单时间
            orderMess.put("payTime", qztGorder.getPayTime() == null ? "—" : qztGorder.getPayTime().getTime());//支付时间
            orderMess.put("finshTime", qztGorder.getFinshTime() == null ? "—" : qztGorder.getFinshTime().getTime());//完成时间
            orderMess.put("isServe", qztGorder.getIsServe());//是否可申请服务（是Y、否N）
            orderMess.put("pickupWay", qztGorder.getPickupWay());//取货方式(自提0、快递1)
            orderMess.put("userTel", qztGorder.getUserTel());//联系方式
            orderMess.put("theVerificationCode", bussId != null ? "" : qztGorder.getTheVerificationCode());//核销码
//            Long time = DateUtil.computingTimeDifference(qztGorder.getOrderTimingTime());
//            orderMess.put("timingTime", time);//自动取消订单时间S
            orderMess.put("timingTime", qztGorder.getOrderTimingTime().getTime());
            orderMess.put("totalAmount", qztGorder.getTotalPrice() + qztGorder.getCashCouponMoney());

            orderMess.put("remarks", qztGorder.getRemarks());
            orderMess.put("courierNo", qztGorder.getCourierNo());
            orderMess.put("courierRemarks", qztGorder.getCourierRemarks());
            orderMess.put("courierCompany", "京东快递");
            orderMess.put("shippingReminder", "预计24小时内发货，特殊商品一旦发货不能退换货。");

            Map payMess = new HashMap();
            payMess.put("cashCouponMoneyStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getCashCouponMoney()));//抵扣券抵付金额
            payMess.put("balanceMoneyStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getBalanceMoney()));//余额抵付金额
            payMess.put("threeSidesMoneyStr", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getThreeSidesMoney()));//三方支付金额
            payMess.put("cashCouponMoney", qztGorder.getCashCouponMoney());//抵扣券抵付金额
            payMess.put("balanceMoney", qztGorder.getBalanceMoney());//余额抵付金额
            payMess.put("threeSidesMoney", qztGorder.getThreeSidesMoney());//三方支付金额

            payMess.put("storeCouponMoney", qztGorder.getStoreCouponMoney());
            payMess.put("goodsCouponMoney", qztGorder.getGoodsCouponMoney());

            remap.put("addressMess", addressMess);
            remap.put("orderMess", orderMess);
            remap.put("payMess", payMess);

            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 查询可申请服务订单
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-20
     */
    @ApiOperation(value = "查询可申请服务订单", notes = "查询可申请服务订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "businessId", value = "服务站ID", dataType = "Long", required = false, paramType = "query"),
    })
    @GetMapping("/webapi/qztGorder/getCanServiceOrderList")
    public Map<String, Object> getCanServiceOrderList(String vno, String cno, String tokenId, Long userId, Long businessId) {
        try {
            Map pmap = new HashMap();
            pmap.put("userId", userId);
            pmap.put("businessId", businessId);
            List<QztGorder> records = this.qztGorderService.getCanServiceOrderList(pmap);
            List<Map> datamaplist = new ArrayList<>();
            for (QztGorder qztGorder : records) {
                Map maps = new HashMap();
                maps.put("orderNo", qztGorder.getOrderNo());//订单状态
                maps.put("goodsName", qztGorder.getGoodsName());//商品名称
                maps.put("buyNum", qztGorder.getBuyNum());//购买数
                maps.put("createTime", qztGorder.getCreateTime());//下单时间
                maps.put("totalPrice", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztGorder.getTotalPrice()));//订单金额
                datamaplist.add(maps);
            }
            return returnUtil.returnMess(datamaplist);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 再来一单，将该单商品添加购物车
     *
     * @return java.util.Map
     * @author Xiaofei
     * @date 2020-01-07
     */
    @ApiOperation(value = "再来一单", notes = "再来一单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztGorder/recurOrder")
    public Map recurOrder(String vno, String cno, String tokenId, Long userId, String orderNo) {
        try {
            QztGorder qztGorder = this.qztGorderService.queryByOrederNoUserId(new QztGorder(userId, orderNo));
            if (qztGorder == null) {
                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
            }
            boolean result = this.qztGorderService.recurOrder(qztGorder);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}


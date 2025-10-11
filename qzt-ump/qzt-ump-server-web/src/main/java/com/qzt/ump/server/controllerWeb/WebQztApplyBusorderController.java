package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztAccount;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.model.QztUserAddress;
import com.qzt.bus.rpc.api.IQztAccountService;
import com.qzt.bus.rpc.api.IQztBusinessService;
import com.qzt.bus.rpc.api.IQztUserAddressService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.model.SysParamModel;
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztApplyBusorder;
import com.qzt.bus.rpc.api.IQztApplyBusorderService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztApplyBusorderController", description = "服务站申请Api")
public class WebQztApplyBusorderController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztApplyBusorderService qztApplyBusorderService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private IQztBusinessService qztBusinessService;


    /**
     * 服务站申请提交订单
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "服务站申请提交订单", notes = "服务站申请提交订单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "contactName", value = "申请人姓名", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "contactTel", value = "联系电话", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "context", value = "城市CODE", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "stateMark", value = "详细地址", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "referrerUserId", value = "推荐人ID", dataType = "Long", required = false, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyBusorder/applySub0rder")
    public Map applySub0rder(String vno, String cno, String tokenId, Long userId, String contactName, String contactTel, String context, String stateMark, Long referrerUserId) {
        try {
            QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderService.queryByUserId(userId);
            QztApplyBusorder qztApplyBusordernew = new QztApplyBusorder(userId, contactName, contactTel, context, stateMark, referrerUserId);
            //校验推荐人是否是服务站
            if (qztApplyBusordernew.getReferrerUserId() != null) {
                if (userId.equals(referrerUserId)) {
                    return returnUtil.returnMess(Constant.REFERRER_ITS_NOT_A_SERVICE_STATION, "当前推荐人为自己，继续保存推荐人将无效");
                } else if (this.qztBusinessService.queryByUserId(qztApplyBusordernew.getReferrerUserId()) == null) {
                    return returnUtil.returnMess(Constant.REFERRER_ITS_NOT_A_SERVICE_STATION);
                }
            }

            String areaName = this.sysAreaService.selectAreaName(context.substring(0, 2), context.substring(2, 7), context.substring(7, 12));
            qztApplyBusordernew.setConsigneeAddress(areaName + qztApplyBusordernew.getStateMark());

            if (qztApplyBusorder == null) {
                qztApplyBusordernew.setOrderNo(OrderUtil.generateOrderNo(OrderUtil.OrderNoEnum.BUSORDER.value()));
                qztApplyBusordernew.setOrderState("01");
                qztApplyBusordernew.setPayState("00");
                SysParamModel sysParamModel = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + "SERVICE_STATION_MONEY");//服务站价格
                Long orderMoney = sysParamModel == null ? 198000L : Long.valueOf(sysParamModel.getParamValue());
                qztApplyBusordernew.setOrderMoney(orderMoney);
                qztApplyBusordernew = this.qztApplyBusorderService.add(qztApplyBusordernew);
            } else {
                qztApplyBusordernew.setOrderNo(qztApplyBusorder.getOrderNo());
                qztApplyBusordernew.setId(qztApplyBusorder.getId());
                qztApplyBusordernew.setOrderState("01");
                qztApplyBusordernew = this.qztApplyBusorderService.modifyById(qztApplyBusordernew);
            }
            if (qztApplyBusordernew == null) {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            } else {
                Map map = new HashMap();
                map.put("data", qztApplyBusordernew.getOrderNo());
                return returnUtil.returnMess(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 服务站申请订单支付
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "服务站申请订单支付", notes = "服务站申请订单支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "addressId", value = "收货地址ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "payType", value = "三方平台支付方式(支付宝z、微信w)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "balanceMoney", value = "余额抵付金额", dataType = "BigDecimal", required = false, paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码（md5加密）", dataType = "String", required = false, paramType = "query")
    })
    @PostMapping("/webapi/qztApplyBusorder/payApplay0rder")
    public Map payApplay0rder(String vno, String cno, String tokenId, Long userId, String orderNo, Long addressId, String payType, BigDecimal balanceMoney, String payPwd) {
        try {
            balanceMoney = balanceMoney == null || balanceMoney.compareTo(BigDecimal.ZERO) < 1 ? BigDecimal.ZERO : balanceMoney;
            balanceMoney = balanceMoney.multiply(BigDecimal.valueOf(100)).setScale(0);
            Map map = this.qztApplyBusorderService.payApplay0rder(orderNo, addressId, payType, Long.valueOf(balanceMoney.toString()), payPwd, userId);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取申请服务站订单支付页信息
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "获取申请服务站订单支付页信息", notes = "获取申请服务站订单支付页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztApplyBusorder/getPayApplay0rderInfo")
    public Map getPayApplay0rderInfo(String vno, String cno, String tokenId, Long userId, String orderNo, HttpServletRequest request) {
        try {
            QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderService.queryByOrederNoUserId(orderNo, userId);
            if (qztApplyBusorder == null) {
                return returnUtil.returnMess(Constant.ORDER_DOES_NOT_EXIST);
            } else if (!"01".equals(qztApplyBusorder.getOrderState())) {
                return returnUtil.returnMess(Constant.ORDER_OFF_THE_STOCKS);
            }
            Map remap = new HashMap();
            String orderMoney = PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getOrderMoney());

            Map addressMess = new HashMap();
            Long addressId = null;
            String companyName = "";
            String consigneeTel = "";
            String consigneeAddress = "";
            if (qztApplyBusorder.getAddressId() == null) {
                QztUserAddress qztUserAddress = this.qztUserAddressService.selectDefaultaccAddress(userId);
                if (qztUserAddress != null) {
                    addressId = qztUserAddress.getId();
                    companyName = qztUserAddress.getRecipientName();
                    consigneeTel = qztUserAddress.getPhone();
                    String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
                    consigneeAddress = areaName + qztUserAddress.getDetailAddress();
                }
            } else {
                addressId = qztApplyBusorder.getAddressId();
                companyName = qztApplyBusorder.getCompanyName();
                consigneeTel = qztApplyBusorder.getConsigneeTel();
                consigneeAddress = qztApplyBusorder.getConsigneeAddress();
            }
            addressMess.put("addressId", addressId);
            addressMess.put("companyName", companyName);
            addressMess.put("consigneeTel", consigneeTel);
            addressMess.put("consigneeAddress", consigneeAddress);
            addressMess.put("addressList", this.qztUserAddressService.findListByUserId(userId));

            Map goodsMess = new HashMap();
            goodsMess.put("goodsName", "七足堂服务站");
            goodsMess.put("goodsExplain", "精诚专注，仁术仁心。");
            goodsMess.put("goodsPic", "https://img.jlqizutang.com/servicelogo.png");
            goodsMess.put("goodsPrice", "¥" + orderMoney);

            Map orderMess = new HashMap();
            Long threeSidesMoney = qztApplyBusorder.getBalanceMoney() != null && qztApplyBusorder.getBalanceMoney().longValue() > 0L ? qztApplyBusorder.getThreeSidesMoney() : qztApplyBusorder.getOrderMoney();
            orderMess.put("orderMoney", orderMoney);
            orderMess.put("threeSidesMoney", PriceUtil.exactlyTwoDecimalPlaces(threeSidesMoney));
            orderMess.put("balanceMoney", PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getBalanceMoney()));

            Map userMess = new HashMap();
            QztAccount qztAccount = this.qztAccountService.selectByUserId(qztApplyBusorder.getApplyUserId());
            userMess.put("usedMoney", PriceUtil.exactlyTwoDecimalPlaces(qztAccount.getUsedMoney()));//可用余额

            remap.put("addressMess", addressMess);
            remap.put("goodsMess", goodsMess);
            remap.put("orderMess", orderMess);
            remap.put("userMess", userMess);
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取申请服务站订单信息
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-16
     */
    @ApiOperation(value = "获取申请服务站订单信息", notes = "获取申请服务站订单信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztApplyBusorder/getApplay0rderInfo")
    public Map getApplay0rderInfo(String vno, String cno, String tokenId, Long userId) {
        try {
            Map remap = new HashMap();
            QztApplyBusorder qztApplyBusorder = this.qztApplyBusorderService.queryByUserId(userId);
            String contactName = "";
            String contactTel = "";
            String context = "";
            String stateMark = "";
            Long referrerUserId = null;
            String proCityArea = "";
            String orderNo = "";
            String orderState = "";
            String orderStateMc = "";
            Date createTime = null;
            Date shipmentsTime = null;
            Date finshTime = null;
            Long addressId = null;
            String consigneeName = "";
            String consigneeTel = "";
            String consigneeAddress = "";
            String courierCompany = "";
            String courierNo = "";
            String payType = "";
            String orderType = "";
            String threeSidesMoney = "";
            if (qztApplyBusorder != null) {
                contactName = qztApplyBusorder.getContactName();
                contactTel = qztApplyBusorder.getContactTel();
                context = qztApplyBusorder.getContext();
                stateMark = qztApplyBusorder.getStateMark();
                referrerUserId = qztApplyBusorder.getReferrerUserId();
                //服务站站 省市区翻译
                if (qztApplyBusorder.getContext() != null && qztApplyBusorder.getContext().length() == 12) {
                    proCityArea = this.sysAreaService.selectAreaName(qztApplyBusorder.getContext().substring(0, 2), qztApplyBusorder.getContext().substring(2, 7), qztApplyBusorder.getContext().substring(7, 12));
                }
                orderNo = qztApplyBusorder.getOrderNo();
                orderState = qztApplyBusorder.getOrderState();
                orderStateMc = DicParamUtil.getDicCodeByType("BUS_ORDER_STATE", qztApplyBusorder.getOrderState());
                createTime = qztApplyBusorder.getCreateTime();
                shipmentsTime = qztApplyBusorder.getShipmentsTime();
                finshTime = qztApplyBusorder.getFinshTime();
                consigneeName = qztApplyBusorder.getContactName();
                addressId = qztApplyBusorder.getAddressId();
                consigneeTel = qztApplyBusorder.getConsigneeTel();
                consigneeAddress = qztApplyBusorder.getConsigneeAddress();
                courierCompany = qztApplyBusorder.getCourierCompany();
                courierNo = qztApplyBusorder.getCourierNo();
                payType = qztApplyBusorder.getPayType();
                orderType = OrderUtil.OrderNoEnum.BUSORDER.value();
                threeSidesMoney = "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztApplyBusorder.getThreeSidesMoney());
            }
            Map applyMess = new HashMap();//申请信息
            applyMess.put("contactName", contactName);//申请人姓名
            applyMess.put("contactTel", contactTel);//申请人联系电话
            applyMess.put("context", context);//服务站地址areacode
            applyMess.put("stateMark", stateMark);//服务站详细地址
            applyMess.put("referrerUserId", referrerUserId);//推荐人ID
            applyMess.put("proCityArea", proCityArea);//服务站站省市区翻译
            applyMess.put("address", proCityArea + stateMark);//服务站站省市区详细地址

            Map borderMess = new HashMap();//订单信息
            borderMess.put("orderNo", orderNo);
            borderMess.put("orderState", orderState);//订单状态
            borderMess.put("orderStateMc", orderStateMc);//订单状态Mc
            borderMess.put("createTime", createTime);//申请时间
            borderMess.put("shipmentsTime", shipmentsTime == null ? "—" : shipmentsTime);//发货时间
            borderMess.put("finshTime", finshTime == null ? "—" : finshTime);//完成时间

            Map addressMess = new HashMap();//收货地址信息
            addressMess.put("addressId", addressId);
            addressMess.put("consigneeName", consigneeName);//收货人名称
            addressMess.put("consigneeTel", consigneeTel);//收货人电话
            addressMess.put("consigneeAddress", consigneeAddress);//收货地址
            addressMess.put("courierCompany", StringUtil.isEmpty(courierCompany) ? "—" : courierCompany);//快递公司
            addressMess.put("courierNo", StringUtil.isEmpty(courierNo) ? "—" : courierNo);//快递单号


            Map payMess = new HashMap();//支付信息
            payMess.put("payType", payType);//如为0  1 则为对应三方支付平台 其他状态则支付成功
            payMess.put("orderType", orderType);
            payMess.put("threeSidesMoney", threeSidesMoney);
//            payMess.put("isContinuePay", addressId != null ? "Y" : "N");
            payMess.put("isContinuePay", "Y");

            remap.put("payMess", payMess);
            remap.put("applyMess", applyMess);
            remap.put("borderMess", borderMess);
            remap.put("addressMess", addressMess);
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

//    @GetMapping("/pubapi/qztApplyBusorder/test")
//    public Map test(String orderNo) {
//        try {
//            return returnUtil.returnMess(this.qztApplyBusorderService.applyOrderPayBack(orderNo));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
//    }

}


package com.qzt.ump.server.controllerBack;

import com.qzt.bus.rpc.api.*;
import com.qzt.common.tools.OrderUtil;
import com.qzt.common.web.BaseController;
import com.qzt.ump.server.message.ExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 导出管理
 *
 * @author Xiaofei
 * @return
 * @date 2019-12-05
 */
@RestController
@RequestMapping("/back/qztexport")
@Api(value = "QztExportController", description = "导出管理API")
public class QztExportController extends BaseController {

    //用户提现模版-银行卡
    private final String USER_WITHDRAW_BANK_TEMPLATE = "user_withdraw_bank_template.xlsx";
    //用户提现模版-支付宝
    private final String USER_WITHDRAW_ALIPAY_TEMPLATE = "user_withdraw_alipay_template.xlsx";
    //商品订单模版 - 快递
    private final String GSORDERO_TEMPLATE = "gsordero_template.xlsx";
    //商品订单模版 - 自提
    private final String GSORDERT_TEMPLATE = "gsordert_template.xlsx";
    //服务站申请订单模版
    private final String SAORDER_TEMPLATE = "saorder_template.xlsx";
    //服务订单模版
    private final String SOORDER_TEMPLATE = "soorder_template.xlsx";
    //试用订单模版
    private final String SYORDER_TEMPLATE = "syorder_template.xlsx";
    //充值记录模版
    private final String RECHARGE_TEMPLATE = "recharge_template.xlsx";
    //注册用户模板
    private final String USER_TEMPLATE = "user_template.xlsx";

    @Autowired
    private IQztWithdrawService qztWithdrawService;

    @Autowired
    private IQztGorderService qztGorderService;

    @Autowired
    private IQztApplyBusorderService qztApplyBusorderService;

    @Autowired
    private IQztApplyTryorderService qztApplyTryorderService;

    @Autowired
    private IQztServiceOrderService qztServiceOrderService;

    @Autowired
    private IQztRechargeService qztRechargeService;

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 导出下载工具类
     */
    @Autowired
    private ExportUtils exportUtils;


    /**
     * 提现记录导出
     *
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-12-04
     */
    @ApiOperation(value = "提现记录导出", notes = "提现记录导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardType", value = "收款账号类型（01银行卡 02支付宝）", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "auditState", value = "审核状态", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "提现时间start", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "提现时间end", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "withdrawIds", value = "提现ID集合", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "auditType", value = "1受理accept、2确认affirm、3完成finish", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/withdrawExcel")
    public String withdrawExcel(HttpServletRequest request, HttpServletResponse response, String cardType, String auditState, String startTime,
                                String endTime, String userId, String withdrawIds, String auditType) {
        String templateName = "";
        String title = "";
        String newFileName = "";
        Integer[] digitPosition = new Integer[]{7, 8, 10};
        if ("01".equals(cardType)) {
            templateName = this.USER_WITHDRAW_BANK_TEMPLATE;
            title = "用户提现记录-银行卡";
            newFileName = "用户提现记录-银行卡";
        } else if ("02".equals(cardType)) {
            templateName = this.USER_WITHDRAW_ALIPAY_TEMPLATE;
            title = "用户提现记录-支付宝";
            newFileName = "用户提现记录-支付宝";
            digitPosition = new Integer[]{5, 6, 8};
        }
        Map map = new HashMap();
        map.put("cardType", cardType);
        map.put("auditState", auditState);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("userId", userId);
        map.put("auditType", auditType);
        map.put("withdrawIdArr", withdrawIds != null && !"".equals(withdrawIds) ? withdrawIds.split(",") : null);
        String[] auditStateArr = null;
        if ("admin".equals(auditType)) {//所有
            if ("11".equals(auditState)) {
                map.put("auditState", null);
                auditStateArr = new String[]{"11", "13", "15"};
            }
        } else if ("accept".equals(auditType)) {//受理accept
            auditStateArr = new String[]{"00", "11"};
        } else if ("affirm".equals(auditType)) {//确认affirm
            auditStateArr = new String[]{"01", "13"};
        } else if ("finish".equals(auditType)) {//完成finish
            auditStateArr = new String[]{"03", "20", "15"};
        }
        map.put("auditStateArr", auditStateArr);
        List<Map<String, Object>> mapList = this.qztWithdrawService.userWithdrawExcel(map);
        this.exportUtils.exportExcel(response, title, templateName, newFileName, mapList, digitPosition, 2);
        return "succeed";
    }

    /**
     * 订单导出
     *
     * @return java.lang.String
     * @author
     * @date
     */
    @ApiOperation(value = "订单导出", notes = "订单导出")
    @GetMapping("/orderExcel")
    public String orderExcel(HttpServletRequest request, HttpServletResponse response, String orderState, String payState, String orderNo,
                             String userId, String goodsName, String startTime, String endTime, String orderType,
                             String sortField, String sortType, String payType, String referrerUserId, String pickupWay, String replenishStatus,
                             String id,String regStartTime,String regEndTime,String state,String referrerFirst,String referrerSecond) {
        Map map = new HashMap();
        Integer[] digitPosition = new Integer[]{8};
        String templateName = "";
        String title = "";
        String newFileName = "";
        List<Map<String, Object>> mapList = null;
        map.put("orderState", orderState);
        map.put("payState", payState);
        map.put("orderNo", orderNo);
        map.put("userId", userId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);

        map.put("sortField", sortField);
        map.put("sortType", sortType);


        if (OrderUtil.OrderNoEnum.BUSORDER.value().equals(orderType)) {
            map.put("referrerUserId", referrerUserId);
            templateName = this.SAORDER_TEMPLATE;
            title = "服务站申请订单";
            newFileName = "服务站申请订单";
            mapList = this.qztApplyBusorderService.saorderExcel(map);
        } else if (OrderUtil.OrderNoEnum.GOODS.value().equals(orderType)) {
            map.put("payType", payType);
            map.put("goodsName", goodsName);
            map.put("pickupWay", pickupWay);
            map.put("replenishStatus", replenishStatus);
            if ("1".equals(pickupWay)) {//快递
                title = "商品发货订单";
                newFileName = "商品发货订单";
                templateName = this.GSORDERO_TEMPLATE;

            } else if ("0".equals(pickupWay)) {//自提
                title = "商品核销订单";
                newFileName = "商品核销订单";
                templateName = this.GSORDERT_TEMPLATE;
            }
            digitPosition = new Integer[]{7, 8, 9,10};
            mapList = this.qztGorderService.gorderExcel(map);
        } else if (OrderUtil.OrderNoEnum.APPLYTRY.value().equals(orderType)) {
            map.put("payType", payType);
            map.put("goodsName", goodsName);
            templateName = this.SYORDER_TEMPLATE;
            title = "试用订单";
            newFileName = "试用订单";
            digitPosition = new Integer[]{};
            mapList = this.qztApplyTryorderService.syorderExcel(map);
        } else if (OrderUtil.OrderNoEnum.SERVER.value().equals(orderType)) {
            map.put("payType", payType);
            map.put("goodsName", goodsName);
            templateName = this.SOORDER_TEMPLATE;
            title = "服务订单";
            newFileName = "服务订单";
            digitPosition = new Integer[]{7};
            mapList = this.qztServiceOrderService.soorderExcel(map);
        }else if(OrderUtil.OrderNoEnum.USER.value().equals(orderType)){
            map.put("id", id);
            map.put("regStartTime", regStartTime);
            map.put("regEndTime", regEndTime);
            map.put("state", state);
            map.put("referrerFirst", referrerFirst);
            map.put("referrerSecond", referrerSecond);
            templateName = this.USER_TEMPLATE;
            title = "注册用户";
            newFileName = "注册用户";
            digitPosition = new Integer[]{};
            mapList = this.qztUserService.userExcel(map);
        }
        this.exportUtils.exportExcel(response, title, templateName, newFileName, mapList, digitPosition, 2);
        return "succeed";
    }

    /**
     * 充值记录导出
     *
     * @return java.lang.String
     * @author Xiaofei
     * @date 2020-04-02
     */
    @ApiOperation(value = "充值记录导出", notes = "充值记录导出")
    @GetMapping("/rechargeExcel")
    public String rechargeExcel(HttpServletRequest request, HttpServletResponse response, String auditState, String userTel, String userId) {
        Map map = new HashMap();
        String templateName = this.RECHARGE_TEMPLATE;
        String title = "充值记录";
        String newFileName = "充值记录";
        map.put("auditState", auditState);
        map.put("userTel", userTel);
        map.put("userId", userId);
        Integer[] digitPosition = new Integer[]{3};
        List<Map<String, Object>> mapList = this.qztRechargeService.rechargeExcel(map);
        this.exportUtils.exportExcel(response, title, templateName, newFileName, mapList, digitPosition, 2);
        return "succeed";
    }


}

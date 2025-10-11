package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztAccount;
import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztAccountService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.tools.BigDecimalUtil;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.model.SysParamModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztWithdraw;
import com.qzt.bus.rpc.api.IQztWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztWithdrawController", description = "提现相关Api")
public class WebQztWithdrawController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztWithdrawService qztWithdrawService;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 提现申请
     *
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "提现申请", notes = "提现申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "withdrawMoney", value = "提现金额", dataType = "BigDecimal", required = true, paramType = "query"),
            @ApiImplicitParam(name = "serviceCharge", value = "提现手续费", dataType = "BigDecimal", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cardId", value = "收款账号ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "支付密码（md5加密）", dataType = "String", required = true, paramType = "query")
    })
    @PutMapping("/webapi/qztWithdraw/withdrawApplyfor")
    public Map<String, Object> withdrawApplyfor(String vno, String cno, String tokenId, Long userId, BigDecimal withdrawMoney, BigDecimal serviceCharge, Long cardId, String payPwd) {
        try {
            QztWithdraw qztWithdraw = new QztWithdraw(cno, userId, withdrawMoney, serviceCharge, cardId);
            Map map = this.qztWithdrawService.userWithdrawApplyfor(qztWithdraw, payPwd);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("6008".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.BALANCE_INSUFFICIENT);
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 提现记录分页查询
     *
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "提现记录分页查询", notes = "提现记录分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "auditState", value = "待审核00、已受理01、已驳回10、已完成 20", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/webapi/qztWithdraw/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, String tokenId, Long userId, Integer pageNum, Integer pageSize, String auditState) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            String[] auditStateArr = null;
            if ("00".equals(auditState)) {//待审核
                auditStateArr = new String[]{"00"};
            } else if ("01".equals(auditState)) {//已受理
                auditStateArr = new String[]{"01", "03"};
            } else if ("10".equals(auditState)) {//已驳回
                auditStateArr = new String[]{"11", "13", "15"};
            } else if ("20".equals(auditState)) {//已完成
                auditStateArr = new String[]{"20"};
            }
            conditionMap.put("auditStateArr", auditStateArr);
            conditionMap.put("userId", userId);
            conditionMap.put("withdrawMoneyType", "01");
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.qztWithdrawService.find(pageModel);
            List<QztWithdraw> records = pageModel.getRecords();
            List<Map> datalist = new ArrayList<>();
            for (QztWithdraw qztWithdraw : records) {
                Map mapd = new HashMap();
                mapd.put("id", qztWithdraw.getId());
                mapd.put("userId", qztWithdraw.getUserId());
                String[] createTime = DateUtil.dateTimeFormat.format(qztWithdraw.getCreateTime()).split(" ");
                mapd.put("day", createTime[0]);//年月日
                mapd.put("time", createTime[1]);//时分秒
                mapd.put("withdrawMoney", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztWithdraw.getWithdrawMoney()));//提现金额
                mapd.put("serviceCharge", qztWithdraw.getServiceCharge() > 0 ? "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztWithdraw.getServiceCharge()) : "");//手续费金额
                mapd.put("arrivalAmount", "¥" + PriceUtil.exactlyTwoDecimalPlaces(qztWithdraw.getArrivalAmount()));//实际到账金额
                mapd.put("auditRemark", qztWithdraw.getAuditRemark());//退回理由
                datalist.add(mapd);
            }
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", datalist);
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 提现页相关信息-验证用户提现是否需要填写支付宝账号与是否有支付密码
     *
     * @author Xiaofei
     * @date 2019-11-12
     */
    @ApiOperation(value = "提现页相关信息", notes = "提现页相关信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztWithdraw/getWithdrawPageInfo")
    public Map<String, Object> getWithdrawPageInfo(String vno, String cno, String tokenId, Long userId) {
//        try {
//            String withdrawMoneyType = "01";
//            QztUser qztUser = this.qztUserService.findUserById(userId.toString());
//            //验证是否设置支付密码
//            if (qztUser.getPayPwd() == null || "".equals(qztUser.getPayPwd())) {
//                return returnUtil.returnMess(Constant.PLEASE_SET_THE_PAYMENT_PASSWORD, "您未设置支付密码，请设置后再提现");
//            }
//            Map map = new HashMap();
//            //获取用户资产
//            QztAccount qztAccount = this.qztAccountService.selectByUserId(userId);
//            map.put("usedMoney", PriceUtil.exactlyTwoDecimalPlaces(qztAccount.getUsedMoney()));//可用余额
//            map.put("userId", userId);//userid
//            SysParamModel sysParamModelr = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + "WITHDRAW_COMMISSION_RATIO");//提现手续费比例（百分比整数）
//            SysParamModel sysParamModell = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + "WITHDRAW_COMMISSION_LEAST");//提现手续费最低金额（单位：分）
//            SysParamModel sysParamModelm = (SysParamModel) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.PARAMES.value() + "WITHDRAW_COMMISSION_MAX");//提现手续费最高金额（单位：分）
//            BigDecimal commissionRatio = BigDecimal.ZERO;//提现手续费比例（百分比整数）
//            Long commissionLeast = 0L;//提现最低手续费金额 单位：分
//            Long commissionMax = 0L;//提现最高手续费金额 单位：分
//            if (sysParamModelr != null) {
//                commissionRatio = new BigDecimal(sysParamModelr.getParamValue());
//            }
//            if (sysParamModell != null) {
//                commissionLeast = Long.valueOf(sysParamModell.getParamValue());
//            }
//            if (sysParamModelm != null) {
//                commissionMax = Long.valueOf(sysParamModelm.getParamValue());
//            }
//            commissionRatio = commissionRatio.divide(new BigDecimal(100), 2, ROUND_HALF_UP);
//            map.put("commissionRatio", commissionRatio.toString());//提现手续费比例（小数）
//            map.put("commissionLeast", PriceUtil.exactlyTwoDecimalPlaces(commissionLeast));//提现最低手续费金额
//            map.put("commissionMax", PriceUtil.exactlyTwoDecimalPlaces(commissionMax));//提现最高手续费金额
////            BigDecimal freeOfFeeMoneySum = BigDecimal.ZERO;//可用免手续费金额
////            freeOfFeeMoneySum = this.qztWithdrawService.calculateFreeOfFeeMoneySum(userId);//可用免手续费金额
////            map.put("freeOfFeeMoneySum", freeOfFeeMoneySum.toString());//可用免手续费金额
//            return returnUtil.returnMessMap(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
        return returnUtil.returnMess(Constant.DATA_ERROR);
    }

}


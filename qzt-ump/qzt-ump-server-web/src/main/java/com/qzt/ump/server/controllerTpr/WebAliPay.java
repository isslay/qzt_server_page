package com.qzt.ump.server.controllerTpr;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayUserInfoShareRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayUserInfoShareResponse;
import com.qzt.bus.rpc.api.IQztPayBacklogService;
import com.qzt.common.pay.config.PayWxConfig;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.ump.rpc.api.ProcessingPayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝相关Api
 *
 * @author Xiaofei
 * @date 2019-11-18
 */
@Slf4j
@RestController
@Api(value = "WebQztAliPay", description = "支付宝相关Api")
public class WebAliPay {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    public PayWxConfig payWxConfig;

    @Autowired
    public ProcessingPayOrderService processingPayOrderService;

    @Autowired
    public IQztPayBacklogService qztPayBacklogService;

    Double moneyNum = 0.01;//测试服支付金额

    /**
     * 支付宝支付 - APP
     *
     * @param orderNo   订单编号
     * @param orderType 订单类型
     * @return Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    @ApiOperation(value = "支付宝支付", notes = "支付宝支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderType", value = "订单类型 （申请服务站SA、商品订单GS）", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping(value = "/pubapi/alipay/pay")
    public Map<String, Object> pay(String vno, String cno, String orderNo, String orderType) {
        try {
            String appBody = "";
            AlipayClient alipayClient = new DefaultAlipayClient(this.payWxConfig.getAliPayUrl(), this.payWxConfig.getAliAppAppId(),
                    this.payWxConfig.getAliAppPrivateKey(), "json", "utf-8", this.payWxConfig.getAliAppAlipayShopPublicKey(), "RSA2");
            AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
            //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
            AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
            Map<String, String> maps = this.processingPayOrderService.getOrderPayAmount(orderNo, orderType);//获取对应金额
            if (maps == null) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "请传入正确的订单类型与订单编号");
            }
            String outTradeNo = maps.get("outTradeNo");//请保证OutTradeNo值每次保证唯一
            String totalAmount = !this.payWxConfig.getTestValue() ? maps.get("totalAmount") : this.moneyNum.toString();//订单三方支付金额
            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount(totalAmount);
            model.setSubject("七足堂");
            model.setProductCode("QUICK_MSECURITY_PAY");
            request.setBizModel(model);
            request.setNotifyUrl(this.payWxConfig.getAliAppBackUrl() + "alipay/payBack");
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            appBody = response.getBody();
            log.info("响应报文：" + appBody);
            return this.returnUtil.returnMess((Object) appBody);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 支付宝支付回调
     *
     * @param out_trade_no 创建支付时的订单编号
     * @param trade_no     三方编号
     * @author Xiaofei
     * @date 2019-11-18
     */
    @PostMapping(value = "/pubapi/alipay/payBack")
    public String payBack(HttpServletRequest request, HttpServletResponse response, String out_trade_no, String trade_no, String trade_status) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> paramsMap = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            paramsMap.put(name, valueStr);
        }
        //支付回调日志
        this.qztPayBacklogService.addPayBacklog("z", out_trade_no, trade_no, paramsMap.get("total_amount"), JSONArray.fromObject(paramsMap).toString());
        log.info("支付报文：" + JSONArray.fromObject(paramsMap).toString());
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, this.payWxConfig.getAliAppAlipayPublicKey(), "UTF-8", "RSA2");
        if (signVerified) {
            if ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status)) {
                this.processingPayOrderService.payBackDispose(out_trade_no, trade_no);//支付回调
            }
            return "success";
        } else {
            return "failure";
        }
    }

    /**
     * 支付宝快捷登录
     */
    @ApiOperation(value = "支付宝快捷登录", notes = "支付宝快捷登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "前台code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
    })
    @PostMapping(value = "/pubapi/alipay/login")
    public Map<String, Object> login(String vno, String cno, String code) {
        Map sParaTemp = new HashMap();
        AlipayClient alipayClient = new DefaultAlipayClient(this.payWxConfig.getAliPayUrl(), this.payWxConfig.getAliAppAppId(),
                this.payWxConfig.getAliAppPrivateKey(), "json", "utf-8", this.payWxConfig.getAliAppAlipayPublicKey(), "RSA2");
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setCode(code);
        request.setGrantType("authorization_code");
        try {
            AlipaySystemOauthTokenResponse oauthTokenResponse = alipayClient.execute(request);
            if (oauthTokenResponse.isSuccess()) {
                Map<String, Object> dataMap = new HashedMap();
                AlipayUserInfoShareRequest requestUser = new AlipayUserInfoShareRequest();
                AlipayUserInfoShareResponse userinfoShareResponse = alipayClient.execute(requestUser, oauthTokenResponse.getAccessToken());
                if (userinfoShareResponse != null) {
                    String avatar = userinfoShareResponse.getAvatar();//用户头像
                    String userName = userinfoShareResponse.getNickName();//用户昵称
                    String userId = userinfoShareResponse.getUserId();//OPENID
                }
            }
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (AlipayApiException e) {
            //处理异常
            e.printStackTrace();
            return returnUtil.returnMess(Constant.OPERATION_FAILURE);
        }

    }

}

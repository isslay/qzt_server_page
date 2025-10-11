package com.qzt.ump.server.controllerTpr;

import com.qzt.bus.model.QztUser;
import com.qzt.bus.model.UserInfo;
import com.qzt.bus.rpc.api.IDgmCouponService;
import com.qzt.bus.rpc.api.IQztPayBacklogService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.bus.rpc.api.IUserInfoService;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.util.RandCodeUtil;
import com.qzt.common.mq.pushMq.MqConstant;
import com.qzt.common.mq.pushMq.MqMess;
import com.qzt.common.mq.utils.MqUtils;
import com.qzt.common.pay.config.PayWxConfig;
import com.qzt.common.pay.util.AppletOfWeChatUtil;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.tools.QiniuUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.pay.util.WeixinUtil;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.pay.util.WeChatTools;
import com.qzt.common.pay.util.WxPayBackTools;
import com.qzt.ump.rpc.api.ProcessingPayOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信相关Api
 *
 * @author Xiaofei
 * @date 2019-11-18
 */
@Slf4j
@RestController
@Api(value = "WebQztWeChat", description = "微信相关Api")
public class WebWeChat {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    public PayWxConfig payWxConfig;

    @Autowired
    public WeChatTools weChatTools;

    @Autowired
    private WxPayBackTools wxPayBackTools;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    public ProcessingPayOrderService processingPayOrderService;

    @Autowired
    public IQztPayBacklogService qztPayBacklogService;

    @Autowired
    private AppletOfWeChatUtil appletOfWeChatUtil;

    @Autowired
    public RedisTemplate redisTemplate;

    @Autowired
    private IDgmCouponService iDgmCouponService;

    private final String weixinAuthTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";

    private final String wxOpenIdUrl = "https://api.weixin.qq.com/sns/jscode2session?grant_type=authorization_code";

    private final String weixinAuthopenidUrl = "https://open.weixin.qq.com/connect/oauth2/authorize";

    private final String weixinAuthUserInfo = "https://api.weixin.qq.com/sns/userinfo?";

    private final Double moneyNum = 0.01;

    /**
     * 微信支付
     *
     * @param orderNo   订单编号
     * @param orderType 订单类型
     * @return Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    @ApiOperation(value = "微信支付", notes = "微信支付")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderNo", value = "订单编号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "orderType", value = "订单类型 （申请服务站SA、商品订单GS）", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "code", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "openid", value = "openid", dataType = "String", required = false, paramType = "query")
    })
    @PostMapping(value = "/pubapi/weChat/pay")
    public Map qztWeChatPay(String vno, String cno, String orderNo, String orderType, String code, String openid, HttpServletRequest request) {
        try {
            String weChatAppId = "";
            String weChatMicId = this.payWxConfig.getWeChatMicId();
            String weChatSign = this.payWxConfig.getWeChatSign();
            Map<String, String> maps = this.processingPayOrderService.getOrderPayAmount(orderNo, orderType);
            if (maps == null) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "请传入正确的订单类型与订单编号");
            }
            String outTradeNo = maps.get("outTradeNo");//请保证OutTradeNo值每次保证唯一
            Double totalAmount = !this.payWxConfig.getTestValue() ? Double.valueOf(maps.get("totalAmount")) : this.moneyNum;//订单三方支付金额
            if ("4".equals(cno) || "5".equals(cno)) {//H5、小程序
                String tokenUrl = this.weixinAuthTokenUrl;
                if ("5".equals(cno)) {//小程序
                    weChatAppId = this.payWxConfig.getAppletAppid();
                } else {//H5
                    weChatAppId = this.payWxConfig.getWeChatAppId();
                    tokenUrl += "&appid=" + this.payWxConfig.getWeChatAppId() + "&secret=" + this.payWxConfig.getWeChatSecret();
                    tokenUrl += "&code=" + code;
                    openid = this.wxPayBackTools.getOpenId(tokenUrl);
                }
                if (StringUtil.isEmpty(openid)) {
                    return returnUtil.returnMess(Constant.OPERATION_FAILURE, "支付出现意外,openid不存在");
                }
            } else {
                weChatAppId = this.payWxConfig.getWeChatAppAppId();
            }
            Map wxMaptoXml = this.wxPayBackTools.getWxMaptoXml(cno, outTradeNo, totalAmount, weChatAppId, weChatMicId, weChatSign, openid, 0,
                    this.payWxConfig.getWeChatPayBackUrl() + "weChat/payBack", request);
            return returnUtil.returnMessMap(wxMaptoXml);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 微信支付回调
     *
     * @param request
     * @param response
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-06-18
     */
    @PostMapping(value = "/pubapi/weChat/payBack")
    public String qztWeChatPayBack(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SAXReader saxReadr = new SAXReader();
        Document document = saxReadr.read(request.getInputStream());
        Map<String, String> params = WeixinUtil.xmlToMap(document);//微信返回参数
        log.info("微信回调报文：" + JSONArray.fromObject(params).toString());
        String payNo = params.get("out_trade_no");// 创建支付时的订单编号
        String transaction_id = params.get("transaction_id");//微信支付订单号
        //支付回调日志
        this.qztPayBacklogService.addPayBacklog("w", payNo, transaction_id, (Double.valueOf(params.get("total_fee")) / 100) + "", JSONArray.fromObject(params).toString());
        String wxsign = WeixinUtil.createSign(params, this.payWxConfig.getWeChatSign());//生成签名
        if (wxsign.equals(params.get("sign"))) {//验证sign
            String return_code = params.get("return_code");//返回状态码
            String result_code = params.get("result_code");//业务结果SUCCESS  SUCCESS/FAIL
            if ("SUCCESS".equals(return_code) && "SUCCESS".equals(result_code)) {
                log.info("签名校验成功");
                this.processingPayOrderService.payBackDispose(payNo, transaction_id);//支付回调
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                out.write(this.wxPayBackTools.GetMapToXML().getBytes());
                out.flush();
                out.close();
                return this.wxPayBackTools.GetMapToXML();
            }
        }
        return "";
    }


//    /**
//     * 微信快捷登录
//     */
//    @ApiOperation(value = "微信快捷登录", notes = "微信快捷登录")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "code", value = "前台code", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
//    })
//    @PostMapping(value = "/pubapi/weChat/weChatLogin")
//    public Map<String, Object> qztLogin(String vno, String cno, String code) {
//        Map<String, Object> dataMap = new HashedMap();
//        String tokenUrl = this.weixinAuthTokenUrl;
//        tokenUrl += "&appid=" + this.payWxConfig.getWeChatAppAppId() + "&secret=" + this.payWxConfig.getWeChatAppAppSecret() + "&code=" + code;
//        HttpClient client = new DefaultHttpClient();
//        HttpGet get = new HttpGet(URI.create(tokenUrl));
//        try {
//            HttpResponse response = client.execute(get);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                HttpEntity entity = response.getEntity();
//                JSONObject json = JSONObject.fromObject(EntityUtils.toString(entity, "utf-8"));
//                String accessToken = json.getString("access_token");
//                String openID = json.getString("openid");
//                String userUrl = this.weixinAuthUserInfo;
//                userUrl += "access_token=" + accessToken + "&openid=" + openID;
//                HttpGet getU = new HttpGet(URI.create(userUrl));
//                HttpResponse responseU = client.execute(getU);
//                if (responseU.getStatusLine().getStatusCode() == 200) {
//                    HttpEntity entityU = responseU.getEntity();
//                    JSONObject jsonU = JSONObject.fromObject(EntityUtils.toString(entityU, "utf-8"));
//                    String nikeName = jsonU.getString("nickname");
//                    String userOpenId = jsonU.getString("openid");
//                    String headImg = jsonU.getString("headimgurl");
//                }
//            }
//            return returnUtil.returnMess(Constant.OPERATION_FAILURE);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.OPERATION_FAILURE);
//        }
//    }

    /**
     * 微信快捷登录
     */
    @ApiOperation(value = "小程序登录", notes = "小程序登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "前台code", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "shareCode", value = "推荐码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "shareUserId", value = "分享人id", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "opinId", value = "微信Id", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "unionId", value = "平台unionid", dataType = "String", required = false, paramType = "query")
    })
    @PostMapping(value = "/pubapi/weChat/weJsChatLogin")
    public Map<String, Object> qztLogin(String vno, String cno, String code, String shareCode, Long shareUserId, String openId, String unionId) {
        Map<String, Object> dataMap = new HashedMap();
//        String tokenUrl = this.wxOpenIdUrl;
//        tokenUrl += "&appid=" + this.payWxConfig.getAppletAppid() + "&secret=" + this.payWxConfig.getAppletAppSecret() + "&js_code=" + code;
//        HttpClient client = new DefaultHttpClient();
//        HttpGet get = new HttpGet(URI.create(tokenUrl));
        if (StringUtil.isEmpty(openId) || StringUtil.isEmpty(unionId)) {
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
        try {

            //判断shareCode 是否为空
            //进入到注册阶段
            QztUser user = qztUserService.findUserById(openId, unionId);
            long userId = 0;
            if (user == null) {
                QztUser qztUser = new QztUser();
                qztUser.setWxOpenId(openId);
                qztUser.setReferrerCode(unionId);
                qztUser.setUserType(0);
                qztUser.setState("0");
                qztUser.setRegSource("3");
                qztUser.setCreateTime(new Date());
                if (!StringUtil.isEmpty(shareCode)) {
                    //直接保存用户
//                    QztUser shareUser = qztUserService.findUserById(shareCode);
                    UserInfo userInfo = userInfoService.findById(Integer.valueOf(shareCode));
                    if (userInfo != null && userInfo.getuStatus() >= 0) {
                        qztUser.setReferrerFirst(userInfo.getuShareCode());
                        qztUser.setReferrerSecond(userInfo.getuName());
                        qztUser.setPid(userInfo.getPid());
                        qztUser.setPhShortName(userInfo.getPhShortName());
                    }
                }

                if (shareUserId != null) {
                    QztUser shareUser = qztUserService.findDGUserById(shareUserId);
                    if (shareUser != null) {
                        qztUser.setInvitationOne(shareUserId + "");
                    }
                }
                //保存用户信息
                userId = qztUserService.saveUserAndAccount(qztUser);
                //新人赠送券
                iDgmCouponService.creatNewUserCoupon(userId);
                //赠送折扣券
                iDgmCouponService.creatUserCoupon(userId);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    //redisTemplate.
                    String accessToken = "";
                    if (CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token") != null) {
                        accessToken = CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token").toString();
                    } else {
                        accessToken = this.appletOfWeChatUtil.getAccessToken();
                        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token", accessToken, 60 * 60);
                    }
//                    Map unlimited = this.appletOfWeChatUtil.getUnlimited(accessToken, userId + "", "pages/index/index", 500);
//                    BufferedImage img = (BufferedImage) unlimited.get("img"); //小程序二维码图片
//                    ImageIO.write(img, "png", out);
//                    Map map = QiniuUtil.qiniuObjectStorage(out.toByteArray(), userId + "" + new Date().getTime() + ".png"); //上传至七牛空间
//                    out.close();
//                    Map<String, Object> updateMap = new HashMap<>();
//                    updateMap.put("id", userId);
//                    updateMap.put("invitationOne", map.get("picUrl").toString());
//                    this.qztUserService.updateUserById(updateMap);
                    // return returnUtil.returnMessMap(unlimited);
                } catch (Exception e) {
                    e.printStackTrace();
                    return returnUtil.returnMess(Constant.DATA_ERROR);
                } finally {
                    out.close();
                }

            } else {
                userId = user.getUserId();
//                if ((user.getReferrerFirst() == null || "".equals(user.getReferrerFirst())) && !StringUtil.isEmpty(shareCode)) {//一期规则
                if ((user.getReferrerFirst() == null || !shareCode.equals(user.getReferrerFirst())) && !StringUtil.isEmpty(shareCode)) {//二期规则
                    //更新用户推荐人
                    UserInfo userInfo = userInfoService.findById(Integer.valueOf(shareCode));
                    if (userInfo != null && userInfo.getuStatus() >= 0) {
                        user.setReferrerFirst(userInfo.getuShareCode());
                        user.setReferrerSecond(userInfo.getuName());
                        Map dataMap1 = new HashMap();
                        dataMap1.put("referId", userInfo.getuShareCode());
                        dataMap1.put("referName", userInfo.getuName());
                        dataMap1.put("pid", userInfo.getPid());
                        dataMap1.put("phShortName", userInfo.getPhShortName());
                        dataMap1.put("userId", userId);
                        dataMap1.put("conType", 3);
                        this.qztUserService.updateUserMess(dataMap1);
                    }
                }

                if (shareUserId != null && (user.getInvitationOne() == null || !shareUserId.equals(user.getInvitationOne())) && !String.valueOf(userId).equals(shareUserId)) {
                    QztUser shareUser = qztUserService.findDGUserById(Long.valueOf(shareUserId));
                    if (shareUser != null) {
                        user.setInvitationOne(shareUserId+"");
                        Map dataMap1 = new HashMap();
                        dataMap1.put("invitationOne", shareUserId);
                        dataMap1.put("userId", userId);
                        dataMap1.put("conType1", 3);
                        this.qztUserService.updateUserMess(dataMap1);
                    }
                }
            }
            //进行登录释放tokenId
            String tokenId = RandCodeUtil.getRandomStrTokenId(6, userId + "");
            dataMap.put("tokenId", tokenId);

            dataMap.put("userData", qztUserService.findUserById(openId, unionId));
            CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.MOBLIECODE.value() + tokenId, userId + "", 60 * 60 * 24 * 30);
//            //发送MQ
//            new MqMess(MqConstant.SHARE_MONEY_TOP, userId + "").sendTop();
            return returnUtil.returnMessMap(dataMap);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

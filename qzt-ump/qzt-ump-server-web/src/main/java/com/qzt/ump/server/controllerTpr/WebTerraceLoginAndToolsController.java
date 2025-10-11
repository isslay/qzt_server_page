package com.qzt.ump.server.controllerTpr;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.pay.util.AppletOfWeChatUtil;
import com.qzt.common.pay.util.WeChatTools;
import com.qzt.common.pay.util.WxJsApiConfig;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Api(value = "WebTerraceLoginAndToolsController", description = "第三方登录以及功能api")
@Slf4j
@RestController
public class WebTerraceLoginAndToolsController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private WeChatTools wxtools;

    @Autowired
    private AppletOfWeChatUtil appletOfWeChatUtil;


    @ApiOperation(value = "微信Wap分享")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shareUrl", value = "分享路径", required = true, paramType = "query", dataType = "String")

    })

    @RequestMapping(value = "/pubapi/share", method = RequestMethod.POST)
    public WxJsApiConfig share(@RequestParam(value = "shareUrl") String shareUrl, String vno, String cno) {
        return wxtools.getWxJsApiConfig(shareUrl);
    }

    @ApiOperation(value = "获取用户信息（微信wap）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信获取access_token标示（五分钟失效）", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(H5、IOS、Android)", dataType = "String", required = true, paramType = "query")
    })
    @RequestMapping(value = "/pubapi/getUserInfo", method = RequestMethod.POST)
    public Map getUserInfo(@RequestParam(value = "code") String code, String vno, String cno) throws IOException {
        String json = this.wxtools.getUserInfo(code);
        JSONObject myJson = JSONObject.fromObject(json);
        return returnUtil.returnMessMap(myJson);
    }

    @ApiOperation(value = "wap微信第三方登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信获取access_token标示（五分钟失效）ios和安卓传不用传", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "terraceType", value = "0wap端1微信2qq3微博", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "openid", value = "ios和安卓传", required = true, paramType = "query", dataType = "String")
    })
    @RequestMapping(value = "/pubapi/WxTerraceLogin", method = RequestMethod.POST)
    public Map WxTerraceLogin(@RequestParam(value = "code") String code, @RequestParam(value = "terraceType") String terraceType, String openid, String vno, String cno) throws IOException {
        try {
            Map map = new HashMap();
            if (terraceType.equals("0")) {
                String json = wxtools.getUserInfo(code);
                System.out.println(json);
                JSONObject myJson = JSONObject.fromObject(json);
                if (myJson.get("unionid") == null) {
                    return returnUtil.returnMess(Constant.DATA_ERROR);
                }
                openid = myJson.get("unionid").toString();
            }
            map.put("openid", openid);
            //type  查询openid的类型   1  微信  2 qq  3 微博
            map.put("openidType", terraceType);
            /*List<klygUser> klygUserList = klygUserService.findOpenid(map);
            map.clear();
            if (klygUserList.size() > 0) {
                klygUser zp = klygUserList.get(0);
                String tokenId = RandCodeUtil.getRandomStrTokenId(6);
                map.put("tokenId", tokenId);
                map.put("id", zp.getUserId());
                map.put("mobile", zp.getMobile());
                map.put("openid", openid);
                map.put("isBindingMobile", "1");
                map.put("userCode", zp.getIdCard() == null ? null : "klyg********klyg");

                CacheUtil.getCache().set(Constants.CacheNamespaceEnum.MOBLIECODE.value() + tokenId, zp.getUserId() + "", 60 * 60 * 24 * 30);
                Object tok = CacheUtil.getCache().get("klyg:mobile:captcha:" + zp.getMobile());
                if (tok != null || tok != "") {
                    CacheUtil.getCache().set("klyg:mobile:captcha:" + zp.getMobile(), tok + "_" + Constants.CacheNamespaceEnum.MOBLIECODE.value() + tokenId);
                } else {
                    CacheUtil.getCache().set("klyg:mobile:captcha:" + zp.getMobile(), Constants.CacheNamespaceEnum.MOBLIECODE.value() + tokenId);
                }
            } else {
                map.put("openid", openid);
                map.put("isBindingMobile", "0");
            }*/
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    @ApiOperation(value = "更新头像")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imgUrl", value = "图片url", required = true, paramType = "query", dataType = "String")

    })
    @RequestMapping(value = "/webapi/updateImgUrl", method = RequestMethod.POST)
    public Map getUserInfo(@RequestParam(value = "imgUrl") String imgUrl, String tokenId, Long userId, String vno, String cno) throws IOException {
        String tokenUserId = (String) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.MOBLIECODE.value() + tokenId);
        if (!tokenUserId.equals(userId.toString())) {
            return returnUtil.returnMess(Constant.LOGIN_OUT);
        }
      /*  klygUser klygUser = new klygUser();
        klygUser.setUserId(userId);
        klygUser.setWxHeadImage(imgUrl);
        this.klygUserService.modifyByIdNew(klygUser);*/
        return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
    }


    @ApiOperation(value = "获取小程序openid", notes = "获取小程序openid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "code", value = "code", required = true, paramType = "query", dataType = "String")
    })
    @GetMapping("/pubapi/getAppletOpenId")
    public Map getAppletOpenId(String vno, String cno, String code) {
        try {
            JSONObject appletOpenId = this.appletOfWeChatUtil.getAppletOpenId(code);
            return returnUtil.returnMessMap(appletOpenId);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "获取小程序二维码", notes = "获取小程序二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scene", value = "二维码链接参数等json", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "page", value = "二维码链接参数等json", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "width", value = "二维码链接参数等json", dataType = "Integer", required = true, paramType = "query")
    })
    //详细使用方法及参数 https://developers.weixin.qq.com/miniprogram/dev/api-backend/wxacode.getUnlimited.html
    @PostMapping("/pubapi/getUnlimited")
    public Map getUnlimited(String scene, String page, Integer width) {
        try {
            String accessToken = this.appletOfWeChatUtil.getAccessToken();
            Map unlimited = this.appletOfWeChatUtil.getUnlimited(accessToken, scene, page, width);

            //log.info("+++++++++++++************+++++++++++++" + unlimited);
            return returnUtil.returnMessMap(unlimited);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


}

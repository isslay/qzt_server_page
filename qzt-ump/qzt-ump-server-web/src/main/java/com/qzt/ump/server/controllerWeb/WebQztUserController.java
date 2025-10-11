package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.UserInfo;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.pay.util.AppletOfWeChatUtil;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztUserController", description = "WebQztUserController")
public class WebQztUserController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztUserService service;

    @Autowired
    private IQztAccountService qztAccountService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    @Autowired
    private AppletOfWeChatUtil appletOfWeChatUtil;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private IDgmCouponService iDgmCouponService;

    @Autowired
    private IDgmIntegralRecordService iDgmIntegralRecordService;

    @Autowired
    private IDgmUsableIntegralService iDgmUsableIntegralService;

    /**
     * 分页查询
     *
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUser/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel();
            pageModel.setCurrent(pageNum);
            pageModel.setSize(pageSize);
            conditionMap.put("referrerFirst", userId);
            pageModel.setCondition(conditionMap);
            pageModel = (PageModel) service.find(pageModel);

            List<QztUser> records = pageModel.getRecords();
            List rsData = new ArrayList();
            for (QztUser qztUser : records) {
               /* Map <String,Object> dataMap = new HashMap<>();
                dataMap.put("obj",qztAccountLog);
                dataMap.put("changeMoney", PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(qztAccountLog.getChangeNum().toString())));*/
                qztUser.setWxHeadImage(StringUtil.isEmpty(qztUser.getWxHeadImage()) ? "" : qztUser.getWxHeadImage());
                qztUser.setWxNickName(StringUtil.isEmpty(qztUser.getWxNickName()) ? "无" : new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8"));
                rsData.add(qztUser);
            }
            returnMap.put("data", rsData);
            returnMap.put("current", pageModel.getCurrent());
            returnMap.put("size", pageModel.getSize());
            returnMap.put("total", pageModel.getTotal());
            Map backData = new HashMap();
            backData.put("pageData", returnMap);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    @ApiOperation(value = "查询服务站分页查询", notes = "查询服务站分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUser/getServiceListPage")
    public Map<String, Object> getServiceListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel();
            pageModel.setCurrent(pageNum);
            pageModel.setSize(pageSize);
            conditionMap.put("referrerSecond", userId);
            conditionMap.put("userType", 0);
            pageModel.setCondition(conditionMap);
            pageModel = (PageModel) service.find(pageModel);
            List<QztUser> records = pageModel.getRecords();
            List rsData = new ArrayList();
            for (QztUser qztUser : records) {
               /* Map <String,Object> dataMap = new HashMap<>();
                dataMap.put("obj",qztAccountLog);
                dataMap.put("changeMoney", PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(qztAccountLog.getChangeNum().toString())));*/
                qztUser.setState(DicParamUtil.getDicCodeByType("SERVICE_TYPE", qztUser.getUserType().toString()));
                qztUser.setWxHeadImage(StringUtil.isEmpty(qztUser.getWxHeadImage()) ? "" : qztUser.getWxHeadImage());
                qztUser.setWxNickName(StringUtil.isEmpty(qztUser.getWxNickName()) ? "无" : new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8"));
                rsData.add(qztUser);
            }
            returnMap.put("data", rsData);
            returnMap.put("current", pageModel.getCurrent());
            returnMap.put("size", pageModel.getSize());
            returnMap.put("total", pageModel.getTotal());
            Map backData = new HashMap();
            backData.put("pageData", returnMap);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据主键id查询详情
     *
     * @param id
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUser/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            QztUser entity = this.service.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "获得用户的基本信息", notes = "获得用户的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "colType", value = "操作类型（当传值表示需要查询晋级情况）", dataType = "String", required = false, paramType = "query"),
    })
    @PostMapping("/webapi/qztUser/queryUserAccount")
    public Map<String, Object> queryUserAccount(String vno, String cno, String tokenId, Long userId, String colType) {
        try {
            Map dataMap = new HashMap();
//            QztUser qztUser = service.findUserById(userId,"");
            QztUser qztUser = service.findDGUserById(userId);
//            QztAccount qztAccount = qztAccountService.selectByUserId(Long.parseLong(userId));
            qztUser.setWxHeadImage(StringUtil.isEmpty(qztUser.getWxHeadImage()) ? "" : qztUser.getWxHeadImage());
            qztUser.setWxNickName(StringUtil.isEmpty(qztUser.getWxNickName()) ? "" : new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8"));
            dataMap.put("userBaseData", qztUser);
//            Map accountMap = new HashMap();
//            accountMap.put("accountMoney", PriceUtil.exactlyTwoDecimalPlaces(qztAccount.getAccountMoney() == null ? 0 : qztAccount.getAccountMoney()));//账户余额
            //查询分享佣金 和 推广佣金
//            accountMap.put("shareMoney", qztAccountRelogService.findAccountById(Long.parseLong(userId)));
            dataMap.put("userName", StringUtil.isEmpty(qztUser.getWxNickName()) ? "" : qztUser.getWxNickName());
//            dataMap.put("userTypeMc", DicParamUtil.getDicCodeByType("SERVICE_TYPE", qztUser.getUserType().toString()));
//            if (colType != null) {
//                //查询出用户下一级别
//                String nextType = DicParamUtil.getDicCodeByType("SERVICE_TYPE", (qztUser.getUserType() + 10) + "");
//                if (nextType == null) {
//                    nextType = "";
//                }
//                dataMap.put("nextType", nextType);
//                Map conditionMap = new HashMap();
//                conditionMap.put("referrerSecond", userId);
//                conditionMap.put("parseType", qztUser.getUserType());
//                Long parseCount = service.findCount(conditionMap);
//                dataMap.put("parseCount", parseCount);
//                if (!nextType.equals("")) {
//                    dataMap.put("parseMessage", "再邀请" + (10 - parseCount) + "位" + dataMap.get("userTypeMc").toString() + ",升到" + nextType);
//                } else {
//                    dataMap.put("parseMessage", "您已成功邀请" + parseCount + "位" + dataMap.get("userTypeMc").toString());
//                }
//            }
//            dataMap.put("accountData", accountMap);
//            dataMap.put("showCode", true);

//            if (qztUser.getInvitationOne() == null) {
//
//                ByteArrayOutputStream out = new ByteArrayOutputStream();
//                try {
//                    //redisTemplate.
//                    String accessToken = "";
//                    if (CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token") != null) {
//                        accessToken = CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token").toString();
//                    } else {
//                        accessToken = this.appletOfWeChatUtil.getAccessToken();
//                        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.ACCESSTOKEN.value() + "token", accessToken, 60 * 60);
//                    }
////                    Map unlimited = this.appletOfWeChatUtil.getUnlimited(accessToken, userId + "", "pages/index/index", 500);
////                    BufferedImage img = (BufferedImage) unlimited.get("img"); //小程序二维码图片
////                    ImageIO.write(img, "png", out);
////                    Map map = QiniuUtil.qiniuObjectStorage(out.toByteArray(), userId + "" + new Date().getTime() + ".png"); //上传至七牛空间
//                    out.close();
////                    Map<String, Object> updateMap = new HashMap<>();
////                    updateMap.put("id", userId);
//////                    updateMap.put("invitationOne", map.get("picUrl").toString());
////                    this.service.updateUserById(updateMap);
//                    // return returnUtil.returnMessMap(unlimited);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return returnUtil.returnMess(Constant.DATA_ERROR);
//                } finally {
//                    out.close();
//                }
//            }
            //每日添加优惠券 2023-11-03取消
            //dataMap.put("newCoupon",iDgmCouponService.creatUserCoupon(userId));
            //添加节日优惠券
            dataMap.put("newCoupon", iDgmCouponService.creatActivityCoupon(userId));

            return returnUtil.returnMessMap(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "修改用户的基本信息，头像，名称 和 手机号等", notes = "修改用户的基本信息，头像，名称 和 手机号等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "nickName", value = "用户名称", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "imgUrl", value = "用户头像", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "mobile", value = "用户手机号", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oldMobile", value = "用户老手机号", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "referId", value = "员工码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "shareUserId", value = "分享人id", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oldPayPwd", value = "老支付密码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "payPwd", value = "新支付密码", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "conType", value = "操作类型(1 修改基本信息(昵称) 2 修改手机号  3 修改员工码&推荐人 4修改头像 5真实姓名(修改支付密码) 6设置支付密码)", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/webapi/qztUser/updateUserMess")
    public Map<String, Object> updateUserMess(String vno, String cno, String tokenId, Long userId, String nickName, String imgUrl, String mobile, String oldMobile, String realName, String referId, Long shareUserId, String payPwd, String oldPayPwd, int conType) {
        try {
            Map dataMap = new HashMap();
            if (conType == 1) {
                try { //微信名带表情图,需要进行 编码 将二进制数据编码为可写的字符型数据
                    dataMap.put("nickName", Base64.getEncoder().encodeToString(nickName.getBytes("UTF-8")));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                dataMap.put("imgUrl", imgUrl);
            }
            if (conType == 2) {
                //校验手机号是否已经存在
                if (mobile == null || "".equals(mobile)) {
                    return returnUtil.returnMess("201", "请输入手机号码！");
                }
//                QztUser qztUser = this.service.findUserByMobile(mobile);
//                if (qztUser != null) {
//                    //提示手机号已存在
//                    return returnUtil.returnMess(Constant.MOBILE_REGISTER);
//                }
//                if (mobile.equals(oldMobile)) {
//                    return returnUtil.returnMess(Constant.DATA_ERROR);
//                }
//                dataMap.put("mobile", mobile);
//                dataMap.put("oldMobile", oldMobile);
                QztUser qztUser = this.service.findDGUserById(userId);
                if (qztUser.getRealName() != null) {
                    QztUser qztUser1 = this.service.findUserByMobile(mobile, qztUser.getRealName(), userId);
                    if (qztUser1 != null) {
                        return returnUtil.returnMess("201", "该手机号与真实姓名已重复！");
                    }
                }
                dataMap.put("mobile", mobile);
            }
            if (conType == 3) {
                //校验推荐人是否存在 和 注册时间
//                QztUser qztUser = this.service.findUserById(userId.toString());
//                if (qztUser.getReferrerFirst() != null) {
//                    //已存在推荐人
//                    return returnUtil.returnMess(Constant.HAVE_REF);
//                }
//                QztUser qztUserRef = this.service.findUserById(referId);
//                if (qztUserRef == null || qztUserRef.getUserId().doubleValue() == userId.doubleValue() || (qztUserRef.getCreateTime().getTime() - qztUser.getCreateTime().getTime() > 0)) {
//                    //推荐人信息不对
//                    return returnUtil.returnMess(Constant.CHANGE_REF_ERROR);
//                }
                if (!StringUtil.isEmpty(referId)) {
                    UserInfo userInfo = userInfoService.findById(Integer.valueOf(referId));
                    if (userInfo != null && userInfo.getuStatus() >= 0) {
                        dataMap.put("referId", userInfo.getuShareCode());
                        dataMap.put("referName", userInfo.getuName());
                        dataMap.put("pid", userInfo.getPid());
                        dataMap.put("phShortName", userInfo.getPhShortName());
                    } else {
                        return returnUtil.returnMess("201", "查无此员工信息，请核对！");
                    }
                } else {
                    return returnUtil.returnMess("201", "员工推荐码为空！");
                }

                if (shareUserId != null && userId != shareUserId) {
                    QztUser shareUser = service.findDGUserById(shareUserId);
                    if (shareUser != null) {
                        dataMap.put("conType1", conType);
                        dataMap.put("invitationOne", shareUserId);
                    }
                }
            }
            if (conType == 4) {
                dataMap.put("imgUrl", imgUrl);
            }
            if (conType == 5) {//真实姓名
                if (realName == null || "".equals(realName)) {
                    return returnUtil.returnMess("201", "请输入真实姓名！");
                }
                QztUser qztUser = this.service.findDGUserById(userId);
                if (qztUser.getMobile() != null) {
                    QztUser qztUser1 = this.service.findUserByMobile(qztUser.getMobile(), realName, userId);
                    if (qztUser1 != null) {
                        return returnUtil.returnMess("201", "该真实姓名与手机号已重复！");
                    }
                }
                dataMap.put("realName", realName);
            }
//            if (conType == 5) {
//                dataMap.put("payPwd", SecureUtil.md5(payPwd));
//                dataMap.put("oldPayPwd", SecureUtil.md5(oldPayPwd));
//            }
//            if (conType == 6) {
//                dataMap.put("payPwd", SecureUtil.md5(payPwd));
//            }
            dataMap.put("userId", userId);
            dataMap.put("conType", conType);
            if (this.service.updateUserMess(dataMap) > 0) {
                return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            }
            return returnUtil.returnMess(Constant.DATA_ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "修改用户的基本信息，头像，名称 和 手机号等", notes = "修改用户的基本信息，头像，名称 和 手机号等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/pubapi/qztUser/updateUserMess")
    public Map<String, Object> updateUserMesstest(int userId) {
        System.out.println("=========");
        iDgmIntegralRecordService.creatNew(userId, 1, "测试", 100);
        return null;
    }

    @ApiOperation(value = "修改用户的基本信息，头像，名称 和 手机号等", notes = "修改用户的基本信息，头像，名称 和 手机号等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/pubapi/qztUser/updateUserMess1")
    public Map<String, Object> updateUserMesstest1(int userId, int value) {
        System.out.println("=========");
        int i = iDgmIntegralRecordService.consumeNew(userId, 21, "消费了", value);
        if (i == 1) {
            return returnUtil.returnMess("201", "积分不足！");
        }
        return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
    }

    @ApiOperation(value = "修改用户的基本信息，头像，名称 和 手机号等", notes = "修改用户的基本信息，头像，名称 和 手机号等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/pubapi/qztUser/updateUserMess2")
    public Map<String, Object> updateUserMesstest2(int userId) {
        System.out.println("=========");
        iDgmIntegralRecordService.overNew();
        return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
    }

    @ApiOperation(value = "修改用户的基本信息，头像，名称 和 手机号等", notes = "修改用户的基本信息，头像，名称 和 手机号等")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "int", required = true, paramType = "query"),
    })
    @PostMapping("/pubapi/qztUser/updateUserMess3")
    public Map<String, Object> updateUserMesstest3(int userId) {

        int a = iDgmUsableIntegralService.sumAll(userId);
        System.out.println("==a:" + a);
        int b = iDgmUsableIntegralService.monthOver(userId);
        System.out.println("==b:" + b);
        return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
    }

}


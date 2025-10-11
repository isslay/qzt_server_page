package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.DateUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.bus.rpc.api.IQztUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;

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
@Api(value = "WebQztUserCouponController", description = "WebQztUserCouponController")
public class WebQztUserCouponController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztUserCouponService service;

    @Autowired
    private IQztUserService qztUserService;


    public static final Map<String, String> INIT_MAP = new HashMap<String, String>();

    static {
        INIT_MAP .put("01", "满减");
        INIT_MAP .put("02", "新手");
        INIT_MAP .put("03", "商品");
        INIT_MAP .put("04", "活动");
    }

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
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "giveUserId", value = "赠送人", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "state", value = "优惠券状态", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserCoupon/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId, Long giveUserId, String state) {
//        try {
//            Map returnMap = new HashMap();
//            Map conditionMap = new HashMap();
//            PageModel pageModel = new PageModel();
//            pageModel.setCurrent(pageNum);
//            pageModel.setSize(pageSize);
//            String[] imgAr = {"https://img.jlqizutang.com/dzsimg.png", "https://img.jlqizutang.com/dsyimg.png", "https://img.jlqizutang.com/ysyimg.png", "https://img.jlqizutang.com/ygqimg.png"};
//            if (state.equals("0")) {
//                conditionMap.put("giveUserId", giveUserId);
//            } else {
//                conditionMap.put("userId", userId);
//                conditionMap.put("state", state);
//            }
//            pageModel.setCondition(conditionMap);
//            pageModel = (PageModel) service.find(pageModel);
//            List<QztUserCoupon> records = pageModel.getRecords();
//            List rsData = new ArrayList();
//            for (QztUserCoupon qztUserCoupon : records) {
//                Map<String, Object> dataMap = new HashMap<>();
//                dataMap.put("obj", qztUserCoupon);
//                dataMap.put("couponMoney", Long.parseLong(qztUserCoupon.getCouponMoney().toString()) / 100);
//                dataMap.put("picUrl", imgAr[Integer.parseInt(qztUserCoupon.getState())]);
//                dataMap.put("startDate", DateTime.getCurDateTime(qztUserCoupon.getCreateTime(), "yyyy-MM-dd"));
//                dataMap.put("endDate", DateTime.getCurDateTime(qztUserCoupon.getOverTime(), "yyyy-MM-dd"));
//                rsData.add(dataMap);
//            }
//            returnMap.put("data", rsData);
//            returnMap.put("current", pageModel.getCurrent());
//            returnMap.put("size", pageModel.getSize());
//            returnMap.put("total", pageModel.getTotal());
//            Map backData = new HashMap();
//            backData.put("pageData", returnMap);
//            return returnUtil.returnMessMap(backData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
        return returnUtil.returnMess(Constant.DATA_ERROR);
    }

    /**全部查询
     * @return Map
     * @author
     * @date
     */
    @ApiOperation(value = "全部查询", notes = "全部查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserCoupon/getList")
    public Map<String, Object> getList(String vno, String cno, String tokenId, Long userId) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("userId", userId);
            conditionMap.put("state", "1");//待使用
            List<QztUserCoupon> records = this.service.findList(conditionMap);
            List rsData1 = new ArrayList();
            for (QztUserCoupon qztUserCoupon : records) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("targetMoney", qztUserCoupon.getTargetMoney() / 100);
                dataMap.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);
                dataMap.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getActiveTime()));
                dataMap.put("usedTime", qztUserCoupon.getUsedTime()==null?"":DateUtil.chineseDateFormat.format(qztUserCoupon.getUsedTime()));
                dataMap.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon.getOverTime()));
                dataMap.put("id",qztUserCoupon.getId());
                dataMap.put("couponRemark",qztUserCoupon.getCouponRemark());
                dataMap.put("couponType",qztUserCoupon.getGoodsType());
                dataMap.put("couponTypeStr",INIT_MAP.get(qztUserCoupon.getGoodsType()));
                rsData1.add(dataMap);
            }
            conditionMap.put("state", "2");//已使用
            List<QztUserCoupon> records2 = this.service.findList(conditionMap);
            List rsData2 = new ArrayList();
            for (QztUserCoupon qztUserCoupon2 : records2) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("targetMoney", qztUserCoupon2.getTargetMoney() / 100);
                dataMap.put("couponMoney", qztUserCoupon2.getCouponMoney() / 100);
                dataMap.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon2.getActiveTime()));
                dataMap.put("usedTime", qztUserCoupon2.getUsedTime()==null?"":DateUtil.chineseDateFormat.format(qztUserCoupon2.getUsedTime()));
                dataMap.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon2.getOverTime()));
                dataMap.put("id",qztUserCoupon2.getId());
                dataMap.put("couponRemark",qztUserCoupon2.getCouponRemark());
                dataMap.put("couponType",qztUserCoupon2.getGoodsType());
                dataMap.put("couponTypeStr",INIT_MAP.get(qztUserCoupon2.getGoodsType()));
                rsData2.add(dataMap);
            }
            conditionMap.put("state", "3");//已过期
            List<QztUserCoupon> records3 = this.service.findList(conditionMap);
            List rsData3 = new ArrayList();
            for (QztUserCoupon qztUserCoupon3 : records3) {
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("targetMoney", qztUserCoupon3.getTargetMoney() / 100);
                dataMap.put("couponMoney", qztUserCoupon3.getCouponMoney() / 100);
                dataMap.put("activeTime", DateUtil.chineseDateFormat.format(qztUserCoupon3.getActiveTime()));
                dataMap.put("usedTime", qztUserCoupon3.getUsedTime()==null?"":DateUtil.chineseDateFormat.format(qztUserCoupon3.getUsedTime()));
                dataMap.put("overTime", DateUtil.chineseDateFormat.format(qztUserCoupon3.getOverTime()));
                dataMap.put("id",qztUserCoupon3.getId());
                dataMap.put("couponRemark",qztUserCoupon3.getCouponRemark());
                dataMap.put("couponType",qztUserCoupon3.getGoodsType());
                dataMap.put("couponTypeStr",INIT_MAP.get(qztUserCoupon3.getGoodsType()));
                rsData3.add(dataMap);
            }
            returnMap.put("usedCoupon", rsData1);
            returnMap.put("overCoupon", rsData2);
            returnMap.put("passCoupon", rsData3);
            return returnUtil.returnMessMap(returnMap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据主键id查询详情
     *
     * @param
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserCoupon/shareBaseMess")
    public Map<String, Object> shareBaseMess(String vno, String cno, String tokenId, Long userId) {
//        try {
//            Map<String, Object> parData = new HashMap<>();
//            parData.put("shareMess", "送你5元优惠券，满百减五，十足抵用。");
//            parData.put("shareUrl", "/pages/lingquyhq/lingquyhq?couponId=");
//            QztUser qztUser = this.qztUserService.findDGUserById(userId);
//            if (qztUser.getWxNickName() != null) {
//                parData.put("shareMess", new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8") + "送你5元优惠券，满百减五，十足抵用。");
//            }
//            parData.put("sharePicUrl", "https://img.jlqizutang.com/share191119.jpg");
//
//            Map<String, Object> backData = new HashMap<>();
//            backData.put("backData", parData);
//            return returnUtil.returnMess(backData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
        return returnUtil.returnMess(Constant.DATA_ERROR);
    }


    /**
     * 创建优惠券
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserCoupon/createCoupone")
    public Map<String, Object> createCoupone(String vno, String cno, String tokenId, Long userId) {
//        Map<String, Object> backData = new HashMap<>();
//        try {
//            //生成优惠券
//            Map<String, Object> params = new HashMap<>();
//            params.put("userId", userId);
//            params.put("reType", 0);//推广佣金
//            params.put("price", 500);
//            String rs = this.service.saveUserCoupon(params);
//            if (rs == "0001") {
//                backData.put("message", "余额不足");
//            }
//            QztUser qztUser = this.qztUserService.findDGUserById(userId);
//            backData.put("rs", rs);
//            backData.put("shareUrl", "/pages/lingquyhq/lingquyhq?couponId=" + rs);
//            backData.put("shareMess", "送你5元优惠券，满百减五，十足抵用。");
//            if (qztUser.getWxNickName() != null) {
//                backData.put("shareMess", new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8") + "送你5元优惠券，满百减五，十足抵用。");
//            }
//            backData.put("sharePicUrl", "https://img.jlqizutang.com/share191119.jpg");
//            return returnUtil.returnMess(backData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
        return returnUtil.returnMess(Constant.DATA_ERROR);
    }

    /**
     * 创建优惠券
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "conId", value = "券ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserCoupon/activeCoupon")
    public Map<String, Object> activeCoupon(String vno, String cno, String tokenId, Long userId, String conId) {
//        try {
//            //激活优惠券
//            Map<String, Object> params = new HashMap<>();
//            QztUserCoupon entity = this.service.selectById(conId);
//            if (entity == null) {
//                return returnUtil.returnMess(Constant.DATA_ERROR);
//            }
//            if (entity.getGiveUserId().doubleValue() == userId.doubleValue()) {
//                //提示 自己不能激活自己分享的优惠券
//                return returnUtil.returnMess(Constant.COUPON_ERROR);
//            }
//            if (!entity.getState().equals("0")) {
//                //提示 对不起券已激活
//                return returnUtil.returnMess(Constant.HAVE_AVTIVE_COUPON);
//            }
//            params.put("activeDate", new Date());
//            params.put("userId", userId);
//            params.put("conId", conId);
//            boolean rs = this.service.activeCoupon(params);
//            if (!rs) {
//                return returnUtil.returnMess(Constant.DATA_ERROR);
//            }
//            QztUser qztUser = this.qztUserService.findDGUserById(entity.getGiveUserId());
//            Map<String, Object> backData = new HashMap<>();
//            backData.put("qztUserCoupon", entity);
//            backData.put("price", entity.getCouponMoney() / 100);
//            if (qztUser.getWxNickName() == null) {
//                backData.put("userName", "");
//            } else {
//                backData.put("userName", new String(Base64.getDecoder().decode(qztUser.getWxNickName()), "UTF-8"));
//            }
//            return returnUtil.returnMess(backData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
        return returnUtil.returnMess(Constant.DATA_ERROR);
    }
}


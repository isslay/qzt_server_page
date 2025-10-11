package com.qzt.ump.server.controllerWeb;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qzt.bus.model.QztGoods;
import com.qzt.bus.model.QztUserAddress;
import com.qzt.bus.model.QztUserCoupon;
import com.qzt.bus.rpc.api.*;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztStoGoods;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-12-24
 */
@RestController
@Api(value = "WebQztStoGoodsController", description = "购物车商品管理Api")
public class WebQztStoGoodsController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztStoGoodsService qztStoGoodsService;

    @Autowired
    private IQztGoodsService qztGoodsService;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private IQztUserCouponService qztUserCouponService;

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 添加商品到购物车
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2019-12-24
     */
    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "buyNum", value = "购买数量", dataType = "Integer", required = true, paramType = "query")
    })
    @PutMapping("/webapi/qztStoGoods/addGoods")
    public Map<String, Object> addGoods(String vno, String cno, String tokenId, Long userId, Long goodsId, Integer buyNum) {
        try {
            QztGoods qztGoods = this.qztGoodsService.queryById(goodsId);
            if (qztGoods == null) {
                return returnUtil.returnMess(Constant.GOODS_NOTHINGNESS);
            }
//            boolean result = this.qztStoGoodsService.addGoods(new QztStoGoods(userId, goodsId, buyNum));
//            if (!result) {
//                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "添加到购物车失败");
//            }
//            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
            QztStoGoods goods = this.qztStoGoodsService.addGoods1(new QztStoGoods(userId, goodsId, buyNum));
            if (goods == null) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "添加到购物车失败");
            }
            return returnUtil.returnMess(goods.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 删除购物车商品
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2019-12-24
     */
    @ApiOperation(value = "删除购物车商品", notes = "删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "stoGoodsId", value = "商品id", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztStoGoods/delGoods")
    public Map<String, Object> delGoods(String vno, String cno, String tokenId, Long userId, Long stoGoodsId) {
        try {
            QztStoGoods qztStoGoodss = new QztStoGoods();
            qztStoGoodss.setId(stoGoodsId);
            qztStoGoodss.setIsDel("Y");
            QztStoGoods qztStoGoods = this.qztStoGoodsService.modifyById(qztStoGoodss);
            if (qztStoGoods == null) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "删除失败");
            }
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "查询购物车商品", notes = "查询购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
    })
    @GetMapping("/webapi/qztStoGoods/getStoGoodsList")
    public Map<String, Object> getStoGoodsList(String vno, String cno, String tokenId, Long userId) {
        try {
            Map pmap = new HashMap();
            pmap.put("userId", userId);
            pmap.put("isDel", "N");
            pmap.put("isCart", "Y");
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.findList(pmap);
            List<Map> relist = new ArrayList<>();
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                QztGoods qztGoods = this.qztGoodsService.queryById(qztStoGoods.getGoodsId());
                if (qztGoods == null || qztGoods.getGoodsNum() == null || qztGoods.getGoodsNum() < 1) {
                    continue;
                }
                Map map = new HashMap();
                map.put("goodsId", qztStoGoods.getGoodsId());
                map.put("stoGoodsId", qztStoGoods.getId());
                map.put("buyNum", qztStoGoods.getBuyNum());
                map.put("isPitchon", qztStoGoods.getIsPitchon());
                map.put("goodsName", qztGoods.getGoodsName());
                map.put("goodsPic", qztGoods.getThumbnail());
                map.put("goodsRemark", qztGoods.getGoodsRemark());
                map.put("goodsPriceOld", PriceUtil.exactlyTwoDecimalPlaces(qztGoods.getGoodsPrice()));
                map.put("goodsPrice", qztGoods.getGoodsPrice());
                map.put("goodsSpec", qztGoods.getGoodsSpec());
                relist.add(map);
            }
//            if(relist.size()==0){
//                Map map = new HashMap();
//                relist.add(map);
//            }
            return returnUtil.returnMess(relist);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    /**
     * 批量更新购物车商品信息
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2019-12-28
     */
    @ApiOperation(value = "批量更新购物车商品信息", notes = "批量更新购物车商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
    })
    @PostMapping("/webapi/qztStoGoods/updateStoGoodsList")
    public Map<String, Object> updateStoGoodsList(String vno, String cno, String tokenId, Long userId, @RequestBody JSONObject jsonParam) {
        try {
            JSONArray stoGoodsJsonArr = JSONObject.parseArray(jsonParam.get("stoGoodsList").toString());
            String uId = jsonParam.get("userId").toString();
            List<QztStoGoods> stoGoodsList = new ArrayList<>();
            for (int i = 0; i < stoGoodsJsonArr.size(); i++) {
                Map maps = (Map) stoGoodsJsonArr.get(i);
                stoGoodsList.add(new QztStoGoods(Long.valueOf(maps.get("stoGoodsId").toString()), Long.valueOf(maps.get("goodsId").toString()), Integer.valueOf(maps.get("buyNum").toString()), maps.get("isPitchon").toString()));
            }
            if (stoGoodsList.size() > 0) {
                this.qztStoGoodsService.updateStoGoodsList1(Long.valueOf(uId));
            }
            this.qztStoGoodsService.updateStoGoodsList(stoGoodsList);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    /**
     * 获取结算页信息
     *
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author
     * @date
     */
    @ApiOperation(value = "获取结算页信息", notes = "获取结算页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
    })
    @GetMapping("/webapi/qztStoGoods/getSettlementPageInfo")
    public Map<String, Object> getSettlementPageInfo(String vno, String cno, String tokenId, Long userId) {
        try {
            Map remap = new HashMap();
            List<QztStoGoods> qztStoGoodsList = this.qztStoGoodsService.getSettlementGoodsList(userId, "N", null);
            if (qztStoGoodsList == null || qztStoGoodsList.size() == 0) {
                return returnUtil.returnMess(Constant.GOODS_NOTHINGNESS, "请添加商品到购物车在进行结算");
            }
            Long totalAmountGoods = 0L;//商品总金额
            Long grossFreight = 0L;//运费总金额
            String goodsIds = "";//商品id集合

            List<Map> stoGoodsList = new ArrayList<>();
            for (QztStoGoods qztStoGoods : qztStoGoodsList) {
                QztGoods qztGoods = this.qztGoodsService.queryById(qztStoGoods.getGoodsId());
                if (qztGoods == null || qztGoods.getGoodsNum() == null || qztGoods.getGoodsNum() < 1) {
                    continue;
                }
                Long goodsPrice = qztGoods.getGoodsPrice() == null ? 0 : qztGoods.getGoodsPrice();
//                Long freight = qztGoods.getFreight() == null ? 0 : qztGoods.getFreight();
                totalAmountGoods += goodsPrice * qztStoGoods.getBuyNum();
//                grossFreight += freight * qztStoGoods.getBuyNum();

                Map map = new HashMap();
                map.put("buyNum", qztStoGoods.getBuyNum());
                map.put("goodsName", qztGoods.getGoodsName());
                map.put("goodsSpec", qztGoods.getGoodsSpec());
                map.put("goodsPic", qztGoods.getThumbnail());
                map.put("goodsRemark", qztGoods.getGoodsRemark());
                map.put("goodsPrice", PriceUtil.exactlyTwoDecimalPlaces(goodsPrice));
                stoGoodsList.add(map);

                goodsIds = goodsIds + qztStoGoods.getGoodsId()+",";
            }
            remap.put("totalAmountGoods", PriceUtil.exactlyTwoDecimalPlaces(totalAmountGoods));//商品总金额
            remap.put("grossFreight", PriceUtil.exactlyTwoDecimalPlaces(grossFreight));//运费总金额
            List<QztUserCoupon> listNew = this.qztUserCouponService.queryStorePromotion(userId);
            if (listNew.size() > 0) {
                QztUserCoupon qztUserCoupon = listNew.get(0);
                Map maps = new HashMap();
                maps.put("name","新用户优惠券");
                maps.put("couponId", qztUserCoupon.getId());//券ID
                maps.put("couponMoney", qztUserCoupon.getCouponMoney() / 100);//券金额
                maps.put("activeTime", qztUserCoupon.getActiveTime().getTime());//开始时间
                maps.put("overTime", qztUserCoupon.getOverTime().getTime());//结束时间

                remap.put("storePromotion",maps);//合计
                remap.put("amountInTotal", PriceUtil.exactlyTwoDecimalPlaces(totalAmountGoods + grossFreight - qztUserCoupon.getCouponMoney()));//合计
            } else {
                remap.put("storePromotion", null);
                remap.put("amountInTotal", PriceUtil.exactlyTwoDecimalPlaces(totalAmountGoods + grossFreight));//合计
            }

            remap.put("couponStatus", 0 == 0 ? "Y" : "N");//0开

            Map addressMess = new HashMap();
            Long addressId = null;
            String companyName = "";
            String consigneeTel = "";
            String consigneeAddress = "";
            QztUserAddress qztUserAddress = this.qztUserAddressService.selectDefaultaccAddress(userId);
            if (qztUserAddress != null) {
                addressId = qztUserAddress.getId();
                companyName = qztUserAddress.getRecipientName();
                consigneeTel = qztUserAddress.getPhone();
//                String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
                consigneeAddress = qztUserAddress.getAllAddress()+qztUserAddress.getDetailAddress();
            }
            addressMess.put("addressId", addressId);
            addressMess.put("companyName", companyName);
            addressMess.put("consigneeTel", consigneeTel);
            addressMess.put("consigneeAddress", consigneeAddress);
            addressMess.put("addressList", this.qztUserAddressService.findListByUserId(userId));

            //根据经纬度获取用户所在地5公里内的服务站数量
            Long kilometer = null;
//            Long kilometer = 5000L;
//            String[] longlat = longitudeAndLatitude.split(",");
//            Integer areaBussinessSize = this.qztBusinessService.findScopeBussinessSize(longlat[0], longlat[1], kilometer);
//            remap.put("isPickupWay", areaBussinessSize == 0);//是否可切换取货方式(不可切换则选择服务站取货)
            remap.put("isPickupWay", false);//暂时只支持自提
            remap.put("kilometer", kilometer);
            remap.put("userTel", this.qztUserService.findDGUserById(userId).getMobile());
            remap.put("addressMess", addressMess);
            remap.put("stoGoodsList", stoGoodsList);
//            remap.put("storePromotion", this.qztUserCouponService.queryStorePromotion(userId));//获取新手券
//            remap.put("couponList", this.qztUserCouponService.queryCouponByUserId(userId));//获取有效券集合
            remap.put("goodsCouponList",this.qztUserCouponService.queryGoodsPromotion(userId,goodsIds));//商品优惠券
            remap.put("couponList", this.qztUserCouponService.queryCouponByUserId(userId, totalAmountGoods + grossFreight));//折扣券+活动
//            remap.put("activityCouponList", this.qztUserCouponService.queryActivityPromotion(userId));//活动优惠券
            remap.put("activityCouponList", new ArrayList<Map>());
            remap.put("minPay", Constant.ORDER_LOW_NUM_INT);//最低支付金额
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 分页查询
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-12-24
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztStoGoods/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.qztStoGoodsService.find(pageModel);
            List<QztStoGoods> records = pageModel.getRecords();
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", records);
            return returnUtil.returnMessMap(map);
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
     * @author Xiaofei
     * @date 2019-12-24
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztStoGoods/selectById")
    public Map<String, Object> selectById(String vno, String cno, Long id) {
        try {
            QztStoGoods entity = this.qztStoGoodsService.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}


package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.*;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author snow
 * @since 2019-11-05
 */
@RestController
@Api(value = "WebQztGoodsController", description = "WebQztGoodsController")
public class WebQztGoodsController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztGoodsService service;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private IQztGoodsBannerService iQztGoodsBannerService;

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private IQztUserCouponService qztUserCouponService;

    @Autowired
    private IQztBusinessService qztBusinessService;

    @Autowired
    private IQztUserService qztUserService;

    /**
     * 分页查询
     *
     * @return Map
     * @author snow
     * @date 2019-11-05
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztGoods/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, Long classId,String goodsName) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("state", "1");
            conditionMap.put("classId", classId);
            conditionMap.put("goodsName", goodsName);
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztGoods> records = pageModel.getRecords();
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
     * @author snow
     * @date 2019-11-05
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztGoods/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            QztGoods entity = this.service.selectById(id);
            Map<String, Object> mapB = new HashMap<String, Object>();
            mapB.put("busId", id);
            List<QztGoodsBanner> list = iQztGoodsBannerService.findList(mapB);

            map.put("good", entity);
            map.put("goodBanner", list);

            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取商品填写订单页信息（X）
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    @ApiOperation(value = "获取商品填写订单页信息", notes = "获取商品填写订单页信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "longitudeAndLatitude", value = "经纬度", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/webapi/qztGoods/queryFillinGorderInfo")
    public Map queryFillinGorderInfo(String vno, String cno, String tokenId, Long userId, Long goodsId, String longitudeAndLatitude) {
        try {
            Map remap = new HashMap();
            QztGoods qztGoods = this.service.selectById(goodsId);
            if (qztGoods == null) {
                return returnUtil.returnMess(Constant.GOODS_NOTHINGNESS);
            } else if (qztGoods.getGoodsNum() == null || qztGoods.getGoodsNum() == 0) {
                return returnUtil.returnMess(Constant.UNDERSTOCK);
            }
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
                String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
                consigneeAddress = areaName + qztUserAddress.getDetailAddress();
            }
            addressMess.put("addressId", addressId);
            addressMess.put("companyName", companyName);
            addressMess.put("consigneeTel", consigneeTel);
            addressMess.put("consigneeAddress", consigneeAddress);
            addressMess.put("addressList", this.qztUserAddressService.findListByUserId(userId));

            Map goodsMess = new HashMap();
            goodsMess.put("couponStatus", qztGoods.getCouponStatus() == 0 ? "Y" : "N");//0开
            goodsMess.put("goodsName", qztGoods.getGoodsName());
            goodsMess.put("goodsExplain", qztGoods.getGoodsRemark());
            goodsMess.put("goodsPic", qztGoods.getThumbnail());
            goodsMess.put("goodsNum", qztGoods.getGoodsNum());
            goodsMess.put("goodsPrice", PriceUtil.exactlyTwoDecimalPlaces(qztGoods.getGoodsPrice()));
            goodsMess.put("goodsFreight", qztGoods.getFreight() == null ? 0 : PriceUtil.exactlyTwoDecimalPlaces(qztGoods.getFreight()));

            //根据经纬度获取用户所在地5公里内的服务站数量
            Long kilometer = null;
//            Long kilometer = 5000L;
//            String[] longlat = longitudeAndLatitude.split(",");
//            Integer areaBussinessSize = this.qztBusinessService.findScopeBussinessSize(longlat[0], longlat[1], kilometer);
//            remap.put("isPickupWay", areaBussinessSize == 0);//是否可切换取货方式(不可切换则选择服务站取货)
            remap.put("isPickupWay", false);//暂时只支持自提
            remap.put("areaCode", kilometer);//兼容上一版本 1.0.5
            remap.put("kilometer", kilometer);
            remap.put("userTel", this.qztUserService.findDGUserById(userId).getMobile());
            remap.put("addressMess", addressMess);
            remap.put("goodsMess", goodsMess);
            remap.put("couponList", this.qztUserCouponService.queryCouponByUserId(userId));//获取有效券集合
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}


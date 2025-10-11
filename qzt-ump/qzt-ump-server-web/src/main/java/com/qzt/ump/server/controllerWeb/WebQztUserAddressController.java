package com.qzt.ump.server.controllerWeb;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztUserAddress;
import com.qzt.bus.rpc.api.IQztUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztUserAddressController", description = "收货地址相关Api")
public class WebQztUserAddressController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztUserAddressService qztUserAddressService;

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 查询收货地址列表
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "查询收货地址列表", notes = "查询收货地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserAddresss/getAddressList")
    public Map<String, Object> getAddressList(String vno, String cno, String tokenId, Long userId) {
        try {
            Map conditionMap = new HashMap();
            conditionMap.put("userId", userId);
            conditionMap.put("isDel", "N");
            List<QztUserAddress> qztUserAddressList = this.qztUserAddressService.findList(conditionMap);
            List<Map> data = new ArrayList<>();
            for (QztUserAddress qztUserAddresss : qztUserAddressList) {
                Map mapr = new HashMap();
                mapr.put("id", qztUserAddresss.getId());
                mapr.put("recipientName", qztUserAddresss.getRecipientName());
                mapr.put("phone", qztUserAddresss.getPhone());
                mapr.put("isDefault", qztUserAddresss.getIsDefault());
                mapr.put("zipCode", qztUserAddresss.getZipCode());
                //翻译省市区
//                String areaName = this.sysAreaService.selectAreaName(qztUserAddresss.getProvince(), qztUserAddresss.getCity(), qztUserAddresss.getArea());
                mapr.put("address", qztUserAddresss.getAllAddress()+qztUserAddresss.getDetailAddress());
                data.add(mapr);
            }
            return returnUtil.returnMess(data);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    /**
     * 查询收货地址详情
     *
     * @param id 用户收货地址ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "查询收货地址详情", notes = "查询收货地址详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "地址ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserAddresss/selectAddressById")
    public Map<String, Object> selectAddressById(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            return returnUtil.returnMessMap(this.addressResult(this.qztUserAddressService.selectById(id)));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 获取默认地址
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "获取默认地址", notes = "获取默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/webapi/qztUserAddresss/selectDefaultaccAddress")
    public Map<String, Object> selectDefaultaccAddress(String vno, String cno, String tokenId, Long userId) {
        try {
            QztUserAddress qztUserAddress = this.qztUserAddressService.selectDefaultaccAddress(userId);
            if (qztUserAddress == null) {
                Map conditionMap = new HashMap();
                conditionMap.put("userId", userId);
                conditionMap.put("isDel", "N");
                List<QztUserAddress> list = this.qztUserAddressService.findList(conditionMap);
                if (list != null && list.size() > 0) {
                    qztUserAddress = list.get(0);
                }
            }
            return returnUtil.returnMessMap(this.addressResult(qztUserAddress));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 新增收货地址
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "新增收货地址", notes = "新增收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "区code", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserAddresss/addAddress")
    public Map<String, Object> addAddress(String vno, String cno, String tokenId, String areaCode, QztUserAddress entity,String provinceCode,String cityCode) {
        try {
            Map map = new HashMap<>();
            map.put("userId", entity.getUserId());
            map.put("isDel", "N");
            List<QztUserAddress> qztUserAddresssList = this.qztUserAddressService.findList(map);
            if (qztUserAddresssList.size() >= 10) {
                return returnUtil.returnMess(Constant.ADDRESS_LIMIT);
            }
            if (StringUtil.isEmpty(areaCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "区code错误");
            }
            if (StringUtil.isEmpty(provinceCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "省code错误");
            }
            if (StringUtil.isEmpty(cityCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "市code错误");
            }
//            entity.setProvince(areaCode.substring(0, 2));
//            entity.setCity(areaCode.substring(2, 7));
//            entity.setArea(areaCode.substring(7, 12));
            entity.setProvince(provinceCode);
            entity.setCity(cityCode);
            entity.setArea(areaCode);
            entity.setIsDefault("Y".equals(entity.getIsDefault()) ? "Y" : "N");
            entity.setCreateBy(entity.getUserId());
            entity.setIsDel("N");
            if ("Y".equals(entity.getIsDefault())) {//如果设为默认地址执行
                entity.setUpdateBy(entity.getUserId());
                entity.setUpdateTime(new Date());
                this.qztUserAddressService.defaultArea(entity);
            }
            QztUserAddress qztUserAddress = this.qztUserAddressService.add(entity);
            if (qztUserAddress == null) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "新增收货地址失败");
            }
            Map remap = new HashMap();
            remap.put("id", qztUserAddress.getId());
            remap.put("recipientName", qztUserAddress.getRecipientName());
            remap.put("phone", qztUserAddress.getPhone());
            remap.put("isDefault", qztUserAddress.getIsDefault());
            remap.put("zipCode", qztUserAddress.getZipCode());
            //翻译省市区
//            String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
//            remap.put("address", areaName + qztUserAddress.getDetailAddress());
            remap.put("address", qztUserAddress.getAllAddress());
            return returnUtil.returnMessMap(remap, Constant.RESULT_CODE_SUCCESS, "新增收货地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    /**
     * 修改用户收货地址
     *
     * @param entity 用户收货地址实体
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "修改用户收货地址", notes = "修改用户收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pkId", value = "地址ID", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "区code", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserAddresss/updateAddress")
    public Map<String, Object> updateAddress(String vno, String cno, String tokenId, Long pkId, String areaCode, QztUserAddress entity,String provinceCode,String cityCode) {
        try {
            if (StringUtil.isEmpty(areaCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "区code错误");
            }

            if (StringUtil.isEmpty(provinceCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "省code错误");
            }
            if (StringUtil.isEmpty(cityCode)) {
                return returnUtil.returnMess(Constant.MISSING_DATA_ITEM, "市code错误");
            }
//            entity.setProvince(areaCode.substring(0, 2));
//            entity.setCity(areaCode.substring(2, 7));
//            entity.setArea(areaCode.substring(7, 12));
            entity.setProvince(provinceCode);
            entity.setCity(cityCode);
            entity.setArea(areaCode);
            entity.setUpdateBy(entity.getUserId());
            entity.setUpdateTime(new Date());
            entity.setId(pkId);
            entity.setIsDefault("Y".equals(entity.getIsDefault()) ? "Y" : "N");
            if ("Y".equals(entity.getIsDefault())) {//如果设为默认地址执行
                entity.setUpdateBy(entity.getUserId());
                entity.setUpdateTime(new Date());
                this.qztUserAddressService.defaultArea(entity);
            }
            this.qztUserAddressService.modifyById(entity);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS, "修改收货地址成功");
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "地址ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserAddresss/delectAddress")
    public Map<String, Object> delBatchByIds(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            QztUserAddress entity = new QztUserAddress();
            entity.setId(id);
            entity.setUpdateBy(userId);
            entity.setUpdateTime(new Date());
            entity.setIsDel("Y");
            this.qztUserAddressService.modifyById(entity);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 设置默认地址
     *
     * @param id
     * @return Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    @ApiOperation(value = "设置默认地址", notes = "设置默认地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户Id", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "地址ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztUserAddresss/defaultArea")
    public Map<String, Object> defaultArea(String vno, String cno, String tokenId, Long userId, Long id) {
        try {
            QztUserAddress entity = new QztUserAddress();
            entity.setUpdateBy(userId);
            entity.setUserId(userId);
            entity.setUpdateTime(new Date());
            entity.setId(id);
            this.qztUserAddressService.defaultArea(entity);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    /**
     * 地址结果集处理
     *
     * @param qztUserAddress
     * @return
     */
    private Map addressResult(QztUserAddress qztUserAddress) {
        Map map = new HashMap();
        if (qztUserAddress != null) {
            map.put("id", qztUserAddress.getId());
            map.put("userId", qztUserAddress.getUserId());
//            map.put("areaCode", qztUserAddress.getProvince() + qztUserAddress.getCity() + qztUserAddress.getArea());
            map.put("provinceCode", qztUserAddress.getProvince());
            map.put("cityCode", qztUserAddress.getCity());
            map.put("areaCode", qztUserAddress.getArea());
            map.put("longitude", qztUserAddress.getLongitude());
            map.put("latitude", qztUserAddress.getLatitude());
            //翻译 省市区
//            String areaName = this.sysAreaService.selectAreaName(qztUserAddress.getProvince(), qztUserAddress.getCity(), qztUserAddress.getArea());
//            map.put("proCityArea", areaName);
            map.put("proCityArea", qztUserAddress.getAllAddress());
            map.put("zipCode", qztUserAddress.getZipCode());
            map.put("detailAddress", qztUserAddress.getDetailAddress());
            map.put("recipientName", qztUserAddress.getRecipientName());
            map.put("phone", qztUserAddress.getPhone());
            map.put("isDefault", qztUserAddress.getIsDefault());
        }
        return map;
    }

}


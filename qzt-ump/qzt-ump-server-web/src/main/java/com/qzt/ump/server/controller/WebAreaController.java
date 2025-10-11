package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.Constant;
import com.qzt.ump.model.SysArea;
import com.qzt.ump.rpc.api.SysAreaService;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2018-10-17
 */

@RestController
@Api(value = "sysAreaController", description = "web地区管理Api")
public class WebAreaController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private SysAreaService sysAreaService;

    @ApiOperation(value = "根据经纬度获取城市", notes = "根据经纬度获取城市")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "longitudeAndLatitude", value = "经纬度英文逗号分隔", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cityType", value = "需要查询的类型（省：p、市：c、区：a）默认为c", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/pubapi/area/getCityAccordingCoordinates")
    public Map<String, Object> getCityAccordingCoordinates(String longitudeAndLatitude, String cityType) {
        try {
            Map remap = new HashMap();
            SysArea sysArea = this.sysAreaService.getCityAccordingCoordinates(longitudeAndLatitude, cityType);
            if (sysArea == null) {
                return this.returnUtil.returnMess(Constant.NO_CITY_INFORMATION_AVAILABLE);
            }
            remap.put("id", sysArea.getId());//地区ID
            remap.put("parentId", sysArea.getParentId());//父级id
            remap.put("areaName", sysArea.getAreaName());//地区名称
            remap.put("zipCode", sysArea.getAreaRemark());//地区邮编
            remap.put("areaCode", sysArea.getAreaCode());//地区code编码
            return this.returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据JSON地区列表
     *
     * @return Map
     * @author Xiaofei
     * @date 2018-10-17
     */
//    @GetMapping("/pubapi/area/findJson")
    public Map<String, Object> findJson() {
        try {
            List<String> pmapList = new LinkedList<>();//省
            List<String> cmapList = new LinkedList<>();//市
            List<String> amapList = new LinkedList<>();//区
            Map pcamapInfo = new HashMap();//省市区详情
            //获取所有省级
            List<Map> pList = this.sysAreaService.findJson("0");
            int count = 0;
            for (Map map : pList) {
                map.remove("parentId");
                map.remove("areaNameSpell");
                map.remove("autonaviCityCode");
                map.remove("autonaviAreaCode");
                pmapList.add(map.get("areaName").toString());
                pcamapInfo.put(String.valueOf(count), map);
                count++;
            }
            Map remap = new TreeMap();
            //获取所有市级
            remap.put("plists", pList);
            List<Map> cList = this.sysAreaService.findListMap(remap);
            for (Map map : cList) {
                map.remove("areaNameSpell");
                map.remove("autonaviCityCode");
                map.remove("autonaviAreaCode");
                cmapList.add(map.get("areaName").toString());
                pcamapInfo.put(String.valueOf(count), map);
                count++;
            }
            //获取所有区级
            remap.put("plists", cList);
            List<Map> aList = this.sysAreaService.findListMap(remap);
            for (Map map : aList) {
                map.remove("areaNameSpell");
                map.remove("autonaviCityCode");
                map.remove("autonaviAreaCode");
                amapList.add(map.get("areaName").toString());
                pcamapInfo.put(String.valueOf(count), map);
                count++;
            }
            Map map = new HashMap();
            map.put("pmapList", pmapList);
            map.put("cmapList", cmapList);
            map.put("amapList", amapList);
            map.put("pcamapInfo", pcamapInfo);
            return this.returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }
}


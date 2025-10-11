package com.qzt.ump.server.controller;

import com.alibaba.fastjson.JSON;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.tools.AutonaviUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.ump.rpc.api.SysAreaService;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysArea;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
@RequestMapping("/back/sysArea")
@Api(value = "SysAreaController", description = "地区管理Api")
public class SysAreaController extends BaseController {

    @Autowired
    private SysAreaService sysAreaService;

    @Autowired
    private ReturnUtilServer returnUtil;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-10-17
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            SysArea entity = this.sysAreaService.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 查询分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-10-17
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<SysArea> pageModel) {
        try {
            pageModel = (PageModel<SysArea>) this.sysAreaService.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-10-08
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody SysArea entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            SysArea entityback = this.sysAreaService.add(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 修改方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-10-17
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody SysArea entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            SysArea entityback = this.sysAreaService.modifyById(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-10-17
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        try {
            return ResultUtil.ok(this.sysAreaService.deleteBatchIds(Arrays.asList(ids)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 一键添加地区redis
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-10-08
     */
    @PostMapping("/updateAreaRedis")
    public ResultModel updateAreaRedis() {
        try {
            this.sysAreaService.initializeArea();
            return ResultUtil.ok("200");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, e.getMessage());
        }
    }

    /**
     * 验证经纬度坐标是否在指定区域内
     *
     * @param longitudeAndLatitude
     * @param acode
     * @return Map
     * @author Xiaofei
     * @date 2019-09-28
     */
    @ApiOperation(value = "验证经纬度是否在区域内", notes = "验证经纬度是否在区域内")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "longitudeAndLatitude", value = "经纬度英文逗号分隔", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "acode", value = "区code", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/verificationAddressRange")
    public Map<String, Object> verificationAddressRange(String longitudeAndLatitude, String acode) {
        try {
            String id = "";
            if (acode != null && acode.length() == 12) {
                id = acode.substring(7, 12);
            } else if (acode != null && acode.length() == 7) {
                id = acode.substring(2, 7);
            } else if (acode != null && acode.length() == 2) {
                id = acode;
            } else {
                return returnUtil.returnMess(Constant.DATA_ERROR, "code编码错误");
            }

            SysArea sysArea = this.sysAreaService.queryById(Long.valueOf(id));
            if (sysArea == null) {
                return returnUtil.returnMess(Constant.DATA_ERROR, "code编码错误");
            }
            Map ablalmap = AutonaviUtil.getAddressByLongitudeAndLatitude(longitudeAndLatitude);
            String citycode = ablalmap.get("citycode").toString();
            String adcode = ablalmap.get("adcode").toString();
            if ("".equals(citycode) || "".equals(adcode)) {
                return returnUtil.returnMess(Constant.DATA_ERROR, "暂无该地址信息，请联系系统管理员");
            }
            Map remap = new HashMap();
            if (citycode.equals(sysArea.getAutonaviCityCode())) {
                remap.put("formattedAddress", ablalmap.get("formattedAddress"));
            } else {
                return returnUtil.returnMess(Constant.DATA_ERROR, "对不起，您选择的地址超出所在地区");
            }
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 高德地图code对应本机数据处理
     */
    @PostMapping("/test")
    public ResultModel test(String longitudeAndLatitude, String cityType) {
        SysArea sysArea = this.sysAreaService.getCityAccordingCoordinates(longitudeAndLatitude, cityType);

//        List<SysArea> bsgclist = new ArrayList<>();
//        SysArea sysAreab = new SysArea();
//        SysArea sysAreas = new SysArea();
//        SysArea sysAreag = new SysArea();
//        SysArea sysAreac = new SysArea();
//        sysAreab.setId(1L);
//        sysAreas.setId(2L);
//        sysAreag.setId(3L);
//        sysAreac.setId(4L);
//        bsgclist.add(sysAreab);
//        bsgclist.add(sysAreas);
//        bsgclist.add(sysAreag);
//        bsgclist.add(sysAreac);
//        Map fmap = new HashMap();
//        fmap.put("plists", bsgclist);
//        List<SysArea> clist = this.service.findList(fmap);//直辖市区级List
//
//        Map cmap = new HashMap();
//        cmap.put("plists", clist);
//        List<SysArea> alist = this.service.findList(cmap);//直辖市区级List
//
//        for (SysArea sysArea : alist) {
//
//            SysArea sysAreap = this.service.queryById(Long.valueOf(sysArea.getParentId()));
//            sysArea.setAutonaviCityCode(sysAreap.getAutonaviCityCode());
//            SysArea sysArea1s = this.service.modifyById(sysArea);
//            System.out.println("sys_area_name：" + sysArea.getAreaName() + "；更新结果：" + (sysArea1s != null));
//
//            System.out.println(sysArea.getAreaName() + "--" + sysArea.getAutonaviCityCode());
//        }
//        Map fmap = new HashMap();
//        fmap.put("parentId", "0");
//        List<SysArea> plist = this.service.findList(fmap);//省级List
//        Map cmap = new HashMap();
//        cmap.put("plists", plist);
//        List<SysArea> clist = this.service.findList(cmap);//市级List
//
//        Map amap = new HashMap();
//        amap.put("plists", clist);
//        List<SysArea> alist = this.service.findList(amap);//区级List
//
//        int i = 0;
//        int s = 0;
//        for (SysArea sysArea : alist) {
//            Map tmap = new HashMap();
//            tmap.put("name", sysArea.getAreaName());
//            SysArea sysAreap = this.service.queryById(Long.valueOf(sysArea.getParentId()));
//            tmap.put("citycode", sysAreap.getAutonaviCityCode());
//            Map maps = this.service.test(tmap);
//            String name = "";
//            String adcode = "";
//            String citycode = "";
//            if (maps != null) {
//                ++i;
//                name = maps.get("name").toString();
//                adcode = maps.get("adcode").toString();
//                citycode = maps.get("citycode").toString();
//            }
//            sysArea.setAutonaviAreaCode(adcode);
//            sysArea.setAutonaviCityCode(sysAreap.getAutonaviCityCode());
//            SysArea sysArea1s = this.service.modifyById(sysArea);
//            System.out.println("sys_area_name：" + sysArea.getAreaName() + "；gd_name：" + name + "；更新结果：" + (sysArea1s != null));*/
//             /*else {
//                ++s;
////                System.out.println(sysArea1.getAutonaviCityCode() + "_sys_area_name：\"" + sysArea.getAreaName() + "\"；gd_name：\"" + name + "\"");
//                if (sysArea.getAreaName().length() == 10) {//2位 3位 4位 5位 6位已处理
//                    System.out.println("UPDATE sys_area SET area_name = '" + sysArea1.getAutonaviCityCode() + "' WHERE area_name = '" + sysArea.getAreaName() + "';");
////                    System.out.println(sysArea1.getAutonaviCityCode() + "_sys_area_name：\"" + sysArea.getAreaName() + "\"；gd_name：\"" + name + "\"");
//                    Map ccmap = new HashMap();
//                    String substring = sysArea.getAreaName().substring(0, 3);
//
//                    ccmap.put("name", substring);
//                    ccmap.put("citycode", sysArea1.getAutonaviCityCode());
//                    Map mapssss = null;
//                    try {
////                        mapssss = this.service.test2(ccmap);
//                    } catch (Exception e) {
//                        System.out.println(sysArea.getAreaName() + "   " + sysArea1.getAutonaviCityCode() + "_error：" + substring);
//                        continue;
//                    }
//                    String names = "";
//                    String adcodes = "";
//                    String citycodes = "";
//                    if (mapssss != null) {
//                        names = mapssss.get("name").toString();
//                        adcodes = mapssss.get("adcode").toString();
//                        citycodes = mapssss.get("citycode").toString();
////                        System.out.println(sysArea1.getAutonaviCityCode() + "_sys_area_name：" + sysArea.getAreaName() + "；gd_name：" + names + "；");
////                        System.out.println("UPDATE sys_area SET area_name = '" + names + "' WHERE area_name = '" + sysArea.getAreaName() + "';");
//                    } else {
////                        System.out.println("SELECT * FROM gd where name LIKE CONCAT('%', '" + sysArea.getAreaName() + "', '%') AND citycode = '" + sysArea1.getAutonaviCityCode() + "';");
////                        System.out.println("UPDATE sys_area SET area_name = '" + sysArea1.getAutonaviCityCode() + "' WHERE area_name = '" + sysArea.getAreaName() + "';");
//                    }
//                }
////                System.out.println("UPDATE sys_area SET area_name = '" + sysArea1.getAutonaviCityCode() + "' WHERE area_name = '" + sysArea.getAreaName() + "';");
//            }*/
////        }
//        System.out.println("有效：" + i + "条；");
//        System.out.println("无效：" + s + "条；");
//
//        Map fmap = new HashMap();
//        fmap.put("parentId", "224");
//        String ces = "0313";
//        List<SysArea> plist = this.service.findList(fmap);//省级List
//        for (SysArea sysArea : plist) {
//            Map tmap = new HashMap();
//            tmap.put("name", sysArea.getAreaName());
//            tmap.put("adcode", "");
//            tmap.put("citycode", ces);
//            Map maps = this.service.test(tmap);
//            String name = "";
//            String adcode = "";
//            String citycode = "";
//            if (maps != null) {
//                name = maps.get("name").toString();
//                adcode = maps.get("adcode").toString();
//                citycode = maps.get("citycode").toString();
//            }
//            sysArea.setAutonaviAreaCode(adcode);
//            sysArea.setAutonaviCityCode("".equals(citycode) ? ces : citycode);
//            SysArea sysArea1 = this.service.modifyById(sysArea);
//            System.out.println("sys_area_name：" + sysArea.getAreaName() + "；gd_name：" + name + "；更新结果：" + (sysArea1 != null));
//        }
        return ResultUtil.ok(sysArea);
    }

    /**
     * 根据JSON地区列表
     *
     * @return Map
     * @author Xiaofei
     * @date 2018-10-17
     */
    @GetMapping("/findJson")
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
                pmapList.add(map.get("name").toString());
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
                cmapList.add(map.get("name").toString());
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
                amapList.add(map.get("name").toString());
                pcamapInfo.put(String.valueOf(count), map);
                count++;
            }

            Map map = new HashMap();
            map.put("pmapList", pmapList);
            map.put("cmapList", cmapList);
            map.put("amapList", amapList);
            map.put("pcamapInfo", pcamapInfo);


//            for (Map map : cList) {
                /*String id = map.get("id").toString();
                Map m1 = map;
                String initial = FirstLetterUtil.getFirstLetter(m1.get("areaName").toString()).substring(0, 1).toUpperCase();
                m1.put("initial", initial);
                cmapList.add(m1);*/
                /*if ("1".equals(id) || "2".equals(id) || "3".equals(id) || "4".equals(id)) {
                    Map m1 = map;
                    String initial = FirstLetterUtil.getFirstLetter(m1.get("areaName").toString()).substring(0, 1).toUpperCase();
                    m1.put("initial",initial);
                    cmapList.add(m1);
                } else {
                    List<Map> cityList = this.sysAreaService.findJson(id);
                    for (Map mm : cityList) {
                        Map m1 = mm;
                        String initial = FirstLetterUtil.getFirstLetter(m1.get("areaName").toString()).substring(0, 1).toUpperCase();
                        m1.put("initial",initial);
                        cmapList.add(m1);
                    }
                }*/
//            }

            return this.returnUtil.returnMessMap(map);
            //根据首字母区分生成JSON 26字母为KEY
                    /*String initial = FirstLetterUtil.getFirstLetter(m1.get("areaName").toString()).substring(0, 1).toUpperCase();
                    List<Map> mapList1;
                    if (remap.get(initial) != null) {
                        mapList1 = (List<Map>) remap.get(initial);
                    } else {
                        mapList1 = new ArrayList<>();
                    }
                    mapList1.add(m1);
                    remap.put(initial, mapList1);*/

            //IOS地址处理
                    /*String areaNamesss = FirstLetterUtil.getFirstLetter(m1.get("areaName").toString());
                    str += "<dict>" +
                            "<key>name</key><string>" + m1.get("areaName") + "</string>" +
                            "<key>pinYin</key><string>" + m1.get("areaNameSpell") + "</string>" +
                            "<key>pinYinHead</key><string>" + areaNamesss + "</string>" +
                            "<key>id</key><string>" + m1.get("id") + "</string>" +
                            "<key>parentId</key><string>" + m1.get("parentId") + "</string>" +
                            "<key>zipCode</key><string>" + (m1.get("zipCode") == null ? "" : m1.get("zipCode")) + "</string>" +
                            "<key>areaCode</key><string>" + m1.get("areaCode") + "</string>" +
                            "<key>autonaviCityCode</key><string>" + (m1.get("autonaviCityCode") == null ? "" : m1.get("autonaviCityCode")) + "</string>" +
                            "<key>autonaviAreaCode</key><string>" + (m1.get("autonaviAreaCode") == null ? "" : m1.get("autonaviAreaCode")) + "</string>" +
                            "</dict>";*/
            //Android本机地区db
                    /*System.out.println("INSERT INTO \"main\".\"city\" VALUES (" + map.get("id") + ", '" + map.get("parentId") + "', '" + map.get("areaName")
                            + "', '" + map.get("areaNameSpell") + "', '" + (map.get("zipCode") == null ? "" : map.get("zipCode")) + "', '" + map.get("areaCode") + "', '"
                            + (map.get("autonaviCityCode") == null ? "" : map.get("autonaviCityCode")) + "', '"
                            + (map.get("autonaviAreaCode") == null ? "" : map.get("autonaviAreaCode")) + "');");*/

            //获取全部地区json
            /*List<Map> mapList = this.sysAreaService.findJson("0");
            for (Map map : mapList) {
                String id = map.get("id").toString();
                List<Map> cityList = this.sysAreaService.findJson(id);
                for (Map city : cityList) {
                    String idcity = city.get("id").toString();
                    List<Map> areaList = this.sysAreaService.findJson(idcity);
                    city.put("juniorList", areaList);
                }
                map.put("juniorList", cityList);
            }*/

            //初始化所有地区 全拼
            /*List<SysArea> list = this.sysAreaService.findList(new HashMap());
            for (SysArea sysArea : list) {
                try {
                    String areaNamePin = FirstLetterUtil.ToPinyin(sysArea.getAreaName());
                    sysArea.setAreaNameSpell(areaNamePin);
                    SysArea sysArea1 = this.sysAreaService.modifyById(sysArea);
                    if (sysArea1 == null) {
                        System.out.println(sysArea.getAreaName() + "_" + sysArea.getId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(sysArea.getAreaName() + "_error_" + sysArea.getId());
                    continue;
                }
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            return this.returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 读取Excel数据
     *
     * @return java.util.List<java.util.Map>
     * @author Xiaofei
     * @date 2019-09-24
     */
    public List<Map> testss() {
        FileInputStream in = null;
        List<Map> mapList = new ArrayList<>();
        try {
            in = new FileInputStream(new File("D:\\AMap_adcode_citycode.xlsx"));
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet sheet = workbook.getSheetAt(0);//得到第一个sheet
            int rowNo = sheet.getLastRowNum(); //得到行数
            for (int i = 1; i < rowNo; i++) {
                XSSFRow row = sheet.getRow(i);
                XSSFCell cell1 = row.getCell((short) 0);
                XSSFCell cell2 = row.getCell((short) 1);
                XSSFCell cell3 = row.getCell((short) 2);
                String ce1 = cell1 == null ? "" : cell1.getStringCellValue();
                String ce2 = cell2 == null ? "" : cell2.getStringCellValue();
                String ce3 = cell3 == null ? "" : cell3.getStringCellValue();
//                System.out.println(ce1 + "\t" + ce2 + "\t" + ce3);
                Map map = new HashMap();
                map.put("name", ce1);
                map.put("adcode", ce2);
                map.put("citycode", ce3);
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mapList;
    }


}


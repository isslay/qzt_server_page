package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztBusPic;
import com.qzt.bus.rpc.api.IQztBusPicService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.AutonaviUtil;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import com.qzt.ump.model.SysArea;
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.rpc.api.IQztBusinessService;
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
@Api(value = "WebQztBusinessController", description = "WebQztBusinessController")
public class WebQztBusinessController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztBusinessService service;

    @Autowired
    private IQztBusPicService qztBusPicService;

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 分页查询服务站列表
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    @ApiOperation(value = "分页查询服务站列表", notes = "分页查询服务站列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busLong", value = "经度", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "busLat", value = "纬度", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "busName", value = "服务站名称", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "areaCode", value = "市code", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "kilometer", value = "距离（千米）", dataType = "Long", required = false, paramType = "query"),
            @ApiImplicitParam(name = "sorts", value = "排序（距离01）", dataType = "String", required = false, paramType = "query")
    })
    @GetMapping("/pubapi/qztBusiness/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String busLong, String busLat, String busName, String areaCode, Long kilometer, String sorts) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("busLong", busLong);
            conditionMap.put("busLat", busLat);
            conditionMap.put("busName", busName);
            conditionMap.put("areaCode", areaCode);
            conditionMap.put("sorts", sorts);
            conditionMap.put("busState", "0");
            if (kilometer != null || !StringUtil.isEmpty(areaCode)) {//兼容1.0.5     if (kilometer != null) {
                kilometer = kilometer == null ? 5000L : kilometer;
                conditionMap.put("kilometer", kilometer);
            }
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztBusiness> records = pageModel.getRecords();
            List<Map> datalist = new ArrayList<>();
            for (QztBusiness qztBusiness : records) {
                Map maps = new HashMap();
                maps.put("id", qztBusiness.getId());
                maps.put("busName", qztBusiness.getBusName());
                maps.put("busTel", qztBusiness.getBusTel());
                maps.put("picUrl", qztBusiness.getPicUrl());
                maps.put("busAddress", qztBusiness.getBusAddress());
                datalist.add(maps);
            }
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
     * 查询服务站详情
     *
     * @param id
     * @return Map
     * @author Xiaofei
     * @date 2019-11-18
     */
    @ApiOperation(value = "查询服务站详情", notes = "查询服务站详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztBusiness/selectById")
    public Map<String, Object> selectById(String vno, String cno, Long id) {
        try {
            QztBusiness qztBusiness = this.service.selectById(id);
            Map remap = new HashMap();
            remap.put("id", qztBusiness.getId());
            remap.put("busName", qztBusiness.getBusName());
            remap.put("busTel", qztBusiness.getBusTel());
            remap.put("busAddress", qztBusiness.getBusAddress());
            remap.put("startEndTime", qztBusiness.getBusTimeStart() + " - " + qztBusiness.getBusTimeEnd());
            remap.put("busTimeStart", qztBusiness.getBusTimeStart());
            remap.put("busTimeEnd", qztBusiness.getBusTimeEnd());
            remap.put("busDetail", qztBusiness.getBusDetail());
            remap.put("busState", qztBusiness.getBusState());
            remap.put("userId", qztBusiness.getUserId());
            Map params = new HashMap();
            params.put("busId", id);
            params.put("busType", "03");
            params.put("state", "0");
            List<QztBusPic> qztBusPics = qztBusPicService.getBusPics(params);//获得商家录播图
            String[] picAr = new String[qztBusPics.size()];
            int i = 0;
            for (QztBusPic qztBusPic : qztBusPics) {
                picAr[i++] = qztBusPic.getBannerUrl();
            }
            remap.put("busPicList", picAr);
            remap.put("busPic", qztBusiness.getPicUrl());
            remap.put("busLong", qztBusiness.getBusLong());
            remap.put("busLat", qztBusiness.getBusLat());
            return returnUtil.returnMessMap(remap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


    @ApiOperation(value = "修改商户基本信息", notes = "修改商户基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busName", value = "商家名称", dataType = "Long", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busTel", value = "联系电话", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busAddress", value = "商家地址", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busTimeStart", value = "营业开始时间", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busTimeEnd", value = "营业截止时间", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busState", value = "服务站状态", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busDetail", value = "商家详情", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "picUrl", value = "商家图片", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "bussId", value = "商家ID", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busLong", value = "经度", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busLat", value = "维度", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "busPicUrl", value = "商家环境图片", dataType = "String", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztBusiness/updateBussinessMess")
    public Map<String, Object> updateBussinessMess(String vno, String cno, String tokenId, String userId, String busState, String busTel, String busLong, String busLat, String busAddress, String busName, String busDetail, String picUrl, String bussId, String busTimeStart, String busTimeEnd, String[] busPicUrl) {
        try {
            Map dataMap = new HashMap();
            QztBusiness qztBusiness = this.service.selectById(bussId);
            if (qztBusiness == null) {
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            if (!StringUtil.isEmpty(busName)) {
                qztBusiness.setBusName(busName);
            }
            if (!StringUtil.isEmpty(busTel)) {
                qztBusiness.setBusTel(busTel);
            }
            if (!StringUtil.isEmpty(busAddress)) {
                qztBusiness.setBusAddress(busAddress);
            }
            if (!StringUtil.isEmpty(busTimeStart)) {
                qztBusiness.setBusTimeStart(busTimeStart);
            }
            if (!StringUtil.isEmpty(busTimeEnd)) {
                qztBusiness.setBusTimeEnd(busTimeEnd);
            }
            if (!StringUtil.isEmpty(busState)) {
                qztBusiness.setBusState(busState);
            }
            if (!StringUtil.isEmpty(picUrl)) {
                qztBusiness.setPicUrl(picUrl);
            }
            if (!StringUtil.isEmpty(busLong)) {
                qztBusiness.setBusLong(busLong);
            }
            if (!StringUtil.isEmpty(busLat)) {
                qztBusiness.setBusLat(busLat);
            }
            if (!StringUtil.isEmpty(busDetail)) {
                qztBusiness.setBusDetail(busDetail);
            }
            SysArea sysArea = sysAreaService.getCityAccordingCoordinates(qztBusiness.getBusLong() + "," + qztBusiness.getBusLat(), "a");
            if (sysArea != null) {
                qztBusiness.setProvince(sysArea.getAreaCode().substring(0, 2));
                qztBusiness.setCity(sysArea.getAreaCode().substring(2, 7));
                qztBusiness.setArea(sysArea.getAreaCode().substring(7));
            }
            if (!this.service.updateById(qztBusiness)) {
                //操作图片信息
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
            //根据进度纬度计算所属区域


            List<QztBusPic> pics = new ArrayList<>();
            int index = 0;
            for (String str : busPicUrl) {
                QztBusPic qztBusPic = new QztBusPic();
                qztBusPic.setBusType("03");
                qztBusPic.setBusId(Long.parseLong(bussId));
                qztBusPic.setBannerUrl(str);
                qztBusPic.setOrderNum(index++);
                qztBusPic.setState("0");
                qztBusPic.setCreateTime(new Date());
                pics.add(qztBusPic);
            }
            qztBusPicService.delBusPics(bussId);
            qztBusPicService.saveBusPics(pics);
            return returnUtil.returnMess(Constant.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }
}


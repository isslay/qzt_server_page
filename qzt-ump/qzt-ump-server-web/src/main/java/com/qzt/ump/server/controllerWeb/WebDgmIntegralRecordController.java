package com.qzt.ump.server.controllerWeb;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.DgmIntegralRecord;
import com.qzt.bus.rpc.api.IDgmIntegralRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分记录表web前端控制器
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@RestController
@Api(value = "WebDgmIntegralRecordController", description = "WebDgmIntegralRecordController")
public class WebDgmIntegralRecordController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IDgmIntegralRecordService service;

    /**
     * 分页查询
     *
     * @return Map
     * @author snow
     * @date 2024-02-19
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/webapi/dgmIntegralRecord/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, Long userId) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("userId", userId);
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<DgmIntegralRecord> records = pageModel.getRecords();
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

//    /**
//     * 根据主键id查询详情
//     *
//     * @param id
//     * @return Map
//     * @author snow
//     * @date 2024-02-19
//     */
//    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
//    })
//    @GetMapping("/pubapi/dgmIntegralRecord/selectById")
//    public Map<String, Object> selectById(String vno, String cno, Long id) {
//        try {
//            DgmIntegralRecord entity = this.service.selectById(id);
//            return returnUtil.returnMess(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
//    }

}


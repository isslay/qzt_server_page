package com.qzt.ump.server.controllerWeb;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztAccountLog;
import com.qzt.bus.rpc.api.IQztAccountLogService;
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
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@Log4j
@Api(value = "WebQztAccountLogController", description = "WebQztAccountLogController")
public class WebQztAccountLogController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztAccountLogService service;

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
    @PostMapping("/webapi/qztAccountLog/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize, String tokenId, Long userId) {
        try {
            Map returnMap = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel();
            pageModel.setCurrent(pageNum);
            pageModel.setSize(pageSize);
            conditionMap.put("userId", userId);
            pageModel.setCondition(conditionMap);
            pageModel = (PageModel) service.find(pageModel);
            returnMap.put("current", pageModel.getCurrent());
            returnMap.put("size", pageModel.getSize());
            returnMap.put("total", pageModel.getTotal());
            List<QztAccountLog> records = pageModel.getRecords();
            List rsData = new ArrayList();
            for(QztAccountLog qztAccountLog:records){
                Map <String,Object> dataMap = new HashMap<>();
                dataMap.put("obj",qztAccountLog);
                dataMap.put("changeMoney", PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(qztAccountLog.getChangeNum().toString())));
                rsData.add(dataMap);
            }
            returnMap.put("data",rsData);
            Map backData = new HashMap();
            backData.put("pageData", returnMap);
            return returnUtil.returnMessMap(backData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单查询错误信息：" + e.getMessage());
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
    @GetMapping("/pubapi/qztAccountLog/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            QztAccountLog entity = this.service.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}


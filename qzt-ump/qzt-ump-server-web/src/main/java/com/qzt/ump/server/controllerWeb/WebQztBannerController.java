package com.qzt.ump.server.controllerWeb;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztBanner;
import com.qzt.bus.rpc.api.IQztBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author snow
 * @since 2019-11-07
 */
@RestController
@Api(value = "WebQztBannerController", description = "WebQztBannerController")
public class WebQztBannerController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztBannerService service;

    /**
     * 分页查询
     *
     * @return Map
     * @author snow
     * @date 2019-11-07
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "位置（1：首页，2：联盟商家页，3：商城页）", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "source", value = "客户端(WX,IOS,AN)", dataType = "String", required = false, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztBanner/getListPage")
    public Map<String, Object> getListPage(String vno, String cno,String type,String source, Integer pageNum, Integer pageSize) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            conditionMap.put("state","01");
            conditionMap.put("bannerType",type);
            conditionMap.put("bannerSource",source);
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztBanner> records = pageModel.getRecords();
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
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author snow
     * @date 2020-3-26
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "轮播主键", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztBanner/getBanner/{id}")
    public Map<String, Object> query(@PathVariable Long id) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            QztBanner entity = service.selectById(id);
            map.put("banner", entity);
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }


}


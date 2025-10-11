package com.qzt.ump.server.controllerWeb;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.DgmGoodsClass;
import com.qzt.bus.rpc.api.IDgmGoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * 商品分类web前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
@RestController
@Api(value = "WebDgmGoodsClassController", description = "WebDgmGoodsClassController")
public class WebDgmGoodsClassController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IDgmGoodsClassService service;

//    /**
//     * 分页查询
//     *
//     * @return Map
//     * @author snow
//     * @date 2023-09-25
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
//    })
//    @GetMapping("/pubapi/dgmGoodsClass/getListPage")
//    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize) {
//        try {
//            Map map = new HashMap();
//            Map conditionMap = new HashMap();
//            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
//            pageModel = (PageModel) this.service.find(pageModel);
//            List<DgmGoodsClass> records = pageModel.getRecords();
//            map.put("current", pageModel.getCurrent());
//            map.put("size", pageModel.getSize());
//            map.put("total", pageModel.getTotal());
//            map.put("data", records);
//            return returnUtil.returnMessMap(map);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
//    }
//
//    /**
//     * 根据主键id查询详情
//     *
//     * @param id
//     * @return Map
//     * @author snow
//     * @date 2023-09-25
//     */
//    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
//    })
//    @GetMapping("/pubapi/dgmGoodsClass/selectById")
//    public Map<String, Object> selectById(String vno, String cno, Long id) {
//        try {
//            DgmGoodsClass entity = this.service.selectById(id);
//            return returnUtil.returnMess(entity);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return returnUtil.returnMess(Constant.DATA_ERROR);
//        }
//    }

    @GetMapping("/pubapi/dgmGoodsClass/classList")
    public Map<String, Object> selectClassList() {
        try {
            Object entity = this.service.getClassList();
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}


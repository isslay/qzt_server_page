package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.tools.BeanTools;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.rpc.api.SysAreaService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztBusiness;
import com.qzt.bus.rpc.api.IQztBusinessService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztBusiness")
@Api(value = "QztBusinessController", description = "QztBusinessController")
public class QztBusinessController extends BaseController {

    @Autowired
    private IQztBusinessService service;

    @Autowired
    private SysAreaService sysAreaService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-11
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztBusiness entity = service.selectById(id);
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
     * @date 2019-11-11
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        try {
            pageModel = (PageModel) service.find(pageModel);
            List<QztBusiness> records = pageModel.getRecords();
            List<Map> datamap = new ArrayList<>();
            for (QztBusiness qztBusiness : records) {
                Map<String, Object> maps = BeanTools.beanToMap(qztBusiness);
                String areaName = this.sysAreaService.selectAreaName(qztBusiness.getProvince(), qztBusiness.getCity(), qztBusiness.getArea());
                maps.put("busAddressMc", areaName + qztBusiness.getBusAddress());
                maps.put("busStateMc", DicParamUtil.getDicCodeByType("BUS_STATE", qztBusiness.getBusState()));
                datamap.add(maps);
            }
            pageModel.setRecords(datamap);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

}


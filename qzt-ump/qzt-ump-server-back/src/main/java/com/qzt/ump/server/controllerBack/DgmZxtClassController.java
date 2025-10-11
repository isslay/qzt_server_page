package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmZxtClass;
import com.qzt.bus.rpc.api.IDgmZxtClassService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * 专孝堂 分类前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
@RestController
@RequestMapping("/back/dgmZxtClass")
@Api(value = "DgmZxtClassController", description = "DgmZxtClassController")
public class DgmZxtClassController extends BaseController {

    @Autowired
    private IDgmZxtClassService service;

    /**
    * 根据专孝堂 分类ID查询
    *
    * @param id 专孝堂 分类ID
    * @return ResultModel
    * @author snow
    * @date 2023-09-25
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmZxtClass entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询专孝堂 分类分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2023-09-25
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmZxtClass> pageModel) {
        try {
            pageModel = (PageModel<DgmZxtClass>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增专孝堂 分类方法
     *
     * @param entity 专孝堂 分类实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-25
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmZxtClass entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmZxtClass entityback = service.add(entity);
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
     * 修改专孝堂 分类方法
     *
     * @param entity 专孝堂 分类实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-25
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmZxtClass entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmZxtClass entityback = service.modifyById(entity);
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
     * 批量删除专孝堂 分类方法
     *
     * @param ids 专孝堂 分类ID集合
     * @return ResultModel
     * @author snow
     * @date 2023-09-25
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        try {
            return ResultUtil.ok(service.deleteBatchIds(Arrays.asList(ids)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    @GetMapping("/classList")
    public ResultModel selectClassList() {
        try {
            Object entity = this.service.getClassList();
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }
}


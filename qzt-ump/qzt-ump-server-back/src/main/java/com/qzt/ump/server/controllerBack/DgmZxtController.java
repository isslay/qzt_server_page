package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmZxt;
import com.qzt.bus.rpc.api.IDgmZxtService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 专孝堂内容前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-27
 */
@RestController
@RequestMapping("/back/dgmZxt")
@Api(value = "DgmZxtController", description = "DgmZxtController")
public class DgmZxtController extends BaseController {

    @Autowired
    private IDgmZxtService service;

    /**
    * 根据专孝堂内容ID查询
    *
    * @param id 专孝堂内容ID
    * @return ResultModel
    * @author snow
    * @date 2023-09-27
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmZxt entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询专孝堂内容分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2023-09-27
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmZxt> pageModel) {
        try {
            pageModel = (PageModel<DgmZxt>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增专孝堂内容方法
     *
     * @param entity 专孝堂内容实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-27
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmZxt entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmZxt entityback = service.add(entity);
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
     * 修改专孝堂内容方法
     *
     * @param entity 专孝堂内容实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-27
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmZxt entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmZxt entityback = service.modifyById(entity);
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
     * 批量删除专孝堂内容方法
     *
     * @param ids 专孝堂内容ID集合
     * @return ResultModel
     * @author snow
     * @date 2023-09-27
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

    @PostMapping("/up/{id}")
    public ResultModel up(@PathVariable Long id) {
        DgmZxt entity = service.queryById(id);
        if (entity != null) {
            entity.setState("1");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }

    @PostMapping("/down/{id}")
    public ResultModel down(@PathVariable Long id) {
        DgmZxt entity = service.queryById(id);
        if (entity != null) {
            entity.setState("2");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
}


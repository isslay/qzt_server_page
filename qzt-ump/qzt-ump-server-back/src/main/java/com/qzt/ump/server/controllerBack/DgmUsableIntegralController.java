package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.bus.rpc.api.IDgmUsableIntegralService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 可用积分表前端控制器
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@RestController
@RequestMapping("/back/dgmUsableIntegral")
@Api(value = "DgmUsableIntegralController", description = "DgmUsableIntegralController")
public class DgmUsableIntegralController extends BaseController {

    @Autowired
    private IDgmUsableIntegralService service;

    /**
    * 根据可用积分表ID查询
    *
    * @param id 可用积分表ID
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmUsableIntegral entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询可用积分表分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmUsableIntegral> pageModel) {
        try {
            pageModel = (PageModel<DgmUsableIntegral>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增可用积分表方法
     *
     * @param entity 可用积分表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmUsableIntegral entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmUsableIntegral entityback = service.add(entity);
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
     * 修改可用积分表方法
     *
     * @param entity 可用积分表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmUsableIntegral entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmUsableIntegral entityback = service.modifyById(entity);
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
     * 批量删除可用积分表方法
     *
     * @param ids 可用积分表ID集合
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
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
}


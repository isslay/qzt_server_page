package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmReduceIntegralDetail;
import com.qzt.bus.rpc.api.IDgmReduceIntegralDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 扣减积分详情表前端控制器
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@RestController
@RequestMapping("/back/dgmReduceIntegralDetail")
@Api(value = "DgmReduceIntegralDetailController", description = "DgmReduceIntegralDetailController")
public class DgmReduceIntegralDetailController extends BaseController {

    @Autowired
    private IDgmReduceIntegralDetailService service;

    /**
    * 根据扣减积分详情表ID查询
    *
    * @param id 扣减积分详情表ID
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmReduceIntegralDetail entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询扣减积分详情表分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmReduceIntegralDetail> pageModel) {
        try {
            pageModel = (PageModel<DgmReduceIntegralDetail>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增扣减积分详情表方法
     *
     * @param entity 扣减积分详情表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmReduceIntegralDetail entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmReduceIntegralDetail entityback = service.add(entity);
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
     * 修改扣减积分详情表方法
     *
     * @param entity 扣减积分详情表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmReduceIntegralDetail entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmReduceIntegralDetail entityback = service.modifyById(entity);
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
     * 批量删除扣减积分详情表方法
     *
     * @param ids 扣减积分详情表ID集合
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


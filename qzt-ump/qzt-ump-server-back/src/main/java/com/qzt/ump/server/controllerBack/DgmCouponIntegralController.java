package com.qzt.ump.server.controllerBack;

import com.qzt.bus.model.DgmCouponIntegral;
import com.qzt.bus.rpc.api.IDgmCouponIntegralService;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-06
 */
@RestController
@RequestMapping("/back/dgmCouponIntegral")
@Api(value = "DgmCouponIntegralController", description = "DgmCouponIntegralController")
public class DgmCouponIntegralController extends BaseController {

    @Autowired
    private IDgmCouponIntegralService service;

    /**
    * 根据ID查询
    *
    * @param id ID
    * @return ResultModel
    * @author snow
    * @date 2023-09-06
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmCouponIntegral entity = service.selectById(id);
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
    * @author snow
    * @date 2023-09-06
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmCouponIntegral> pageModel) {
        try {
            pageModel = (PageModel<DgmCouponIntegral>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-06
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmCouponIntegral entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmCouponIntegral entityback = service.add(entity);
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
     * 修改方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-06
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmCouponIntegral entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmCouponIntegral entityback = service.modifyById(entity);
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
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author snow
     * @date 2023-09-06
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
        DgmCouponIntegral entity = service.queryById(id);
        if (entity != null) {
            entity.setStatus(0);
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }

    @PostMapping("/down/{id}")
    public ResultModel down(@PathVariable Long id) {
        DgmCouponIntegral entity = service.queryById(id);
        if (entity != null) {
            entity.setStatus(1);
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
}


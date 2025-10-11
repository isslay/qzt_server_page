package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztBanner;
import com.qzt.bus.rpc.api.IQztBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author snow
 * @since 2019-11-07
 */
@RestController
@RequestMapping("/back/qztBanner")
@Api(value = "QztBannerController", description = "QztBannerController")
public class QztBannerController extends BaseController {

    @Autowired
    private IQztBannerService service;

    /**
    * 根据ID查询
    *
    * @param id ID
    * @return ResultModel
    * @author snow
    * @date 2019-11-07
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztBanner entity = service.selectById(id);
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
    * @date 2019-11-07
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<QztBanner> pageModel) {
        try {
            pageModel = (PageModel<QztBanner>) service.find(pageModel);
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
     * @date 2019-11-07
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody QztBanner entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztBanner entityback = service.add(entity);
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
     * @date 2019-11-07
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody QztBanner entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztBanner entityback = service.modifyById(entity);
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
     * @date 2019-11-07
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
        QztBanner entity = service.queryById(id);
        if(entity!=null){
            entity.setState("01");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
    @PostMapping("/down/{id}")
    public ResultModel down(@PathVariable Long id) {
        QztBanner entity = service.queryById(id);
        if(entity!=null){
            entity.setState("02");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
}


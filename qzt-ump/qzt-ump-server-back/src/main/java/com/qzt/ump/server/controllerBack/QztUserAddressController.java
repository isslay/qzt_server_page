package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztUserAddress;
import com.qzt.bus.rpc.api.IQztUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztUserAddress")
@Api(value = "QztUserAddressController", description = "QztUserAddressController")
public class QztUserAddressController extends BaseController {

    @Autowired
    private IQztUserAddressService service;

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
            QztUserAddress entity = service.selectById(id);
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
    public ResultModel queryListPage(@RequestBody PageModel<QztUserAddress> pageModel) {
        try {
            pageModel = (PageModel<QztUserAddress>) service.find(pageModel);
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
     * @author Xiaofei
     * @date 2019-11-11
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody QztUserAddress entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztUserAddress entityback = service.add(entity);
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
     * @author Xiaofei
     * @date 2019-11-11
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody QztUserAddress entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztUserAddress entityback = service.modifyById(entity);
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
     * @author Xiaofei
     * @date 2019-11-11
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


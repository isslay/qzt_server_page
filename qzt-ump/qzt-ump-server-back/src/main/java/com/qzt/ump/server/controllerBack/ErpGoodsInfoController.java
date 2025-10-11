package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.ErpGoodsInfo;
import com.qzt.bus.rpc.api.IErpGoodsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 商品信息表前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2023-08-28
 */
@RestController
@RequestMapping("/back/erpGoodsInfo")
@Api(value = "ErpGoodsInfoController", description = "ErpGoodsInfoController")
public class ErpGoodsInfoController extends BaseController {

    @Autowired
    private IErpGoodsInfoService service;

    /**
    * 根据商品信息表ID查询
    *
    * @param id 商品信息表ID
    * @return ResultModel
    * @author Xiaofei
    * @date 2023-08-28
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            ErpGoodsInfo entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询商品信息表分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author Xiaofei
    * @date 2023-08-28
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<ErpGoodsInfo> pageModel) {
        try {
            pageModel = (PageModel<ErpGoodsInfo>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    @PostMapping("/getList")
    public ResultModel getList(@RequestBody ErpGoodsInfo pageModel) {
        try {
            List<ErpGoodsInfo> list = service.findAll(pageModel);
            return ResultUtil.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增商品信息表方法
     *
     * @param entity 商品信息表实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2023-08-28
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody ErpGoodsInfo entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            ErpGoodsInfo entityback = service.add(entity);
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
     * 修改商品信息表方法
     *
     * @param entity 商品信息表实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2023-08-28
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody ErpGoodsInfo entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            ErpGoodsInfo entityback = service.modifyById(entity);
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
     * 批量删除商品信息表方法
     *
     * @param ids 商品信息表ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2023-08-28
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


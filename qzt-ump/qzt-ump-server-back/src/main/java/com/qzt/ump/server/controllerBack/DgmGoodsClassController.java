package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmGoodsClass;
import com.qzt.bus.rpc.api.IDgmGoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * <p>
 * 商品分类前端控制器
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
@RestController
@RequestMapping("/back/dgmGoodsClass")
@Api(value = "DgmGoodsClassController", description = "DgmGoodsClassController")
public class DgmGoodsClassController extends BaseController {

    @Autowired
    private IDgmGoodsClassService service;

    /**
    * 根据商品分类ID查询
    *
    * @param id 商品分类ID
    * @return ResultModel
    * @author snow
    * @date 2023-09-25
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmGoodsClass entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询商品分类分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2023-09-25
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmGoodsClass> pageModel) {
        try {
            pageModel = (PageModel<DgmGoodsClass>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增商品分类方法
     *
     * @param entity 商品分类实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-25
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmGoodsClass entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmGoodsClass entityback = service.add(entity);
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
     * 修改商品分类方法
     *
     * @param entity 商品分类实体
     * @return ResultModel
     * @author snow
     * @date 2023-09-25
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmGoodsClass entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmGoodsClass entityback = service.modifyById(entity);
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
     * 批量删除商品分类方法
     *
     * @param ids 商品分类ID集合
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


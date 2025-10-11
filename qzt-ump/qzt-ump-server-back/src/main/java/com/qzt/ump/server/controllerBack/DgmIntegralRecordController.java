package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.DgmIntegralRecord;
import com.qzt.bus.rpc.api.IDgmIntegralRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 积分记录表前端控制器
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@RestController
@RequestMapping("/back/dgmIntegralRecord")
@Api(value = "DgmIntegralRecordController", description = "DgmIntegralRecordController")
public class DgmIntegralRecordController extends BaseController {

    @Autowired
    private IDgmIntegralRecordService service;

    /**
    * 根据积分记录表ID查询
    *
    * @param id 积分记录表ID
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            DgmIntegralRecord entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
    * 查询积分记录表分页方法
    *
    * @param pageModel 分页实体
    * @return ResultModel
    * @author snow
    * @date 2024-02-19
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<DgmIntegralRecord> pageModel) {
        try {
            pageModel = (PageModel<DgmIntegralRecord>) service.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增积分记录表方法
     *
     * @param entity 积分记录表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody DgmIntegralRecord entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            DgmIntegralRecord entityback = service.add(entity);
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
     * 修改积分记录表方法
     *
     * @param entity 积分记录表实体
     * @return ResultModel
     * @author snow
     * @date 2024-02-19
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody DgmIntegralRecord entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            DgmIntegralRecord entityback = service.modifyById(entity);
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
     * 批量删除积分记录表方法
     *
     * @param ids 积分记录表ID集合
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


package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztHealthInfo;
import com.qzt.bus.rpc.api.IQztHealthInfoService;
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
@RequestMapping("/back/qztHealthInfo")
@Api(value = "QztHealthInfoController", description = "QztHealthInfoController")
public class QztHealthInfoController extends BaseController {

    @Autowired
    private IQztHealthInfoService service;

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
            QztHealthInfo entity = service.selectById(id);
            if (!StringUtil.isEmpty(entity.getInfoRemark())) {
                String newInfo = entity.getInfoRemark().replaceAll("audio", "embed type=\"application/x-shockwave-flash\"");
                entity.setInfoRemark(newInfo);
            }
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
    public ResultModel queryListPage(@RequestBody PageModel<QztHealthInfo> pageModel) {
        try {
            pageModel = (PageModel<QztHealthInfo>) service.find(pageModel);
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
    public ResultModel add(@Valid @RequestBody QztHealthInfo entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            String newInfo = entity.getInfoRemark().replaceAll("embed type=\"application/x-shockwave-flash\"","audio");
            entity.setInfoRemark(newInfo);
            QztHealthInfo entityback = service.add(entity);
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
    public ResultModel modify(@RequestBody QztHealthInfo entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            if (!StringUtil.isEmpty(entity.getInfoRemark())) {
                String newInfo = entity.getInfoRemark().replaceAll("audio", "embed type=\"application/x-shockwave-flash\"");
                entity.setInfoRemark(newInfo);
            }
            QztHealthInfo entityback = service.modifyById(entity);
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
        QztHealthInfo entity = service.queryById(id);
        if(entity!=null){
            entity.setState("1");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
    @PostMapping("/down/{id}")
    public ResultModel down(@PathVariable Long id) {
        QztHealthInfo entity = service.queryById(id);
        if(entity!=null){
            entity.setState("2");
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            service.modifyById(entity);
        }
        return ResultUtil.ok();
    }
}


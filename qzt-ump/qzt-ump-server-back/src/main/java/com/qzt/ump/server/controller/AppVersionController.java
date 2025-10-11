package com.qzt.ump.server.controller;

import com.qzt.common.redis.util.CacheUtil;
import com.qzt.ump.rpc.api.AppVersionService;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-26
 */

@RestController
@RequestMapping("/back/appVersion")
public class AppVersionController extends BaseController {

    @Autowired
    private AppVersionService service;

    /**
     * 新增APP版本信息方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-06-23
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody AppVersion entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            AppVersion appVersion = this.service.add(entity);
            if (appVersion == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
            } else {
                //向redis 加 版本数据
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersion.getSourceMode() + ":" + appVersion.getVersionNo(), appVersion.getVersionNo() + "_" + appVersion.getIsForcedUpdate() + "_" + appVersion.getDownloadUrl() + "_" + appVersion.getRemark());
                this.service.updateVersionCache(entity);
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 更新APP版本信息方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-27
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody AppVersion entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            AppVersion appVersion = this.service.modifyById(entity);
            if (appVersion != null) {
                //向redis 更新版本数据
                CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.APPVERSION.value() + appVersion.getSourceMode() + ":" + appVersion.getVersionNo(), appVersion.getVersionNo() + "_" + appVersion.getIsForcedUpdate() + "_" + appVersion.getDownloadUrl() + "_" + appVersion.getRemark());
                this.service.updateVersionCache(entity);
                return ResultUtil.ok();
            } else {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-27
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        AppVersion entity = service.selectById(id);
        return ResultUtil.ok(entity);
    }

    /**
     * 查询分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-27
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<AppVersion> pageModel) {
        pageModel = (PageModel<AppVersion>) service.find(pageModel);
        return ResultUtil.ok(pageModel);
    }

    /**
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-27
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        return ResultUtil.ok(service.deleteBatchIds(Arrays.asList(ids)));
    }

}


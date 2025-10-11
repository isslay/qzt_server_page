package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysDic;
import com.qzt.ump.model.SysDicSublist;
import com.qzt.ump.rpc.api.SysDicSublistService;
import com.qzt.ump.rpc.api.SysDicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */

@RestController
@RequestMapping("/back/sysDic")
@Api(value = "SysDicController", description = "新版字典管理")
public class SysDicController extends BaseController {

    @Autowired
    private SysDicService service;

    @Autowired
    private SysDicSublistService iSysDicsSublistService;


    /**
     * 分页查询字典主表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        pageModel = (PageModel) service.find(pageModel);
        return ResultUtil.ok(pageModel);
    }


    /**
     * 分页查询字典子表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/queryListSublistPage")
    public ResultModel queryListSublistPage(@RequestBody PageModel pageModel) {
        pageModel = (PageModel) service.queryListSublistPage(pageModel);
        return ResultUtil.ok(pageModel);
    }


    /**
     * 查询主表字典详情
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @GetMapping("/queryz/{id}")
    public ResultModel query(@PathVariable Long id) {
        return ResultUtil.ok(service.selectById(id));
    }


    /**
     * 字典主表新增
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody SysDic entity) {
        if (entity != null) {
            entity.setIsDel("0");
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
        }
        List<SysDic> mapList = this.service.verifyDicType(entity.getDicType());
        if (mapList != null && mapList.size() > 0) {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, "字典类型重复");
        }
        return ResultUtil.ok(this.service.add(entity));
    }

    /**
     * 字典主表修改
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody SysDic entity) {
        entity.setUpdateBy(this.getCurrentUserId());
        entity.setUpdateTime(new Date());
        SysDic sysDic = this.service.queryById(entity.getId());
        List<SysDic> mapList = this.service.verifyDicType(entity.getDicType());
        if (mapList != null && mapList.size() > 0) {
            for (SysDic dics : mapList) {
                if (!sysDic.getDicType().equals(dics.getDicType())) {
                    return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, "字典类型重复");
                }
            }
        }
        return ResultUtil.ok(this.service.modifyById(entity));
    }

    /**
     * 主表字典删除单条
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/delectById/{id}")
    public ResultModel delectById(@PathVariable Long id) {
        SysDic entity = new SysDic();
        entity.setIsDel("1");
        entity.setId(id);
        return ResultUtil.ok(this.service.modifyById(entity));
    }


    /**
     * 主表字典启用停用
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/enableById")
    public ResultModel enableById(Long id, String enable) {
        SysDic entity = new SysDic();
        entity.setEnable(enable);
        entity.setId(id);
        return ResultUtil.ok(this.service.modifyById(entity));
    }


    /**
     * 查询子表字典详情
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @GetMapping("/querySublist/{id}")
    public ResultModel querySublist(@PathVariable Long id) {
        return ResultUtil.ok(this.iSysDicsSublistService.selectById(id));
    }


    /**
     * 字典子表新增
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/addSublist")
    public ResultModel addSublist(@Valid @RequestBody SysDicSublist entity) {
        if (entity != null) {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
        }
        List<Map> mapList = this.service.verifyDicCode(entity.getCode(), entity.getParentId());
        if (mapList != null && mapList.size() > 0) {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, "关键字重复");
        }
        return ResultUtil.ok(this.iSysDicsSublistService.add(entity));
    }

    /**
     * 字典子表修改
     *
     * @param entity 实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/modifySublist")
    public ResultModel modifySublist(@RequestBody SysDicSublist entity) {
        entity.setUpdateBy(this.getCurrentUserId());
        entity.setUpdateTime(new Date());
        SysDicSublist sysDicsSublist = this.iSysDicsSublistService.queryById(entity.getId());
        List<Map> mapList = this.service.verifyDicCode(entity.getCode(), entity.getParentId());
        if (mapList != null && mapList.size() > 0) {
            for (Map map : mapList) {
                String code = map.get("code") + "";
                if (!sysDicsSublist.getCode().equals(code)) {
                    return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, "关键字重复");
                }
            }
        }
        return ResultUtil.ok(this.iSysDicsSublistService.modifyById(entity));
    }

    /**
     * 字典子表删除单条
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/delectSublistById/{id}")
    public ResultModel delectSublistById(@PathVariable Long id) {
        Long[] ids = {id};
        return ResultUtil.ok(this.iSysDicsSublistService.deleteBatchIds(Arrays.asList(ids)));
    }


    /**
     * 字典子表启用停用
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06
     */
    @PostMapping("/enableSublistById")
    public ResultModel enableSublistById(Long id, String enable) {
        SysDicSublist entity = new SysDicSublist();
        entity.setEnable(enable);
        entity.setId(id);
        return ResultUtil.ok(this.iSysDicsSublistService.modifyById(entity));
    }


    /**
     * 新版查询字典列表分页
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-06 15:22
     */
    @ApiOperation(value = "新版查询字典列表分页", notes = "新版查询字典列表分页")
    @PostMapping("/dicListPage")
    @RequiresPermissions("sys:dic:read")
    public ResultModel queryDicListPage(@RequestBody PageModel pageModel) {
        pageModel = (PageModel) this.service.find(pageModel);
        return ResultUtil.ok(pageModel);
    }

    /**
     * 一键添加redis
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-09-14
     */
    @PostMapping("/oneKeyDics")
    public ResultModel oneKeyDics() {
        try {
            this.service.initializeDic();
            return ResultUtil.ok("200");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY, e.getMessage());
        }
    }
}


package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtil;
import com.qzt.ump.model.SysDeptModel;
import com.qzt.ump.model.SysTreeModel;
import com.qzt.ump.rpc.api.SysDeptService;
import com.qzt.ump.server.annotation.SysLogOpt;
import com.xiaoleilu.hutool.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 部门管理控制器
 *
 * @author Ricky Wang
 * @date 17/12/1 11:23:17
 */
@RestController
@RequestMapping("/dept")
@Api(value = "部门管理", description = "部门管理")
public class SysDeptController extends BaseController {

    @Autowired
    private ReturnUtil returnUtil;

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 根据部门ID查询
     *
     * @param id 部门ID
     * @return ResultModel<SysDeptModel>
     * @author RickyWang
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询部门", notes = "根据部门主键ID查询部门")
    @ApiImplicitParam(name = "id", value = "部门ID", required = true, dataType = "Long")
    @GetMapping("/query/{id}")
    @RequiresPermissions("sys:dept:read")
    public ResultModel query(@PathVariable Long id) {
        Assert.notNull(id);
        SysDeptModel sysDeptModel = sysDeptService.queryOne(id);
        return ResultUtil.ok(sysDeptModel);
    }

    /**
     * 查询部门分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:28:13
     */
    @ApiOperation(value = "分页查询部门列表", notes = "根据分页参数查询部门列表")
    @PostMapping("/queryListPage")
    @RequiresPermissions("sys:dept:read")
    public ResultModel queryListPage(@RequestBody PageModel<SysDeptModel> pageModel) {
        pageModel = (PageModel<SysDeptModel>) sysDeptService.queryListPage(pageModel);
        return ResultUtil.ok(pageModel);
    }

    /**
     * 新增部门方法
     *
     * @param sysDeptModel 部门实体
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:28:41
     */
    @ApiOperation(value = "新增部门", notes = "根据部门实体新增部门")
    @PostMapping("/add")
    @RequiresPermissions("sys:dept:add")
    @SysLogOpt(module = "部门管理", value = "部门新增", operationType = SysConstant.LogOptEnum.ADD)
    public ResultModel add(@Valid @RequestBody SysDeptModel sysDeptModel) {
        if (sysDeptModel != null) {
            sysDeptModel.setCreateBy(super.getCurrentUserId());
            sysDeptModel.setUpdateBy(super.getCurrentUserId());
        }
        return ResultUtil.ok(sysDeptService.addDept(sysDeptModel));
    }

    /**
     * 修改部门方法
     *
     * @param sysDeptModel 部门实体
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:29:09
     */
    @ApiOperation(value = "修改部门", notes = "根据主键ID修改部门")
    @PutMapping("/modify")
    @RequiresPermissions("sys:dept:update")
    @SysLogOpt(module = "部门管理", value = "部门修改", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modify(@RequestBody SysDeptModel sysDeptModel) {
        sysDeptModel.setUpdateBy(super.getCurrentUserId());
        sysDeptModel.setUpdateTime(new Date());
        sysDeptService.modifyDept(sysDeptModel);
        return ResultUtil.ok();
    }

    /**
     * 批量删除部门方法
     *
     * @param ids 部门ID集合
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:29:23
     */
    @ApiOperation(value = "批量删除部门", notes = "根据主键ID集合批量删除部门")
    @DeleteMapping("/delBatchByIds")
    @RequiresPermissions("sys:dept:delete")
    @SysLogOpt(module = "部门管理", value = "部门批量删除", operationType = SysConstant.LogOptEnum.DELETE)
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        Assert.notNull(ids);
        return ResultUtil.ok(sysDeptService.deleteBatch(ids));
    }

    /**
     * 根据部门id过滤查询部门树方法
     *
     * @param id 部门ID
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:29:52
     */
    @ApiOperation(value = "查询部门树", notes = "根据部门ID查询部门树结构数据")
    @GetMapping("/queryTree/{id}")
    @RequiresPermissions("sys:dept:read")
    public ResultModel queryTree(@PathVariable(value = "id", required = false) Long id) {
        List<SysTreeModel> list = sysDeptService.queryTree(id);
        return ResultUtil.ok(list);
    }

    /**
     * 查询部门树方法
     *
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:30:28
     */
    @ApiOperation(value = "查询部门树", notes = "查询全部部门树结构数据")
    @GetMapping("/queryTree")
    @RequiresPermissions("sys:dept:read")
    public ResultModel queryTree() {
        List<SysTreeModel> list = sysDeptService.queryTree();
        return ResultUtil.ok(list);
    }

    /**
     * 查询部门树通用方法
     *
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:30:28
     */
    @ApiOperation(value = "查询部门树通用方法", notes = "查询部门树通用方法")
    @GetMapping("/queryTreeDept/{id}")
    @RequiresPermissions("sys:dept:read")
    public Map<String, Object> queryTreeDept(@PathVariable(value = "id", required = false) Long id) {
        try {
            id = id == null ? 0 : id;
            List<SysDeptModel> sysDeptModelList = this.sysDeptService.querySubDept(id);
            //一级菜单list
            List<Map> mapListStair = new ArrayList();
            Map fmap = new HashMap();
            fmap.put("title", "一级部门");
            mapListStair.add(fmap);
            for (SysDeptModel sysDeptModel : sysDeptModelList) {
                Map rmapStair = new HashMap();
                rmapStair.put("title", sysDeptModel.getDeptName());
                rmapStair.put("id", sysDeptModel.getId());
                rmapStair.put("parentId", sysDeptModel.getParentId());
//                rmapStair.put("type", sysDeptModel.getType());
                List<SysDeptModel> secondLevelDeptModelList = this.sysDeptService.querySubDept(sysDeptModel.getId());
                List<Map> childrenStair = new ArrayList();//二级菜单
                if (secondLevelDeptModelList != null && secondLevelDeptModelList.size() > 0) {
                    for (SysDeptModel secondLevelDeptModels : secondLevelDeptModelList) {
                        Map rmapSecondLevel = new HashMap();
                        rmapSecondLevel.put("title", secondLevelDeptModels.getDeptName());
                        rmapSecondLevel.put("id", secondLevelDeptModels.getId());
                        rmapSecondLevel.put("parentId", secondLevelDeptModels.getParentId());
//                        rmapSecondLevel.put("type", secondLevelDeptModels.getType());
                        List<SysDeptModel> threeDeptModelList = this.sysDeptService.querySubDept(secondLevelDeptModels.getId());
                        List<Map> childrenSecondLevel = new ArrayList();
                        if (threeDeptModelList != null && threeDeptModelList.size() > 0) {
                            for (SysDeptModel threeDeptModels : threeDeptModelList) {
                                System.out.println(threeDeptModels);
                                Map rmapThree = new HashMap();
                                rmapThree.put("title", threeDeptModels.getDeptName());
                                rmapThree.put("id", threeDeptModels.getId());
                                rmapThree.put("parentId", threeDeptModels.getParentId());
//                                rmapThree.put("type", threeDeptModels.getType());
                                childrenSecondLevel.add(rmapThree);
                            }
                        }
                        rmapSecondLevel.put("children", childrenSecondLevel);
                        childrenStair.add(rmapSecondLevel);
                    }
                }
                rmapStair.put("children", childrenStair);
                mapListStair.add(rmapStair);
            }
            return returnUtil.returnMess(mapListStair);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 删除部门方法
     *
     * @param id 部门ID
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:30:45
     */
    @ApiOperation(value = "删除部门", notes = "根据部门ID删除部门")
    @DeleteMapping("/delDept")
    @RequiresPermissions("sys:dept:delete")
    @SysLogOpt(module = "部门管理", value = "部门删除", operationType = SysConstant.LogOptEnum.DELETE)
    public ResultModel delDept(@RequestBody Long id) {
        Assert.notNull(id);
        return ResultUtil.ok(sysDeptService.delDept(id));
    }

}

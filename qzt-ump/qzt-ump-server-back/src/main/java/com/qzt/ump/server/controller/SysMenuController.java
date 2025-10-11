package com.qzt.ump.server.controller;


import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.redis.util.DicParamUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtil;
import com.qzt.ump.model.SysMenuModel;
import com.qzt.ump.model.SysTreeModel;
import com.qzt.ump.rpc.api.SysMenuService;
import com.qzt.ump.server.annotation.SysLogOpt;
import com.xiaoleilu.hutool.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author cgw
 * @since 2017-11-29
 */
@RestController
@Slf4j
@RequestMapping("/menu")
@Api(value = "菜单管理", description = "菜单管理")
public class SysMenuController extends BaseController {

    @Autowired
    private ReturnUtil returnUtil;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 查询所有菜单
     *
     * @return ResultModel
     * @author cgw
     * @date 2017-12-02 00:24
     */
    @ApiOperation(value = "查询菜单列表", notes = "查询全部菜单列表")
    @GetMapping("/getList")
    public ResultModel queryList() {
        return ResultUtil.ok(sysMenuService.queryList());
    }

    /**
     * 分页查询菜单列表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:34
     */
    @ApiOperation(value = "分页查询菜单列表", notes = "根据分页参数查询菜单列表")
    @PostMapping("/queryListPage")
    @RequiresPermissions("sys:menu:read")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        return ResultUtil.ok(sysMenuService.queryListPage(pageModel));
    }

    /**
     * 查询用户权限菜单
     *
     * @param userId 用户ID
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:36
     */
    @ApiOperation(value = "查询用户权限菜单", notes = "根据用户ID查询用户权限菜单")
    @GetMapping("/tree/{userId}")
    @RequiresAuthentication
    public ResultModel queryMenuTreeByUserId(@PathVariable(value = "userId") Long userId) {
        List<SysTreeModel> rs = sysMenuService.queryMenuTreeByUserId(userId);

        log.info("rs===" + rs.size());
        return ResultUtil.ok(rs);
    }

    /**
     * 根据ID删除菜单
     *
     * @param id 菜单ID
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:51
     */
    @ApiOperation(value = "删除菜单", notes = "根据菜单ID删除菜单")
    @DeleteMapping("/delete")
    @RequiresPermissions("sys:menu:delete")
    @SysLogOpt(module = "菜单管理", value = "菜单删除", operationType = SysConstant.LogOptEnum.DELETE)
    public ResultModel delete(@RequestBody Long id) {
        return ResultUtil.ok(sysMenuService.delete(id));
    }

    /**
     * 批量删除菜单
     *
     * @param ids 菜单ID集合
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:52
     */
    @ApiOperation(value = "批量删除菜单", notes = "根据主键ID集合批量删除菜单")
    @PostMapping("/deleteBatchIds")
    @RequiresPermissions("sys:menu:delete")
    @SysLogOpt(module = "菜单管理", value = "菜单批量删除", operationType = SysConstant.LogOptEnum.DELETE)
    public ResultModel deleteBatchIds(@RequestBody Long[] ids) {
        Assert.notNull(ids);
        return ResultUtil.ok(sysMenuService.deleteBatch(ids));
    }

    /**
     * 查询菜单
     *
     * @param id 菜单ID
     * @return ResultModel
     * @author cgw
     * @date 2018-01-03 14:10
     */
    @ApiOperation(value = "查询菜单", notes = "根据主键ID查询菜单")
    @GetMapping("/query/{id}")
    @RequiresPermissions("sys:menu:read")
    public ResultModel query(@PathVariable(value = "id") Long id) {
        Assert.notNull(id);
        SysMenuModel sysMenuModel = sysMenuService.queryById(id);
        return ResultUtil.ok(sysMenuModel);
    }

    /**
     * 根据ID修改菜单
     *
     * @param sysMenuModel 菜单实体
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:54
     */
    @ApiOperation(value = "修改菜单", notes = "根据主键ID修改菜单")
    @PostMapping("/modify")
    @RequiresPermissions("sys:menu:update")
    @SysLogOpt(module = "菜单管理", value = "菜单修改", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modify(@RequestBody SysMenuModel sysMenuModel) {
        sysMenuModel.setUpdateBy(super.getCurrentUserId());
        sysMenuModel.setUpdateTime(new Date());
        sysMenuService.modifyById(sysMenuModel);
        return ResultUtil.ok();
    }

    /**
     * 新增菜单
     *
     * @param sysMenuModel 菜单实体
     * @return ResultModel
     * @author shadj
     * @date 2017/12/18 21:54
     */
    @ApiOperation(value = "新增菜单", notes = "根据菜单实体新增菜单")
    @PostMapping("/add")
    @RequiresPermissions("sys:menu:add")
    @SysLogOpt(module = "菜单管理", value = "菜单新增", operationType = SysConstant.LogOptEnum.ADD)
    public ResultModel add(@Valid @RequestBody SysMenuModel sysMenuModel) {
        if (sysMenuModel != null) {
            Date now = new Date();
            sysMenuModel.setCreateTime(now);
            sysMenuModel.setCreateBy(super.getCurrentUserId());
            sysMenuModel.setUpdateBy(super.getCurrentUserId());
            sysMenuModel.setUpdateTime(now);
        }
        sysMenuService.add(sysMenuModel);
        return ResultUtil.ok();
    }

    /**
     * 根据角色ID查询角色对应权限功能树
     *
     * @param roleId 角色ID
     * @return ResultModel
     * @author cgw
     * @date 2018-01-03 19:20
     */
    @ApiOperation(value = "查询权限功能树", notes = "根据角色ID查询角色对应权限功能树")
    @PostMapping("/roleFuncTree")
    @RequiresPermissions("sys:menu:read")
    public ResultModel queryFuncMenuTree(@RequestBody Long roleId) {
        List<SysTreeModel> treeModelList = sysMenuService.queryFuncMenuTree(roleId);
        return ResultUtil.ok(treeModelList);
    }

    /**
     * 查询所有权限功能树
     *
     * @return ResultModel
     * @author cgw
     * @date 2018-01-03 19:22
     */
    @ApiOperation(value = "查询所有权限功能树", notes = "查询所有权限功能树")
    @PostMapping("/funcTree")
    @RequiresPermissions("sys:menu:read")
    public ResultModel queryFuncMenuTree() {
        List<SysTreeModel> treeModelList = sysMenuService.queryFuncMenuTree(null);
        return ResultUtil.ok(treeModelList);
    }

    /**
     * 根据菜单类型和菜单ID查询菜单树
     *
     * @param menuType 菜单类型
     * @param menuId   菜单ID
     * @return ResultModel
     * @author cgw
     * @date 2018-01-03 19:23
     */
    @ApiOperation(value = "查询菜单树", notes = "根据菜单类型和菜单ID查询菜单树")
    @GetMapping("/queryTree/{menuType}/{menuId}")
    @RequiresPermissions("sys:menu:read")
    public ResultModel queryTree(@PathVariable(required = false, value = "menuType") Integer menuType, @PathVariable(value = "menuId") Long menuId) {
        List<SysTreeModel> list = sysMenuService.queryTree(menuId, menuType);
        return ResultUtil.ok(list);
    }

    /**
     * 根据菜单类型查询菜单树
     *
     * @param menuType 菜单类型
     * @return ResultModel
     * @author cgw
     * @date 2018-01-03 19:25
     */
    @ApiOperation(value = "查询菜单树", notes = "根据菜单类型查询菜单树")
    @GetMapping("/queryTree/{menuType}")
    @RequiresPermissions("sys:menu:read")
    public ResultModel queryTree(@PathVariable(required = false, value = "menuType") Integer menuType) {
        List<SysTreeModel> list = sysMenuService.queryTree(null, menuType);
        return ResultUtil.ok(list);
    }

    /**
     * 分页查询菜单
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-04-17
     */
    @PostMapping("/selectListPage")
    public ResultModel selectListPage(@RequestBody PageModel pageModel) {
        try {
            pageModel = (PageModel) this.sysMenuService.find(pageModel);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }


    /**
     * 查询菜单树
     *
     * @return Map
     * @author Xiaofei
     * @date 2019-10-16
     */
    @ApiOperation(value = "查询菜单树", notes = "查询菜单树")
    @GetMapping("/getMenuTree")
    @RequiresPermissions("sys:menu:read")
    public Map<String, Object> getMenuTree() {
        try {
            List<SysTreeModel> stairTreeModelList = this.sysMenuService.queryTreeMenu(null, 2, 0L);
            //一级菜单list
            List<Map> mapListStair = new ArrayList();
            for (SysTreeModel stairTreeModel : stairTreeModelList) {
                Map rmapStair = new HashMap();
                rmapStair.put("title", stairTreeModel.getName() + "(" + DicParamUtil.getDicCodeByType("SYS_MENU_TYPE", stairTreeModel.getType().toString()) + ")");
                rmapStair.put("id", stairTreeModel.getId());
                rmapStair.put("parentId", stairTreeModel.getParentId());
                rmapStair.put("type", stairTreeModel.getType());
                List<SysTreeModel> secondLevelTreeModelList = this.sysMenuService.queryTreeMenu(null, 2, stairTreeModel.getId());
                List<Map> childrenStair = new ArrayList();//二级菜单
                if (secondLevelTreeModelList != null && secondLevelTreeModelList.size() > 0) {
                    for (SysTreeModel secondLevelTreeModels : secondLevelTreeModelList) {
                        Map rmapSecondLevel = new HashMap();
                        rmapSecondLevel.put("title", secondLevelTreeModels.getName() + "(" + DicParamUtil.getDicCodeByType("SYS_MENU_TYPE", secondLevelTreeModels.getType().toString()) + ")");
                        rmapSecondLevel.put("id", secondLevelTreeModels.getId());
                        rmapSecondLevel.put("parentId", secondLevelTreeModels.getParentId());
                        rmapSecondLevel.put("type", secondLevelTreeModels.getType());
                        List<SysTreeModel> threeTreeModelList = this.sysMenuService.queryTreeMenu(null, 2, secondLevelTreeModels.getId());
                        List<Map> childrenSecondLevel = new ArrayList();
                        if (threeTreeModelList != null && threeTreeModelList.size() > 0) {
                            for (SysTreeModel threeTreeModels : threeTreeModelList) {
                                System.out.println(threeTreeModels);
                                Map rmapThree = new HashMap();
                                rmapThree.put("title", threeTreeModels.getName() + "(" + DicParamUtil.getDicCodeByType("SYS_MENU_TYPE", threeTreeModels.getType().toString()) + ")");
                                rmapThree.put("id", threeTreeModels.getId());
                                rmapThree.put("parentId", threeTreeModels.getParentId());
                                rmapThree.put("type", threeTreeModels.getType());
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

}


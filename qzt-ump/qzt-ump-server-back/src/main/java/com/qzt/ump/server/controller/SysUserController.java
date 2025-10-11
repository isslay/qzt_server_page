package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.exception.BusinessException;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysUserModel;
import com.qzt.ump.model.SysUserRoleModel;
import com.qzt.ump.rpc.api.SysUserService;
import com.qzt.ump.server.annotation.SysLogOpt;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.lang.Assert;
import com.xiaoleilu.hutool.util.ObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author cgw
 * @date 2017/11/17 00:22
 */
@Slf4j
@Api(value = "用户管理", description = "用户管理")
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据用户ID查询用户
     *
     * @param id
     * @return ResultModel<SysUserModel>
     * @author cgw
     * @date 2017-12-05 13:35
     */
    @ApiOperation(value = "查询用户", notes = "根据用户主键ID查询用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Long")
    @GetMapping("/query/{id}")
    @RequiresPermissions("sys:user:read")
    public ResultModel query(@PathVariable(value = "id") Long id) {
        Assert.notNull(id);
        SysUserModel sysUserModel = sysUserService.queryOne(id);
        return ResultUtil.ok(sysUserModel);
    }

    /**
     * 分页查询用户列表
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author cgw
     * @date 2017/12/2 14:31
     */
    @ApiOperation(value = "分页查询用户列表", notes = "根据分页参数查询用户列表")
    @PostMapping("/listPage")
    @RequiresPermissions("sys:user:read")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        return ResultUtil.ok(sysUserService.queryListPage(pageModel));
    }

    /**
     * 新增用户
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author cgw
     * @date 2017-12-03 10:18
     */
    @ApiOperation(value = "新增用户", notes = "根据用户实体新增用户")
    @PostMapping("/add")
    @RequiresPermissions("sys:user:add")
    @SysLogOpt(module = "用户管理", value = "用户新增", operationType = SysConstant.LogOptEnum.ADD)
    public ResultModel add(@Valid @RequestBody SysUserModel sysUserModel) {
        SysUserModel existSysUserModel = sysUserService.queryByAccount(sysUserModel.getAccount());
        if (ObjectUtil.isNotNull(existSysUserModel)) {
            throw new BusinessException("已存在相同账号的用户");
        }
        // 设置初始密码: 123456
        sysUserModel.setPassword(SecureUtil.md5("123456"));
        sysUserModel.setCreateBy(getCurrentUserId());
        sysUserService.add(sysUserModel);
        return ResultUtil.ok();
    }

    /**
     * 根据用户ID集合批量删除用户
     *
     * @param ids 用户ID集合
     * @return ResultModel
     * @author cgw
     * @date 2018-01-04 11:32
     */
    @ApiOperation(value = "批量删除用户", notes = "根据主键ID集合批量删除用户")
    @PostMapping("/delBatchByIds")
    @RequiresPermissions("sys:user:delete")
    @SysLogOpt(module = "用户管理", value = "用户批量删除", operationType = SysConstant.LogOptEnum.DELETE)
    public ResultModel delBatchByIds(@RequestBody List<Long> ids) {
        if (ids.size() == 0) {
            throw new BusinessException("用户ID集合不能为空");
        }
        return ResultUtil.ok(sysUserService.delBatchByIds(ids));
    }

    /**
     * 修改用户
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author cgw
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改用户", notes = "根据用户ID修改用户")
    @PostMapping("/modify")
    @RequiresPermissions("sys:user:update")
    @SysLogOpt(module = "用户管理", value = "用户修改", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modify(@RequestBody SysUserModel sysUserModel) {
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserModel.setUpdateTime(new Date());
        return ResultUtil.ok(sysUserService.modifyUser(sysUserModel));
    }


    /**
     * 停用启用用户
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-08-02 11:33
     */
    @ApiOperation(value = "停用启用用户", notes = "根据用户ID停用启用用户")
    @PostMapping("/modifyStopUp")
    @SysLogOpt(module = "停用启用用户", value = "停用启用用户", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modifyStopUp(Long id, Integer enable) {
        if (id.compareTo(super.getCurrentUserId()) == 0) {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.UNAUTHORIZED.value(), SysConstant.ResultCodeEnum.UNAUTHORIZED.getMessage());
        }
        SysUserModel sysUserModel = new SysUserModel();
        sysUserModel.setId(id);
        sysUserModel.setEnable(enable);
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserModel.setUpdateTime(new Date());
        return ResultUtil.ok(sysUserService.modifyStopUp(sysUserModel));
    }


    /**
     * 个人资料修改
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author cgw
     * @date 2018-01-04 11:33
     */
    @ApiOperation(value = "修改个人资料", notes = "根据用户ID修改用户个人资料")
    @PostMapping("/modifyMySelf")
    @SysLogOpt(module = "用户管理", value = "个人资料修改", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modifyMySelf(@RequestBody SysUserModel sysUserModel) {
        if (!sysUserModel.getId().equals(super.getCurrentUserId())) {
            throw new BusinessException("不能修改其他用户信息");
        }
        sysUserModel.setCreateBy(super.getCurrentUserId());
        sysUserModel.setUpdateTime(new Date());
        return ResultUtil.ok(sysUserService.modifyById(sysUserModel));
    }

    /**
     * 根据用户id查询用户角色关系
     *
     * @param userId 用户ID
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:26:57
     */
    @ApiOperation(value = "查询用户角色关系", notes = "根据用户id查询用户角色关系")
    @GetMapping("/queryUserRoles/{userId}")
    @RequiresPermissions("sys:user:read")
    public ResultModel queryUserRoles(@PathVariable(value = "userId") Long userId) {
        Assert.notNull(userId);
        List<SysUserRoleModel> list = sysUserService.queryUserRoles(userId);
        return ResultUtil.ok(list);
    }

    /**
     * 修改密码
     *
     * @param sysUserModel 用户实体
     * @return ResultModel
     * @author cgw
     * @date 2017/12/30 22:18
     */
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @PostMapping("/modifyPassword")
    @SysLogOpt(module = "用户管理", value = "修改密码", operationType = SysConstant.LogOptEnum.MODIFY)
    public ResultModel modifyPassword(@RequestBody SysUserModel sysUserModel) {
        Assert.notEmpty(sysUserModel.getOldPassword());
        Assert.notEmpty(sysUserModel.getPassword());
        String encryptOldPassword = SecureUtil.md5(sysUserModel.getOldPassword());
        SysUserModel currentSysUserModel = sysUserService.queryById(super.getCurrentUserId());
        if (!encryptOldPassword.equals(currentSysUserModel.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        String encryptPassword = SecureUtil.md5(sysUserModel.getPassword());
        sysUserModel.setPassword(encryptPassword);
        sysUserModel.setId(super.getCurrentUserId());
        return ResultUtil.ok(sysUserService.modifyById(sysUserModel));
    }

    @ApiOperation(value = "查询用户角色关系", notes = "根据用户id查询用户角色关系")
    @PostMapping("/updateUserPwd")
    @SysLogOpt(module = "用户管理", value = "用户修改密码", operationType = SysConstant.LogOptEnum.DELETE)
    @RequiresPermissions("sys:user:read")
    public ResultModel updateUserPwd(@RequestBody List<Long> userIds) {
        for (Long userId : userIds) {
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(userId);
            sysUserModel.setPassword(SecureUtil.md5("123456"));
            sysUserService.updateById(sysUserModel);
        }

        return ResultUtil.ok(true);
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @PostMapping("/updatePassword")
    public ResultModel updatePassword(@RequestBody String account) {

        SysUserModel sysUserModel = sysUserService.queryByAccount(account);
        if (sysUserModel != null) {
            sysUserModel.setPassword(SecureUtil.md5("123456"));
            return ResultUtil.ok(sysUserService.modifyUser1(sysUserModel));
        }
        return ResultUtil.ok();
    }

    /**
     * 根据角色ID查询用户列表
     *
     * @param roleId 角色ID
     * @return ResultModel
     * @author RickyWang
     * @date 17/12/25 21:26:57
     */
    @ApiOperation(value = "角色查询用户列表", notes = "根据角色查询用户列表")
    @GetMapping("/queryUserByRoleId/{roleId}")
    public ResultModel queryUserByRoleId(@PathVariable(value = "roleId") Long roleId) {
        Assert.notNull(roleId);
        return ResultUtil.ok(sysUserService.queryByRoleId(roleId));
    }
}

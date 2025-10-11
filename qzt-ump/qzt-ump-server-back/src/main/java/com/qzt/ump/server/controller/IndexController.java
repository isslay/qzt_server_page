package com.qzt.ump.server.controller;

import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysMenuModel;
import com.qzt.ump.rpc.api.SysAuthorizeService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页前端控制器
 *
 * @author shadj
 * @date 2017/12/31 21:13
 */
@RestController
@Api(value = "订单功能", description = "订单功能")
@RequestMapping("/index")
public class IndexController extends BaseController {

    @Autowired
    private SysAuthorizeService sysAuthorizeService;

    /**
     * 获取当前用户的权限集合
     *
     * @return ResultModel
     * @author shadj
     * @date 2017/12/31 21:10
     */
    @GetMapping("/permissions")
    @RequiresAuthentication
    public ResultModel<List<SysMenuModel>> queryMyPermissions() {
        return ResultUtil.ok(sysAuthorizeService.queryPermissionsByUserId(super.getCurrentUserId()));
    }
}

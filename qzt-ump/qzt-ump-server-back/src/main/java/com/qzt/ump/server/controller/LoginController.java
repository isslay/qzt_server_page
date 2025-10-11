package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.exception.LoginException;
import com.qzt.common.core.model.LoginModel;
import com.qzt.common.core.util.CircleCaptchay;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.common.web.BaseController;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysUserModel;
import com.qzt.ump.rpc.api.SysUserService;
import com.qzt.ump.server.annotation.SysLogOpt;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 登陆控制器
 *
 * @author cgw
 * @date 2017-11-30
 **/
@Slf4j
@RestController
@Api(value = "登录接口", description = "登录接口")
public class LoginController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * z
     * 获取验证码
     *
     * @param captchaId 验证码ID
     * @return ResultModel
     * @author cgw
     * @date 2017-12-27 21:10
     */
    @ApiOperation(value = "获取验证码")
    @GetMapping("/captcha/{captchaId}")
    public ResultModel queryCaptcha(@PathVariable(value = "captchaId", required = false) String captchaId) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CircleCaptchay captcha = new CircleCaptchay(80, 30, 4, 0);
        captcha.createCode();
        if (StrUtil.isBlank(captchaId) || !CacheUtil.getCache().exists(SysConstant.CacheNamespaceEnum.CAPTCHA.value() + captchaId)) {
            captchaId = RandomUtil.randomUUID();
        }
        CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.CAPTCHA.value() + captchaId, captcha.getCode(), 120);
        captcha.write(outputStream);
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("captchaId", captchaId);
        map.put("captcha", Base64.encode(outputStream.toByteArray()));
        return ResultUtil.ok(map);
    }

    /**
     * 登陆
     *
     * @param loginModel 登录对象
     * @return ResultModel
     * @author cgw
     * @date 2017-11-30 16:14
     */
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    @SysLogOpt(module = "登录接口", value = "用户登录", operationType = SysConstant.LogOptEnum.LOGIN)
    public ResultModel login(@Valid @RequestBody LoginModel loginModel) {
        // 校验验证码
        String redisCaptchaValue = (String) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.CAPTCHA.value() + loginModel.getCaptchaId());
        if (StrUtil.isBlank(redisCaptchaValue)) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.getMessage());
        }
        if (!redisCaptchaValue.equalsIgnoreCase(loginModel.getCaptchaValue())) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_CAPTCHA_ERROR.getMessage());
        }
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginModel.getAccount(), SecureUtil.md5(loginModel.getPassword()));
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(usernamePasswordToken);
        } catch (LockedAccountException e) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_LOCKED.getMessage(), e);
        } catch (DisabledAccountException e) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_DISABLED.getMessage(), e);
        } catch (ExpiredCredentialsException e) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_EXPIRED.getMessage(), e);
        } catch (UnknownAccountException e) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_DISABLED.getMessage(), e);
//            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_ACCOUNT_UNKNOWN.getMessage(), e);
        } catch (IncorrectCredentialsException e) {
            throw new LoginException(SysConstant.ResultCodeEnum.LOGIN_FAIL_INCORRECT_CREDENTIALS.getMessage(), e);
        } catch (Exception e) {
            throw new LoginException(e);
        }
        // 清空验证码缓存
        CacheUtil.getCache().del(SysConstant.CacheNamespaceEnum.CAPTCHA.value() + loginModel.getCaptchaId());
        // 验证通过，返回前端所需的用户信息
        SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
        SysUserModel sysUserModel = new SysUserModel();
        sysUserModel.setId(currentUser.getId());
        sysUserModel.setAccount(currentUser.getAccount());
        sysUserModel.setUserName(currentUser.getUserName());
        sysUserModel.setAvatar(currentUser.getAvatar());
        return ResultUtil.ok(sysUserModel);
    }

    /**
     * 登出
     *
     * @return ResultModel
     * @author cgw
     * @date 2018-01-04 11:36
     */
    @ApiOperation(value = "用户登出")
    @PostMapping("/logout")
    @SysLogOpt(module = "登录接口", value = "用户登出", operationType = SysConstant.LogOptEnum.LOGIN)
    public ResultModel logout() {
        SecurityUtils.getSubject().logout();
        return ResultUtil.ok();
    }

    /**
     * 未登陆
     *
     * @return ResultModel
     * @author cgw
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unlogin", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public ResultModel unlogin() {
        return ResultUtil.fail(SysConstant.ResultCodeEnum.UNLOGIN);
    }

    /**
     * 未授权
     *
     * @return ResultModel
     * @author cgw
     * @date 2017-11-30 16:03
     */
    @RequestMapping(value = "/unauthorized", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public ResultModel unauthorized() {
        return ResultUtil.fail(SysConstant.ResultCodeEnum.UNAUTHORIZED);
    }

    /**
     * 锁屏解锁
     *
     * @return ResultModel
     * @author Xiaofei
     * @date 2018-08-02 16:00
     */
    @RequestMapping(value = "/lockScreenUnlock", method = RequestMethod.GET)
    public ResultModel lockScreenUnlock(String pwd) {
        SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
        if (SecureUtil.md5(pwd).equals(currentUser.getPassword())) {
            return ResultUtil.ok();
        } else {
            return ResultUtil.fail(SysConstant.ResultCodeEnum.LOGIN_FAIL_INCORRECT_CREDENTIALS.value(), SysConstant.ResultCodeEnum.LOGIN_FAIL_INCORRECT_CREDENTIALS.getMessage());
        }
    }


}

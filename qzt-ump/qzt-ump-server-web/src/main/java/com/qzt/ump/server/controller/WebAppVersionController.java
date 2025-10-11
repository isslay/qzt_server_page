package com.qzt.ump.server.controller;

import com.qzt.common.core.constant.Constant;
import com.qzt.ump.rpc.api.AppVersionService;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-06-25
 */
@RestController
@Api(value = "WebAppVersionController", description = "APP版本控制Api")
public class WebAppVersionController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private AppVersionService appVersionService;


    /**
     * 查询版本信息
     *
     * @param vno
     * @param cno
     * @author Xiaofei
     * @date 2019-06-26
     */
    @ApiOperation(value = "查询版本信息", notes = "查询版本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/appVersion/selectAppVersion")
    public Map<String, Object> selectAppVersion(String vno, String cno) {
        try {
            return returnUtil.returnMessMap(this.appVersionService.findVersionCache(cno, vno));
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

}

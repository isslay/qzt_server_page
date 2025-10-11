package com.qzt.ump.server.controllerTpr;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.web.util.ReturnUtilServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@Slf4j
@RestController
@Api(value = "WebQQ", description = "QQ相关Api")
public class WebQQ {

    @Autowired
    private ReturnUtilServer returnUtil;


    /**
     * QQ快捷登录
     */
    @ApiOperation(value = "QQ快捷登录", notes = "QQ快捷登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "openId", value = "前台openId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
    })
    @PostMapping(value = "/pubapi/QQ/QQLogin")
    public Map<String, Object> QQLogin(String vno, String cno, String openId) {
        Map<String, Object> dataMap = new HashedMap();
        if (openId == null || openId.equals("")) {
            return returnUtil.returnMess(Constant.OPERATION_FAILURE);
        }
        /*QztUser klUser = new QztUser();
        klUser.setQqOpenId(openId);
        QztUser qrhUser = this.qztUserService.findKlUser(klUser);

        if (qrhUser == null) {
            QztUser qrhUser1 = new QztUser();
            dataMap.put("QQlogin", "0");//未注册
            dataMap.put("tokenId", "");
            dataMap.put("id", "");
            dataMap.put("mobile", "");
            dataMap.put("userData", qrhUser1);
            return returnUtil.returnMessMap(dataMap);
        } else {
            String tokenId = RandCodeUtil.getRandomStrTokenId(6);
            dataMap.put("tokenId", tokenId);
            CacheUtil.getCache().set(Constants.CacheNamespaceEnum.MOBLIECODE.value() + tokenId, qrhUser.getUserId() + "", 60 * 60 * 24 * 30);
            dataMap.put("id", qrhUser.getUserId());
            dataMap.put("mobile", qrhUser.getMobile());
            dataMap.put("userData", qrhUser);
            dataMap.put("QQlogin", "1");//已注册
            return returnUtil.returnMessMap(dataMap);
        }*/
        return returnUtil.returnMessMap(dataMap);
    }

}

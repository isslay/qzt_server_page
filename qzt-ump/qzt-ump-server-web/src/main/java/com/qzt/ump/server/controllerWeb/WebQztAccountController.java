package com.qzt.ump.server.controllerWeb;

import com.qzt.bus.model.QztUser;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.common.core.constant.Constant;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.common.core.model.PageModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.bus.model.QztAccount;
import com.qzt.bus.rpc.api.IQztAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * <p>
 * web前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@Api(value = "WebQztAccountController", description = "WebQztAccountController")
public class WebQztAccountController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztAccountService service;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    /**
     * 分页查询
     *
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageNum", value = "/页", dataType = "int", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "页/条", dataType = "int", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztAccount/getListPage")
    public Map<String, Object> getListPage(String vno, String cno, Integer pageNum, Integer pageSize) {
        try {
            Map map = new HashMap();
            Map conditionMap = new HashMap();
            PageModel pageModel = new PageModel(pageNum, pageSize, conditionMap);
            pageModel = (PageModel) this.service.find(pageModel);
            List<QztAccount> records = pageModel.getRecords();
            map.put("current", pageModel.getCurrent());
            map.put("size", pageModel.getSize());
            map.put("total", pageModel.getTotal());
            map.put("data", records);
            return returnUtil.returnMessMap(map);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    /**
     * 根据主键id查询详情
     *
     * @param id
     * @return Map
     * @author Cgw
     * @date 2019-11-11
     */
    @ApiOperation(value = "根据主键id查询详情", notes = "根据主键id查询详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "id", value = "主键id", dataType = "Long", required = true, paramType = "query")
    })
    @GetMapping("/pubapi/qztAccount/selectById/{id}")
    public Map<String, Object> selectById(@PathVariable Long id) {
        try {
            QztAccount entity = this.service.selectById(id);
            return returnUtil.returnMess(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }

    @ApiOperation(value = "获得用户的基本信息", notes = "获得用户的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "vno", value = "当前客户端版本号", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "cno", value = "客户端(PC：1、IOS：2、Android：3、H5：4、小程序：5)", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "tokenId", value = "tokenId", dataType = "String", required = true, paramType = "query"),
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "Long", required = true, paramType = "query")
    })
    @PostMapping("/webapi/qztAccount/queryUserAccountMess")
    public Map<String, Object> queryUserAccountMess( String vno, String cno,String tokenId,String userId) {
        try {
            Map dataMap = new HashMap();
            QztAccount qztAccount = this.service.selectByUserId(Long.parseLong(userId));
            Map accountMap = new HashMap();
            accountMap.put("accountMoney", PriceUtil.exactlyTwoDecimalPlaces(qztAccount.getAccountMoney()));//账户余额
            //查询分享佣金 和 推广佣金
            accountMap.put("shareMoney",qztAccountRelogService.findAccountById(Long.parseLong(userId)));

            //查询推广佣金分项
            Map<String,Object> params = new HashMap();
            params.put("userId",userId);
            params.put("reType",0);
            Map tMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
            accountMap.put("tMoneyMess",tMoneyMess);
            //查询分享佣金分项
            params.put("reType",1);
            params.put("odateTime", DateTime.getCurDateTime("yyyy-MM-dd"));
            Map sMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
            accountMap.put("sMoneyMess",sMoneyMess);
            //查询我的今日分享佣金
            params.remove("odateTime");
            params.put("dateTime", DateTime.getCurDateTime("yyyy-MM-dd"));
            Map sDMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
            accountMap.put("sDMoneyMess",sDMoneyMess);
            dataMap.put("accountData",accountMap);
            return returnUtil.returnMess(dataMap);
        } catch (Exception e) {
            e.printStackTrace();
            return returnUtil.returnMess(Constant.DATA_ERROR);
        }
    }



}


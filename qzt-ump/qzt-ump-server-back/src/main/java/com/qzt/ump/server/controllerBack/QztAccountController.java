package com.qzt.ump.server.controllerBack;

import com.qzt.bus.rpc.api.IQztAccountLogService;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.bus.rpc.api.IQztUserService;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.tools.DateTime;
import com.qzt.common.tools.PriceUtil;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztAccount;
import com.qzt.bus.rpc.api.IQztAccountService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztAccount")
@Api(value = "QztAccountController", description = "QztAccountController")
public class QztAccountController extends BaseController {

    @Autowired
    private IQztAccountService service;

    @Autowired
    private IQztUserService qztUserService;

    @Autowired
    private IQztAccountLogService qztAccountLogService;

    @Autowired
    private IQztAccountRelogService qztAccountRelogService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztAccount entity = service.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 查询分页方法
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel pageModel) {
        try {
            pageModel = (PageModel) service.findBack(pageModel);
            List<Map> records = pageModel.getRecords();
            List<Map> datalist = new ArrayList<>();
            for (Map qztAccount : records) {
                Long userId = (Long) qztAccount.get("userId");
                Map<String, Object> dataMap = new HashMap<>();
                dataMap.put("obj", qztAccount);
                dataMap.put("objUser", this.qztUserService.findDGUserById(userId));
                Map<String, Object> userAccountMap = qztAccountRelogService.findAccountById(userId);
                userAccountMap.put("userAccount", PriceUtil.exactlyTwoDecimalPlaces((Long) qztAccount.get("accountMoney")));
                dataMap.put("shareMoney", userAccountMap);

                //查询推广佣金分项
                Map<String, Object> params = new HashMap();
                params.put("userId", userId);
                params.put("reType", 0);
                Map tMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
                dataMap.put("tMoneyMess", tMoneyMess);
                //查询分享佣金分项
                params.put("reType", 1);
                params.put("odateTime", DateTime.getCurDateTime("yyyy-MM-dd"));
                Map sMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
                dataMap.put("sMoneyMess", sMoneyMess);
                //查询我的今日分享佣金
                params.remove("odateTime");
                params.put("dateTime", DateTime.getCurDateTime("yyyy-MM-dd"));
                Map sDMoneyMess = this.qztAccountRelogService.findAccountByIdAndType(params);
                dataMap.put("sDMoneyMess", sDMoneyMess);
                datalist.add(dataMap);
            }
            pageModel.setRecords(datalist);
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 新增方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody QztAccount entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztAccount entityback = service.add(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 修改方法
     *
     * @param entity 实体
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody QztAccount entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztAccount entityback = service.modifyById(entity);
            if (entityback == null) {
                return ResultUtil.fail(SysConstant.ResultCodeEnum.INCREASE_THE_FAILURE);
            } else {
                return ResultUtil.ok();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 批量删除方法
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Cgw
     * @date 2019-11-11
     */
    @PostMapping("/delBatchByIds")
    public ResultModel delBatchByIds(@RequestBody Long[] ids) {
        try {
            return ResultUtil.ok(service.deleteBatchIds(Arrays.asList(ids)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }
}


package com.qzt.ump.server.controllerBack;

import com.qzt.common.core.constant.Constant;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.common.web.util.ReturnUtilServer;
import com.qzt.ump.model.SysUserModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztWithdraw;
import com.qzt.bus.rpc.api.IQztWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
@RestController
@RequestMapping("/back/qztWithdraw")
@Api(value = "QztWithdrawController", description = "QztWithdrawController")
public class QztWithdrawController extends BaseController {

    @Autowired
    private ReturnUtilServer returnUtil;

    @Autowired
    private IQztWithdrawService qztWithdrawService;

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-12
     */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztWithdraw entity = qztWithdrawService.selectById(id);
            return ResultUtil.ok(entity);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }

    /**
     * 查询提现申请记录
     *
     * @param pageModel 分页实体
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-11-12
     */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<QztWithdraw> pageModel) {
        try {
            pageModel = (PageModel<QztWithdraw>) qztWithdrawService.find(this.disposeCondition(pageModel));
            return ResultUtil.ok(pageModel);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(SysConstant.ResultCodeEnum.DATA_ERROR_KEY);
        }
    }


    /**
     * 提现记录条件处理
     *
     * @param pageModel
     * @return com.qzt.common.core.model.PageModel
     * @author Xiaofei
     * @date 2019-09-05
     */
    private PageModel disposeCondition(PageModel pageModel) {
        Map condition = pageModel.getCondition();
        condition = condition == null ? new HashMap() : condition;
        String auditType = condition.get("auditType").toString();
        String[] auditStateArr = null;
        if ("admin".equals(auditType)) {//所有
            if (condition.get("auditState") != null && "11".equals(condition.get("auditState").toString())) {
                condition.put("auditState", null);
                auditStateArr = new String[]{"11", "13", "15"};
            }
        } else if ("accept".equals(auditType)) {//受理accept
            auditStateArr = new String[]{"00", "11"};
        } else if ("affirm".equals(auditType)) {//确认affirm
            auditStateArr = new String[]{"01", "13"};
        } else if ("finish".equals(auditType)) {//完成finish
            auditStateArr = new String[]{"03", "20", "15"};
        }
        condition.put("auditStateArr", auditStateArr);
        pageModel.setCondition(condition);
        return pageModel;
    }

    /**
     * 审核-受理方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/theBatchAcceptance")
    public Map theBatchAcceptance(@RequestBody Long[] ids) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "01", "");
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "受理失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 审核-确认方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/batchConfirmation")
    public Map<String, Object> batchConfirmation(@RequestBody Long[] ids) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "03", "");
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "确认失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 审核-完成方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/batchToComplete")
    public Map batchToComplete(@RequestBody Long[] ids) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "20", "");
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "完成失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 受理驳回方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/batchAcceptanceRejection")
    public Map batchAcceptanceRejection(@RequestBody Long[] ids, String auditRemark) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "11", auditRemark);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "驳回失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 确认驳回方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/batchConfirmationRejection")
    public Map batchConfirmationRejection(@RequestBody Long[] ids, String auditRemark) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "13", auditRemark);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "驳回失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

    /**
     * 完成驳回方法(批量-单条)
     *
     * @param ids ID集合
     * @return ResultModel
     * @author Xiaofei
     * @date 2019-08-01
     */
    @PostMapping("/batchRejection")
    public Map batchRejection(@RequestBody Long[] ids, String auditRemark) {
        try {
            SysUserModel currentUser = (SysUserModel) super.getCurrentUser();
            //处理操作权限
            Map map = this.qztWithdrawService.batchWithdrawalAudit(ids, currentUser, "15", auditRemark);
            return returnUtil.returnMess(map, map.get("code"), map.get("message"));
        } catch (Exception e) {
            if ("0201".equals(e.getMessage())) {
                return returnUtil.returnMess(Constant.OPERATION_FAILURE, "驳回失败");
            } else {
                e.printStackTrace();
                return returnUtil.returnMess(Constant.DATA_ERROR);
            }
        }
    }

}


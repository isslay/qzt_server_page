package com.qzt.ump.server.controllerBack;

import com.qzt.bus.rpc.api.IQztAccountService;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.model.PageModel;
import com.qzt.common.tools.StringUtil;
import com.qzt.common.web.model.ResultModel;
import com.qzt.common.web.util.ResultUtil;
import com.qzt.ump.model.SysUserModel;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import com.qzt.common.web.BaseController;
import com.qzt.bus.model.QztRecharge;
import com.qzt.bus.rpc.api.IQztRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Cgw
 * @since 2019-11-22
 */
@RestController
@RequestMapping("/back/qztRecharge")
@Api(value = "QztRechargeController", description = "QztRechargeController")
public class QztRechargeController extends BaseController {

    @Autowired
    private IQztRechargeService service;

    @Autowired
    private IQztAccountService qztAccountService;

    /**
    * 根据ID查询
    *
    * @param id ID
    * @return ResultModel
    * @author Cgw
    * @date 2019-11-22
    */
    @GetMapping("/query/{id}")
    public ResultModel query(@PathVariable Long id) {
        try {
            QztRecharge entity = service.selectById(id);
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
    * @date 2019-11-22
    */
    @PostMapping("/queryListPage")
    public ResultModel queryListPage(@RequestBody PageModel<QztRecharge> pageModel) {
        try {
            pageModel = (PageModel<QztRecharge>) service.find(pageModel);
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
     * @date 2019-11-22
     */
    @PostMapping("/add")
    public ResultModel add(@Valid @RequestBody QztRecharge entity) {
        try {
            entity.setCreateBy(super.getCurrentUserId());
            entity.setUpdateBy(this.getCurrentUserId());
            QztRecharge entityback = service.add(entity);
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

    @PostMapping("/updateRechargeState")
    public ResultModel updateRechargeState(@RequestBody Long[] ids, String auditState,String auditRemark) {
        try {
            for(Long id : ids){
                QztRecharge entity = service.selectById(id);
                entity.setCreateBy(super.getCurrentUserId());
                entity.setUpdateBy(this.getCurrentUserId());
                entity.setAuditUserId(super.getCurrentUserId());
                SysUserModel sysUser = (SysUserModel)super.getCurrentUser();
                entity.setAuditUserName(sysUser.getUserName());
                entity.setAuditTime(new Date());
                if(entity.getAuditState().equals(auditState)){
                    if(auditState.equals("10") && StringUtil.isEmpty(auditRemark)){
                        entity.setAuditState("20");
                    }
                    if(auditState.equals("10") && !StringUtil.isEmpty(auditRemark)){
                        entity.setAuditState("01");
                        entity.setAuditRemark(auditRemark+";"+entity.getAuditRemark());
                    }
                    if(auditState.equals("20") && StringUtil.isEmpty(auditRemark)){
                        entity.setAuditState("90");
                        //给用户充值余额

                    }
                    if(auditState.equals("20") && !StringUtil.isEmpty(auditRemark)){
                        entity.setAuditState("11");
                        entity.setAuditRemark(auditRemark+";"+entity.getAuditRemark());
                    }
                    if(auditState.equals("00")){
                        entity.setAuditState("10");
                    }
                }
                service.modifyById(entity);
                if(entity.getAuditState().equals("90")){
                    qztAccountService.updateQztAccount(entity.getId()+"",entity.getUserId(),entity.getTopUpMoney(),"30","01","晋升系统每日结算获得分润");
                }
            }
            return ResultUtil.ok();
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
     * @date 2019-11-22
     */
    @PostMapping("/modify")
    public ResultModel modify(@RequestBody QztRecharge entity) {
        try {
            entity.setUpdateBy(this.getCurrentUserId());
            entity.setUpdateTime(new Date());
            QztRecharge entityback = service.modifyById(entity);
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
     * @date 2019-11-22
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


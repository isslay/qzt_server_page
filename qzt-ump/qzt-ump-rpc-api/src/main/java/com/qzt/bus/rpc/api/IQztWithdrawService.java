package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztWithdraw;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysUserModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztWithdrawService extends BaseService<QztWithdraw> {

    Page<QztWithdraw> find(Page<QztWithdraw> pageModel);

    /**
     * 前端用户提现
     *
     * @param qztWithdraw
     * @param payPwd
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map userWithdrawApplyfor(QztWithdraw qztWithdraw, String payPwd) throws Exception;

    /**
     * 提现审核（批量）
     *
     * @param ids
     * @param currentUser
     * @param auditState
     * @param auditRemark
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-12
     */
    Map batchWithdrawalAudit(Long[] ids, SysUserModel currentUser, String auditState, String auditRemark) throws Exception;

    /**
     * 用户提现记录导出集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author Xiaofei
     * @date 2019-12-04
     */
    List<Map<String, Object>> userWithdrawExcel(Map map);

}

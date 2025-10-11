package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztWithdraw;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztWithdrawMapper extends BaseMapper<QztWithdraw> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztWithdraw> find(Map<String, Object> map);

    /**
     * 查询用户某天的提现次数
     *
     * @param userId
     * @param dateDay
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-11-12
     */
    Long verifyWithdrawFrequencyLimit(@Param("userId") Long userId, @Param("dateDay") String dateDay);

    /**
     * 提现审核方法
     *
     * @param qztWithdraw
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer withdrawalAudit(QztWithdraw qztWithdraw);

    /**
     * 提现审核（批量）
     *
     * @param ids
     * @param auditUserId
     * @param auditUserName
     * @param auditState
     * @param auditTime
     * @param auditRemark
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer batchWithdrawalAudit(@Param("ids") Long[] ids, @Param("auditUserId") Long auditUserId, @Param("auditUserName") String auditUserName,
                                 @Param("auditState") String auditState, @Param("auditTime") String auditTime, @Param("auditRemark") String auditRemark);

}

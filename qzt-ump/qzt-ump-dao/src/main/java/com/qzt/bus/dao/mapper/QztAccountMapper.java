package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztAccount;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztAccountMapper extends BaseMapper<QztAccount> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztAccount> find(Map<String, Object> map);

    List<Map> findBack(Map<String, Object> map);

    /**
     * 根据用户ID查询资产信息
     *
     * @param userId
     * @return com.qzt.bus.model.QztAccount
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztAccount selectByUserId(Long userId);

    /**
     * 修改账户资产信息
     *
     * @param userId
     * @param changeMoney 变动金额
     * @param changeType  变动类别
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-12
     */
    Integer changeAccount(@Param("userId") Long userId, @Param("changeMoney") Long changeMoney, @Param("changeType") String changeType);

}

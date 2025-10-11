package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztUserBank;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface QztUserBankMapper extends BaseMapper<QztUserBank> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Xiaofei
     * @date 2019-11-11
     */
    List<QztUserBank> find(Map<String, Object> map);

    /**
     * 根据ID与用户ID查询详情
     *
     * @param qztUserBank
     * @return com.qzt.bus.model.QztUserBank
     * @author Xiaofei
     * @date 2019-11-11
     */
    QztUserBank selectByIdAndUserId(QztUserBank qztUserBank);

    /**
     * 修改默认地址
     *
     * @param qztUserBank
     * @return void
     * @author Xiaofei
     * @date 2019-11-11
     */
    void defaultUserBank(QztUserBank qztUserBank);

    /**
     * 查询用户有效收款账号数量
     *
     * @param userId
     * @return java.util.Map
     * @author Xiaofei
     * @date 2019-11-11
     */
    Map selectUserBankSize(Long userId);

}

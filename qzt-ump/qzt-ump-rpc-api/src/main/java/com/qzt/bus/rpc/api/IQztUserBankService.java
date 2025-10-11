package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztUserBank;
import com.qzt.common.core.base.BaseService;

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
public interface IQztUserBankService extends BaseService<QztUserBank> {

    Page<QztUserBank> find(Page<QztUserBank> pageModel);

    List<QztUserBank> findList(Map map);

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
     * 查询用户有效收款账号数量
     *
     * @param userId
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-11-11
     */
    Long selectUserBankSize(Long userId);

    /**
     * 修改默认地址
     *
     * @param entity
     * @return void
     * @author Xiaofei
     * @date 2019-11-11
     */
    void defaultUserBank(QztUserBank entity);
}

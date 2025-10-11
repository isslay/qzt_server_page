package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztAccountLog;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztAccountLogService extends BaseService<QztAccountLog> {

    Page<QztAccountLog> find(Page<QztAccountLog> pageModel);

    /**
     * 添加资金操作日志
     *
     * @return void
     * @author Xiaofei
     * @date 2019-11-12
     */
    void addAccountLog(String busId, Long userId, Long id, String s, Long changeMoney, String changeSource, Long moneyBalanceEnd, Long moneyBalance, String remark, int logState);

    int findAccountByBusId(Map<String, Object> params);

    Long queryUserAccount(Map<String, Object> params);

    /**
     * 查询某业务分润记录
     *
     * @param busId
     * @return java.util.List
     * @author Xiaofei
     * @date 2019-11-23
     */
    List<QztAccountLog> findShareList(String busId);

    List<Map> findShareMapList(String busId);

}

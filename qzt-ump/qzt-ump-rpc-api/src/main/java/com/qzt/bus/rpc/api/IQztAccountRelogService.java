package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztAccountRelog;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztAccountRelogService extends BaseService<QztAccountRelog> {

    Page<QztAccountRelog> find(Page <QztAccountRelog> pageModel);

    Map<String,Object> findAccountById(Long userId);

    /**
     * 根据用户ID 和 分享金额类型查询总数据
     * @param params
     * @return
     */
    Map<String,Object> findAccountByIdAndType(Map<String,Object> params);

    int findAccountByBusId(Map<String,Object> params);

    /**
     * 查询所有当日有分享佣金的用户信息
     * @param param
     * @return
     */
    List<Map> findAccountRelogByUserId(Map<String,Object> param);

    /**
     * 根据用户ID查询需要转换的佣金
     * @param param
     * @return
     */
    List<QztAccountRelog> findCommendAllMoneyByUserId(Map<String,Object> param);

    /**
     * 查询某业务分润记录
     * @param busId
     * @return
     */
    List<Map> findShareMapList(String busId);
 }

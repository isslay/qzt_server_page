package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztBaseBank;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztBaseBankService extends BaseService<QztBaseBank> {

    Page<QztBaseBank> find(Page <QztBaseBank> pageModel);

    List<QztBaseBank> findList();

}

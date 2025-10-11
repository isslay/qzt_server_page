package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztOrderCommen;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztOrderCommenService extends BaseService<QztOrderCommen> {

    Page<QztOrderCommen> find(Page <QztOrderCommen> pageModel);
}

package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztGoodsBanner;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2019-11-06
 */
public interface IQztGoodsBannerService extends BaseService<QztGoodsBanner> {

    Page<QztGoodsBanner> find(Page<QztGoodsBanner> pageModel);

    List<QztGoodsBanner> findList(Map<String, Object> map);
}

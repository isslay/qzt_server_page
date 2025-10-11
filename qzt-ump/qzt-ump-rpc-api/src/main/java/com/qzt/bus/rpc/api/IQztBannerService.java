package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztBanner;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2019-11-07
 */
public interface IQztBannerService extends BaseService<QztBanner> {

    Page<QztBanner> find(Page<QztBanner> pageModel);
}

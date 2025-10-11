package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztPageBanner;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-25
 */
public interface IQztPageBannerService extends BaseService<QztPageBanner> {

    Page<QztPageBanner> find(Page<QztPageBanner> pageModel);
}

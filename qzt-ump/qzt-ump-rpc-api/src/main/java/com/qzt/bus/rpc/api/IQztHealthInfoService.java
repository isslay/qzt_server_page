package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztHealthInfo;
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
public interface IQztHealthInfoService extends BaseService<QztHealthInfo> {

    Page<QztHealthInfo> find(Page<QztHealthInfo> pageModel);
}

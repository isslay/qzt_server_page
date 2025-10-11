package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmReduceIntegralDetail;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
public interface IDgmReduceIntegralDetailService extends BaseService<DgmReduceIntegralDetail> {

    Page<DgmReduceIntegralDetail> find(Page<DgmReduceIntegralDetail> pageModel);
}

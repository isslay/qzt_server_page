package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmOrderRefund;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-11-06
 */
public interface IDgmOrderRefundService {

    Page<DgmOrderRefund> find(Page<DgmOrderRefund> pageModel);

    int insert(DgmOrderRefund dgmOrderRefund);
}

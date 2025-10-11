package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.VErpOrderDetail;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-10-23
 */
public interface IVErpOrderDetailService extends BaseService<VErpOrderDetail> {

    Page<VErpOrderDetail> find(Page<VErpOrderDetail> pageModel);

    List<VErpOrderDetail> findList(Map<String, Object> map);


}

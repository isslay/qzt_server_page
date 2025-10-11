package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.ErpGoodsInfo;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2023-08-28
 */
public interface IErpGoodsInfoService extends BaseService<ErpGoodsInfo> {

    Page<ErpGoodsInfo> find(Page<ErpGoodsInfo> pageModel);

    List<ErpGoodsInfo> findAll(ErpGoodsInfo pageModel);
}

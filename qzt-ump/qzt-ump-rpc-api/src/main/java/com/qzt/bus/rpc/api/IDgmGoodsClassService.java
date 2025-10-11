package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmGoodsClass;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
public interface IDgmGoodsClassService extends BaseService<DgmGoodsClass> {

    Page<DgmGoodsClass> find(Page<DgmGoodsClass> pageModel);

    Object getClassList();
}

package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmZxt;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-09-27
 */
public interface IDgmZxtService extends BaseService<DgmZxt> {

    Page<DgmZxt> find(Page<DgmZxt> pageModel);
}

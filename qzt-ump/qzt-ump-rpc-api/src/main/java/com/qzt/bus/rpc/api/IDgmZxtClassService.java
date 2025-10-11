package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmZxtClass;
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
public interface IDgmZxtClassService extends BaseService<DgmZxtClass> {

    Page<DgmZxtClass> find(Page<DgmZxtClass> pageModel);

    Object getClassList();
}

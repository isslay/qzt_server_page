package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztResource;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2020-03-26
 */
public interface IQztResourceService extends BaseService<QztResource> {

    Page<QztResource> find(Page<QztResource> pageModel);
}

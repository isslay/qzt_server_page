package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
public interface IDgmUsableIntegralService extends BaseService<DgmUsableIntegral> {

    Page<DgmUsableIntegral> find(Page<DgmUsableIntegral> pageModel);

    DgmUsableIntegral findById(Long id);

    List<DgmUsableIntegral> findList(Map<String, Object> map);

    int sumAll(int userId);

    int monthOver(int userId);
}

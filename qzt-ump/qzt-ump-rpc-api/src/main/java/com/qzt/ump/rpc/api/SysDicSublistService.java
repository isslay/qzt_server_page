package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysDicSublist;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
public interface SysDicSublistService extends BaseService<SysDicSublist> {

    Page<SysDicSublist> find(Page <SysDicSublist> pageModel);


    /**
     * 根据字典类型查询子表数据
     * @param dicType
     * @return
     */
    List<Map> selectDicListByDicType(String dicType);
}

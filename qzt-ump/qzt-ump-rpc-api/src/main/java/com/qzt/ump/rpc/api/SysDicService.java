package com.qzt.ump.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseService;
import com.qzt.ump.model.SysDic;
import com.qzt.ump.model.SysDicSublist;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
public interface SysDicService extends BaseService<SysDic> {

    /**
     * 分页查询字典主表
     *
     * @param pageModel
     * @return
     */
    Page<Map> find(Page<Map> pageModel);

    /**
     * 分页查询字典子表
     *
     * @param pageModel
     * @return
     */
    Page<Map> queryListSublistPage(Page<Map> pageModel);

    List<SysDic> test();

    /**
     * 验证字典类型是否重复
     *
     * @param dicType
     */
    List<SysDic> verifyDicType(String dicType);

    /**
     * 验证字典子表关键字是否重复
     *
     * @param code
     * @param parentId
     * @return
     */
    List<Map> verifyDicCode(String code, String parentId);

    /**
     * 系统启动加载字典用查询主表
     *
     * @return
     */
    List<SysDic> selectDicList();

    /**
     * 系统启动加载字典用查询子表
     *
     * @param id
     * @return
     */
    List<Map> selectParentIdDicList(Long id);

    /**
     * 根据字典类型查询
     *
     * @param dicType
     * @return
     */
    Map selectDicByDicType(String dicType, String code);

    /**
     * 初始化字典参数
     *
     * @return void
     * @author Xiaofei
     * @date 2019-07-31
     */
    void initializeDic();
}

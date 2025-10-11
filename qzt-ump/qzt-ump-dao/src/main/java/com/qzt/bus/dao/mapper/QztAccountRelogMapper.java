package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztAccountRelog;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztAccountRelogMapper extends BaseMapper<QztAccountRelog> {

    /**
     *  通用查询方法
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztAccountRelog> find(Map<String,Object> map);


    List<Map<String,Object>> findAccountById(@Param("userId") Long userId);

    /**
     * 根据用户ID 和 分享金额类型查询总数据
     * @param params
     * @return
     */
    Map<String,Object> findAccountByIdAndType(Map<String,Object> params);

    /**
     * 查询剩余的金额
     * @param map
     * @return
     */
    List<QztAccountRelog> findHaveAccount(Map<String,Object> map);


    Long findCountHaveAccount(Map<String,Object> map);

    /**
     * 根据业务ID 和用户查询数据总数
     * @param params
     * @return
     */
    int findAccountByBusId(Map<String, Object> params);

    /**
     * 查询所有需要激活的分享佣金
     * @param map
     * @return
     */
    List<Map> findAccountRelogByUserId(Map<String,Object> map);


    List<Map> findShareMapList(String busId);

}

package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztUserReg;
import com.qzt.common.core.base.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface QztUserRegMapper extends BaseMapper<QztUserReg> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author Cgw
     * @date 2019-11-11
     */
    List<QztUserReg> find(Map<String, Object> map);

    int saveUserRegAll(List<QztUserReg> userRegs);

    /**
     * 根据父级子级type查询该层级是否存在
     *
     * @param qztUserReg
     * @return com.qzt.bus.model.QztUserReg
     * @author Xiaofei
     * @date 2019-11-17
     */
    Long queryByPuserIdAndRuserId(QztUserReg qztUserReg);

    /**
     * 创建关系树
     *
     * @param qztUserReg
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-17
     */
    Integer createRelationshipTree(QztUserReg qztUserReg);

}

package com.qzt.bus.dao.mapper;


import com.qzt.bus.model.QztUser;
import com.qzt.common.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

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
public interface QztUserMapper extends BaseMapper<QztUser> {

    /**
     * 通用查询方法
     *
     * @param map
     * @author
     * @date
     */
    List<QztUser> find(Map<String, Object> map);

    List<QztUser> findBack(Map<String, Object> map);


    List<QztUser> findListByOpenId(Map<String, Object> map);


    QztUser findUserById(@Param("openId") String openId,@Param("unionId") String unionId);

    QztUser findDGUserById(@Param("id") Long id);

    QztUser findUserByMobile(@Param("mobile") String mobile,@Param("realName")String realName,@Param("id") Long id);

    QztUser findUserByMobileName(@Param("mobile") String mobile,@Param("realName")String realName);

    /**
     * 验证支付密码是否正确
     * 1为密码正确，-1为无支付密码
     *
     * @param userId
     * @param payPwd
     * @return
     */
    Long verifyPayPwd(@Param("userId") Long userId, @Param("payPwd") String payPwd);


    int updateUserMess(Map<String, Object> params);

    Long findCount(Map<String, Object> params);

    /**
     * 根据id修改用户
     *
     * @param map
     * @return
     */
    boolean updateUserById(Map<String, Object> map);


    /**
     * 查询用户的上级关系树集合
     *
     * @param userId
     * @param type
     * @return java.util.List<com.qzt.bus.model.QztUser>
     * @author Xiaofei
     * @date 2019-11-24
     */
    List<QztUser> findRelationalTree(@Param("userId") Long userId, @Param("type") Integer type);

    /**
     * 查询用户指定身份的直属下级数量
     *
     * @param parentUserId
     * @param userType
     * @param regType
     * @return java.lang.Long
     * @author Xiaofei
     * @date 2019-11-24
     */
    Long queryDirectlyUnderLowerNum(@Param("parentUserId") Long parentUserId, @Param("userType") Integer userType, @Param("regType") Integer regType);

    /**
     * 更新用户类型
     *
     * @param userId
     * @param userType
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-24
     */
    Integer updateUserType(@Param("userId") Long userId, @Param("userType") Integer userType);

    /**
     * 更新用户身份
     *
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-24
     */
    Integer modifyUserInfo(@Param("userId") Long userId, @Param("bussId") Long bussId, @Param("referrerSecond") String referrerSecond, @Param("userType") Integer userType, @Param("updateTime") String updateTime);

    /**
     * 修改用户身份
     *
     * @param map
     * @return java.lang.Integer
     * @author Xiaofei
     * @date 2019-11-24
     */
    Integer updateUserTypepMap(Map map);

}

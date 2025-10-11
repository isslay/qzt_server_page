package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztUser;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author snow
 * @since
 */
public interface IQztUserService extends BaseService<QztUser> {

    Page<QztUser> find(Page<QztUser> pageModel);

    Page<QztUser> findBack(Page<QztUser> pageModel);

    /**
     * 验证支付密码是否正确
     * 1为密码正确，-1为无支付密码
     *
     * @param userId
     * @param payPwd
     * @return java.lang.Long
     * @author snow
     * @date 2019-11-12
     */
    Long verifyPayPwd(Long userId, String payPwd);


    List<QztUser> findListByOpenId(Map<String, Object> params);

    /**
     * 通过微信信息 进行用户查询
     *
     * @param
     * @return
     */
    QztUser findUserById(String openId,String unionId);

    QztUser findDGUserById(Long id);

    QztUser findUserByMobile(String mobile,String realName,Long id);

    QztUser findUserByMobileName(String mobile,String realName);

    Long saveUserAndAccount(QztUser qztUser) throws Exception;


    Long findCount(Map<String, Object> params);


    int updateUserMess(Map<String, Object> params);


    List<QztUser> findListByType(Map<String, Object> params);

    /**
     * 根据id修改用户
     *
     * @param map
     * @return
     */
    boolean updateUserById(Map<String, Object> map);

    /**
     * 处理用户升级
     *
     * @param userId 子级用户ID
     * @return boolean
     * @author snow
     * @date 2019-11-24
     */
    void userUpgrade(Long userId) throws Exception;

    /**
     * 更新用户信息 （反写）
     *
     * @param qztUser
     * @return java.lang.Integer
     * @author snow
     * @date 2019-11-24
     */
    boolean modifyUserInfo(QztUser qztUser);

    /**
     * 修改用户身份
     *
     * @param emap
     * @return java.lang.Integer
     * @author snow
     * @date 2019-12-03
     */
    Integer updateUserType(Map emap);

    /**
     * 添加服务站推荐人
     *
     * @param emap
     * @return java.lang.String
     * @author snow
     * @date 2019-12-11
     */
    String addSuperior(Map emap) throws Exception;

    void test(Long userId, Long referrerSecond) throws Exception;

    /**
     * 注册用户导出数据集合
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @author
     * @date
     */
    List<Map<String, Object>> userExcel(Map map);

}

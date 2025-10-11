package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztUserReg;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztUserRegService extends BaseService<QztUserReg> {

    Page<QztUserReg> find(Page<QztUserReg> pageModel);

    /**
     * 创建关系树
     *
     * @param progressType 发展类型（正向01、逆向03）
     * @param type         关系类型（用户0、服务站2）
     * @param userId       用户ID
     * @param referrerId   推荐人ID
     * @return boolean
     * @author Xiaofei
     * @date 2019-11-17
     */
    boolean createRelationshipTree(String progressType, Integer type, Long userId, Long referrerId);

}

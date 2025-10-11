package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztAccount;
import com.qzt.common.core.base.BaseService;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztAccountService extends BaseService<QztAccount> {

    Page<QztAccount> find(Page<QztAccount> pageModel);


    Page findBack(Page pageModel);

    /**
     * 修改账户资产信息
     *
     * @param busId        业务订单id
     * @param userId       用户id
     * @param changeMoney  变更金额
     * @param changeSource 变动来源（余额： 购买商品消费01、服务站申请03、余额提现05、、、、、、
     *                     取消商品订单23、余额提现驳回25、、、、、30购买商品消费分润 31服务订单分润 32 推广佣金激活 33下级服务金额）
     * @param changeType   变动类别（增加余额01、减少余额11）
     * @param remark       描述
     * @return 01
     */

    boolean updateQztAccount(String busId, Long userId, Long changeMoney, String changeSource, String changeType, String remark);

    /**
     * 根据用户ID查询资产信息
     *
     * @param userId
     * @return com.qzt.bus.model.QztAccount
     * @author Xiaofei
     * @date 2019-11-12
     */
    QztAccount selectByUserId(Long userId);


}

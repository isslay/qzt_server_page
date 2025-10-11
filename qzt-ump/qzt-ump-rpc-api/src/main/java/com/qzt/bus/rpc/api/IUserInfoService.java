package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.UserInfo;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author snow
 * @since 2023-08-29
 */
public interface IUserInfoService extends BaseService<UserInfo> {

    Page<UserInfo> find(Page<UserInfo> pageModel);

    UserInfo findById(Integer id);

}

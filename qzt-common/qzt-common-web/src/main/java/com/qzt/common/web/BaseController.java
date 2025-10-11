package com.qzt.common.web;

import com.qzt.common.web.util.WebUtil;

/**
 * 控制器基类
 *
 * @author cgw
 * @date 2017/11/9 23:45
 */
public abstract class BaseController {

    /**
     * 获取当前用户Id
     *
     * @return Long
     * @author cgw
     * @date 2017-11-30 17:45
     */
    protected Long getCurrentUserId() {
        return WebUtil.getCurrentUserId();
    }

    /**
     * 获取当前用户
     *
     * @return Object
     * @author cgw
     * @date 2017-11-30 17:45
     */
    protected Object getCurrentUser() {
        return WebUtil.getCurrentUser();
    }
}

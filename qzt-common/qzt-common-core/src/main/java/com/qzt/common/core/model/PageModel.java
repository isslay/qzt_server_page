package com.qzt.common.core.model;


import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.common.core.base.BaseModel;

import java.util.Map;

/**
 * 分页实体类
 *
 * @author cgw
 * @date 2017/11/23 22:14
 */
public class PageModel<T extends BaseModel> extends Page<T> {

    public PageModel() {
    }

    public PageModel(int current, int size) {
        super(current, size);
    }

    public PageModel(int current, int size, String orderByField) {
        super(current, size);
        super.setOrderByField(orderByField);
    }

    public PageModel(Integer current, Integer size, Map conditionMap) {
        super(current == null || current < 1 ? 1 : current, size == null || size < 1 ? 1 : size);
        super.setCondition(conditionMap);
    }
}

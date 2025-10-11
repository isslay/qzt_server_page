package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztBusPic;
import com.qzt.common.core.base.BaseService;

import java.util.List;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Xiaofei
 * @since 2019-11-11
 */
public interface IQztBusPicService extends BaseService<QztBusPic> {

    Page<QztBusPic> find(Page <QztBusPic> pageModel);

    boolean saveBusPics(List<QztBusPic> busPicList);

    List<QztBusPic> getBusPics(Map<String,Object> params);

    /**
     * 根据BUSid 删除图片信息
     * @param busId
     * @return
     */
    int delBusPics(String busId);
}

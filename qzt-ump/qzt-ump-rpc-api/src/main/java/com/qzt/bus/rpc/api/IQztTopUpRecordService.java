package com.qzt.bus.rpc.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.qzt.bus.model.QztTopUpRecord;
import com.qzt.common.core.base.BaseService;
import java.util.Map;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
public interface IQztTopUpRecordService extends BaseService<QztTopUpRecord> {

    Page<QztTopUpRecord> find(Page <QztTopUpRecord> pageModel);
}

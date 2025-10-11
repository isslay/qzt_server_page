package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmIntegralRecordMapper;
import com.qzt.bus.dao.mapper.DgmReduceIntegralDetailMapper;
import com.qzt.bus.dao.mapper.DgmUsableIntegralMapper;
import com.qzt.bus.model.DgmIntegralRecord;
import com.qzt.bus.model.DgmReduceIntegralDetail;
import com.qzt.bus.model.DgmUsableIntegral;
import com.qzt.bus.rpc.api.IDgmIntegralRecordService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.DateUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author snow
 * @since 2024-02-19
 */
@Service("dgmIntegralRecordService")
public class DgmIntegralRecordServiceImpl extends BaseServiceImpl<DgmIntegralRecordMapper, DgmIntegralRecord> implements IDgmIntegralRecordService {

    @Autowired
    private DgmIntegralRecordMapper dgmIntegralRecordMapper;//记录
    @Autowired
    private DgmReduceIntegralDetailMapper dgmReduceIntegralDetailMapper;//扣减详情
    @Autowired
    private DgmUsableIntegralMapper dgmUsableIntegralMapper;//可用

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public Page<DgmIntegralRecord> find(Page<DgmIntegralRecord> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<DgmIntegralRecord> rb = this.dgmIntegralRecordMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public void creatNew(int id, int type, String remark, int value) {
        try {
            Date now = new Date();
            Date nowEnd = DateUtil.getTimingDay(now, 365);
            String ss = sdf.format(nowEnd) + " 23:59:59";

            DgmIntegralRecord record = new DgmIntegralRecord();
            record.setIntValue(value);
            record.setIntType(type);
            record.setUserId(id);
            record.setActiveTime(now);
            record.setOverTime(sdf1.parse(ss));
            record.setIntRemark(remark);
            record.setCreateTime(now);
            dgmIntegralRecordMapper.insert(record);


            DgmUsableIntegral usableIntegral = new DgmUsableIntegral();
            usableIntegral.setUserId(id);
            usableIntegral.setOverTime(sdf1.parse(ss));
            usableIntegral.setRecordId(record.getId().intValue());
            usableIntegral.setUseValue(value);
            usableIntegral.setCreateTime(now);
            dgmUsableIntegralMapper.insert(usableIntegral);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int consumeNew(int id, int type, String remark, int value) {
        Date now = new Date();
        int all = dgmUsableIntegralMapper.sumAll(id);
        if (all + value >= 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", id);
            map.put("isDel", "N");
            List<DgmUsableIntegral> list = dgmUsableIntegralMapper.find(map);

            DgmIntegralRecord record = new DgmIntegralRecord();
            record.setIntValue(value);
            record.setIntType(type);
            record.setUserId(id);
            record.setIntRemark(remark);
            record.setCreateTime(now);
            dgmIntegralRecordMapper.insert(record);

            for (DgmUsableIntegral dgmUsableIntegral : list) {
                if (value == 0) {
                    break;
                }
                int result = dgmUsableIntegral.getUseValue() + value;
                if (result >= 0) {
                    dgmUsableIntegral.setUseValue(result);
                    dgmUsableIntegral.setUpdateTime(now);
                    if (result == 0) {
                        dgmUsableIntegral.setIsDel("Y");
                    }
                    dgmUsableIntegralMapper.updateById(dgmUsableIntegral);

                    DgmReduceIntegralDetail integralDetail = new DgmReduceIntegralDetail();
                    integralDetail.setUserId(id);
                    integralDetail.setRecordId(dgmUsableIntegral.getRecordId());
                    integralDetail.setNewId(record.getId().intValue());
                    integralDetail.setRedValue(Math.abs(value));
                    integralDetail.setOverTime(dgmUsableIntegral.getOverTime());
                    integralDetail.setCreateTime(now);
                    dgmReduceIntegralDetailMapper.insert(integralDetail);

                    value = 0;
                } else {
                    DgmReduceIntegralDetail integralDetail = new DgmReduceIntegralDetail();
                    integralDetail.setUserId(id);
                    integralDetail.setRecordId(dgmUsableIntegral.getRecordId());
                    integralDetail.setNewId(record.getId().intValue());
                    integralDetail.setRedValue(Math.abs(dgmUsableIntegral.getUseValue()));
                    integralDetail.setOverTime(dgmUsableIntegral.getOverTime());
                    integralDetail.setCreateTime(now);
                    dgmReduceIntegralDetailMapper.insert(integralDetail);

                    dgmUsableIntegral.setUseValue(0);
                    dgmUsableIntegral.setIsDel("Y");
                    dgmUsableIntegral.setUpdateTime(now);
                    dgmUsableIntegralMapper.updateById(dgmUsableIntegral);

                    value = result;
                }
            }
            return 0;
        }
        return 1;
    }

    @Override
    public void overNew() {
        Date now = new Date();

        Map<String, Object> map = new HashMap<>();
        map.put("isDel", "N");
        map.put("overTime", sdf1.format(now));
        List<DgmUsableIntegral> list = dgmUsableIntegralMapper.find(map);

        if (list.size() > 0) {
            for (DgmUsableIntegral dgmUsableIntegral : list) {
                DgmIntegralRecord record = new DgmIntegralRecord();
                record.setIntValue(0 - dgmUsableIntegral.getUseValue());
                record.setIntType(99);
                record.setUserId(dgmUsableIntegral.getUserId());
                record.setIntRemark("积分过期");
                record.setCreateTime(now);
                dgmIntegralRecordMapper.insert(record);

                DgmReduceIntegralDetail integralDetail = new DgmReduceIntegralDetail();
                integralDetail.setUserId(dgmUsableIntegral.getUserId());
                integralDetail.setRecordId(dgmUsableIntegral.getRecordId());
                integralDetail.setNewId(record.getId().intValue());
                integralDetail.setRedValue(Math.abs(dgmUsableIntegral.getUseValue()));
                integralDetail.setOverTime(dgmUsableIntegral.getOverTime());
                integralDetail.setCreateTime(now);
                dgmReduceIntegralDetailMapper.insert(integralDetail);

                dgmUsableIntegral.setUseValue(0);
                dgmUsableIntegral.setIsDel("Y");
                dgmUsableIntegral.setUpdateTime(now);
                dgmUsableIntegralMapper.updateById(dgmUsableIntegral);
            }
        }
    }
}

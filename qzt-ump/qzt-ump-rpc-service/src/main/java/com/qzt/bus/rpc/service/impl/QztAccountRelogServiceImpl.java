package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.QztAccountRelogMapper;
import com.qzt.bus.model.QztAccountRelog;
import com.qzt.bus.rpc.api.IQztAccountRelogService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.tools.PriceUtil;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Cgw
 * @since 2019-11-11
 */
@Service("qztAccountRelogService")
public class QztAccountRelogServiceImpl extends BaseServiceImpl<QztAccountRelogMapper, QztAccountRelog> implements IQztAccountRelogService {

    @Autowired
    private QztAccountRelogMapper qztAccountRelogMapper;

    @Override
    public Page<QztAccountRelog> find(Page<QztAccountRelog> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<QztAccountRelog> rb = this.qztAccountRelogMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public Map<String, Object> findAccountById(Long userId) {

        List<Map<String,Object>>  rs = this.qztAccountRelogMapper.findAccountById(userId);
        Map dataMap = new HashMap();
        dataMap.put("sMoney","0.00");
        dataMap.put("tMoney","0.00");
        for(Map map : rs){
            if(map.get("reType").toString().equals("0")){
                dataMap.put("tMoney", PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(map.get("reMoney").toString())));
            }
            if(map.get("reType").toString().equals("1")){
                dataMap.put("sMoney", PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(map.get("reMoney").toString())));
            }
        }
        return dataMap;
    }

    @Override
    public Map<String,Object> findAccountByIdAndType(Map<String,Object> params){
        Map<String,Object> rs = this.qztAccountRelogMapper.findAccountByIdAndType(params);
        long haveMoney = Long.parseLong(rs.get("allMoney").toString())-Long.parseLong(rs.get("changeMoney").toString())-Long.parseLong(rs.get("giveMoney").toString());
        Map<String,Object> backData = new HashMap<>();
        backData.put("allMoney",PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(rs.get("allMoney").toString())));
        backData.put("changeMoney",PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(rs.get("changeMoney").toString())));
        backData.put("giveMoney",PriceUtil.exactlyTwoDecimalPlaces(Long.parseLong(rs.get("giveMoney").toString())));
        backData.put("haveMoney", PriceUtil.exactlyTwoDecimalPlaces(haveMoney));
        return backData;
    }

    @Override
    public int findAccountByBusId(Map<String, Object> params) {
        return this.qztAccountRelogMapper.findAccountByBusId(params);
    }

    @Override
    public List<Map> findAccountRelogByUserId(Map<String, Object> param) {
        return this.qztAccountRelogMapper.findAccountRelogByUserId(param);
    }

    @Override
    public List<QztAccountRelog> findCommendAllMoneyByUserId(Map<String, Object> param) {
        return this.qztAccountRelogMapper.findHaveAccount(param);
    }

    @Override
    public List<Map> findShareMapList(String busId) {
        return this.qztAccountRelogMapper.findShareMapList(busId);
    }
}

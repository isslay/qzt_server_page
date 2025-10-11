package com.qzt.ump.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.common.core.constant.SysConstant;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.common.redis.serialMode.RredisMode;
import com.qzt.common.redis.util.CacheUtil;
import com.qzt.pagedef.PageDef;
import com.qzt.ump.dao.mapper.SysDicMapper;
import com.qzt.ump.model.SysDic;
import com.qzt.ump.rpc.api.SysDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Xiaofei
 * @since 2018-09-06
 */
@Service("sysDicService")
public class SysDicServiceImpl extends BaseServiceImpl<SysDicMapper, SysDic> implements SysDicService {

    @Autowired
    private SysDicMapper sysDicMapper;

    @Override
    public Page find(Page page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<SysDic> rb = this.sysDicMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public Page<Map> queryListSublistPage(Page<Map> page) {
        PageHelper.startPage(page.getCurrent(), page.getSize());
        List<Map> rb = this.sysDicMapper.queryListSublistPage(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos, page);
    }

    @Override
    public List<SysDic> test() {
        return this.sysDicMapper.test();
    }

    @Override
    public List<SysDic> verifyDicType(String dicType) {
        Map map = new HashMap();
        map.put("dicType", dicType);
        List<SysDic> mapList = this.sysDicMapper.find(map);
        return mapList;
    }

    @Override
    public List<Map> verifyDicCode(String code, String parentId) {
        Map map = new HashMap();
        map.put("code", code);
        map.put("parentId", parentId);
        List<Map> rb = sysDicMapper.queryListSublistPage(map);
        return rb;
    }

    @Override
    public List<SysDic> selectDicList() {
        return this.sysDicMapper.find(new HashMap<>());
    }

    @Override
    public List<Map> selectParentIdDicList(Long id) {
        Map map = new HashMap();
        map.put("parentId", id);
        return this.sysDicMapper.queryListSublistPage(map);
    }

    @Override
    public Map selectDicByDicType(String dicType, String code) {
        Map map = new HashMap();
        Map maps = new HashMap();
        map.put("dicType", dicType);
        map.put("code", code);
        List<Map> rb = sysDicMapper.queryListSublistPage(map);
        maps.put("codeText", rb.get(0).get("codeText"));
        return maps;
    }

    @Override
    public void initializeDic() {
        List<SysDic> sysDicList = this.selectDicList();
        CacheUtil.getCache().delAll(SysConstant.CacheNamespaceEnum.DIC.value() + "*");
        for (SysDic sysDic : sysDicList) {
            if ("0".equals(sysDic.getEnable()) && "0".equals(sysDic.getIsDel())) {
                List<Map> dicRs = this.selectParentIdDicList(sysDic.getId());
                Map<String, String> newDic = new HashMap<>();
                for (Map dic : dicRs) {
                    if (dic.get("enable") != null && "0".equals(dic.get("enable").toString())) {
                        newDic.put(dic.get("code").toString(), dic.get("codeText").toString());
                    }
                }
                if (newDic.size() > 0) {
                    RredisMode redisMode = new RredisMode();
                    redisMode.setDicMap(newDic);
                    redisMode.setDicList(dicRs);
                    CacheUtil.getCache().set(SysConstant.CacheNamespaceEnum.DIC.value() + sysDic.getDicType(), redisMode);
                }
            }
        }
    }

}

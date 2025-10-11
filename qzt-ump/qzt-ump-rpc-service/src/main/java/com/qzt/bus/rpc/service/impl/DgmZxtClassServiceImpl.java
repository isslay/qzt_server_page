package com.qzt.bus.rpc.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qzt.bus.dao.mapper.DgmZxtClassMapper;
import com.qzt.bus.model.DgmZxtClass;
import com.qzt.bus.rpc.api.IDgmZxtClassService;
import com.qzt.common.core.base.BaseServiceImpl;
import com.qzt.pagedef.PageDef;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author snow
 * @since 2023-09-25
 */
@Service("dgmZxtClassService")
public class DgmZxtClassServiceImpl extends BaseServiceImpl<DgmZxtClassMapper, DgmZxtClass> implements IDgmZxtClassService {

    @Autowired
    private DgmZxtClassMapper dgmZxtClassMapper;

    @Override
    public Page<DgmZxtClass> find(Page<DgmZxtClass> page) {
        PageHelper.startPage(page.getCurrent(),page.getSize());
        List<DgmZxtClass> rb = this.dgmZxtClassMapper.find(page.getCondition());
        PageInfo pageInfos = new PageInfo<>(rb);
        return PageDef.defPage(pageInfos,page);
    }

    @Override
    public Object getClassList() {
        Map<String, Object> map = new HashMap<>();
        map.put("isDel", "0");
        List<DgmZxtClass> rb = this.dgmZxtClassMapper.find(map);
        if (rb.size() > 0) {
            Map<Long, DgmZxtClass> mapRe = new HashMap();
            rb.forEach(e -> {
                mapRe.put(e.getId(), e);
            });
            List<Object> list1 = getNodeJson(0L, mapRe);
            return list1;
        } else {
            return null;
        }
    }

    /**
     * 递归处理   数据库树结构数据->树形json
     *
     * @param nodeId
     * @param nodes
     * @return
     */
    public static List getNodeJson(Long nodeId, Map<Long, DgmZxtClass> nodes) {

        //当前层级当前node对象'
        //Node cur = nodes.get(nodeId);
        //当前层级当前点下的所有子节点（实战中不要慢慢去查,一次加载到集合然后慢慢处理）
        List<DgmZxtClass> childList = getChildNodes(nodeId, nodes);
        List<Object> childTree = new ArrayList<>();
        for (DgmZxtClass node : childList) {
            Map o = new HashMap();
            o.put("id", node.getId());
            o.put("title", node.getName());
            o.put("leaf", node.getLeaf());
            o.put("pic", node.getPicUrl());
            List childs = getNodeJson(node.getId(), nodes);  //递归调用该方法
            if (!childs.isEmpty()) {
                o.put("children", childs);
            }
            childTree.add(o);
        }
        return childTree;
    }

    /**
     * 获取当前节点的所有子节点
     *
     * @param nodeId
     * @param nodes
     * @return
     */
    public static List<DgmZxtClass> getChildNodes(Long nodeId, Map<Long, DgmZxtClass> nodes) {
        List<DgmZxtClass> list = new ArrayList<>();
        for (Long key : nodes.keySet()) {
            if (nodes.get(key).getParentId() == nodeId) {
                list.add(nodes.get(key));
            }
        }
        list.sort(Comparator.comparingLong(DgmZxtClass::getId));
        return list;
    }
}

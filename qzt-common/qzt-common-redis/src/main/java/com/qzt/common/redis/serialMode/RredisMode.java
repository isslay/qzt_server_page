package com.qzt.common.redis.serialMode;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by cgw on 2018/6/1.
 */
public class RredisMode implements Serializable {

    private Map<String, String> dicMap;

    private List<Map> dicList;

    private Map<String, Object> areaMap;

    private List<Map> areaList;


    public List<Map> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Map> areaList) {
        this.areaList = areaList;
    }

    public Map<String, String> getDicMap() {
        return dicMap;
    }

    public void setDicMap(Map<String, String> dicMap) {
        this.dicMap = dicMap;
    }

    public List<Map> getDicList() {
        return dicList;
    }

    public void setDicList(List<Map> dicList) {
        this.dicList = dicList;
    }

    public Map<String, Object> getAreaMap() {
        return areaMap;
    }

    public void setAreaMap(Map<String, Object> areaMap) {
        this.areaMap = areaMap;
    }
}

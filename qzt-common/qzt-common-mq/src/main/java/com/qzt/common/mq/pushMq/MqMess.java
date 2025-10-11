package com.qzt.common.mq.pushMq;

import com.qzt.common.mq.utils.MqUtils;

import java.util.HashMap;
import java.util.Map;

public class MqMess {
    private String code;
    //业务ID :当CODE为：20 21 30 31必须上传此数据
    private String busId;
    private Map params;

    public MqMess(String code, String busId) {
        this.code = code;
        this.busId = busId;
        this.params = new HashMap();
        this.params.put("code", this.code);
        this.params.put("busId", this.busId);
    }

    public void sendTop() {
        MqUtils.sendTopic("VirtualTopic." + ForcePointStr.getForceStrPoine(this.code), getParams());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}

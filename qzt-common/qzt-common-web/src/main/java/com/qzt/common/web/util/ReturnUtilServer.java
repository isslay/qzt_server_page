package com.qzt.common.web.util;

/**
 * Created by cgw on 2018/5/28.
 */

import com.qzt.common.core.constant.Constant;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据工具类
 * Created by admin on 2016/12/8.
 */
@Component
public class ReturnUtilServer {

    @Resource
    private MessageSource messageSource;

    /**
     * 接口调用返回编码
     */
    private static final String returnCode = "code";
    /**
     * 接口调用返回信息描述
     */
    private static final String returnMessage = "message";

    /**
     * 返回数据增加操作成功编码及操作信息
     *
     * @param data
     * @return
     */
    public Map<String, Object> returnMess(Map<String, Object> data) {
        data.put(returnCode, Constant.RESULT_CODE_SUCCESS);
        data.put(returnMessage, MessageUtil.getMessage(messageSource, Constant.RESULT_CODE_SUCCESS));
        return data;
    }

    public Map<String, Object> returnMessMap(Map<String, Object> object) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(returnCode, Constant.RESULT_CODE_SUCCESS);
        data.put(returnMessage, MessageUtil.getMessage(messageSource, Constant.RESULT_CODE_SUCCESS));
        data.put("data", object);
        return data;
    }

    /**
     * 返回数据  map=data
     *
     * @param object
     * @param codeobj
     * @param messageobj
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2019-11-24
     */
    public Map<String, Object> returnMessMap(Map<String, Object> object, Object codeobj, Object messageobj) {
        Map<String, Object> data = new HashMap<String, Object>();
        String code = codeobj == null ? Constant.RESULT_CODE_SUCCESS : codeobj.toString();
        String message = messageobj == null ? MessageUtil.getMessage(messageSource, code) : messageobj.toString();
        data.put(returnCode, code);
        data.put(returnMessage, message);
        data.put("data", object);
        return data;
    }

    public Map<String, Object> returnMess(Object object) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(returnCode, Constant.RESULT_CODE_SUCCESS);
        data.put(returnMessage, MessageUtil.getMessage(messageSource, Constant.RESULT_CODE_SUCCESS));
        data.put("data", object);
        return data;
    }

    /**
     * 系统错误返回错误编码及相对应操作信息
     *
     * @return
     */
    public Map<String, Object> returnMess(String code) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(returnCode, code);
        data.put(returnMessage, MessageUtil.getMessage(messageSource, code));
        return data;
    }

    /**
     * 系统错误返回错误编码及相对应操作信息
     *
     * @return
     */
    public Map<String, Object> returnMess(String code, String message) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put(returnCode, code);
        data.put(returnMessage, message);
        return data;
    }

    /**
     * 返回数据及操作编码
     *
     * @param data 返回数据
     * @param code 错误编码
     * @return
     */
    public Map<String, Object> returnMess(Map<String, Object> data, String code) {
        data.put(returnCode, code);
        data.put(returnMessage, MessageUtil.getMessage(messageSource, code));
        return data;
    }

    /**
     * 返回数据及操作编码
     *
     * @param data       返回数据
     * @param codeobj    编码
     * @param messageobj 编码描述
     * @return
     */
    public Map<String, Object> returnMess(Map<String, Object> data, Object codeobj, Object messageobj) {
        String code = codeobj == null ? Constant.RESULT_CODE_SUCCESS : codeobj.toString();
        String message = messageobj == null ? MessageUtil.getMessage(messageSource, code) : messageobj.toString();
        data.put(returnCode, code);
        data.put(returnMessage, message);
        return data;
    }

    //将javabean实体类转为map类型，然后返回一个map类型的值
    public static Map<String, Object> beanToMap(Object obj) {
        Map<String, Object> params = new HashMap<String, Object>(0);
        try {
            PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
            PropertyDescriptor[] descriptors = propertyUtilsBean.getPropertyDescriptors(obj);
            for (int i = 0; i < descriptors.length; i++) {
                String name = descriptors[i].getName();
                if (!"class".equals(name)) {
                    params.put(name, propertyUtilsBean.getNestedProperty(obj, name));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }

}


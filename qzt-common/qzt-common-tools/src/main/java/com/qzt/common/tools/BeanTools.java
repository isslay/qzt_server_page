package com.qzt.common.tools;

import org.apache.commons.beanutils.PropertyUtilsBean;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体工类具
 *
 * @author Xiaofei
 * @date 2019-11-28
 */
public class BeanTools {

    /**
     * 实体类转MAP
     *
     * @param obj
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @author Xiaofei
     * @date 2019-11-28
     */
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

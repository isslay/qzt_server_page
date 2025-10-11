package com.qzt.common.redis.util;

import com.qzt.common.redis.serialMode.RredisMode;
import com.qzt.common.core.constant.SysConstant;

import java.util.List;
import java.util.Map;

/**
 * @author Xiaofei
 * @date 2019-07-31
 */
public class DicParamUtil {

    /**
     * 根据字典类型和字典CODE得到对应的值
     *
     * @param dicType
     * @param code
     * @return
     */
    public static String getDicCodeByType(String dicType, String code) {
        RredisMode rm = (RredisMode) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.DIC.value() + dicType);
        Map<String, String> dicMap = rm.getDicMap();
        if (dicMap == null) {
            return "";
        }
        String dicValue = dicMap.get(code);
        return dicValue == null ? "" : dicValue;
    }

    public static List<Map> getDicList(String dicType) {
        RredisMode rm = (RredisMode) CacheUtil.getCache().get(SysConstant.CacheNamespaceEnum.DIC.value() + dicType);
        return rm.getDicList();
    }

}

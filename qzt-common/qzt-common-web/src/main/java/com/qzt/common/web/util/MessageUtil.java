package com.qzt.common.web.util;

import org.springframework.context.MessageSource;
import java.util.Locale;

/**
 *
 * @author Xiaofei
 * @date 2019-10-16
 */
public final class MessageUtil {

    /**
     * 获取当前key对应的文字信息
     * @param key
     * @return
     */
    public static String getMessage(MessageSource messageSource,String key) {
        return messageSource.getMessage(key, new Object[]{}, Locale.CHINA);
    }

    /**
     * 获取当前key对应的文字信息
     * @param key
     * @param args
     * @return
     */
    public static String getMessage(MessageSource messageSource,String key, Object[] args) {
        // return messageSource.getMessage(key, new Object[]{}, LocaleContextHolder.getLocale());
        return messageSource.getMessage(key, args, Locale.CHINA);
    }
}

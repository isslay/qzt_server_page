package com.qzt.common.web.configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ContentTemplet {

    /**
     * 关联账号验证码
     */
    public static final Object[] ASSOCIATED_ACCOUNT = {237868, "您的短信验证码为：{1}。用于关联账号操作，10分钟内有效。（请勿泄露）"};

    /**
     * 注册验证码
     */
    public static final Object[] USER_REG = {215829, "您的短信验证码为：{1}。用于注册操作，10分钟内有效。（请勿泄露）"};

    /**
     * 找回密码验证码
     */
    public static final Object[] USER_RETRIEVE_PASSWORD = {236849, "您的短信验证码为：{1}。用于找回密码操作，10分钟内有效。（请勿泄露）"};

    /**
     * 实名认证验证码
     */
    public static final Object[] USER_CTFT = {217400, "您的短信验证码为：{1}。用于实名认证操作，10分钟内有效。（请勿泄露）"};

    public static void main(String[] args) {
        String str = "我是{0},我来自{1},今年{2}岁";
        String[] arr = {"中国人", "北京", "212", "55"};
        Matcher m = Pattern.compile("\\{(\\d)\\}").matcher(str);
        while (m.find()) {
            str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
        }
        System.out.println(str);
    }


}

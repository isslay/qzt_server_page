package com.qzt.common.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by cgw on 2018/7/19.
 */

@Component
@ConfigurationProperties(prefix = "wxpay")
public class PayWxConfig {
    /**
     * 是否测试服
     */
    private Boolean testValue;
    /**
     * 公众号APPID
     */
    private String weChatAppId;
    /**
     * 公众号appSecret
     */
    private String weChatSecret;
    /**
     * 商户ID
     */
    private String weChatMicId;
    /**
     * 商户ID平台秘钥（签名）
     */
    private String weChatSign;
    /**
     * 小程序appletAppid
     */
    private String appletAppid;
    /**
     * 小程序AppSecret
     */
    private String appletAppSecret;
    /**
     * 微信支付回调地址
     */
    private String weChatPayBackUrl;
    /**
     * APP应用ID
     */
    private String weChatAppAppId;
    /**
     * APP应用秘钥
     */
    private String weChatAppAppSecret;
    /**
     * 支付宝APP_ID
     */
    private String aliAppAppId;
    /**
     * 支付宝调起url
     */
    private String aliPayUrl;
    /**
     * 支付宝APP_PRIVATE_KEY
     */
    private String aliAppPrivateKey;
    /**
     * 支付商家公钥ALIPAY_PUBLIC_KEY
     */
    private String aliAppAlipayShopPublicKey;
    /**
     * 支付公钥ALIPAY_PUBLIC_KEY
     */
    public String aliAppAlipayPublicKey;
    /**
     * 支付宝回调地址
     */
    public String aliAppBackUrl;


    public Boolean getTestValue() {
        return testValue;
    }

    public void setTestValue(Boolean testValue) {
        this.testValue = testValue;
    }

    public String getWeChatAppId() {
        return weChatAppId;
    }

    public void setWeChatAppId(String weChatAppId) {
        this.weChatAppId = weChatAppId;
    }

    public String getWeChatSecret() {
        return weChatSecret;
    }

    public void setWeChatSecret(String weChatSecret) {
        this.weChatSecret = weChatSecret;
    }

    public String getWeChatMicId() {
        return weChatMicId;
    }

    public void setWeChatMicId(String weChatMicId) {
        this.weChatMicId = weChatMicId;
    }

    public String getWeChatSign() {
        return weChatSign;
    }

    public void setWeChatSign(String weChatSign) {
        this.weChatSign = weChatSign;
    }

    public String getAppletAppid() {
        return appletAppid;
    }

    public void setAppletAppid(String appletAppid) {
        this.appletAppid = appletAppid;
    }

    public String getAppletAppSecret() {
        return appletAppSecret;
    }

    public void setAppletAppSecret(String appletAppSecret) {
        this.appletAppSecret = appletAppSecret;
    }

    public String getWeChatPayBackUrl() {
        return weChatPayBackUrl;
    }

    public void setWeChatPayBackUrl(String weChatPayBackUrl) {
        this.weChatPayBackUrl = weChatPayBackUrl;
    }

    public String getWeChatAppAppId() {
        return weChatAppAppId;
    }

    public void setWeChatAppAppId(String weChatAppAppId) {
        this.weChatAppAppId = weChatAppAppId;
    }

    public String getWeChatAppAppSecret() {
        return weChatAppAppSecret;
    }

    public void setWeChatAppAppSecret(String weChatAppAppSecret) {
        this.weChatAppAppSecret = weChatAppAppSecret;
    }

    public String getAliAppAppId() {
        return aliAppAppId;
    }

    public void setAliAppAppId(String aliAppAppId) {
        this.aliAppAppId = aliAppAppId;
    }

    public String getAliPayUrl() {
        return aliPayUrl;
    }

    public void setAliPayUrl(String aliPayUrl) {
        this.aliPayUrl = aliPayUrl;
    }

    public String getAliAppPrivateKey() {
        return aliAppPrivateKey;
    }

    public void setAliAppPrivateKey(String aliAppPrivateKey) {
        this.aliAppPrivateKey = aliAppPrivateKey;
    }

    public String getAliAppAlipayShopPublicKey() {
        return aliAppAlipayShopPublicKey;
    }

    public void setAliAppAlipayShopPublicKey(String aliAppAlipayShopPublicKey) {
        this.aliAppAlipayShopPublicKey = aliAppAlipayShopPublicKey;
    }

    public String getAliAppAlipayPublicKey() {
        return aliAppAlipayPublicKey;
    }

    public void setAliAppAlipayPublicKey(String aliAppAlipayPublicKey) {
        this.aliAppAlipayPublicKey = aliAppAlipayPublicKey;
    }

    public String getAliAppBackUrl() {
        return aliAppBackUrl;
    }

    public void setAliAppBackUrl(String aliAppBackUrl) {
        this.aliAppBackUrl = aliAppBackUrl;
    }
}

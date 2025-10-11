package com.qzt.common.pay.util;

import java.util.UUID;

public class WxJsApiConfig {
	public boolean debug = false;
	public String appId = "";
	public long timestamp = System.currentTimeMillis()/1000;
	public String nonceStr = UUID.randomUUID().toString();
	public String signature;
	public String url;
	public String[] jsApiList = new String[]{
		"onMenuShareTimeline",
		"onMenuShareAppMessage",
		"onMenuShareQQ",
		"onMenuShareWeibo",
		"onMenuShareQZone",
		"downloadImage",
		"hideMenuItems",
		"showMenuItems",
		"hideAllNonBaseMenuItem",
		"closeWindow"
	};
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String[] getJsApiList() {
		return jsApiList;
	}
	public void setJsApiList(String[] jsApiList) {
		this.jsApiList = jsApiList;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}

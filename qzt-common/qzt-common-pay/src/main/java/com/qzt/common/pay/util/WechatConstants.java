package com.qzt.common.pay.util;

public class WechatConstants {
	
	public String appId;
	
	public String appSecret;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	
	
	public static final class MsgType{
		//文本
		public static final String Text = "text";
		//图片
		public static final String Image = "image";
		//声音
		public static final String Voice = "voice";
		//视频
		public static final String Video = "video";
		//缩略图
		public static final String Thumb = "thumb";
		//小视频
		public static final String Shortvideo = "shortvideo";
		//地理位置
		public static final String Location = "location";
		//链接消息
		public static final String Link = "link";
		//音乐消息
		public static final String Music = "music";
		//图文消息
		public static final String News = "news";
		//事件消息
		public static final String Event = "event";
		//事件消息
		public static final String CustomerService = "transfer_customer_service";
	}
	
}

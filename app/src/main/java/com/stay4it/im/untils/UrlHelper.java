package com.stay4it.im.untils;

/**
 * Helper class to generate escaped URIs
 * 
 */
public class UrlHelper {
	public static final String DEFAULT_ENCODING = "UTF-8";
	// public static final String DOMAIN = "http://test.coreteacher.cn/v1";
	public static String DOMAIN = "http://im.stay4it.com";
	private static final String ACTION_LOGIN = "/user/account/login";
	private static final String ACTION_BIND_BAIDU_PUSH = "/user/account/bindBaiduPushUserId";
	private static final String ACTION_GET_CONVERSATION = "/user/message/getConversationList";
	private static final String ACTION_GET_MESSAGE = "/user/message/getAllMessages";
	private static final String ACTION_SEND_MSG = "/user/message/sendMsg";
	private static final String ACTION_SEND_MEDIA_MSG = "/user/message/sendMediaMsg";
	

	public static String getDomain() {
		return DOMAIN + "/v1";
	}

	public static String loadLogin() {
		return getDomain() + ACTION_LOGIN;
	}
	
	public static String loadConversation(){
		return getDomain() + ACTION_GET_CONVERSATION;
	}
	
	public static String loadAllMsg(String id, int state, long timestamp) {
		if (state == Constants.REFRESH && timestamp != 0l) {
			return getDomain() + ACTION_GET_MESSAGE + "/" + id + "?endTimestamp=" + timestamp + "&timestamp=0&count=" + Integer.MAX_VALUE;
		}
		return getDomain() + ACTION_GET_MESSAGE + "/" + id + "?timestamp=" + timestamp + "&count=" + Constants.PAGECOUNT;
	}

	public static String loadBindBaidu(String userId) {
		return getDomain() + ACTION_BIND_BAIDU_PUSH + "?baiduPushUserId=" + userId;
	}
	
	public static String loadSendMsg() {
		return getDomain() + ACTION_SEND_MSG;
	}

	public static String loadSendMediaMsg() {
		return getDomain() + ACTION_SEND_MEDIA_MSG;
	}
	
	public static String loadImg(String picture, boolean entire) {
		if (entire) {
			return loadAttachment(picture);
		} else {
			return loadAttachment(picture) + "?width=150&height=150";
		}
	}
	
	public static String loadAttachment(String picture) {
		return getDomain() + picture;
	}

}

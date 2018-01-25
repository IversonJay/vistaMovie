package com.lhh.vista.web.sms;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.TimeUnit;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.WinphoneNotification;
import cn.jpush.api.report.ReceivedsResult;
import cn.jpush.api.report.ReceivedsResult.Received;
import cn.jpush.api.report.UsersResult;

public class JdpushUtil {
	protected static final Logger log = LoggerFactory.getLogger(Jdpush.class);

	private static final String APPKEY = "0843516bd1a60da7a72683f6";
	private static final String MASTERSECRET = "28c651e3c86d2d9407d02f50";
//	private static final String DAY = config.getValue("jpush.offlineday");
	public static JPushClient jpushClient = null;

	/**
	 * 推送通知接口
	 * @param alias 别名
	 * @param tags tag数组
	 * @param title 推送标题
	 * @param btype 推送类型
	 * @param content 推送内容
	 */
	public static void sendPushNotice(String alias, String[] tags, String title, String btype, String content) {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY);
		PushPayload payload = null;
		// 生成推送的内容，这里我们先测试全部推送
		// 通知提示信息
		if (content != null) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("btype", btype);
			// 根据别名推送
			if (alias != null && tags == null) {
				payload = buldPushObject_all_all_alias(alias, title, content, map);
			} else if (alias == null && tags != null) { // 根据tag[]推送
				payload = buldPushObject_all_all_tag(tags, title, content, map);
			} else if (alias != null && tags != null) { // 别名和tags[] 推送通知
				payload = buldPushObject_all_all_aliasAndTag(alias, tags, title, content, map);
			} else if (alias == null && tags == null) {
				payload = buldPushObject_all_all(title, content, map);
			}
		} else {
			log.info("No notification - " + content);
		}
		try {
			System.out.println(payload.toString());
			PushResult result = jpushClient.sendPush(payload);
			System.out.println(result.msg_id+ "................................");
			log.info("Got result - " + result);
		} catch (APIConnectionException e) {
			log.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			log.error("Error response from JPush server. Should review and fix it. ", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			log.info("Msg ID: " + e.getMsgId());
		}
	}

	public static void main(String[] args) {
//		sendPushMessage("18562836208", 1219, "距您观影时间还有一小时，祝您观影愉快。");
		Map<String, String> map = new HashMap<>();
		map.put("mtype", "1");
		map.put("target", "http://www.baidu.com");
		sendAllPushMessage("推送测试", map);
	}
	
	
	/**
	 * 推送自定义消息接口.根据别名修改标签（tag）
	 * @param alias 别名
	 * @param content 推送内容
	 */
	public static void sendPushMessage(String alias, Map map, String content) {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY);
		PushPayload payload = null;
		// 判断用户别名和tag不为空的情况下才推送修改标签（tag）
		if (content != null && alias != null) {
			payload = PushPayload.newBuilder()
					.setAudience(Audience.alias(alias))
					.setPlatform(Platform.all())
					.setNotification(
							Notification
									.newBuilder()
									.addPlatformNotification(
											IosNotification.newBuilder()
													.setAlert(content)
													.addExtras(map).build())
									.addPlatformNotification(
											AndroidNotification.newBuilder()
													.setAlert(content)
													.addExtras(map) 
													.build())
									.build())
					.setOptions(Options.newBuilder().setApnsProduction(true).build())
					.setMessage(Message.content(content)).build();
		} else {
			log.info("No notification - " + content);
		}
		try {
			System.out.println(payload.toString());
			PushResult result = jpushClient.sendPush(payload);
			System.out.println(result + "................................");
			log.info("Got result - " + result);
		} catch (APIConnectionException e) {
			log.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			log.error("Error response from JPush server. Should review and fix it. ", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			log.info("Msg ID: " + e.getMsgId());
		}
	}

	/**
	 * 查询记录推送成功条数
	 * @param mid
	 */
	public static void countPush(String mid) {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY);
		PushPayload payload = null;
		try {
			ReceivedsResult result = jpushClient.getReportReceiveds(mid);
			Received received = result.received_list.get(0);
			System.out.println("android_received:" + received.android_received
					+ "\nios:" + received.ios_apns_sent);
			log.debug("Got result - " + result);
		} catch (APIConnectionException e) {
			// Connection error, should retry later
			log.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			log.error("Should review the error, and fix the request", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
		}
	}

	/**
	 * 统计用户数据。需要vip用户才能访问
	 */
	public static void getReportUser() {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY);
		PushPayload payload = null;
		try {
			UsersResult result = jpushClient.getReportUsers(TimeUnit.DAY, "2015-04-28", 8);
			// Received received =result
			// System.out.println("android_received:"+received.android_received+"\nios:"+received.ios_apns_sent);
			log.debug("Got result - " + result);
		} catch (APIConnectionException e) {
			// Connection error, should retry later
			log.error("Connection error, should retry later", e);
		} catch (APIRequestException e) {
			// Should review the error, and fix the request
			log.error("Should review the error, and fix the request", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
		}
	}

	/**
	 * 根据别名通知推送
	 * @param alias 别名
	 * @param alert 推送内容
	 * @return
	 */
	public static PushPayload buldPushObject_all_all_alias(String alias, String title, String content, Map<String, String> map) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(alias))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setAlert(content)
												.setTitle(title).addExtras(map)
												.build())
								.addPlatformNotification(
										WinphoneNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.build()).build();
	}

	/**
	 * 根据tag通知推送
	 * @param alias 别名
	 * @param alert 推送内容
	 * @return
	 */
	public static PushPayload buldPushObject_all_all_tag(String[] tags, String title, String content, Map<String, String> map) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.tag(tags))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setAlert(content)
												.setTitle(title).addExtras(map)
												.build())
								.addPlatformNotification(
										WinphoneNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.build()).build();
	}

	/**
	 * 根据tag通知推送 
	 * @param alias  别名
	 * @param alert  推送内容
	 * @return
	 */
	public static PushPayload buldPushObject_all_all_aliasAndTag(String alias, String[] tags, String title, String content, Map<String, String> map) {
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.alias(alias))
				.setAudience(Audience.tag(tags))
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setAlert(content)
												.setTitle(title).addExtras(map)
												.build())
								.addPlatformNotification(
										WinphoneNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.build()).build();
	}

	/**
	 * 根据通知推送
	 * @param alias 别名
	 * @param alert 推送内容
	 * @return
	 */
	public static PushPayload buldPushObject_all_all(String title, String content, Map<String, String> map) {
		System.out.println("haha");
		return PushPayload
				.newBuilder()
				.setPlatform(Platform.all())
				.setAudience(Audience.all())
				.setNotification(
						Notification
								.newBuilder()
								.addPlatformNotification(
										IosNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.addPlatformNotification(
										AndroidNotification.newBuilder()
												.setAlert(content)
												.setTitle(title).addExtras(map)
												.build())
								.addPlatformNotification(
										WinphoneNotification.newBuilder()
												.setAlert(content)
												.addExtras(map).build())
								.build()).build();
	}
	
	
	
	
	public static void sendAllPushMessage(String content, Map map) {
		jpushClient = new JPushClient(MASTERSECRET, APPKEY);
		PushPayload payload = null;
		// 判断用户别名和tag不为空的情况下才推送修改标签（tag）
		if (content != null) {
			payload = PushPayload.newBuilder()
					.setAudience(Audience.all())
					.setPlatform(Platform.all())
					.setNotification(
							Notification
									.newBuilder()
									.addPlatformNotification(
											IosNotification.newBuilder()
													.setAlert(content)
													.addExtras(map).build())
									.addPlatformNotification(
											AndroidNotification.newBuilder()
													.setAlert(content)
													.addExtras(map) 
													.build())
									.build())
					.setOptions(Options.newBuilder().setApnsProduction(true).build())
					.setMessage(Message.content(content)).build();
		} else {
			log.info("No notification - " + content);
		}
		try {
			System.out.println(payload.toString());
			PushResult result = jpushClient.sendPush(payload);
			System.out.println(result + "................................");
			log.info("Got result - " + result);
		} catch (APIConnectionException e) {
			log.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			log.error("Error response from JPush server. Should review and fix it. ", e);
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			log.info("Msg ID: " + e.getMsgId());
		}
	}
	
	
	

}
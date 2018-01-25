package com.lhh.vista.web.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.service.service.AppUserOrderService;
import com.lhh.vista.web.sms.JdpushUtil;
import com.lhh.vista.web.sms.SmsUtil;

/**
 * Created by liu on 2016/12/27.
 */
@Component
public class SendMessageTask {

	@Autowired
	private SmsUtil smsUtil;
	@Autowired
	private AppUserOrderService appUserOrderService;

	public void doTask() {
		long currentTime = System.currentTimeMillis();
		currentTime += 120 * 60 * 1000;
		Date date = new Date(currentTime);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String queryTime = sdf.format(date) + ":00";

		List<AppUserOrder> appUserOrderList = appUserOrderService.getOrderByStime(queryTime);
		if (appUserOrderList != null) {
			for (AppUserOrder appUserOrder : appUserOrderList) {
				// 激光推送
				smsUtil.sendMovieStart(appUserOrder.getMphone());
				Map<String, String> map = new HashMap<>();
				map.put("target", appUserOrder.getId() + "");
				map.put("mtype", "3");
				JdpushUtil.sendPushMessage(appUserOrder.getMphone(), map, "距您观影时间还有两小时，请预留足够取票时间。");
			}
		}

		System.out.println("开始发送短信的时间 : " + queryTime + "  发送成功");

	}

	public static void main(String[] args) {
		long currentTime = System.currentTimeMillis();
		currentTime += 60 * 60 * 1000;
		Date date = new Date(currentTime);
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String queryTime = sdf.format(date) + ":00";

		System.out.println("哈哈哈哈哈" + queryTime);
	}
}

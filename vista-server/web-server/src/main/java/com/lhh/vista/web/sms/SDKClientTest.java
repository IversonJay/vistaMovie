package com.lhh.vista.web.sms;

import java.net.URLEncoder;
import java.util.List;

/**
 * 
 * @项目名称：EmayClientForHttpV1.0 
 * @类描述：  
 * @创建人：HL.W  
 * @创建时间：2015-9-9 下午5:29:46  
 * @修改人：HL.W  
 * @修改时间：2015-9-9 下午5:29:46  
 * @修改备注：
 */
public class SDKClientTest {

	public static String sn = "9SDK-EMY-0999-SCSOL";
	public static String key = "444098";
	public static String password = "444098";
	public static String baseUrl = "http://sh999.eucp.b2m.cn:8080/sdkproxy/";
	public static String sendMethod = "get";

	public static void main(String[] args) {
		try {
			StartMenu();
			while (true) {
				System.out.println("请输入序号进行操作");
				byte[] command = new byte[4];
				System.in.read(command);
				int operate = 0;
				try {
					String commandString = new String(command);
					commandString = commandString.replaceAll("\r\n", "").trim();
					operate = Integer.parseInt(commandString);
				} catch (Exception e) {
					System.out.println("命令输入错误！！！");
				}
				String param = "";
				String ret="";

				switch (operate) {
				case 1:
					String url = baseUrl + "regist.action";
					param = "cdkey=" + sn + "&password=" + password;
					ret = SDKHttpClient.registAndLogout(url, param);
					System.out.println("注册结果:" + ret);
					break;
				case 2:
					param = "cdkey=" + sn + "&password=" + key;
					url = baseUrl + "querybalance.action";
					String balance = SDKHttpClient.getBalance(url, param);
					System.out.println("当前余额:" + balance);
					break;
				case 3:
					String mdn = "15092168039";
					String message = "send->" + System.currentTimeMillis();
					message = URLEncoder.encode(message, "UTF-8");
					String code = "";
					long seqId = System.currentTimeMillis();
					param = "cdkey=" + sn + "&password=" + key + "&phone=" + mdn + "&message=" + message + "&addserial=" + code + "&seqid=" + seqId;
					url = baseUrl + "sendsms.action";
					if ("get".equalsIgnoreCase(sendMethod)) {
						ret = SDKHttpClient.sendSMS(url, param);
					} else {
						//ret = SDKHttpClient.sendSMSByPost(url, param);
					}

					System.out.println("发送结果:" + ret);
					break;
				case 4:
					param = "cdkey=" + sn + "&password=" + key;
					url = baseUrl + "getmo.action";
					List<Mo> mos = SDKHttpClient.getMos(url, sn, key);
					System.out.println("获取上行数量：" + mos.size());
					break;
				case 5:
					param = "cdkey=" + sn + "&password=" + key;
					url = baseUrl + "getreport.action";
					List<StatusReport> srs = SDKHttpClient.getReports(url, sn, key);
					System.out.println("获取报告数量：" + srs.size());
					break;
				case 6:
					url = baseUrl + "logout.action";
					param = "cdkey=" + sn + "&password=" + password;
					ret = SDKHttpClient.registAndLogout(url, param);
					System.out.println("注销结果:" + ret);
					break;
				case 7:
					System.exit(0);
				default:
					System.out.println("没有该命令 " + operate);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void StartMenu() {
		int i = 1;
		System.out.println(i + "、激活序列号,初次使用、已注销后再次使用时调用该方法.");
		i += 1;
		System.out.println(i + "、余额查询");
		i += 1;
		System.out.println(i + "、发送带有信息ID的短信,可供查询状态报告");
		i += 1;
		System.out.println(i + "、获取上行短信");
		i += 1;
		System.out.println(i + "、获得下行短信状态报告");
		i += 1;
		System.out.println(i + "、注销序列号");
		i += 1;
		System.out.println(i + "、关闭程序");
	}
}

package com.lhh.vista.web.sms;

import java.net.URLEncoder;
import java.util.List;


public class SmsTest {

    public static String sn = "0SDK-EBB-6699-RDRMR";
    public static String key = "301625";
    public static String password = "301625";
    public static String baseUrl = "http://hprpt2.eucp.b2m.cn:8080/sdkproxy/";
    public static String sendMethod = "get";

    public static void main(String[] args) {
        try {
            String ret;
            String mdn = "13210078536";
            String message = "测试" + System.currentTimeMillis();
            message = URLEncoder.encode(message, "UTF-8");
            System.out.println(message);
            String code = "";
            long seqId = System.currentTimeMillis();
            String param = "cdkey=" + sn + "&password=" + key + "&phone=" + mdn + "&message=" + message + "&addserial=" + code + "&seqid=" + seqId;
            String url = baseUrl + "sendsms.action";
            if ("get".equalsIgnoreCase(sendMethod)) {
                ret = SDKHttpClient.sendSMS(url, param);
            } else {
                // ret = SDKHttpClient.sendSMSByPost(url, param);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
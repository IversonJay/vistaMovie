package com.lhh.vista.web.sms;

import com.lhh.vista.common.util.StateTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Random;

/**
 * Created by liu on 2017/2/8.
 */
@Component
public class SmsUtil {
    @Value("#{app.smssn}")
    private String sn = "9SDK-EMK-0999-SCSOL";// 软件序列号,请通过亿美销售人员获取
    @Value("#{app.smskey}")
    private String key = "444098";// 序列号首次激活时自己设定
    @Value("#{app.smspassword}")
    private String password = "444098";// 密码,请通过亿美销售人员获取
    @Value("#{app.smsbaseUrl}")
    private String baseUrl = "http://sh999.eucp.b2m.cn:8080/sdkproxy/";

    public String sendSms(String mdn) {
        try {
            String ret;
            String code = getCode();
            String message = "【海上明珠电影】您的短信验证码为：" + code;
            message = URLEncoder.encode(message, "UTF-8");
            long seqId = System.currentTimeMillis();
            String param = "cdkey=" + sn + "&password=" + key + "&phone=" + mdn + "&message=" + message + "&seqid=" + seqId;
            String url = baseUrl + "sendsms.action";
            ret = SDKHttpClient.sendSMS(url, param);
            System.out.println(ret);
            if ("0".equals(ret)) {
                return code;
            }
        } catch (Exception e) {
        }
        StateTool.checkState(false, StateTool.State.FAIL);
        return "";
    }
    
    
    public String sendMms(String phone, String mInfo) {

      String mInfoArr[] = mInfo.split("@_@");
      try {
          String ret;
          String message = "【海上明珠电影】" + mInfoArr[1] + mInfoArr[2] + mInfoArr[4] + "《" + mInfoArr[0] + "》已购票成功。座位号:" + mInfoArr[5] + "。取票码: " + mInfoArr[3] + "。请使用取票码至影院自助取票机或前台取票，祝您观影愉快！";
          message = URLEncoder.encode(message, "UTF-8");
          long seqId = System.currentTimeMillis();
          String param = "cdkey=" + sn + "&password=" + key + "&phone=" + phone + "&message=" + message + "&seqid=" + seqId;
          String url = baseUrl + "sendsms.action";
          ret = SDKHttpClient.sendSMS(url, param);
          System.out.println(ret);
          if ("0".equals(ret)) {
              return "";
          }
      } catch (Exception e) {
      }
      StateTool.checkState(false, StateTool.State.FAIL);
      return "";
  }
    
    public String sendMovieStart(String phone) {

        try {
            String ret;
            String message = "【海上明珠影城温馨提示】距您观影时间还有两小时，请预留足够取票时间。为提升3D观影舒适度，现已提供专属眼镜自由选购服务。更多精美小食、健康饮品欢迎现场品鉴，祝您观影愉快。";
            message = URLEncoder.encode(message, "UTF-8");
            long seqId = System.currentTimeMillis();
            String param = "cdkey=" + sn + "&password=" + key + "&phone=" + phone + "&message=" + message + "&seqid=" + seqId;
            String url = baseUrl + "sendsms.action";
            ret = SDKHttpClient.sendSMS(url, param);
            System.out.println(ret);
            if ("0".equals(ret)) {
                return "";
            }
        } catch (Exception e) {
        }
        StateTool.checkState(false, StateTool.State.FAIL);
        return "";
    }
    
    
    private String getCode() {
        Random random = new Random();
        int x = random.nextInt(899999);
        x = x + 100000;
        return x + "";
    }

    public static void main(String[] xx) {
        new SmsUtil().sendSms("18366183726");
    }
}

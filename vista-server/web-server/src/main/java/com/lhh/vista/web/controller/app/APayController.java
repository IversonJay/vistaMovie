package com.lhh.vista.web.controller.app;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.v2s.dto.Order;
import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.service.model.AppUserRecharge;
import com.lhh.vista.service.service.AppUserRechargeService;
import com.lhh.vista.service.service.OrderService;
import com.lhh.vista.web.pay.weixin.CommonUtil;
import com.lhh.vista.web.pay.weixin.PayCommonUtil;
import com.lhh.vista.web.pay.weixin.WxPayUtil;
import com.lhh.vista.web.pay.weixin.XMLUtil;
import com.lhh.vista.web.pay.weixin.http.HttpRequestUtil;
import com.lhh.vista.web.sms.SmsUtil;

/**
 * Created by liu on 2017/1/18.
 */
@Controller
@RequestMapping("a_pay")
public class APayController extends BaseController {
//	public static final String WX_APPID = "wx926456573216d158";
//	public static final String WX_MCH_ID = "1440608902";
//	public static final String WX_API_KEY = "a4df4458c06679433e89524326c7323a";
//	public static final String WX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//	public static final String WX_APPID = "wxe3aa85309494564e";
//	public static final String WX_MCH_ID = "1463239402";
//	public static final String WX_API_KEY = "lanhaihuiwangluokeji888888888888";
//	public static final String WX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String WX_APPID = "wx808e8168562bbfa4";
	public static final String WX_MCH_ID = "1487202432";
	public static final String WX_API_KEY = "QuJianQian0123456789987654321000";
	public static final String WX_UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	
	public static final String ALI_URL = "https://openapi.alipay.com/gateway.do";
	// 支付宝公钥
	public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmEUB6DGkmQAhGAiv5HNHkhNkld8z1zFRP/q5der2c7ES0Rt4DTLfhHwx7uYEhUoeluC+9xbDW2ExRQI+MBQSD38IFxBLgo35CI1b2OS4VasLH2/fyFpz8vwlzTHAOARMgzExiD0GrPBa2PONOmUJ1tqc5GVyrSYF4fFfIoiCy0KM41JM66Px3/KXebq/Auq2/ui92vcjidRSbz3XEPv8I2m+cOeHWBvagVgA9aGwvSQJQBiAwZ/mipLNAdzYe/y9a8ceX2Nof2/LX/fMneqoPmIyBHTCKlpFYj2eFnafnC0MjgaYlVFar+7icFJ3mUtVpORwQ/zKzS/ti5ltei0jcQIDAQAB";// 支付宝商户的公钥
	// 应用公钥
	public static final String ALIPAY_PUBLIC_SIGN_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtxPfffl27r+1WEduREm0Z3H1fzP8kVXXPoYxh8/wGMTESoHEFlyeSjwrHpwQqXTFqBMVK9ifXOGpukqu8WYjxzx7d8fJrTt+rP6fXzOmLxpbVUZP1ar7uxzvQ6JQWb13O858cLt0rw4KtLZ0r/pmPvxWo/oxV7aSGbG4x7JuXQd1kcXy/1Dx+anpYhWE36tnr1PTvXyVmQb+Zz9q7t4ITZ46kjDocfYNDXo4pgMv3nQH8xxp9ATX8cJ2UkcvedvoSWRWcH5Kak4b3zywPvGluBqrnAx40FA7HLj7M70GzNWc+zNvN/9FLEiesbFyko/fkxAqqbBbPSP2PMsOlxlGzQIDAQAB";
	public static final String ALIPAY_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC3E999+Xbuv7VYR25ESbRncfV/M/yRVdc+hjGHz/AYxMRKgcQWXJ5KPCsenBCpdMWoExUr2J9c4am6Sq7xZiPHPHt3x8mtO36s/p9fM6YvGltVRk/Vqvu7HO9DolBZvXc7znxwu3SvDgq0tnSv+mY+/Faj+jFXtpIZsbjHsm5dB3WRxfL/UPH5qeliFYTfq2evU9O9fJWZBv5nP2ru3ghNnjqSMOhx9g0NejimAy/edAfzHGn0BNfxwnZSRy952+hJZFZwfkpqThvfPLA+8aW4GqucDHjQUDscuPszvQbM1Zz7M283/0UsSJ6xsXKSj9+TECqpsFs9I/Y8yw6XGUbNAgMBAAECggEBALN87FQZS5zunuAZx+VzTUdN1p4LXccvHYPTFKROHNprrvZNGu3Bh/7H1o5/+2XnchFCBDOkgTkSlNBrf/BzbwhaJ8JbQUJxXdRFHgK8JGVxtbF46bRVmc7sozbv307cDhW4bxAm+FLXtW9OokAVgkCmLFs/YzjXaY1/S+Wc7c6NYnAe1IkUdamN/qFBZiifcVSzergMZgRCaM/IJDIhjVohbZ3Ulo7NKAiTgttNEwxxbXj6HeS4A+tCnvsU+m8D3RtusrECijy0oep4Wbfe3fzkm/VgIWhlY3kH3m794GikaAJc7NpzWfnAM/Pg5vInjgbn/aYHUWtb4pUhdv4D+JECgYEA8itIHSaKFplXvdZ9Zxjby+aSBVkWZ4tw5yQpzbHi/LX5Xlg57Rn/7dwX3ZzDKMnfBmkdtWBpVC0WqUsdr0RWq0TQfQEDGgei5U5qnprIzK+DDiv+wxy+PW45JJ/S8EbjdQBwuDc7b5qJXOQZNvg/lxtBYljJZs9s3q6ih3YH+K8CgYEAwYifw1wkv47HiAAQQO0V+skavQXAWDhshbLM/P7+ZOfsvP4MQalCKcCdMrxOK6qQyntyJLMjwnv0i6WH2/W26g0nDaGRK2JWr/7DOweGpaMLstFNYkim7tmxIl0rMauWIcTXVz0SGbJYlR9pv6F1n5I5QcwwWRfvjMkgPW/6H0MCgYBYQFOgY+oQwEMMaHz8tD9tOBJgpo2WoGc9pJ0jW783ju1YZ1h8wmvU988usLEiTSbchDQSZBceWOwYN8qSk5nuAj7kPxktRDklzYTnVcr1ZMSixn+qBPCDHIbwhNnvPDYp7kYlq+MaEbwbQPi7Yo5x97nFKEwFDnRDVmreaLIQ+wKBgQCCPPX+mxijzGrcS+akOqhK72DOCXvKIpZtVsj6DIOX1jeLa49F9Khwvo1LIkucuRjdGjvTFLag35fPDiMiU/W3Bgjc9Hhj8LaTk2dAZy0mdFWgDZf85xVnsRmyHW6+61CKAxRDQfbXUhyopc4dRoOIUfe0Hrv+iMwNOyA2GBADTwKBgF0T6JV903Kh0UjfKfZVCsL7t6jqreQCiM+F/xYgaIrdxfSLFyD6QGd04gvR3JX3tAdRmS0Z2Sx1tsHGqHNZpACu0KpAGpki8f2Uoy10FT9mkHMp2fvMZlBoaF6kMOl2sP5q8NUx4u1txGab9Ep8E9cjj9CSj5fRtVw3kY11SElD";// 支付宝商户的公钥
	public static final String ALIPAY_APPID = "2017080808089105";

	@Autowired
	private OrderService orderService;

	@Autowired
	private AppUserRechargeService appUserRechargeService;
	
	@Autowired
	private SmsUtil smsUtil;

	@ResponseBody
	@RequestMapping("/wxMPay")
	public Map<String, Object> wxMPay(int orderId) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		AppUserOrder appUserOrder = orderService.getOrderById(orderId);
		// int totalPrice = appUserOrder.getTotalPrice();
		int totalPrice = 1;
		// 获取订单
		// int price = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
		if (totalPrice <= 0) {
			resultMap.put("msg", "付款金额错误");
			resultMap.put("code", "500");
			return resultMap;
		}

		// 设置回调地址-获取当前的地址拼接回调地址
		String url = request.getRequestURL().toString();
		String domain = url.substring(0, url.length() - 13);
		// 回调地址
		String notify_url = "http://45dad2bd.ngrok.io/web-server/a_pay/wxNotify";

		SortedMap<String, String> parameters = new TreeMap<String, String>();
		parameters.put("appid", WX_APPID);
		parameters.put("mch_id", WX_MCH_ID);
		parameters.put("device_info", "WEB");
		parameters.put("nonce_str", PayCommonUtil.CreateNoncestr());
		parameters.put("body", "海上明珠电影票支付");
		parameters.put("out_trade_no", appUserOrder.getOrderId()); // 订单id
		parameters.put("fee_type", "CNY");
		parameters.put("total_fee", totalPrice + "");
		parameters.put("spbill_create_ip", CommonUtil.toIpAddr(request));
		parameters.put("notify_url", notify_url);
		parameters.put("trade_type", "APP");
		String sign = WxPayUtil.generateSignature(parameters, WX_API_KEY, "MD5");
		parameters.put("sign", sign);
		// 设置签名
		// 封装请求参数结束
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		// 调用统一下单接口
		String result = HttpRequestUtil.postData(WX_UNIFIED_ORDER_URL, requestXML);
//		String result = PayCommonUtil.httpsRequest(WX_UNIFIED_ORDER_URL, "POST", requestXML);
		System.out.println("\n" + result);
		try {
			/**
			 * 统一下单接口返回正常的prepay_id，再按签名规范重新生成签名后，将数据传输给APP。参与签名的字段名为appId，partnerId，prepayId，nonceStr，timeStamp，package。注意：package的值格式为Sign=WXPay
			 **/
			Map<String, String> map = XMLUtil.doXMLParse(result);
			SortedMap<Object, Object> parameterMap2 = new TreeMap<Object, Object>();
			parameterMap2.put("appid", WX_APPID);
			parameterMap2.put("partnerid", WX_MCH_ID);
			parameterMap2.put("prepayid", map.get("prepay_id"));
			parameterMap2.put("package", "Sign=WXPay");
			parameterMap2.put("noncestr", PayCommonUtil.CreateNoncestr());
			// 本来生成的时间戳是13位，但是ios必须是10位，所以截取了一下
			parameterMap2.put("timestamp",
					Long.parseLong(String.valueOf(System.currentTimeMillis()).toString().substring(0, 10)));
			String sign2 = PayCommonUtil.createSign("UTF-8", parameterMap2);
			parameterMap2.put("sign", sign2);
			resultMap.put("code", "200");
			resultMap.put("msg", parameterMap2);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 微信异步通知
	 * 
	 */
	@RequestMapping("/wxNotify")
	public void wxNotify() throws IOException, JDOMException {
		// 读取参数
		InputStream inputStream;
		StringBuffer sb = new StringBuffer();
		inputStream = request.getInputStream();
		String s;
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		while ((s = in.readLine()) != null) {
			sb.append(s);
		}
		in.close();
		inputStream.close();
		// 解析xml成map
		Map<String, String> m = new HashMap<String, String>();
		m = XMLUtil.doXMLParse(sb.toString());
		for (Object keyValue : m.keySet()) {
			System.out.println(keyValue + "=" + m.get(keyValue));
		}
		// 过滤空 设置 TreeMap
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String parameter = (String) it.next();
			String parameterValue = m.get(parameter);

			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}

		// 判断签名是否正确
		String resXml = "";
		if (PayCommonUtil.isTenpaySign("UTF-8", packageParams)) {
			if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
				// 这里是支付成功
				////////// 执行自己的业务逻辑////////////////
				String mch_id = (String) packageParams.get("mch_id"); // 商户号
				String openid = (String) packageParams.get("openid"); // 用户标识
				String out_trade_no = (String) packageParams.get("out_trade_no"); // 商户订单号
				String total_fee = (String) packageParams.get("total_fee");
				String transaction_id = (String) packageParams.get("transaction_id"); // 微信支付订单号
				// GoodsTrade gt = new GoodsTrade();
				// gt.setTid(out_trade_no);
				// 查询订单 根据订单号查询订单 GoodsTrade -订单实体类
				// GoodsTrade trade = 订单查询;
				Order order = new Order();

				if (!WX_MCH_ID.equals(mch_id) || order == null || new BigDecimal(total_fee)
						.compareTo(new BigDecimal(1000).multiply(new BigDecimal(100))) != 0) {
					System.out.println("支付失败,错误信息：" + "参数错误");
					resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
							+ "<return_msg><![CDATA[参数错误]]></return_msg>" + "</xml> ";
				} else {
					// if("no_pay".equals(trade.getPayStatus()) &&
					// "wait_buyer_pay".equals(trade.getStatus())){//支付的价格
					// 订单状态的修改。根据实际业务逻辑执行
					if (1 == 1/*
								 * "no_pay".equals(trade.getPayStatus()) &&
								 * "wait_buyer_pay".equals(trade.getStatus())
								 */) {// 支付的价格

						resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";

					} else {
						resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
								+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
						System.out.println("订单已处理");
					}
				}

			} else {
				System.out.println("支付失败,错误信息：" + packageParams.get("err_code"));
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}

		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[通知签名验证失败]]></return_msg>" + "</xml> ";
			System.out.println("通知签名验证失败");
		}

		// ------------------------------
		// 处理业务完毕
		// ------------------------------
		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();

	}

	@RequestMapping("/zfbPay")
	@ResponseBody
	public Object zfbPay(int orderId) throws ServletException, IOException {
		AppUserOrder appUserOrder = orderService.getOrderById(orderId);
		int totalPrice = appUserOrder.getTotalPrice();
		float price = totalPrice / 100;

		AlipayClient alipayClient = new DefaultAlipayClient(ALI_URL, ALIPAY_APPID, ALIPAY_PRIVATE_KEY, "json", "UTF-8",
				ALIPAY_PUBLIC_KEY, "RSA2");

		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("海上明珠电影票支付");
		model.setSubject("海上明珠电影票支付"); // 商品标题
		model.setOutTradeNo(appUserOrder.getOrderId()); // 商家订单编号
		model.setTimeoutExpress("30m"); // 超时关闭该订单时间
		model.setTotalAmount("0.01"); // 订单总金额
		model.setProductCode("QUICK_MSECURITY_PAY"); // 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
		request.setBizModel(model);
//		 request.setNotifyUrl("http://hr.lanhaihui.net/a_pay/verifyPay"); // 回调地址
//		request.setNotifyUrl("http://46868836.ngrok.io/web-server/a_pay/verifyPay");
		request.setNotifyUrl("http://www.vistacinemas.cn:9898/a_pay/verifyPay");
		String orderStr = "";
		Map<String, String> result = new HashMap<>();
		try {
			// 这里和普通的接口调用不同，使用的是sdkExecute
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			orderStr = response.getBody();
			result.put("alipay_sdk", orderStr);
			System.out.println(orderStr);// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/verifyPay")
	@ResponseBody
	public void verifyPay(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		String out_trade_no = new String(httpRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
		String trade_no = new String(httpRequest.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
		String trade_status = new String(httpRequest.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
		String notify_id = httpRequest.getParameter("notify_id");
		String sign = httpRequest.getParameter("sign");
		String total_amount = httpRequest.getParameter("total_amount");
		String seller_id = httpRequest.getParameter("seller_id");
		String app_id = httpRequest.getParameter("app_id");
		System.out.println("app_id:" + app_id);
		System.out.println("seller_id:" + seller_id);
		System.out.println("trade_status:" + trade_status);
		System.out.println("out_trade_no:" + out_trade_no);

		// 切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		boolean flag = true;
		try {
			flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_KEY, "UTF-8", "RSA2");
			if (flag) {
				AppUserOrder appUserOrder = orderService.getOrder(out_trade_no);
				AppUserOrder newOrder= orderService.completeOrder(appUserOrder.getUserId(), appUserOrder.getId(), 3, null);
				smsUtil.sendMms(newOrder.getMphone(), newOrder.getMname() + "@_@" + newOrder.getStime() + "@_@" + newOrder.getCname() + "@_@" + newOrder.getVistaBookingId() + "@_@" + newOrder.getSname() + "@_@" + newOrder.getPlaceNames());
				System.out.println("验签成功");
				httpResponse.getWriter().write("success");
			} else {
				System.out.println("验签失败");
				httpResponse.getWriter().write("failure");
			}
		} catch (AlipayApiException e) {
			System.out.println("验签异常了");
			e.printStackTrace();
		}
	}

	// 余额充值
	@RequestMapping("/zfbYuePay")
	@ResponseBody
	public Object zfbYuePay(String orderId) throws ServletException, IOException {
		AppUserRecharge appUserRecharge = appUserRechargeService.get(orderId);
		int totalPrice = appUserRecharge.getRechargeAmount();
		float price = totalPrice / 100;

		AlipayClient alipayClient = new DefaultAlipayClient(ALI_URL, ALIPAY_APPID, ALIPAY_PRIVATE_KEY, "json", "UTF-8",
				ALIPAY_PUBLIC_KEY, "RSA2");

		AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setBody("海上明珠余额充值");
		model.setSubject("海上明珠余额充值"); // 商品标题
		model.setOutTradeNo(orderId); // 商家订单编号
		model.setTimeoutExpress("30m"); // 超时关闭该订单时间
		model.setTotalAmount("0.01"); // 订单总金额
		model.setProductCode("QUICK_MSECURITY_PAY"); 
		request.setBizModel(model);
//		request.setNotifyUrl("http://hr.lanhaihui.net/a_pay/verifyYuePay"); // 回调地址www.vistacinemas.cn
		request.setNotifyUrl("http://www.vistacinemas.cn:9898/a_pay/verifyYuePay");
//		request.setNotifyUrl("http://hr.lanhaihui.net/a_pay/verifyYuePay");
//		request.setNotifyUrl("http://45dad2bd.ngrok.io/web-server/a_pay/verifyYuePay");
		String orderStr = "";
		Map<String, String> result = new HashMap<>();
		try {
			AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
			orderStr = response.getBody();
			result.put("alipay_sdk", orderStr);
			System.out.println(orderStr);// 就是orderString 可以直接给客户端请求，无需再做处理。
		} catch (AlipayApiException e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping("/verifyYuePay")
	@ResponseBody
	public void verifyYuePay(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {
		// 获取支付宝POST过来反馈信息
		try {
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
				}
			}
			String out_trade_no = new String(httpRequest.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String trade_no = new String(httpRequest.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
			String trade_status = new String(httpRequest.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
			String notify_id = httpRequest.getParameter("notify_id");
			String sign = httpRequest.getParameter("sign");
			String total_amount = httpRequest.getParameter("total_amount");
			String seller_id = httpRequest.getParameter("seller_id");
			String app_id = httpRequest.getParameter("app_id");
			System.out.println("app_id:" + app_id);
			System.out.println("seller_id:" + seller_id);
			System.out.println("trade_status:" + trade_status);
			System.out.println("out_trade_no:" + out_trade_no);

			boolean flag = true;
//			try {
//				flag = AlipaySignature.rsaCheckV1(params, ALIPAY_PUBLIC_SIGN_KEY, "UTF-8");
//			} catch (AlipayApiException e) {
//				System.out.println("验签异常了");
//				e.printStackTrace();
//			}
			if (flag) {
				AppUserRecharge appUserRecharge = appUserRechargeService.get(out_trade_no);
				System.out.println(appUserRecharge.toString());
//				StateTool.checkState(
//						appUserRecharge != null
//								&& Double.parseDouble(total_amount) * 100 == appUserRecharge.getRechargeAmount(),
//						StateTool.State.FAIL);
				appUserRechargeService.finishUserRecharge(appUserRecharge.getUserId(), appUserRecharge.getId(), 3);
				System.out.println("验签成功");
				httpResponse.getWriter().write("success");
			} else {
				System.out.println("验签失败");
				httpResponse.getWriter().write("failure");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			response.getWriter().write("failure");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public static String setXml(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}
}
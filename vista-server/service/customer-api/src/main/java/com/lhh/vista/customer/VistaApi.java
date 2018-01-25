package com.lhh.vista.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.google.common.base.Strings;
import com.lhh.vista.customer.service.OrderApi;
import com.lhh.vista.customer.service.SessionApi;
import com.lhh.vista.customer.service.UserApi;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.customer.v2s.value.CinemasRes;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes;
import com.lhh.vista.customer.v2s.value.MovieRes;
import com.lhh.vista.customer.v2s.value.ShowingMovieRes;
import com.lhh.vista.customer.v2s.value.SoonComeMovieRes;
import com.lhh.vista.customer.v2s.value.VoucherRes;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by liu on 2016/12/1.
 */
public class VistaApi {
    public static final int CACHE_SEAT_FOR_SESSION = 1000;
    public static final int CACHE_MEMBER_ITEM = 1001;

    @Autowired
    @Getter
    private ObjectMapper jsonUtil;
    @Setter
    @Getter
    private String token;
    @Setter
    @Getter
    private String mobileapitoken;
    @Setter
    @Getter
    private String server;//已不用
    @Setter
    @Getter
//    private String serverMovie="http://211.152.60.237";//影院、影片、排期、卖品、订座买票服务器地址  正式库
    private String serverMovie="http://123.207.174.230";//影院、影片、排期、卖品、订座买票服务器地址  正式库

    @Setter
    @Getter
//    private String serverOrder="http://211.152.60.237";//订单server
    private String serverOrder="http://123.207.174.230";//订单server

    @Setter
    @Getter
//    private String serverUser="http://211.152.60.235";//用户server
    private String serverUser="http://123.207.174.230";//用户server
    
    
    @Setter
    @Getter
    private String serverVoucher="https://membershipapi-test01.chinacloudsites.cn";//用户server

    @Setter
    @Getter
    private String clientClass;

    @Setter
    @Getter
    private String clientId;

    @Setter
    @Getter
    private String clientName;

    @Setter
    @Getter
    private Integer clubID;
    
    @Setter
    @Getter
    private Integer clubZXID;

    @Setter
    @Getter
    private int seatForSessionCacheTime = 10000;

    @Getter
    private CacheManage cacheManage;

    @Getter
    private UserApi userApi;

    @Getter
    private OrderApi orderApi;

    @Getter
    private SessionApi sessionApi;

    public VistaApi() {
        jsonUtil = new ObjectMapper();
        cacheManage = new CacheManage();

        userApi = new UserApi(this);
        sessionApi = new SessionApi(this);
        orderApi = new OrderApi(this);
    }

    /**
     * 获取电影院信息
     *
     * @return
     */
    public String post(String url, Object data) {
        String r = null;
        try {
            if (Strings.isNullOrEmpty(token)) {
                return r;
            }
            if (data != null) {
                String json = jsonUtil.writeValueAsString(data);
                System.out.println("发送的json:" + json);
                r = HttpRequest.post( url)
                        .header("connectapitoken", token).contentType("application/json")
                        .send(json).body();
            } else {
                r = HttpRequest.post(url).header("connectapitoken", token).body();
            }
            String xx = r;
//            if (xx.length() > 200) {
//                xx = xx.substring(0, 190);
//            }
            System.out.println("URL:" + url);
            System.out.println("返回值:" + xx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    //httpclient Get请求
    public static String httpGet(String url,Map<String,String> headers,String encode){    
        if(encode == null){    
            encode = "utf-8";    
        }    
        String content = null;    
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();     
        HttpGet httpGet = new HttpGet(url);    
        //设置header  
        if (headers != null && headers.size() > 0) {  
            for (Map.Entry<String, String> entry : headers.entrySet()) {  
                httpGet.setHeader(entry.getKey(),entry.getValue());  
            }  
        }  
        CloseableHttpResponse httpResponse = null;    
        try {    
            httpResponse = closeableHttpClient.execute(httpGet);    
            HttpEntity entity = httpResponse.getEntity();    
            content = EntityUtils.toString(entity, encode);    
        } catch (Exception e) {    
            e.printStackTrace();    
        }finally{    
            try {    
                httpResponse.close();    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }    
        try {  //关闭连接、释放资源    
            closeableHttpClient.close();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }
        return content;
    }

    /**
     * 获取电影院信息
     *
     * @return
     */
    public String delete(String url, Object data) {
        String r = null;
        try {
            if (Strings.isNullOrEmpty(token)) {
                return r;
            }
            System.out.println("URL:" + url);
            if (data != null) {
                String json = jsonUtil.writeValueAsString(data);
                System.out.println("发送的json:" + json);

                HttpRequest hr = HttpRequest.delete(url)
                        .header("connectapitoken", token).contentType("application/json");
                hr.send(json);
                r = hr.body();

            } else {
                r = HttpRequest.delete(url).header("connectapitoken", token).body();
            }
            String xx = r;
            System.out.println("返回值:" + xx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public String get(String url) {
        String r = null;
        try {
            if (Strings.isNullOrEmpty(token)) {
                return r;
            }
            r = HttpRequest.get(url).header("connectapitoken", token).body();
            String xx = r;
            System.out.println("URL:" + url);
            System.out.println("返回值:" + xx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    

    /**
     * 获取电影院信息
     *
     * @return
     */
    public CinemasRes getCinemas() {
        CinemasRes res = null;
        try {
            String r = HttpRequest.get(serverMovie + "/WSVistaWebClient/OData.svc/Cinemas?$format=json").header("connectapitoken", token).body();
            System.out.println("getCinemas:" + r);
            res = jsonUtil.readValue(r, CinemasRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取正在上映
     *
     * @return
     */
    public ShowingMovieRes getShowingMovie() {
        ShowingMovieRes res = null;
        try {
            String r = HttpRequest.get(serverMovie + "/WSVistaWebClient/OData.svc/GetNowShowingScheduledFilms?$format=json" +
                    "&$expand=Sessions/Attributes&$select=ScheduledFilmId,Sessions/SessionId,Sessions/CinemaId,Sessions/Showtime,Sessions/ScreenName,Sessions/Attributes/Description").header("connectapitoken", token).body();
            res = jsonUtil.readValue(r, ShowingMovieRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取即将上映
     *
     * @return
     */
    public SoonComeMovieRes getSoonComeMovie() {
        SoonComeMovieRes res = null;
        try {
            // String r = HttpRequest.get(server + "/WSVistaWebClient/OData.svc/GetComingSoonScheduledFilms?$format=json&$select=ScheduledFilmId").header("connectapitoken", token).body();
            String r = HttpRequest.get(serverMovie + "/WSVistaWebClient/OData.svc/GetComingSoonScheduledFilms?$format=json").header("connectapitoken", token).body();
            System.out.println(r);
            res = jsonUtil.readValue(r, SoonComeMovieRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 获取电影信息
     *
     * @return
     */
    public MovieRes getMovie() {
        MovieRes res = null;
        try {
            //
            String r = HttpRequest.get(serverMovie + "/WSVistaWebClient/OData.svc/Films?$format=json&$select=ID,Title,TitleAlt,RatingDescription,ShortSynopsis,Synopsis,RunTime,OpeningDate,HOFilmCode,GenreId").header("connectapitoken", token).body();
            res = jsonUtil.readValue(r, MovieRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    
    /**
     * 获取电影类型
     *
     */
    public String getMovieType() {
        String content = "";    
        //since 4.3 不再使用 DefaultHttpClient    
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();     
        HttpGet httpGet = new HttpGet(serverMovie + "/WSVistaWebClient/OData.svc/FilmGenres");    
        //设置header  
        httpGet.setHeader("connectapitoken", token);  
        CloseableHttpResponse httpResponse = null;    
        try {    
            httpResponse = closeableHttpClient.execute(httpGet);    
            HttpEntity entity = httpResponse.getEntity();    
            content = EntityUtils.toString(entity, "utf-8");      
        } catch (Exception e) {    
            e.printStackTrace();    
        }finally{    
            try {    
                httpResponse.close();    
            } catch (IOException e) {    
                e.printStackTrace();    
            }    
        }    
        try {  //关闭连接、释放资源    
            closeableHttpClient.close();    
        } catch (IOException e) {    
            e.printStackTrace();    
        }
    	return content;
    }

    /**
     * 获取票类信息
     *
     * @param cid
     * @param sid
     * @return
     */
    public GetTicketsForSessionRes getTicketsForSession(String cid, String sid) {
        GetTicketsForSessionRes res = null;
        try {
            String url = "/WSVistaWebClient/RESTData.svc/cinemas/{cinemaId}/sessions/{sessionId}/tickets?includeLoyaltyTicketsForNonMembers=true&salesChannelFilter=" + clientClass;
            url = url.replace("{cinemaId}", cid);
            url = url.replace("{sessionId}", sid);
            String r = HttpRequest.get(serverMovie + url).header("connectapitoken", token).body();
            res = jsonUtil.readValue(r, GetTicketsForSessionRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    
    /**
     * 获取优惠券
     *
     * @param mphone
     * @return
     */
	public List<VoucherRes> getVoucher(String mphone) {
		List<VoucherRes> res = null;
    	try {
    		JavaType javaType = jsonUtil.getTypeFactory().constructParametricType(ArrayList.class, VoucherRes.class);
    		Map<String, String> map = new HashMap<String, String>();
    		map.put("mobileapitoken", mobileapitoken);
    		String url = "/api/v1/mobile/vouchers/" + mphone;
    		String r = httpGet(serverVoucher + url, map, null);
			res = jsonUtil.readValue(r, javaType);
			System.out.println(res.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return res;
	}

    public static void main(String[] xx) throws Exception {
        VistaApi api = new VistaApi();
        api.setToken("A414C892-FB2B-40BF-97F6-93D6-B10BA30D");
//        api.setServerMovie("http://211.152.60.235");
        api.setClientClass("CELL");
        api.setClientId("211.152.60.237");
        api.setClientName("Mobile");
        api.setClubID(8);
//        api.getUserApi().update("Web7YPTF6TMJ2",null,"1",null);
//        api.getUserApi().g
        MovieRes showingMovieRes=api.getMovie();
        System.out.printf(showingMovieRes.getValue().size()+"") ;
//        GetMemberTransactionRes res = api.getUserApi().memberTransactions("LMK78HMNHRMZD");


        //res.obtainYueList();
        //GetMemberTransactionRes res = api.getUserApi().memberTransactions("LMK04MNV75SZR");
//        for (GetMemberTransactionRes.ShowItem showItem : res.obtainYueList()) {
//            System.out.println(showItem);
//        }
//        for (GetMemberTransactionRes.Item item : res.getMemberTransactionDetails().getTransactions()) {
//            System.out.println(item.getCinemaName() + "--" + item.getTransactionDate());
//            if (item.getTotalPointsEarnedByBalanceType().size() > 0) {
//                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsEarnedByBalanceType()) {
//                    System.out.println("增加:" + c);
//                }
//            }
//
//            if (item.getTotalPointsRedeemedByBalanceType().size() > 0) {
//                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsRedeemedByBalanceType()) {
//                    System.out.println("消耗:" + c);
//                }
//            }
//
//            if (item.getTotalPointsBalanceByBalanceType().size() > 0) {
//                for (GetMemberTransactionRes.TotalPointsChange c : item.getTotalPointsBalanceByBalanceType()) {
//                    System.out.println("总量:" + c);
//                }
//            }
//        }
        //api.getUserApi().memberItem("LMK04MNV75SZR", true);
        //api.getOrderApi().getConcessionList("0200");
        // api.getUserApi().nextCard();
        //String orderId = "test595702861f8eda179bfd34e6f801";
        //api.getOrderApi().checkUser("LMKNJ20495PQT", orderId);
        //.getOrderApi().getConcessionList("0200");

        //MemberValidateRes res=api.getUserApi().validate("13222222222", "123456");
        //System.out.println(res.getMember());
        //System.out.println(res.getMember().getMemberId());

        // api.getTicketsForSession("0200", "37770");
//        ShowingMovieRes showingMovieRes = api.getShowingMovie();
//
//        for (ShowingMovieRes.Value moviceSession : showingMovieRes.getValue()) {
//            System.out.println(moviceSession.getScheduledFilmId());
//            for (ShowingMovieRes.Session msession : moviceSession.getSessions()) {
//                System.out.println("└"+msession.getCinemaId());
//            }
//        }
        //api.getUserApi().validate("dongpan","198111",true);
//        // String orderId = "test595702861f8eda179bfd34e6f801";
//        String orderId = "test595702861f8eda179b7d34e6f811";
//        api.getOrderApi().checkUser("LMKNJ20495PQT", orderId);
//        AddConcessionWarp addwarp = new AddConcessionWarp();
//        addwarp.setCinemaId("0200");
//        addwarp.setSessionId("37314");
//        addwarp.setOrderId(orderId);
//        addwarp.setReturnOrder(true);
//        List<AddOrRemoveConcession> cons = new ArrayList<>();
//
//        AddOrRemoveConcession con1 = new AddOrRemoveConcession();
//        con1.setItemId("2467");
//        con1.setQuantity(1);
//        cons.add(con1);
//        addwarp.setConcessions(cons);
//
//        api.getOrderApi().addConcessionList(addwarp);
//        //OrderRes ress=api.getOrderApi().getOrder(orderId);
//        //System.out.println(ress);
////
//        RemoveConcessionWarp remvoewarp = new RemoveConcessionWarp();
//        remvoewarp.setCinemaId("0200");
//        remvoewarp.setSessionId("37314");
//        remvoewarp.setOrderId(orderId);
//        remvoewarp.setReturnOrder(true);
//        remvoewarp.setConcessionRemoves(cons);
//        api.getOrderApi().removeConcessionList(remvoewarp);
//
//        api.getOrderApi().addConcessionList(warp);
//
//        api.getOrderApi().addConcessionList(warp);
    }




}

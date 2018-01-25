package com.lhh.vista.web.controller.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.service.dto.AppUserLoginRes;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.service.service.SystemValueService;
import com.lhh.vista.temp.dto.BaseSession;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Session;
import com.lhh.vista.temp.model.Ticket;
import com.lhh.vista.temp.model.TicketInfo;
import com.lhh.vista.temp.service.CinemaService;
import com.lhh.vista.temp.service.MovieService;
import com.lhh.vista.temp.service.SessionService;
import com.lhh.vista.temp.service.TicketInfoService;
import com.lhh.vista.temp.service.TicketService;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.GetSeatBySessionRes;

/**
 * Created by soap on 2016/12/11.
 */

@Controller
@RequestMapping("a_session")
public class ASessionController extends BaseController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private SystemValueService systemValueService;
    @Autowired
    private VistaApi vistaApi;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private TicketInfoService ticketInfoService;
    
    /**
     * 根据你电影和日期获取场次列表
     *
     * @return
     */
    @RequestMapping("getSessionByMovieAndDate")
    @ResponseBody
    public Object getSessionByMovieAndDate(String cid, String merge, String token) {
    	ResponseResult<List<Result>> r = new ResponseResult<>(StateTool.State.FAIL);
    	
    	AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
    	if (user == null) {
    		try {
	    		AppUserLoginRes appUserLoginRes = appUserService.loginByToken(token);
	    		user = appUserLoginRes.getAppUser();
    		} catch (Exception e) {

            }
    	}
    	
    	//获取club的值  key=1023海上明珠俱乐部  key=1024尊享卡俱乐部
    	String clubId = systemValueService.getValue(SystemValue.VISTA_SERVER_CLUB_ID);
    	String zxClubId = systemValueService.getValue(SystemValue.VISTA_SERVER_ZXCLUB_ID);
    	String clubTemp = "";
        if (user == null) { 
        	clubTemp = "unlogin";
        } else { 
        	//验证用户身份
        	MemberValidateRes memberValidateRes = vistaApi.getUserApi().checkUser(user.getMemberId(), true);
        	if (clubId.equals(memberValidateRes.showClubID() + "")) { //会员俱乐部
        		clubTemp = clubId;
        	} else if (zxClubId.equals(memberValidateRes.showClubID() + "")){ //尊享卡俱乐部
        		clubTemp = zxClubId;
        	}
        }
    	
        //票类描述
        List<TicketInfo> ticketInfoList = ticketInfoService.getAll();
    	Map<String, String> ticketInfoMap = new HashMap<>();
    	Map<String, Integer> bookingFeeMap = new HashMap<>();
    	for (TicketInfo info : ticketInfoList) {
    		ticketInfoMap.put(info.getKey(), info.getName());
    		bookingFeeMap.put(info.getKey(), (int) (info.getBookingFee() * 100));
    	}
    	
        try {
        	
        	//获取前四天的数据
        	String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        	List<String> showingTimeList = sessionService.get4ShowingDay(cid, merge, today);
        	String lastDay = null;
        	if(showingTimeList.size() != 0) {
        		for(int i = showingTimeList.size() - 1; i >= 0; i--) {
        			lastDay = showingTimeList.get(i);
        			break;
        		}
        	}
        	
        	List<BaseSession> sessionList = sessionService.getSessionByMovieAndDate(cid, today, lastDay, merge);
        	Map<String, List<BaseSession>> sessionMap = new HashMap<>();
        	if(showingTimeList.size() != 0) {
        		for (String tempDay : showingTimeList) {
            		for (BaseSession session : sessionList) {
            			//根据不同身份用户设置一下价格
            			Ticket ticket = ticketService.getTicketByCS(cid, session.getSid());
            			if (clubTemp.equals(clubId)) { //会员俱乐部
            				session.setClubId(1);
            				session.setSprice(ticket.getMemberPrice() + bookingFeeMap.get("1001"));
            				session.setOriginalPrice(ticket.getSalePrice()  + bookingFeeMap.get("1002"));
            				session.setSpriceDescr(ticketInfoMap.get("1001"));
            				session.setOriginalPriceDescr(ticketInfoMap.get("1002"));
            			} else if (clubTemp.equals(zxClubId)) { //尊享卡
            				session.setClubId(2);
            				session.setSprice(ticket.getZxPrice()  + bookingFeeMap.get("1003"));
            				session.setOriginalPrice(ticket.getPrice()  + bookingFeeMap.get("1000"));
            				session.setSpriceDescr(ticketInfoMap.get("1003"));
            				session.setOriginalPriceDescr(ticketInfoMap.get("1000"));
            			} else if (clubTemp.equals("unlogin")) { //未登录
            				session.setClubId(3);
            				session.setSprice(ticket.getMemberPrice() + bookingFeeMap.get("1001"));
            				session.setOriginalPrice(ticket.getPrice() + bookingFeeMap.get("1000"));
            				session.setSpriceDescr(ticketInfoMap.get("1001"));
            				session.setOriginalPriceDescr(ticketInfoMap.get("1000"));
            			}
            			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            			Date date = null;
            	    	try {
            	    		date = format.parse(session.getStime());
            	    	} catch (Exception ex) {   
            	            ex.printStackTrace();   
            	        }    
            	        Calendar cal = Calendar.getInstance();   
            	        cal.setTime(date);   
            	        cal.add(Calendar.MINUTE, session.getRunTime());// 24小时制   
            	        date = cal.getTime(); 
            	        session.setEtime(format.format(date));
            			
            			if(tempDay.equals(session.getStime().substring(0,10)) && sessionMap.containsKey(session.getStime().substring(0,10))) {
            				sessionMap.get(session.getStime().substring(0,10)).add(session);
            			} else if (tempDay.equals(session.getStime().substring(0,10)) && !sessionMap.containsKey(session.getStime().substring(0,10))) {
            				List<BaseSession> list = new ArrayList<BaseSession>();
            				list.add(session);
            				sessionMap.put(session.getStime().substring(0,10), list);
            			}
            		}
            	}
        	}
        	
        	List<Result> endList = new ArrayList<>();
        	for (String tempDay : showingTimeList) {
        		if(sessionMap.get(tempDay) != null) {
        			Result result = new Result();
            		result.setDate(tempDay);
            		result.setSession(sessionMap.get(tempDay));
            		endList.add(result);
        		}
        	}
            r.setResult(endList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getSessionInfo")
    @ResponseBody
    public Object getSessionInfo(String sid) {
        ResponseResult<GetSeatBySessionRes> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            StateTool.checkState(!Strings.isNullOrEmpty(sid), StateTool.State.FAIL);
            Session session = sessionService.find(sid);
            StateTool.checkState(session != null, StateTool.State.FAIL);
            Cinema cinema = cinemaService.find(session.getCid());
            StateTool.checkState(cinema != null, StateTool.State.FAIL);
            Movie movie = movieService.find4Session(session.getMid());
            StateTool.checkState(movie != null, StateTool.State.FAIL);

            GetSeatBySessionRes getSeatBySessionRes = new GetSeatBySessionRes();
            getSeatBySessionRes.setCid(cinema.getCid());
            getSeatBySessionRes.setCname(cinema.getCname());
            getSeatBySessionRes.setCadd(cinema.getCadd());

            getSeatBySessionRes.setSname(session.getScreenName());
            getSeatBySessionRes.setStime(session.getStime());
            getSeatBySessionRes.setMname(movie.getMname());
            getSeatBySessionRes.setMid(movie.getMid());
            getSeatBySessionRes.setRunTime(movie.getRunTime());

            getSeatBySessionRes.setSeatLayoutData(vistaApi.getSessionApi().getSeatForSession(session.getCid(), session.getSid()));
            StateTool.checkState(getSeatBySessionRes.getSeatLayoutData() != null, StateTool.State.FAIL);
            r.setResult(getSeatBySessionRes);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
}



class Result {
	private String date;
	private Object session;
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Object getSession() {
		return session;
	}
	public void setSession(Object session) {
		this.session = session;
	}
	
	
}

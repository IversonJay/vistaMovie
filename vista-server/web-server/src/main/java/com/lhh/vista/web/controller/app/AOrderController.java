
package com.lhh.vista.web.controller.app;

import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.MemberValidateRes;
import com.lhh.vista.service.dto.*;
import com.lhh.vista.service.model.AppUser;
import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.service.service.AppUserService;
import com.lhh.vista.service.service.OrderService;
import com.lhh.vista.service.service.SystemValueService;
import com.lhh.vista.temp.service.ConcessionService;
import com.lhh.vista.util.Const;
import com.lhh.vista.web.common.CommonData;
import com.lhh.vista.web.dto.CheckTicketRes;
import com.lhh.vista.web.dto.CreateOrderRes;
import com.lhh.vista.web.sms.SmsUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soap on 2016/12/18.
 */
@Controller
@RequestMapping("a_order")
public class AOrderController extends BaseController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private VistaApi vistaApi;
    
    @Autowired
    private AppUserService appUserService;
    
    @Autowired
    private SystemValueService systemValueService;
    
    @Autowired
    private ConcessionService concessionService;
    
    @Autowired
	private SmsUtil smsUtil;
    
    @RequestMapping("getConcessionXS")
    @ResponseBody
    public Object getConcessionXS(Integer orderId) {
        ResponseResult<List<com.lhh.vista.temp.model.Concession>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(concessionService.getConcession(user.getMemberId(), orderId));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getConcession")
    @ResponseBody
    public Object getConcession(Integer orderId) {
        ResponseResult<List<Concession>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(orderService.getConcession(user.getMemberId(), orderId));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("setCons")
    @ResponseBody
    public Object setCons(@RequestBody SetConcessionWarp warp) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            orderService.setConcession(warp.getCons(), user.getMemberId(), warp.getOid(), warp.getMphone());
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("checkTicket")
    @ResponseBody
    public CheckTicketRes checkTicket(@RequestBody CheckTicketWarp checkTicketWarp) {
        CheckTicketRes baseResult = new CheckTicketRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            Map<String, List<UserTicket>> tickets = orderService.checkTicket(checkTicketWarp.getAreas(), checkTicketWarp.getSid(), user.getMemberId());
            //这里看看要不要选座
            boolean needSelect = false;
            for (String key : tickets.keySet()) {
                if (tickets.get(key).size() > 0) {
                    needSelect = true;
                    break;
                }
            }
            baseResult.setNeedSelect(needSelect);
            if (needSelect) {
                baseResult.setTickets(tickets);
            }
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }


    @RequestMapping("createOrder")
    @ResponseBody
    public CreateOrderRes createOrder(@RequestBody CreateOrderWarp createOrderWarp) {
        System.out.println(createOrderWarp);
        CreateOrderRes baseResult = new CreateOrderRes();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            //获得用户发起支付的方式
            String clubId = systemValueService.getValue(SystemValue.VISTA_SERVER_CLUB_ID);
        	String zxClubId = systemValueService.getValue(SystemValue.VISTA_SERVER_ZXCLUB_ID);
            MemberValidateRes memberValidateRes = vistaApi.getUserApi().checkUser(user.getMemberId(), true);
            if (null == createOrderWarp.getPayment()) {//换成余额支付  会传payment  其余的不会传
            	if (clubId.equals(memberValidateRes.showClubID() + "")) { //会员俱乐部
            		createOrderWarp.setPayment(Const.HYPRICE);
            	} else if (zxClubId.equals(memberValidateRes.showClubID() + "")){ //尊享卡俱乐部
            		createOrderWarp.setPayment(Const.ZXPRICE);
            	}            	
            }
            Integer orderId = orderService.createOrder(createOrderWarp.getSeatInfoList(), createOrderWarp.getSid(), createOrderWarp.getPayment(), user.getMemberId(), user.getMphone());
            baseResult.setOrderId(orderId);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof StateTool.StateException) {
                baseResult.setState(((StateTool.StateException) e).getState());
            }
        }
        return baseResult;
    }

    @RequestMapping("completeOrder")
    @ResponseBody
    public Object completeOrder(CompleteOrderWarp warp) {
        BaseResult baseResult = new BaseResult();
        baseResult.setState(StateTool.State.FAIL);
        try {
            Integer payType = warp.getPayType();
            //只有余额支付走的这个接口
            StateTool.checkState(payType == 1, StateTool.State.FAIL);

            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            if (user == null) {
            	user = appUserService.loginValidateToken(warp.getToken());
            }
            StateTool.checkState(user != null, StateTool.State.FAIL);

            AppUserOrder appUserOrder = orderService.completeOrder(user.getMemberId(), warp.getOrderId(), payType, warp.getPin());
            smsUtil.sendMms(appUserOrder.getMphone(), appUserOrder.getMname() + "@_@" + appUserOrder.getStime() + "@_@" + appUserOrder.getCname() + "@_@" + appUserOrder.getVistaBookingId() + "@_@" + appUserOrder.getSname() + "@_@" + appUserOrder.getPlaceNames());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof StateTool.StateException) {
                baseResult.setState(((StateTool.StateException) e).getState());
            }
        }
        return baseResult;
    }
    
    /**
     * @param orderId
     * @return
     */
    @RequestMapping("exchangeWithXS") 
    @ResponseBody
    public Object exchangeWithXS(@RequestBody SetConcessionWarp warp) {
    	List<String> voucherInfo = warp.getCodes();
    	ResponseResult<HashMap> r = new ResponseResult<>(StateTool.State.FAIL);
        AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
        StateTool.checkState(user != null, StateTool.State.FAIL);
        
        String clubId = systemValueService.getValue(SystemValue.VISTA_SERVER_CLUB_ID);
    	String zxClubId = systemValueService.getValue(SystemValue.VISTA_SERVER_ZXCLUB_ID);
        MemberValidateRes memberValidateRes = vistaApi.getUserApi().checkUser(user.getMemberId(), true);
        String payment = "";
    	if (clubId.equals(memberValidateRes.showClubID() + "")) { //会员俱乐部
    		payment = Const.HYPRICE;
    	} else if (zxClubId.equals(memberValidateRes.showClubID() + "")){ //尊享卡俱乐部
    		payment = Const.ZXPRICE;
    	}            	
    	
    	//更换票类  更换为兑换券
    	AppUserOrder appUserOrder = orderService.completeExchangeOrder(user.getMemberId(), warp.getOid(), warp.getCodes(), payment);
    	
    	HashMap<String, Object> map = new HashMap<>();
    	map.put("orderInfo", appUserOrder);
    	map.put("voucherInfo", voucherInfo);
    	r.setResult(map);
    	r.setState(StateTool.State.SUCCESS);
    	
		return r;
    }
    
    
    /**
     * 余额支付（含有优惠券的余额支付）接口
     * @param orderId
     * @return
     */
    @RequestMapping("completeVoucherOreder") 
    @ResponseBody
    public Object completeVoucherOreder(@RequestBody SetConcessionWarp warp) {
        BaseResult baseResult = new BaseResult();
        baseResult.setState(StateTool.State.FAIL);
        try {
//            Integer payType = warp.getPayType();
//            StateTool.checkState(payType == 1, StateTool.State.FAIL);

            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            if (user == null) {
            	user = appUserService.loginValidateToken(warp.getToken());
            }
            StateTool.checkState(user != null, StateTool.State.FAIL);

            AppUserOrder appUserOrder = orderService.completeVoucherOrder(user.getMemberId(), warp.getOid(), warp.getCodes());
            smsUtil.sendMms(appUserOrder.getMphone(), appUserOrder.getMname() + "@_@" + appUserOrder.getStime() + "@_@" + appUserOrder.getCname() + "@_@" + appUserOrder.getVistaBookingId() + "@_@" + appUserOrder.getSname() + "@_@" + appUserOrder.getPlaceNames());
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof StateTool.StateException) {
                baseResult.setState(((StateTool.StateException) e).getState());
            }
        }
        return baseResult;
    }

    @RequestMapping("cancelOrder")
    @ResponseBody
    public Object cancelOrder(Integer orderId) {
        BaseResult baseResult = new BaseResult();
        baseResult.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            orderService.cancelOrder(user.getMemberId(), orderId);
            baseResult.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResult;
    }


    @RequestMapping("getOrderPager")
    @ResponseBody
    public Object getOrderPager(PagerRequest pagerRequest, Integer otype) {
        ResponseResult<PagerResponse<BaseUserOrder>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null, StateTool.State.FAIL);
            r.setResult(orderService.getUserOrder(pagerRequest, user.getMemberId(), otype));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    
    @RequestMapping("getOrder")
    @ResponseBody
    public Object getOrder(Integer orderId) {
        ResponseResult<AppUserOrder> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null && orderId != null && orderId > 0, StateTool.State.FAIL);
            AppUserOrder appUserOrder = orderService.getOrder(user.getMemberId(), orderId);
            appUserOrder.setMphone(user.getMphone()); 
            r.setResult(appUserOrder);
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @RequestMapping("getOrderForPay")
    @ResponseBody
    public Object getOrderForPay(Integer orderId) {
        ResponseResult<BaseOrderForPay> r = new ResponseResult<>(StateTool.State.FAIL);
        r.setState(StateTool.State.FAIL);
        try {
            AppUser user = (AppUser) request.getSession().getAttribute(CommonData.APP_LOGIN_USER);
            StateTool.checkState(user != null && orderId != null && orderId > 0, StateTool.State.FAIL);
            r.setResult(orderService.getOrderForPay(user.getMemberId(), orderId));
            r.setState(StateTool.State.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    @Setter
    @Getter
    @ToString
    public static class CreateOrderWarp {
        private List<SeatInfo> seatInfoList;
        private String sid;
        private String payment;
    }

    @Setter
    @Getter
    @ToString
    public static class SetConcessionWarp {
        private List<Concession> cons;
        private List<String> codes;
        private Integer oid;
        private String mphone;
        private String token;
    }

    @Setter
    @Getter
    public static class CheckTicketWarp {
        private List<String> areas;
        private String sid;
    }

    @Setter
    @Getter
    public static class CompleteOrderWarp {
    	private String token;
        private Integer orderId;
        private Integer payType;
        private String pin;
        private List<String> codes;
    }
    
    @Setter
    @Getter
    public static class CodeWarp {
    	private String code;
    }
}

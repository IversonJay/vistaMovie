package com.lhh.vista.customer.service;

import com.lhh.vista.customer.CacheManage;
import com.lhh.vista.customer.Memeber4Pin;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.s2v.*;
import com.lhh.vista.customer.s2v.MemberCreateWarp.LoyaltyMember;
import com.lhh.vista.customer.v2s.*;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes;

import java.util.UUID;

/**
 * Created by liu on 2016/12/21.
 */
public class UserApi {
    private VistaApi vistaApi;

    public UserApi(VistaApi vistaApi) {
        this.vistaApi = vistaApi;
    }

    public MemberValidateRes checkUser(String memberId, boolean rm) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setMemberId(memberId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId("00000000000000000000");
        memberwarp.setReturnMember(rm);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    
    public MemberValidateRes rewardValidate(String memberId, boolean rm, String sessionId) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setMemberId(memberId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(sessionId);
        memberwarp.setReturnMember(rm);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public MemberValidateRes checkCard(String cardId) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setCardNumber(cardId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId("00000000000000000000");
        memberwarp.setReturnMember(true);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public StateResult create(String mphone, String pass) {
        MemberCreateWarp memberwarp = new MemberCreateWarp();
        memberwarp.setMemberInfo(new MemberCreateWarp.LoyaltyMember());
//        memberwarp.getMemberInfo().setMphone(mphone);
        memberwarp.getMemberInfo().setUserName(mphone);
        memberwarp.getMemberInfo().setPass(pass);
//        memberwarp.getMemberInfo().setFirstName("##");
//        memberwarp.getMemberInfo().setLastName("##");
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId("00000000000000000000");
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/create", memberwarp);
//        System.out.println(res);
//        System.out.print("注册成功！！");
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            System.out.print("注册失败！！"+e.getMessage());
            e.printStackTrace();
        }
        return r;
    }

    public MemberValidateRes validate(String mphone, String pass, boolean rm) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setUserName(mphone);
        memberwarp.setPass(pass);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId("00000000000000000000");
        memberwarp.setReturnMember(rm);
        long a=System.currentTimeMillis();
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
//        System.out.println("\r调用 " + vistaApi.getServerUser() + "/WSVistaWebClient/RESTLoyalty.svc/member/validate  接口执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
//        System.out.println(res);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    public MemberValidateRes validateByClubID(String mphone, String pass, String clubId, boolean rm) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setUserName(mphone);
        memberwarp.setPass(pass);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId("00000000000000000000");
        memberwarp.setReturnMember(rm);
        memberwarp.setClubId(clubId);
        long a=System.currentTimeMillis();
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
//        System.out.println("\r调用 " + vistaApi.getServerUser() + "/WSVistaWebClient/RESTLoyalty.svc/member/validate  接口执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
//        System.out.println(res);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    public RepeatCheckRes repeatCheck(String mphone) {
        MemberRepeatCheckWarp memberwarp = new MemberRepeatCheckWarp();
        memberwarp.setUserName(mphone);
        memberwarp.setClubID(vistaApi.getClubID());
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/VerifyNewMembershipDetails", memberwarp);
        RepeatCheckRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, RepeatCheckRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    public RepeatCheckRes repeatCheckZX(String mphone) {
        MemberRepeatCheckWarp memberwarp = new MemberRepeatCheckWarp();
        memberwarp.setUserName(mphone);
        memberwarp.setClubID(vistaApi.getClubZXID());
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/VerifyNewMembershipDetails", memberwarp);
        RepeatCheckRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, RepeatCheckRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 获取票类信息
     *
     * @param cid
     * @param sid
     * @return
     */
    public GetTicketsForSessionRes getTicketsForSessionWithUser(String cid, String sid, String userSessionId) {
        GetTicketsForSessionRes res = null;
        try {
            String url = vistaApi.getServerMovie()+"/WSVistaWebClient/RESTData.svc/cinemas/{cinemaId}/sessions/{sessionId}/tickets?userSessionId=" + userSessionId + "&salesChannelFilter=" + vistaApi.getClientClass();
            url = url.replace("{cinemaId}", cid);
            url = url.replace("{sessionId}", sid);
            String r = vistaApi.get(url);
            res = vistaApi.getJsonUtil().readValue(r, GetTicketsForSessionRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public GetMemberItemRes memberItem(String memberId, boolean refresh) {
        String userSessionId = ("get_member_item_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        checkUser(memberId, userSessionId);

        if (!refresh) {
            CacheManage.CacheItem item = vistaApi.getCacheManage().get(VistaApi.CACHE_MEMBER_ITEM, memberId);
            if (item != null && item.obj != null) {
                return (GetMemberItemRes) item.obj;
            }
        }

        GetMemberItemWarp warp = new GetMemberItemWarp();
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());
        warp.setSessionId(userSessionId);
        GetMemberItemRes res = null;
        try {
            String url =vistaApi.getServerUser()+ "/WSVistaWebClient/RESTLoyalty.svc/member/items";
            String r = vistaApi.post(url, warp);
            res = vistaApi.getJsonUtil().readValue(r, GetMemberItemRes.class);
            vistaApi.getCacheManage().put(VistaApi.CACHE_MEMBER_ITEM, memberId, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public StateResult update(String userId, String mphone, String pass, String cardId) {
        String userSessionId = ("member_update_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        checkUser(userId, userSessionId);

        MemberCreateWarp memberwarp = new MemberCreateWarp();
        memberwarp.setMemberInfo(new MemberCreateWarp.LoyaltyMember());
        memberwarp.getMemberInfo().setMemberId(userId);
        memberwarp.getMemberInfo().setUserName(mphone);
        memberwarp.getMemberInfo().setMphone(mphone);
        memberwarp.getMemberInfo().setCardNumber(cardId);
        memberwarp.getMemberInfo().setPass(pass);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(userSessionId);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/update", memberwarp);
        System.out.println(res);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    public StateResult update4Pass(String userId, String mphone, String pass, String cardId, Integer loyaltyCode, String clubId) {
        String userSessionId = ("member_update_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        checkUser(userId, userSessionId);

        MemberCreateWarp memberwarp = new MemberCreateWarp();
        memberwarp.setMemberInfo(new MemberCreateWarp.LoyaltyMember());
        memberwarp.getMemberInfo().setMemberId(userId);
        memberwarp.getMemberInfo().setUserName(mphone);
        memberwarp.getMemberInfo().setMphone(mphone);
        memberwarp.getMemberInfo().setCardNumber(cardId);
        memberwarp.getMemberInfo().setPass(pass);
        memberwarp.getMemberInfo().setPreferredComplex(loyaltyCode);
        memberwarp.getMemberInfo().setClubID(Integer.parseInt(clubId));
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(userSessionId);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/update", memberwarp);
        System.out.println(res);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    
    public StateResult update4Cinema(String userId, Integer clubid, Integer preferredComplex) {
        String userSessionId = ("member_update_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        checkUser(userId, userSessionId);

        MemberCreateWarp memberwarp = new MemberCreateWarp();
        memberwarp.setMemberInfo(new MemberCreateWarp.LoyaltyMember());
        memberwarp.getMemberInfo().setMemberId(userId);
        memberwarp.getMemberInfo().setPreferredComplex(preferredComplex);
        memberwarp.getMemberInfo().setClubID(clubid);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(userSessionId);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/update", memberwarp);
        System.out.println(res);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    

    public GetMemberTransactionRes memberTransactions(String userId) {
        String userSessionId = ("get_member_tran_" + System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
        checkUser(userId, userSessionId);


        GetMemberTransactionWarp warp = new GetMemberTransactionWarp();
        warp.setClientName(vistaApi.getClientName());
        warp.setClientId(vistaApi.getClientId());
        warp.setClientClass(vistaApi.getClientClass());
        warp.setSessionId(userSessionId);
        GetMemberTransactionRes res = null;
        try {
            String url = vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/transactions";
            long a=System.currentTimeMillis();
            String r = vistaApi.post(url, warp);
//            System.out.println("\r调用 " + url + " 接口执行耗时 : "+(System.currentTimeMillis()-a)/1000f+" 秒 ");
            res = vistaApi.getJsonUtil().readValue(r, GetMemberTransactionRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
    /**
     * 生成订单接口,传当前登录用户的ID和生成的订单号
     *
     * @param memberId
     * @param orderId
     * @return
     */
    public StateResult checkUser(String memberId, String orderId) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setMemberId(memberId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(orderId);
        memberwarp.setReturnMember(false);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
//        System.out.println(res);
        StateResult r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, StateResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
    
    /**
     * 生成订单接口,传当前登录用户的ID和生成的订单号
     *
     * @param memberId
     * @param orderId
     * @return
     */
    public MemberValidateRes checkUser4PIN(String memberId, String orderId) {
        MemberValidateWarp memberwarp = new MemberValidateWarp();
        memberwarp.setMemberId(memberId);
        memberwarp.setClientName(vistaApi.getClientName());
        memberwarp.setClientId(vistaApi.getClientId());
        memberwarp.setClientClass(vistaApi.getClientClass());
        memberwarp.setSessionId(orderId);
        memberwarp.setReturnMember(true);
        String res = vistaApi.post(vistaApi.getServerUser()+"/WSVistaWebClient/RESTLoyalty.svc/member/validate", memberwarp);
//        System.out.println(res);
        MemberValidateRes r = null;
        try {
            r = vistaApi.getJsonUtil().readValue(res, MemberValidateRes.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}

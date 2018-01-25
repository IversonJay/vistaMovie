package com.lhh.vista.customer.service;

import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.CacheManage;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.value.GetSeatForSessionRes;
import com.lhh.vista.customer.v2s.value.SeatLayoutData;

/**
 * Created by liu on 2016/12/23.
 */
public class SessionApi {
    private VistaApi vistaApi;

    public SessionApi(VistaApi vistaApi) {
        this.vistaApi = vistaApi;
    }

    /**
     * 获取座位信息
     * 这里要加缓存
     *
     * @param cid
     * @param sid
     * @return
     */
    public SeatLayoutData getSeatForSession(String cid, String sid) {
        SeatLayoutData res = null;
        try {
            String key = "getSeatForSession_" + cid + "_" + sid;
            System.out.println("key:" + key);
            CacheManage.CacheItem item = vistaApi.getCacheManage().get(VistaApi.CACHE_SEAT_FOR_SESSION, key);
            if (item != null && item.obj != null && System.currentTimeMillis() - item.cacheTime < vistaApi.getSeatForSessionCacheTime()) {
                return (SeatLayoutData) item.obj;
            }
            String url =vistaApi.getServerMovie()+ "/WSVistaWebClient/RESTData.svc/cinemas/{cinemaId}/sessions/{sessionId}/seat-plan";
            url = url.replace("{cinemaId}", cid);
            url = url.replace("{sessionId}", sid);
            String r = vistaApi.get(url);
            GetSeatForSessionRes rr = vistaApi.getJsonUtil().readValue(r, GetSeatForSessionRes.class);
            
            StateTool.checkState(rr.getResponseCode() == 0, StateTool.State.FAIL);
            res = rr.getSeatLayoutData();
            vistaApi.getCacheManage().put(VistaApi.CACHE_SEAT_FOR_SESSION, key, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}

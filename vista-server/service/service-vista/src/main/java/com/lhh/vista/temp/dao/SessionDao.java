package com.lhh.vista.temp.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.dto.BaseSession;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Session;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SessionDao extends BaseTempDao {

    public PagerResponse<Session> getPager(PagerRequest pager) {
        return getPagerByCmd("Session.getAll", pager, null);
    }

    public Session find(String id) {
        return tempSqlSession.selectOne("Session.getById", id);
    }

    public void create(Session session) {
        tempSqlSession.insert("Session.create", session);
    }

    public void createByList(List<Session> movies) {
        tempSqlSession.insert("Session.createByList", movies);
    }

    public void update(Session session) {
        tempSqlSession.update("Session.update", session);
    }

    public void updateByList(List<Session> movies) {
        tempSqlSession.update("Session.updateByList", movies);
    }

    public void del(String id) {
        tempSqlSession.delete("Session.del", id);
    }

    public void delAll() {
        tempSqlSession.delete("Session.delAll");
    }


    public int getCinemaCountByMovie(String mid) {
        return tempSqlSession.selectOne("Session.getCinemaCountByMovie", mid);
    }

    public Integer getMinPriceByCinema(String cid) {
        System.out.println("getMinPriceByCinema:" + cid);
        return tempSqlSession.selectOne("Session.getMinPriceByCinema", cid);
    }


    public List<BaseSession> getSessionByMovieAndDate(String cid/*, String mid*/, String sdate, String edate, String merge) {
        Map<String, Object> param = new HashMap<>();
        
        param.put("cid", cid);
//        param.put("mid", mid);
        param.put("stime", sdate);
        param.put("etime", edate);
        param.put("merge", merge);
        return tempSqlSession.selectList("Session.getSessionByMovieAndDate", param);
    }

    public List<Session> getAll() {
        return tempSqlSession.selectList("Session.getAll");
    }


    public Integer getSessionByMid(String mid) {
        return tempSqlSession.selectOne("Session.getSessionByMid", mid);
    }
    //getSessionWithCinemaByMovieAndDate
    //select * from session where mid='HO00010392' and  stime>"2016-12-12 00:00:00" and stime<"2016-12-19 23:59:59";

	public List<String> get4ShowingDay(String cid, String merge, String today) {
		Map<String, Object> param = new HashMap<>();
		 
		param.put("cid", cid);
        param.put("merge", merge);
        param.put("today", today);
		 
		return tempSqlSession.selectList("Session.get4ShowingDay", param);
	}

	public PagerResponse<Movie> getMergePager(PagerRequest pager, String type) {
		 return getPagerByCmd("Session.getMergePager", pager, type);
	}

	public Session getLeastSession() {
		return tempSqlSession.selectOne("Session.getLeastSession", null);
	}
}

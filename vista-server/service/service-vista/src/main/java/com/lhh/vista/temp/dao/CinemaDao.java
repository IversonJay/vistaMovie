package com.lhh.vista.temp.dao;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.dto.BaseCinema;
import com.lhh.vista.temp.dto.CinemaWithSession;
import com.lhh.vista.temp.model.Cinema;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CinemaDao extends BaseTempDao {

    public PagerResponse<Cinema> getPager(PagerRequest pager, String cityEname) {
        Map<String, Object> param = new HashMap<>();
        if (!Strings.isNullOrEmpty(cityEname)) {
            param.put("cityName", cityEname);
        }
        return getPagerByCmd("Cinema.getAll", pager, param);
    }

    public Cinema find(String id) {
        return tempSqlSession.selectOne("Cinema.getById", id);
    }

    public void create(Cinema Cinema) {
        tempSqlSession.insert("Cinema.create", Cinema);
    }

    public void createByList(List<Cinema> movies) {
        tempSqlSession.insert("Cinema.createByList", movies);
    }

    public void update(Cinema cinema) {
        tempSqlSession.update("Cinema.update", cinema);
    }


    public void updateByList(List<Cinema> movies) {
        tempSqlSession.update("Cinema.updateByList", movies);
    }

    public void del(String id) {
        tempSqlSession.delete("Cinema.del", id);
    }

    public List<CinemaWithSession> getCinemaWithSession(String mid, String date, String cityName) {
        Map<String, Object> param = new HashMap<>();
        param.put("date", date);
        param.put("mid", mid);
        param.put("cityName", cityName);
        System.out.println("p:" + param);
        return tempSqlSession.selectList("Cinema.getCinemaWithSession", param);
    }

    public List<Cinema> getAll() {
        return tempSqlSession.selectList("Cinema.getAll");
    }

    public List<BaseCinema> getBaseAll(String cityEname, String cname) {
        Map<String, Object> param = new HashMap<>();
        if (!Strings.isNullOrEmpty(cityEname)) {
            param.put("cityName", cityEname);
            param.put("cname", cname);
        }
        return tempSqlSession.selectList("Cinema.getBaseAll", param);
    }

	public String selectOne() {
		return tempSqlSession.selectOne("Cinema.selectOne");
	}
}

package com.lhh.vista.temp.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.dto.CinemaMovie;
import com.lhh.vista.temp.model.Movie;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MovieDao extends BaseTempDao {

    public PagerResponse<Movie> getPager(PagerRequest pager) {
        return getPagerByCmd("Movie.getAll", pager, null);
    }

    public Movie find(String merge) {
        return tempSqlSession.selectOne("Movie.getById", merge);
    }

    public void create(Movie movie) {
        tempSqlSession.insert("Movie.create", movie);
    }

    public void createByList(List<Movie> movies) {
        tempSqlSession.insert("Movie.createByList", movies);
    }

    public void update(Movie movie) {
        tempSqlSession.update("Movie.update", movie);
    }

    public void updateByList(List<Movie> movies) {
        tempSqlSession.update("Movie.updateByList", movies);
    }

    public void chongzhiState() {
        tempSqlSession.update("Movie.chongzhiState");
    }


    public void del(String id) {
        tempSqlSession.delete("Movie.del", id);
    }

    public PagerResponse<BaseMovie> getRecentBest(PagerRequest pager, String cid) {
        return getPagerByCmd("Movie.getRecentBest", pager, cid);
    }

    public PagerResponse<BaseMovie> getSoonCome(PagerRequest pager) {
        return getPagerByCmd("Movie.getSoonCome", pager, null);
    }

    public List<CinemaMovie> getMovieByCinemaId(String cid) {
        return tempSqlSession.selectList("Movie.getMovieByCinemaId", cid);
    }

    public List<BaseMovie> search(Map<String, Object> p) {
        return tempSqlSession.selectList("Movie.search", p);
    }

	public PagerResponse<BaseMovie> getMovieAll(PagerRequest pager, Map map) {
		return getPagerByCmd("Movie.getMovieAll", pager, map);
	}

	public Movie find4Session(String mid) {
		return tempSqlSession.selectOne("Movie.get4SessionById", mid);
	}

	
}

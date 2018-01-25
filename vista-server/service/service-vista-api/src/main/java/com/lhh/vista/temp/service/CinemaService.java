package com.lhh.vista.temp.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.dto.BaseCinema;
import com.lhh.vista.temp.dto.CinemaAndMovie;
import com.lhh.vista.temp.dto.CinemaWithSession;
import com.lhh.vista.temp.model.Cinema;

import java.util.List;

/**
 * Created by soap on 2016/12/10.
 */
public interface CinemaService {
    void insert(Cinema cinema);

    Cinema find(String id);

    PagerResponse<Cinema> getPager(PagerRequest pager, String cityEname, Integer orderBy);

    CinemaAndMovie findCinemaWithMovie(String id);

    List<CinemaWithSession> getCinemaWithSession(String id, String date, String cityEname);

    List<BaseCinema> getBaseCinema(String cityEname, String cname);

	String selectOne();

	List<Cinema> getAll();
}

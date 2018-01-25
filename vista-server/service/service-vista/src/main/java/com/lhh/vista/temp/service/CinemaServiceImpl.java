package com.lhh.vista.temp.service;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.temp.dao.CinemaDao;
import com.lhh.vista.temp.dao.MovieDao;
import com.lhh.vista.temp.dto.BaseCinema;
import com.lhh.vista.temp.dto.CinemaAndMovie;
import com.lhh.vista.temp.dto.CinemaWithSession;
import com.lhh.vista.temp.model.Cinema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soap on 2016/12/10.
 */
@Service
public class CinemaServiceImpl implements CinemaService {
    @Autowired
    private CinemaDao cinemaDao;

    @Autowired
    private MovieDao movieDao;

    @Override
    public void insert(Cinema cinema) {
        cinemaDao.create(cinema);
    }

    @Override
    public Cinema find(String id) {
        return cinemaDao.find(id);
    }

    @Override
    public PagerResponse<Cinema> getPager(PagerRequest pager, String cityEname, Integer orderBy) {
        return cinemaDao.getPager(pager, cityEname);
    }

    @Override
    public CinemaAndMovie findCinemaWithMovie(String id) {
        CinemaAndMovie cinemaAndMovie = new CinemaAndMovie();
        Cinema cinema = cinemaDao.find(id);
        StateTool.checkState(cinema != null, StateTool.State.FAIL);
        cinemaAndMovie.setCid(id);
        cinemaAndMovie.setCname(cinema.getCname());
        cinemaAndMovie.setLat(cinema.getLat());
        cinemaAndMovie.setLon(cinema.getLon());
        cinemaAndMovie.setCadd(cinema.getCadd());
        cinemaAndMovie.setMovies(movieDao.getMovieByCinemaId(id));
        return cinemaAndMovie;
    }

    @Override
    public List<CinemaWithSession> getCinemaWithSession(String id, String date, String cityEname) {
        return cinemaDao.getCinemaWithSession(id, date, cityEname);
    }

    @Override
    public List<BaseCinema> getBaseCinema(String cityEname, String cname) {
        return cinemaDao.getBaseAll(cityEname, cname);
    }

	@Override
	public String selectOne() {
		return cinemaDao.selectOne();
	}

	@Override
	public List<Cinema> getAll() {
		return cinemaDao.getAll();
	}
}

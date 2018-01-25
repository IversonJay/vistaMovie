package com.lhh.vista.temp.service;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.dao.MovieDao;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.dto.CinemaMovie;
import com.lhh.vista.temp.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soap on 2016/12/10.
 */
@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    private MovieDao movieDao;

    @Override
    public void insert(Movie movie) {
        movieDao.create(movie);
    }

    @Override
    public Movie find(String merge) {
        return movieDao.find(merge);
    }

    @Override
    public PagerResponse<Movie> getPager(PagerRequest pager) {
        return movieDao.getPager(pager);
    }

    @Override
    public PagerResponse<BaseMovie> getRecentBest(PagerRequest pager, String cid) {
        return movieDao.getRecentBest(pager, cid);
    }

    @Override
    public PagerResponse<BaseMovie> getSoonCome(PagerRequest pager) {
        return movieDao.getSoonCome(pager);
    }

    /**
     * 搜索
     *
     * @return
     */
    @Override
    public List<BaseMovie> search(String mark) {
        if (Strings.isNullOrEmpty(mark)) {
            return new ArrayList<>();
        }

        Map<String, BaseMovie> res = new HashMap<>();

        Map<String, Object> p = new HashMap<>();
        p.put("mname", mark);
        List<BaseMovie> r = movieDao.search(p);
        for (BaseMovie bm : r) {
            if (!res.containsKey(bm.getMid())) {
                res.put(bm.getMid(), bm);
            }
        }

//        p.clear();
//        p.put("details", mark);
//        r = movieDao.search(p);
//        for (BaseMovie bm : r) {
//            if (!res.containsKey(bm.getMid())) {
//                res.put(bm.getMid(), bm);
//            }
//        }
//
//        p.clear();
//        p.put("mtype", mark);
//        r = movieDao.search(p);
//        for (BaseMovie bm : r) {
//            if (!res.containsKey(bm.getMid())) {
//                res.put(bm.getMid(), bm);
//            }
//        }
//
//        p.clear();
//        p.put("performer", mark);
//        r = movieDao.search(p);
//        for (BaseMovie bm : r) {
//            if (!res.containsKey(bm.getMid())) {
//                res.put(bm.getMid(), bm);
//            }
//        }
//
//        p.clear();
//        p.put("mark", mark);
//        r = movieDao.search(p);
//        for (BaseMovie bm : r) {
//            if (!res.containsKey(bm.getMid())) {
//                res.put(bm.getMid(), bm);
//            }
//        }

        return new ArrayList<>(res.values());
    }

	@Override
	public PagerResponse<BaseMovie> getMovieAll(PagerRequest pager, String cid, String type) {
		Map<String, String> map = new HashMap<>();
		map.put("cid", cid);
		map.put("type", type);
		return movieDao.getMovieAll(pager, map);
	}

	@Override
	public Movie find4Session(String mid) {
		 return movieDao.find4Session(mid);
	}

	
}

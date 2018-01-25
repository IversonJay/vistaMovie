package com.lhh.vista.temp.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.model.Manager;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.dto.CinemaMovie;
import com.lhh.vista.temp.model.Movie;

import java.util.List;

/**
 * Created by soap on 2016/12/10.
 */
public interface MovieService {
    void insert(Movie movie);

    Movie find(String merge);

    PagerResponse<Movie> getPager(PagerRequest pager);


    /**
     * 获取近期热映
     *
     * @param pager
     * @return
     */
    PagerResponse<BaseMovie> getRecentBest(PagerRequest pager, String cid);

    /**
     * 获取即将上映
     *
     * @param pager
     * @return
     */
    PagerResponse<BaseMovie> getSoonCome(PagerRequest pager);


    /**
     * 搜索
     * @return
     */
    List<BaseMovie> search(String mark);

	PagerResponse<BaseMovie> getMovieAll(PagerRequest request, String cid, String type);

	Movie find4Session(String mid);

}

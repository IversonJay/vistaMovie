package com.lhh.vista.temp.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.dto.BaseSession;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Sequence;
import com.lhh.vista.temp.model.Session;

import java.util.List;

/**
 * Created by soap on 2016/12/10.
 */
public interface SessionService {
    void insert(Session session);

    Session find(String id);

    PagerResponse<Session> getPager(PagerRequest pager);

    List<BaseSession> getSessionByMovieAndDate(String cid/*,String mid*/,String date, String lastDay, String merge);

    Integer getSessionByMid(String mid);

	List<String> get4ShowingDay(String cid, String merge, String today);

	PagerResponse<Movie> getMergePager(PagerRequest pager, String type);

	void editSequence(String mid, String idx);

	void mergeMovie(String[] midArr);

	void recoverMovie(String mid);

	List<Sequence> getAll();
}

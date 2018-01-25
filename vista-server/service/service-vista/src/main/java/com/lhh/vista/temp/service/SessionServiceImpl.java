package com.lhh.vista.temp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.DateTool;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.service.dao.SystemValueDao;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.temp.dao.SequenceDao;
import com.lhh.vista.temp.dao.SessionDao;
import com.lhh.vista.temp.dto.BaseSession;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.model.Sequence;
import com.lhh.vista.temp.model.Session;

/**
 * Created by soap on 2016/12/10.
 */
@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private SessionDao sessionDao;

    @Autowired
    private SystemValueDao systemValueDao;
    
    @Autowired
    private SequenceDao sequenceDao;

    @Override
    public void insert(Session session) {
        sessionDao.create(session);
    }

    @Override
    public Session find(String id) {
        return sessionDao.find(id);
    }

    @Override
    public PagerResponse<Session> getPager(PagerRequest pager) {
        return sessionDao.getPager(pager);
    }

    @Override
    public List<BaseSession> getSessionByMovieAndDate(String cid/*, String mid*/, String date, String lastDay, String merge) {
        StateTool.checkState(!Strings.isNullOrEmpty(cid), StateTool.State.FAIL);
        //StateTool.checkState(!Strings.isNullOrEmpty(mid), StateTool.State.FAIL);
        StateTool.checkState(!Strings.isNullOrEmpty(merge), StateTool.State.FAIL);
        StateTool.checkState(!Strings.isNullOrEmpty(date), StateTool.State.FAIL);
        String sdate = date + " 00:00:00";// HH:mm:ss
        if (date.equals(DateTool.getNowTime("yyyy-MM-dd"))) {
            int p = 0;
            try {
                p = Integer.parseInt(systemValueDao.getValue(SystemValue.VISTA_TINGSHOU_MIN));
            } catch (Exception e) {
            }
            sdate = date + DateTool.dateToString(new Date(System.currentTimeMillis() + p * 60000), " HH:mm:ss");//
        }
        String edate = lastDay + " 23:59:59";
        return sessionDao.getSessionByMovieAndDate(cid,/* mid,*/ sdate, edate, merge);
    }

    public Integer getSessionByMid(String mid){
        return sessionDao.getSessionByMid(mid);
    }

	@Override
	public List<String> get4ShowingDay(String cid, String merge, String today) {
		StateTool.checkState(!Strings.isNullOrEmpty(cid), StateTool.State.FAIL);
        StateTool.checkState(!Strings.isNullOrEmpty(merge), StateTool.State.FAIL);
        StateTool.checkState(!Strings.isNullOrEmpty(today), StateTool.State.FAIL);
		return sessionDao.get4ShowingDay(cid, merge, today);
	}

	@Override
	public PagerResponse<Movie> getMergePager(PagerRequest pager, String type) {
		return sessionDao.getMergePager(pager, type);
	}

	@Override
	public void editSequence(String mid, String idx) { 
		sequenceDao.editSequence(mid, idx);
	}

	@Override
	public void mergeMovie(String[] midArr) {
		sequenceDao.mergeMovie(midArr);
	}

	@Override
	public void recoverMovie(String mid) {
		sequenceDao.recoverMovie(mid);
	}

	@Override
	public List<Sequence> getAll() {
		return sequenceDao.getAllMovie();
	}
}

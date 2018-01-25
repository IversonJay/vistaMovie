package com.lhh.vista.service.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.service.dao.CollectDao;
import com.lhh.vista.service.model.Collect;
import com.lhh.vista.temp.dao.CinemaDao;
import com.lhh.vista.temp.dao.MovieDao;
import com.lhh.vista.temp.dto.BaseMovie;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Movie;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/1/5.
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private CinemaDao cinemaDao;

    @Override
    public Integer add(String collectId, Integer collectType, String userId) {
        if (collectDao.find(collectId, collectType, userId) == null) {
            Collect collect = new Collect();
            collect.setCollectId(collectId);
            collect.setCollectType(collectType);
            collect.setUserId(userId);
            collectDao.create(collect);
        }
        return 1;
    }

    @Override
    public void del(String collectId, Integer collectType, String userId) {
        collectDao.del(collectId, collectType, userId);
    }

    @Override
    public Collect find(String collectId, Integer collectType, String userId) {
        return collectDao.find(collectId, collectType, userId);
    }

    @Override
    public PagerResponse<BaseMovie> getMoviePager(PagerRequest pager, String userId) {
        PagerResponse<Collect> tempRes = collectDao.getPager(pager, 1, userId);
        List<BaseMovie> cms = new ArrayList<>();
        for (Collect c : tempRes.getRows()) {
            Movie m = movieDao.find(c.getCollectId());
            if (m != null) {
                BaseMovie cm = new BaseMovie();
                cm.setCinemaCount(m.getCinemaCount());
                cm.setHOFilmCode(m.getHOFilmCode());
                cm.setMid(m.getMid());
                cm.setMname(m.getMname());
                cm.setOpeningDate(m.getOpeningDate());
                cm.setSynopsis(m.getSynopsis());
                cms.add(cm);
            }
        }
        PagerResponse<BaseMovie> res = new PagerResponse<>();
        res.setTotal(tempRes.getTotal());
        res.setRows(cms);
        return res;
    }

    @Override
    public PagerResponse<Cinema> getCinemaPager(PagerRequest pager, String userId) {
        PagerResponse<Collect> tempRes = collectDao.getPager(pager, 2, userId);
        List<Cinema> cms = new ArrayList<>();
        for (Collect c : tempRes.getRows()) {
            Cinema m = cinemaDao.find(c.getCollectId());
            System.out.println(m);
            if (m != null) {
                cms.add(m);
            }
        }
        PagerResponse<Cinema> res = new PagerResponse<>();
        res.setTotal(tempRes.getTotal());
        res.setRows(cms);
        return res;
    }
}

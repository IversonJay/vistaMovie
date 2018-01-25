package com.lhh.vista.temp.dao;

import org.springframework.stereotype.Repository;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.Statistics;

@Repository
public class StatisticsDao extends BaseTempDao {

	public void insert(Statistics statistics) {
		tempSqlSession.insert("Statistics.insert", statistics);
	}

	public PagerResponse<Statistics> getAllPager(PagerRequest pager) {
		return getPagerByCmd("Statistics.getAllPager", pager, null);
	}

}

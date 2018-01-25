package com.lhh.vista.web.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lhh.vista.temp.dao.StatisticsDao;
import com.lhh.vista.temp.model.Statistics;
import com.lhh.vista.web.interceptors.UserInterceptor;

@Component
public class clearUsersTask {
	
	@Autowired
	private StatisticsDao statisticsDao;
	
	public void doTask() {	
		//数量统计
		Statistics statistics = new Statistics();
		statistics.setCount(UserInterceptor.userSet.size());
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		statistics.setDate(dateStr);
		statisticsDao.insert(statistics);
		UserInterceptor.userSet.clear();	
	}

}

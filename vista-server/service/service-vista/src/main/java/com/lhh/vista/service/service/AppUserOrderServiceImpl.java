package com.lhh.vista.service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.vista.service.dao.AppUserOrderDao;
import com.lhh.vista.service.model.AppUserOrder;

@Service
public class AppUserOrderServiceImpl implements AppUserOrderService {
	
	@Autowired
	private AppUserOrderDao appUserOrderDao;

	@Override
	public void createOrder() {
		
	}

	@Override
	public List<AppUserOrder>  getOrderByStime(String queryTime) {
		return appUserOrderDao.selectOrderByStime(queryTime);
	}

}

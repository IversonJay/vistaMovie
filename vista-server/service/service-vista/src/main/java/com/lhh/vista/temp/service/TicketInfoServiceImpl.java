package com.lhh.vista.temp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.StateResult;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes;
import com.lhh.vista.customer.v2s.value.GetTicketsForSessionRes.Ticket;
import com.lhh.vista.service.dto.UserTicket;
import com.lhh.vista.temp.dao.SessionDao;
import com.lhh.vista.temp.dao.TicketInfoDao;
import com.lhh.vista.temp.model.Session;
import com.lhh.vista.temp.model.TicketInfo;

/**
 * 
 * @author LiuHJ
 *
 */
@Service
public class TicketInfoServiceImpl implements TicketInfoService {
	
	@Autowired
	private VistaApi vistaApi;
    @Autowired
    private TicketInfoDao ticketInfoDao;
    @Autowired
    private SessionDao sessionDao;

	@Override
	public PagerResponse<TicketInfo> ticketZXData(PagerRequest pager, String type) {
		return ticketInfoDao.ticketZXData(pager, type);
	}

	@Override
	public List<TicketInfo> getTicketAll() {
    	String orderId = (System.currentTimeMillis() + UUID.randomUUID().toString().replace("-", "")).substring(0, 32);
    	//获取最近一部场次信息 获取用来获取票类信息
    	Session session = sessionDao.getLeastSession();
    	
        //根据订单号和认证会员,去获取当前用户的票务情况
        GetTicketsForSessionRes ticketsForSession = vistaApi.getUserApi().getTicketsForSessionWithUser(session.getCid(), session.getSid(), orderId);
        List<TicketInfo> ticketList = new ArrayList<>();
        for (Ticket ticket : ticketsForSession.getTickets()) { 
        	TicketInfo ticketInfo = new TicketInfo();
        	ticketInfo.setHopk(ticket.getHopk());
        	ticketInfo.setDescription(ticket.getDescription());
//        	ticketList.add("HOPK:" + ticket.getHopk() + "," + ticket.getDescription());
        	ticketList.add(ticketInfo);
        }
    	
		return ticketList;
	}

	@Override
	public List<TicketInfo> getAll() {
		return ticketInfoDao.selectAll();
	}

	@Override
	public TicketInfo getByKey(String key) {
		return ticketInfoDao.getByKey(key);
	}

	@Override
	public int update(TicketInfo ticketInfo) {
		return ticketInfoDao.update(ticketInfo);
	}


}

package com.lhh.vista.temp.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.dao.TicketDao;
import com.lhh.vista.temp.dao.TicketInfoDao;
import com.lhh.vista.temp.model.Ticket;
import com.lhh.vista.temp.model.TicketInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by soap on 2016/12/13.
 */
@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketDao ticketDao;

    @Override
    public void save(Ticket ticket) {
        if (ticketDao.find(ticket.getCid(), ticket.getSid(), ticket.getArea()) != null) {
            ticketDao.update(ticket);
        } else {
            ticketDao.insert(ticket);
        }
    }

    @Override
    public Ticket find(String cid, String sid, String area) {
        return ticketDao.find(cid, sid, area);
    }

    @Override
    public List<Ticket> getAll(String cid, String sid) {
        return ticketDao.getAll(cid, sid);
    }

	@Override
	public Ticket getTicketByCS(String cid, Object sid) {
		return ticketDao.getTicketByCS(cid, sid);
	}

}

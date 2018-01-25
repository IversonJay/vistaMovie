package com.lhh.vista.temp.service;

import java.util.List;

import com.lhh.vista.temp.model.Ticket;

/**
 * Created by soap on 2016/12/10.
 */
public interface TicketService {
    void save(Ticket ticket);

    Ticket find(String cid,String sid,String area);

    List<Ticket> getAll(String cid,String sid);

	Ticket getTicketByCS(String cid, Object sid);

}

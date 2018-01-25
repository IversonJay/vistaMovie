package com.lhh.vista.temp.service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.model.Ticket;
import com.lhh.vista.temp.model.TicketInfo;

import java.util.List;

/**
 * Created by soap on 2016/12/10.
 */
public interface TicketInfoService {

	PagerResponse<TicketInfo> ticketZXData(PagerRequest pager, String type);

	List<TicketInfo> getTicketAll();

	List<TicketInfo> getAll();

	TicketInfo getByKey(String key);

	int update(TicketInfo ticketInfo);
}

package com.lhh.vista.temp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.TicketInfo;

@Repository
public class TicketInfoDao extends BaseTempDao {

	public List<TicketInfo> selectAll() {
		return tempSqlSession.selectList("TicketInfo.selectAll");
	}

	public PagerResponse<TicketInfo> ticketZXData(PagerRequest pager, String type) {
		return getPagerByCmd("TicketInfo.ticketZXData", pager, type);
	}

	public List<TicketInfo> selectAllTicket() {
		return tempSqlSession.selectList("TicketInfo.selectAllTicket");
	}

	public TicketInfo getByKey(String key) {
		return tempSqlSession.selectOne("TicketInfo.selectByKey", key);
	}

	public int update(TicketInfo ticketInfo) {
		return tempSqlSession.update("TicketInfo.update", ticketInfo);
	}

}

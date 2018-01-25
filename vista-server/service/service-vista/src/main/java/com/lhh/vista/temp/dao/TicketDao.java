package com.lhh.vista.temp.dao;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.Ticket;
import com.lhh.vista.temp.model.TicketInfo;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by soap on 2016/12/13.
 */
@Repository
public class TicketDao extends BaseTempDao {
    public void insert(Ticket ticket) {
        tempSqlSession.insert("Ticket.create", ticket);
    }

    public void update(Ticket ticket) {
        tempSqlSession.update("Ticket.update", ticket);
    }
    
    public int updateTicket(Ticket ticket) {
        return tempSqlSession.update("Ticket.update", ticket);
    }

    public Ticket find(String cid, String sid, String area) {
        Map<String, Object> param = new HashMap<>();
        param.put("cid", cid);
        param.put("sid", sid);
        param.put("area", area);
        return tempSqlSession.selectOne("Ticket.get", param);
    }

    public List<Ticket> getAll(String cid, String sid) {
        Map<String, Object> param = new HashMap<>();
        param.put("cid", cid);
        param.put("sid", sid);
        return tempSqlSession.selectList("Ticket.getAll", param);
    }

    public void delAll(String cid, String sid) {
        Map<String, Object> param = new HashMap<>();
        param.put("cid", cid);
        param.put("sid", sid);
        tempSqlSession.delete("Ticket.delAll", param);
    }

	public PagerResponse<TicketInfo> getTicketPager(PagerRequest pager, String type) {
		return getPagerByCmd("Ticket.getTicketPager", pager, type);
	}

	public Ticket getTicketByCS(Object cid, Object sid) {
		Map<String, Object> param = new HashMap<>();
        param.put("cid", cid);
        param.put("sid", sid);
		return tempSqlSession.selectOne("Ticket.getTicketByCS", param);
	}
}

package com.lhh.vista.temp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.Concession;
import com.lhh.vista.temp.model.TicketInfo;

@Repository
public class ConcessionDao extends BaseTempDao {

	public PagerResponse<Concession> list(PagerRequest pager, String ptype, String cid) {
		Map<String, Object> map = new HashMap();
		map.put("ptype", ptype);
		map.put("cid", cid);
		return getPagerByCmd("Concession.selectAll", pager, map);
	}

	public List<Concession> selectCZList(String dcinema) {
		Map<String, Object> map = new HashMap();
		map.put("ptype", 1);
		map.put("cid", dcinema);
		return tempSqlSession.selectList("Concession.selectAll", map);
	}

	public void insert(Concession concession) {
		tempSqlSession.insert("Concession.insert", concession);
	}

	public void del(Integer id) {
		tempSqlSession.delete("Concession.del", id);
	}

	public Concession getOneById(int id) {
		return tempSqlSession.selectOne("Concession.getOneById", id);
	}

	public void update(Concession concession) {
		tempSqlSession.update("Concession.update", concession);
	}

	public List<Concession> getConcessionList(int ptype, String cid) {
		Map<String, Object> map = new HashMap();
		map.put("ptype", ptype);
		map.put("cid", cid);
		return tempSqlSession.selectList("Concession.getConcessionList", map);
	}
	
}

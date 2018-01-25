package com.lhh.vista.temp.service;

import java.util.List;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.temp.model.Concession;

/**
 * Created by soap on 2016/12/10.
 */
public interface ConcessionService {

	PagerResponse<Concession> list(PagerRequest pager, String ptype, String cid);

	List<Concession> selectCZList(String dcinema);

	List<Concession> getAllByCid(String cid);

	void insert(Concession concession);

	void del(Integer id);

	Concession getOneById(int id);

	void update(Concession concession);

	List<com.lhh.vista.temp.model.Concession> getConcession(String memberId, Integer orderId);

}
	
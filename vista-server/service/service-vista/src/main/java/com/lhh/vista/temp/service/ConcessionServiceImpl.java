package com.lhh.vista.temp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.customer.v2s.GetConcessionListRes;
import com.lhh.vista.service.dao.AppUserOrderDao;
import com.lhh.vista.service.model.AppUserOrder;
import com.lhh.vista.temp.dao.ConcessionDao;
import com.lhh.vista.temp.model.Concession;

/**
 * 
 * @author LiuHJ
 *
 */
@Service
public class ConcessionServiceImpl implements ConcessionService {
	
    @Autowired
    private ConcessionDao concessionDao;
    @Autowired
    private VistaApi vistaApi;
    @Autowired
	private AppUserOrderDao appUserOrderDao;
    
	@Override
	public PagerResponse<Concession> list(PagerRequest pager, String ptype, String cid) {
		return concessionDao.list(pager, ptype, cid);
	}

	@Override
	public List<Concession> selectCZList(String dcinema) {
		return concessionDao.selectCZList(dcinema);
	}

	@Override
	public List<Concession> getAllByCid(String cid) {
		GetConcessionListRes concessionListRes = vistaApi.getOrderApi().getConcessionList(cid);//根据影城id获取卖品的id
		List<Concession> list = new ArrayList<Concession>();
		for(com.lhh.vista.customer.v2s.GetConcessionListRes.Concession concession : concessionListRes.getItems()) {
			Concession c = new Concession();
			c.setPid(concession.getId());
			c.setName(concession.getDescription());
			c.setPrice(concession.getPrice());
			c.setCid(cid);
			list.add(c);
		}
		return list;
	}

	@Override
	public void insert(Concession concession) {
		concessionDao.insert(concession);
	}

	@Override
	public void del(Integer id) {
		concessionDao.del(id);
	}

	@Override
	public Concession getOneById(int id) {
		return concessionDao.getOneById(id);
	}

	@Override
	public void update(Concession concession) {
		concessionDao.update(concession);
	}

	@Override
	public List<Concession> getConcession(String memberId, Integer orderId) {
		AppUserOrder appUserOrder = appUserOrderDao.find(orderId);
		StateTool.checkState(appUserOrder != null && appUserOrder.getUserId().equals(memberId), StateTool.State.FAIL);
		//服务器上的数据
		GetConcessionListRes res = vistaApi.getOrderApi().getConcessionList(appUserOrder.getCid());
		List<Concession> list = new ArrayList<>();
		//app服务器上数据
		List<Concession> clist = concessionDao.getConcessionList(0, appUserOrder.getCid());
		List<String> pidList = new ArrayList<>(); 
		for (GetConcessionListRes.Concession con : res.getItems()) {
			pidList.add(con.getId());
		}
		for (Concession c : clist) {
			if(pidList.contains(c.getPid())) {
				list.add(c);
			}
		}
		return list;
	}

}

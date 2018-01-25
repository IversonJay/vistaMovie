package com.lhh.vista.temp.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.Mtype;

@Repository
public class MtypeDao extends BaseTempDao  {

	public void insert(Mtype mtype) {
		tempSqlSession.insert("Mtype.create", mtype);
	}

	public int update(Mtype mtype) {
		return tempSqlSession.update("Mtype.update", mtype);
	}

	public List<Mtype> getAll() {
		return tempSqlSession.selectList("Mtype.getAll");
	}

}

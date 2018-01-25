package com.lhh.vista.temp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.lhh.vista.temp.BaseTempDao;
import com.lhh.vista.temp.model.Sequence;

@Repository
public class SequenceDao extends BaseTempDao {

	public List<String> getAll(int type) {
		return tempSqlSession.selectList("Sequence.getAll", type);
	}

	public void insert(Sequence sequence) {
		tempSqlSession.insert("Sequence.insert", sequence);
	}

	public void del(Map<String, Object> map) {
		tempSqlSession.delete("Sequence.del", map);	
	}

	public void editSequence(String mid, String idx) {
		Map<String, String> map = new HashMap<>();
		map.put("mid", mid);
		map.put("idx", idx);
		tempSqlSession.update("Sequence.editSequence", map);
	}

	public void mergeMovie(String[] midArr) {
		tempSqlSession.update("Sequence.mergeMovie", midArr);
	}

	public void recoverMovie(String mid) {
		tempSqlSession.update("Sequence.recoverMovie", mid);
	}

	public List<Sequence> getAllMovie() {
		return tempSqlSession.selectList("Sequence.getAllMovie");
	}

}

package com.lhh.vista.service.dao;

import com.lhh.vista.temp.BaseTempDao;
import org.springframework.stereotype.Repository;

/**
 * Created by soap on 2016/12/10.
 */
@Repository
public class SystemDao extends BaseTempDao{
    public void createTable(){
        tempSqlSession.update("System.initTempDb");
    }
}

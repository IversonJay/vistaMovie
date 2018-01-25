package com.lhh.vista.web.controller.manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.temp.dao.StatisticsDao;
import com.lhh.vista.temp.model.Statistics;

@Controller
@RequestMapping("m_statistics")
public class MStatisticsController  extends BaseController {
	@Autowired
    private StatisticsDao statisticsDao;
	
	 /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public ResponseResult<PagerResponse<Statistics>> getPager(PagerRequest pager) {
        ResponseResult<PagerResponse<Statistics>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            PagerResponse<Statistics> movieList = statisticsDao.getAllPager(pager);
            r.setResult(movieList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
   


	
}

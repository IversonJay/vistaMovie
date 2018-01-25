package com.lhh.vista.web.controller.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.temp.model.TicketInfo;
import com.lhh.vista.temp.service.TicketInfoService;

@Controller
@RequestMapping("m_ticketV")
public class MTicketController extends BaseController {
	 
	@Autowired
    private TicketInfoService ticketInfoService;
    /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("ticketZX")
    @ResponseBody
    public ResponseResult<PagerResponse<TicketInfo>> ticketZX(PagerRequest pager, String type) {
        ResponseResult<PagerResponse<TicketInfo>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	PagerResponse<TicketInfo> ticketList = ticketInfoService.ticketZXData(pager, type);
        	r.setResult(ticketList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    
    @RequestMapping("toEdit")
    @ResponseBody
    public ResponseResult<Object> toPage(String key) {
        ResponseResult<Object> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	//获取所有票类  随机取一场电影座位
        	List<TicketInfo> ticketList = ticketInfoService.getTicketAll();
        	Map<String, Object> map = new HashMap<>();
        	map.put("ticketList", ticketList);
        	
        	//票类信息
        	TicketInfo ticketInfo = ticketInfoService.getByKey(key);
        	map.put("ticketInfo", ticketInfo);
        	
            r.setResult(getContent("manage/edit_ticket.ftl", map));
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        } catch (Exception e) {
            r.setState(StateTool.State.FAIL);
            e.printStackTrace();
        }
        return r;
    }
    
    
    @RequestMapping("save")
    @ResponseBody
    public ResponseResult<Object> save(TicketInfo ticketInfo, String info) {
    	ResponseResult<Object> r = new ResponseResult<>(StateTool.State.FAIL);
    	
    	String hopk = info.substring(info.indexOf(":") + 1, info.indexOf(","));
    	String description = info.substring(info.indexOf(",") + 1);
    	ticketInfo.setHopk(hopk);
    	ticketInfo.setDescription(description);
    	
    	ticketInfoService.update(ticketInfo);
        r.setState(StateTool.State.SUCCESS);
    	return r;
    }
}

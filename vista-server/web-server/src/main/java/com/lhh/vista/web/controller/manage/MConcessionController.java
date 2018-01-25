package com.lhh.vista.web.controller.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.BaseResult;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.temp.model.Cinema;
import com.lhh.vista.temp.model.Concession;
import com.lhh.vista.temp.service.CinemaService;
import com.lhh.vista.temp.service.ConcessionService;

@Controller
@RequestMapping("m_concession")
public class MConcessionController extends BaseController {
	 
	@Autowired
    private ConcessionService concessionService;
	@Autowired
    private CinemaService cinemaService;
    /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public ResponseResult<PagerResponse<Concession>> list(PagerRequest pager, String ptype, String cid) {
        ResponseResult<PagerResponse<Concession>> r = new ResponseResult<>(StateTool.State.FAIL);
        if (Strings.isNullOrEmpty(cid)) {
        	cid = cinemaService.selectOne();
        }
        try {
        	PagerResponse<Concession> connectionList = concessionService.list(pager, ptype, cid);
        	r.setResult(connectionList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    
    @RequestMapping("cinemaList")
    @ResponseBody
    public Object cinemaList() {
    	ResponseResult<List<Cinema>> r = new ResponseResult<>(StateTool.State.FAIL);
    	
    	try {
	    	List<Cinema> list = cinemaService.getAll();
	    	r.setResult(list);
	    	r.setState(StateTool.State.SUCCESS);
    	} catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
    	
    	return r;
    	
    }
    
    
    @RequestMapping("add")
    @ResponseBody
    public ResponseResult<String> add(String cid) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	
        	List<Concession> ConcessionList = concessionService.getAllByCid(cid);
        	List<Cinema> cinemaList = cinemaService.getAll();
        	Map<String, Object> map = new HashMap<>();
        	map.put("concessionList", ConcessionList);
        	map.put("cinemaList", cinemaList);
        	map.put("cid", cid);
            r.setResult(getContent("manage/add_concession.ftl", map));
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
    
    @RequestMapping("edit")
    @ResponseBody
    public ResponseResult<String> edit(String cid, int id) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	
        	List<Concession> ConcessionList = concessionService.getAllByCid(cid);
        	List<Cinema> cinemaList = cinemaService.getAll();
        	Concession concession = concessionService.getOneById(id);
        	Map<String, Object> map = new HashMap<>();
        	map.put("concessionList", ConcessionList);
        	map.put("cinemaList", cinemaList);
        	map.put("pid", concession.getPid() + "");
        	map.put("ptype", concession.getPtype());
        	map.put("cid", cid);
        	map.put("id", id);
            r.setResult(getContent("manage/edit_concession.ftl", map));
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
    public ResponseResult<String> save(String info, Concession concession) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	String[] infoArr = info.split(":");
        	String[] cinemaArr = concession.getCid().split(",");
        	
        	concession.setName(infoArr[1]);
        	concession.setPid(infoArr[0]);
        	concession.setPrice(Integer.parseInt(infoArr[2]));
        	
        	//全选操作
        	if (isExist(cinemaArr)) {
        		List<Cinema> cinemaList = cinemaService.getAll();
        		for (Cinema cinema : cinemaList) {
        			concession.setCid(cinema.getCid());
        			concessionService.insert(concession);
        		}
        	} else {
        		for (int i = 0; i < cinemaArr.length; i++) {
        			concession.setCid(cinemaArr[i]);
        			concessionService.insert(concession);
        		}
        	}
        	      	
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
    
    
    
    @RequestMapping("saveE")
    @ResponseBody
    public ResponseResult<String> saveE(String info, Concession concession) {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	String[] infoArr = info.split(":");
        	
        	concession.setName(infoArr[1]);
        	concession.setPid(infoArr[0]);
        	concession.setPrice(Integer.parseInt(infoArr[2]));
			concessionService.update(concession);
        	      	
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
    
    /**
     * 删除信息
     *
     * @return
     */
    @RequestMapping("del")
    @ResponseBody
    public BaseResult del(Integer id) {
        BaseResult r = new BaseResult(StateTool.State.FAIL);
        try {
        	concessionService.del(id);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    
    
    
    private boolean isExist(String[] arr) {
    	boolean flag = false;
    	for (int i = 0; i < arr.length; i++) {
    		if ("all".equals(arr[i])) {
    			flag = true;
    			break;
    		}
    	}
    	return flag;
    }
    
//    
//    @RequestMapping("toEdit")
//    @ResponseBody
//    public ResponseResult<Object> toPage(String key) {
//        ResponseResult<Object> r = new ResponseResult<>(StateTool.State.FAIL);
//        try {
//        	//获取所有票类  随机取一场电影座位
//        	List<TicketInfo> ticketList = ticketInfoService.getTicketAll();
//        	Map<String, Object> map = new HashMap<>();
//        	map.put("ticketList", ticketList);
//        	
//        	//票类信息
//        	TicketInfo ticketInfo = ticketInfoService.getByKey(key);
//        	map.put("ticketInfo", ticketInfo);
//        	
//            r.setResult(getContent("manage/edit_ticket.ftl", map));
//            r.setState(StateTool.State.SUCCESS);
//        } catch (StateTool.StateException e) {
//            r.setState(e.getState());
//            e.printStackTrace();
//        } catch (Exception e) {
//            r.setState(StateTool.State.FAIL);
//            e.printStackTrace();
//        }
//        return r;
//    }
//    
//    
//    @RequestMapping("save")
//    @ResponseBody
//    public ResponseResult<Object> save(TicketInfo ticketInfo, String info) {
//    	ResponseResult<Object> r = new ResponseResult<>(StateTool.State.FAIL);
//    	
//    	String hopk = info.substring(info.indexOf(":") + 1, info.indexOf(","));
//    	String description = info.substring(info.indexOf(",") + 1);
//    	ticketInfo.setHopk(hopk);
//    	ticketInfo.setDescription(description);
//    	
//    	ticketInfoService.update(ticketInfo);
//        r.setState(StateTool.State.SUCCESS);
//    	return r;
//    }
}

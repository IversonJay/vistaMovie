package com.lhh.vista.web.controller.manage;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.service.service.SystemValueService;
import com.lhh.vista.web.sms.JdpushUtil;

@Controller
@RequestMapping("m_systemValue")
public class MSystemValueController extends BaseController {
    @Autowired
    private SystemValueService systemValueService;

    /**
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("toEdit_input")
    @ResponseBody
    public ResponseResult<String> toEdit_input() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_system_value_input.ftl", null));
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
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("toEdit_num")
    @ResponseBody
    public ResponseResult<String> toEdit_num() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_system_value_num.ftl", null));
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
    
    
    
    @RequestMapping("toEdit_push")
    @ResponseBody
    public ResponseResult<String> toEdit_push() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_system_value_push.ftl", null));
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
    
//    
//    @RequestMapping("toEditZX_num")
//    @ResponseBody
//    public ResponseResult<String> toEditZX_num() {
//        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
//        try {
//            r.setResult(getContent("manage/edit_system_value_num.ftl", null));
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

    /**
     * 跳转到用户管理页面
     *
     * @return
     */
    @RequestMapping("toEdit_html")
    @ResponseBody
    public ResponseResult<String> toEdit_html() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_system_value_html.ftl", null));
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
     * 获取分页
     *
     * @return
     */
    @RequestMapping("getAll")
    @ResponseBody
    public ResponseResult<PagerResponse<SystemValue>> getAll() {
        ResponseResult<PagerResponse<SystemValue>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            PagerResponse<SystemValue> pagerResponse = new PagerResponse<>();
            pagerResponse.setTotal(0);
            pagerResponse.setRows(systemValueService.getList());
            r.setResult(pagerResponse);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }

    /**
     * 保存信息
     *
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public ResponseResult<Integer> save(int key, String value) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	if (key == 8080) {
        		Map<String, String> map = new HashMap<>();
        		map.put("mtype", request.getParameter("mtype"));
        		map.put("target", request.getParameter("target"));
        		JdpushUtil.sendAllPushMessage("value", map);
        		value = value + "@_@!!!" + request.getParameter("mtype") + "@_@!!!" + request.getParameter("target");
        		//开始推送
        		
        	}
            systemValueService.setValue(key, value);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

}

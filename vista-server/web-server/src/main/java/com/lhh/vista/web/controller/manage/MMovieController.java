package com.lhh.vista.web.controller.manage;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Strings;
import com.lhh.vista.common.model.PagerRequest;
import com.lhh.vista.common.model.PagerResponse;
import com.lhh.vista.common.model.ResponseResult;
import com.lhh.vista.common.util.StateTool;
import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.temp.model.Movie;
import com.lhh.vista.temp.service.SessionService;

@Controller
@RequestMapping("m_movie")
public class MMovieController  extends BaseController {
	@Autowired
    private SessionService sessionService;
	
	 /**
     * 获取用户分页
     *
     * @return
     */
    @RequestMapping("getPager")
    @ResponseBody
    public ResponseResult<PagerResponse<Movie>> getPager(PagerRequest pager, String type) {
    	System.out.println(type);
        ResponseResult<PagerResponse<Movie>> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            if (Strings.isNullOrEmpty(type)) {
            	type = null;
            }
            PagerResponse<Movie> movieList = sessionService.getMergePager(pager, type);
            for (Movie movie : movieList.getRows()) {
            	if (!judgeContainsStr(movie.getMerge())) {
            		movie.setMerge(movie.getMerge() + "（已合并）");
            	}
            }
            r.setResult(movieList);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException e) {
            r.setState(e.getState());
            e.printStackTrace();
        }
        return r;
    }
    
    
    private boolean judgeContainsStr(String cardNum) {  
        String regex=".*[a-zA-Z]+.*";  
        Matcher m = Pattern.compile(regex).matcher(cardNum);  
        return m.matches();  
    }  
    /**
     * 跳转到编辑页面
     *
     * @return
     */
    @RequestMapping("toEdit_input")
    @ResponseBody
    public ResponseResult<String> toEdit_input() {
        ResponseResult<String> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
            r.setResult(getContent("manage/edit_movie_input.ftl", null));
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
     * 保存信息
     *
     * @return
     */
    @RequestMapping("editSequence")
    @ResponseBody
    public ResponseResult<Integer> editSequence(String mid, String idx) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	sessionService.editSequence(mid, idx);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }

    
    /**
     * 保存信息
     *
     * @return
     */
    @RequestMapping("mergeMovie")
    @ResponseBody
    public ResponseResult<Integer> mergeMovie(String mids) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	String midArr[] = mids.split(",");
        	sessionService.mergeMovie(midArr);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }
    
    
    @RequestMapping("recoverMovie")
    @ResponseBody
    public ResponseResult<Integer> recoverMovie(String mid) {
        ResponseResult<Integer> r = new ResponseResult<>(StateTool.State.FAIL);
        try {
        	sessionService.recoverMovie(mid);
            r.setState(StateTool.State.SUCCESS);
        } catch (StateTool.StateException se) {
            r.setState(se.getState());
            se.printStackTrace();
        }
        return r;
    }


	
}

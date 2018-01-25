package com.lhh.vista.web.controller.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lhh.vista.common.web.BaseController;
import com.lhh.vista.temp.service.CinemaService;

/**
 * 
 * @author LiuHJ
 *
 */
@Controller
@RequestMapping("a_consession")
public class AConcessionController extends BaseController {

    @Autowired
    private CinemaService cinemaService;

    
}

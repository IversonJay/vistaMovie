package com.lhh.vista.web.common;

import com.lhh.vista.service.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by liu on 2016/12/27.
 */
@Component
public class GetSessionTask {
    @Autowired
    private SystemService systemService;

    public void doTask() {
        systemService.synVistaData();
    }
}

package com.lhh.vista.web.common;

import com.lhh.vista.customer.VistaApi;
import com.lhh.vista.service.model.SystemValue;
import com.lhh.vista.service.service.SystemService;
import com.lhh.vista.service.service.SystemValueService;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Created by soap on 2016/12/10.
 */
public class ContextListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private SystemService systemService;

    @Autowired
    private SystemValueService systemValueService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private VistaApi vistaApi;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {

            vistaApi.setToken(systemValueService.getValue(SystemValue.VISTA_SERVER_TOKEN));
            vistaApi.setServer(systemValueService.getValue(SystemValue.VISTA_SERVER_URL));
            vistaApi.setClientClass(systemValueService.getValue(SystemValue.VISTA_SERVER_CLIENT_CLASS));
            vistaApi.setClientId(systemValueService.getValue(SystemValue.VISTA_SERVER_CLIENT_ID));
            vistaApi.setClientName(systemValueService.getValue(SystemValue.VISTA_SERVER_CLIENT_NAME));
            int id = -1;
            int idzx = -1;
            try {
                id = Integer.parseInt(systemValueService.getValue(SystemValue.VISTA_SERVER_CLUB_ID));
                idzx = Integer.parseInt(systemValueService.getValue(SystemValue.VISTA_SERVER_ZXCLUB_ID));
            } catch (Exception e) {

            }
            vistaApi.setMobileapitoken(systemValueService.getValue(SystemValue.VISTA_VOUCHER_TOKEN));
            vistaApi.setClubID(id);
            vistaApi.setClubZXID(idzx);


//            vistaApi.setToken("123456");
//            vistaApi.setServer("http://180.167.19.17");
//            vistaApi.setClientClass("RSP");
//            vistaApi.setClientId("111.111.111.111");
//            vistaApi.setClientName("3rdparty");
//            vistaApi.setClubID(8);

            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Spring容器加载完成触发,可用于初始化环境，准备测试数据、加载一些数据到内存");
                    systemService.initDb();
                }
            });

//            try {
//                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//                QuartzManager.addJob(scheduler, CommonData.TASK_UPDATE_TICKET, GetTicketTask.class, "0/1 * * * * ?");
//            } catch (Exception e) {
//            }
        }
    }
}

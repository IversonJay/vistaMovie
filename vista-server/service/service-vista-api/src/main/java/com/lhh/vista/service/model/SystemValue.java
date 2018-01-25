package com.lhh.vista.service.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 系统参数表
 * Created by soap on 2016/12/18.
 */
@Setter
@Getter
public class SystemValue {
    private Integer key;
    private String value;

    public static final int VISTA_SERVER_URL = 1000;//客户的服务器地址
    public static final int VISTA_SERVER_TOKEN = 1001;//客户服务器的密钥

    public static final int VISTA_SERVER_CLIENT_CLASS = 1020;//客户的服务器地址
    public static final int VISTA_SERVER_CLIENT_ID = 1021;//客户的服务器地址
    public static final int VISTA_SERVER_CLIENT_NAME = 1022;//客户的服务器地址
    public static final int VISTA_SERVER_CLUB_ID = 1023;//海上明珠俱乐部key
    public static final int VISTA_SERVER_ZXCLUB_ID = 1024;//尊享卡俱乐部key

    public static final int VISTA_FENGMIAN_PIC_URL = 1002;//客户的图片服务器地址
    public static final int VISTA_HAIBAO_PIC_URL = 1003;//客户的图片服务器地址
    public static final int VISTA_MAIPIN_PIC_URL = 1004;//客户的图片服务器地址

    public static final int VISTA_TINGSHOU_MIN = 1006;//停售分钟

    public static final int VISTA_ACTIVITY_DAZHGUYANPAN = 1007;//大转盘总概率


    public static final int VISTA_TIAOKUAN = 1008;//条款

    public static final int VISTA_ACTIVITY_DAZHGUYANPAN_GUIZE = 1009;//条款

    public static final int VISTA_GUANYUWOMEN = 1010;//条款
    public static final int BACKGROUND_SERVER_URL = 1011;//这是后台服务的地址
    
    public static final int VISTA_VOUCHER_TOKEN = 1025;
}

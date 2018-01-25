package com.lhh.vista.web.dto;

import com.google.common.base.Strings;
import com.lhh.vista.common.util.DateTool;
import com.lhh.vista.service.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by liu on 2016/12/8.
 */
@Setter
@Getter
public class ActivityWithDateTime extends Activity {
    private String sdatetime;
    private String edatetime;

    public Activity toActivity() {
        Activity a = new Activity();
        a.setId(getId());
        a.setAcover(getAcover());
        a.setContent(getContent());
        a.setAname(getAname());
        a.setState(getState());
        if (!Strings.isNullOrEmpty(sdatetime)) {
            Date d = DateTool.stringToDateTime(sdatetime);
            if (d != null) {
                a.setStime(d.getTime());
            }
        }
        if (!Strings.isNullOrEmpty(edatetime)) {
            Date d = DateTool.stringToDateTime(edatetime);
            if (d != null) {
                a.setEtime(d.getTime());
            }
        }
        return a;
    }

    public ActivityWithDateTime() {
    }

    public ActivityWithDateTime(Activity a) {
        setId(a.getId());
        setAcover(a.getAcover());
        setContent(a.getContent());
        setAname(a.getAname());
        setState(a.getState());
        setSdatetime(DateTool.dateTimeToString(new Date(a.getStime())));
        setEdatetime(DateTool.dateTimeToString(new Date(a.getEtime())));
    }
}

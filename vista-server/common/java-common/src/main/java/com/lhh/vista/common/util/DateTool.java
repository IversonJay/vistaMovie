package com.lhh.vista.common.util;

import com.google.common.base.Strings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {
	public static Date stringToDateTime(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (Exception e) {
		}
		return date;
	}

	public static String dateToStringTime(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = null;
		try {
			date = sdf.format(d);
		} catch (Exception e) {
			date = "";
		}
		return date;
	}
	public static Date stringToDate(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (Exception e) {
		}
		return date;
	}
	public static Date stringToDate2(String d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = null;
		try {
			date = sdf.parse(d);
		} catch (Exception e) {
		}
		return date;
	}

	public static String dateToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = null;
		try {
			date = sdf.format(d);
		} catch (Exception e) {
			date = "";
		}
		return date;
	}

	public static String dateToString(Date d,String f) {
		SimpleDateFormat sdf = new SimpleDateFormat(f);
		String date = null;
		try {
			date = sdf.format(d);
		} catch (Exception e) {
			date = "";
		}
		return date;
	}

	public static String dateTimeToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = null;
		try {
			date = sdf.format(d);
		} catch (Exception e) {
			date = "";
		}
		return date;
	}
	
	public static String getNowTime() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sdf.format(now);
		return date;
	}

	public static String getNowTime(String fmtStr) {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(fmtStr);
		String date = sdf.format(now);
		return date;
	}

    public static String getNowDate() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(now);
        return date;
    }

	/**
	 * 比较两个日期之间的大小
	 *
	 * @param date1
	 * @param date2
	 * @return 前者大于后者返回true 反之false
	 */
	public static boolean compareDate(String date1, String date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = format.parse(date1);
			d2 = format.parse(date2);

			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(d1);
			c2.setTime(d2);

			int result = c1.compareTo(c2);
            if (result > 0) {
                return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
    /**
     * 该方法是类属性，根据传入的日期格式返回符合格式要求的当前日期字符串
     *
     * @param fmtStr
     * @return String
     * @roseuid 3FE7C9DF01A7
     */
    public static String getNowDateStr(String fmtStr) {
        if (fmtStr.equals("yyyy-MM-dd hh:mm")) {
            fmtStr = "yyyy-MM-dd HH:mm";
        } else if (fmtStr.equals("YYYY-MM-dd")) {
            fmtStr = "yyyy-MM-dd";
        } else if (fmtStr.equals("YYYY-MM-dd hh:mm:ss")) {
            fmtStr = "yyyy-MM-dd HH:mm:ss";
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(fmtStr);
        String datestr = "";
        try {
            datestr = df.format(cal.getTime());
        } catch (Exception e) {
        }
        return datestr.toString();
    }
}

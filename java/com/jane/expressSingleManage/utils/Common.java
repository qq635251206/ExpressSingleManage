package com.jane.expressSingleManage.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jane on 2015/4/5.
 */
public class Common {
    public static String PATTERN_TIME = "yyyy/MM/dd HH:mm:ss";
    public static String PATTERN_DATE = "yyyy/MM/dd";

    public static String getTime(String pattern){
        String time = "";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        time = format.format(new Date());
        return time;
    }
}

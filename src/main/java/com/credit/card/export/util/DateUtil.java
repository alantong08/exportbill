package com.credit.card.export.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static final SimpleDateFormat standardTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat yyyyMMddHHmmssFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public static Date strToDate(String dateStr) {
        try {
            Date d = yyyyMMddHHmmssFormat.parse(dateStr);
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String formatTime(Date date){
        try{
            return standardTimeFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String  convertStrToStr(String dateStr){
        Date date = strToDate(dateStr);
        if(date!=null){
            return formatTime(date);
        }
        return null;
    }
}

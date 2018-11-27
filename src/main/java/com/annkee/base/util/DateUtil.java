package com.annkee.base.util;

import com.annkee.base.enums.ResultCodeEnum;
import com.annkee.base.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author wangan
 */
@Slf4j
public class DateUtil {
    
    public static final String DATE_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PAR = "yyyy-MM-dd";
    
    public static final SimpleDateFormat formatLongDate = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    
    public static final SimpleDateFormat formatShortDate = new SimpleDateFormat(
            "yyyy-MM-dd");
    
    /**
     * 将短时间格式字符串yyyy-MM-dd转换为时间
     *
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
        ParsePosition pos = new ParsePosition(0);
        synchronized (formatLongDate) {
            Date date = formatLongDate.parse(strDate, pos);
            return date;
        }
    }
    
    /**
     * 显示年月日 格式yyyy-MM-dd
     * 增加dateStr为空值处理
     *
     * @param date 时间
     * @return
     */
    public static String getDateFromString(Date date) {
        synchronized (formatLongDate) {
            return formatLongDate.format(date);
        }
    }
    
    /**
     * 得到一个时间延后或前移几天的时间
     *
     * @param strDate
     * @param delay
     * @return
     */
    public static String getCountedDay(String strDate, Integer delay) {
        try {
            Date d = strToDate(strDate);
            long myTime = (d.getTime() / 1000) + delay * 24 * 60 * 60;
            d.setTime(myTime * 1000);
            synchronized (formatShortDate) {
                String mdate = formatShortDate.format(d);
                return mdate;
            }
        } catch (Exception e) {
            log.warn("getCountedDay error:{}", e.getMessage());
            throw new BaseException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
    
    /**
     * 判断两个日期之间差了多少天，不足一天，则按一天计算，即20.01天也算21天
     */
    public static int dateDiff(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        long baseNum = 3600 * 1000 * 24;
        long absNum = Math.abs(date1.getTime() - date2.getTime());
        long mod = absNum % baseNum;
        int num = (int) (absNum / baseNum);
        if (mod > 0) {
            num++;
        }
        return num;
    }
    
    /**
     * 得到一个时间延后或前移几月的时间
     *
     * @param strDate
     * @param delay
     * @return
     */
    public static String getCountedMonth(String strDate, Integer delay) {
        try {
            Date dd = strToDate(strDate);
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(dd);
            currentDate.add(Calendar.MONDAY, delay);
            synchronized (formatLongDate) {
                return formatLongDate.format(currentDate.getTime());
            }
        } catch (Exception e) {
            log.warn("getCountedMonth error:{}", e.getMessage());
            throw new BaseException(ResultCodeEnum.SYSTEM_ERROR);
        }
    }
    
    /**
     * 日期转毫秒
     *
     * @param expireDate
     * @return
     */
    public static Long getSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals("")) {
            return 0L;
        }
        Date date = null;
        try {
            synchronized (formatLongDate) {
                date = formatLongDate.parse(expireDate);
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }
    
    
    public static String formatDateStr(Date date) {
        synchronized (formatLongDate) {
            return formatLongDate.format(date);
        }
    }
    
    
    public static Date parseStr2Date(String date) throws ParseException {
        synchronized (formatLongDate) {
            return formatLongDate.parse(date);
        }
    }
    
    
}

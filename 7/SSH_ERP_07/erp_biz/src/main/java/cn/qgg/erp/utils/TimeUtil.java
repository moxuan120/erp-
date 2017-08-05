package cn.qgg.erp.utils;

import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
    public static Date getBeforeDayStart(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new Date(date.getTime() -
                gc.get(GregorianCalendar.HOUR_OF_DAY) * 60 * 60 * 1000
                - gc.get(GregorianCalendar.MINUTE) * 60 * 1000
                - gc.get(GregorianCalendar.SECOND) * 1000
                - 24 * 60 * 60 * 1000);
    }
    public static Date getBeforeDayEnd(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        return new Date(date.getTime() -
                gc.get(GregorianCalendar.HOUR_OF_DAY) * 60 * 60 * 1000
                - gc.get(GregorianCalendar.MINUTE) * 60 * 1000
                - gc.get(GregorianCalendar.SECOND) * 1000);
    }
}

package com.qixing.utlis;

import android.content.Context;
import android.text.format.Time;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by SunQi on 2015/7/21.
 */
public class DateUtils {


    /**
     * 获取当前系统时间戳
     * @return
     */
    public static  long getCurrentMillis(){

        long currentMillis=System.currentTimeMillis()/1000;

        return  currentMillis;

    }


    /**
     * 格式化时间
     * @param context
     * @param date
     * @return
     */
    public static String formatTime(Context context, Date date) {
        return android.text.format.DateUtils.formatDateTime(context, date.getTime(),
                android.text.format.DateUtils.FORMAT_SHOW_TIME
                        | android.text.format.DateUtils.FORMAT_NO_NOON
                        | android.text.format.DateUtils.FORMAT_NO_MIDNIGHT
        );
    }

    /**
     * 显示时间格式为今天、昨天、yyyy/MM/dd hh:mm
     *
     * @param context
     * @param when
     * @return String
     */
    public static String formatTimeString(Context context, long when) {


        Time then = new Time();

        then.set(when);
        Time now = new Time();
        now.setToNow();

        String formatStr;
        if (then.year != now.year) {
            formatStr = "yyyy-MM-dd";
        } else if (then.yearDay != now.yearDay) {
            // If it is from a different day than today, show only the date.
            formatStr = "MM-dd";
        } else {
            // Otherwise, if the message is from today, show the time.
            formatStr = "HH:MM";
        }

        if (then.year == now.year && then.yearDay == now.yearDay) {
            return "今天";
        } else if ((then.year == now.year) && ((now.yearDay - then.yearDay) == 1)) {
            return "昨天";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
            String temp = sdf.format(when);
            if (temp != null && temp.length() == 5 && temp.substring(0, 1).equals("0")) {
                temp = temp.substring(1);
            }
            return temp;
        }
    }



    /**定义常量**/
    public static final String DATE_FULL_MIN = "yyyy-MM-dd HH:mm";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_LONG_STR = "yyyy-MM-dd kk:mm:ss.SSS";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyMMddHHmmss";
    public static final String DATE_All_KEY_STR = "yyyyMMddHHmmss";

    /**
     * 给指定的日期加上(减去)月份
     * @param date
     * @param pattern
     * @param num
     * @return
     */
    public static String addMoth(Date date,String pattern,int num){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.MONTH, num);
        return simpleDateFormat.format(calender.getTime());
    }


    /**
     * 给制定的时间加上(减去)天
     * @param date
     * @param pattern
     * @param num
     * @return
     */
    public static String addDay(Date date,String pattern,int num){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Calendar calender = Calendar.getInstance();
        calender.setTime(date);
        calender.add(Calendar.DATE, num);
        return simpleDateFormat.format(calender.getTime());
    }

    /**
     * 获取系统当前时间  yyyy-MM-dd HH:mm:ss"
     * @return
     */
    public static String getNowTime() {
        SimpleDateFormat df = new SimpleDateFormat(DATE_FULL_STR);
        return df.format(new Date());
    }

    /**
     * 获取系统当前时间(指定返回类型)
     * @return
     */
    public static String getNowTime(String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        return df.format(new Date());
    }

    /**
     * 使用预设格式提取字符串日期
     * @param date 日期字符串
     * @return
     */
    public static Date parse(String date) {
        return parse(date, DATE_FULL_STR);
    }

    /**
     * 指定指定日期字符串
     * @param date
     * @param pattern
     * @return
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 两个时间比较
     * @param
     * @return
     */
    public static int compareDateWithNow(Date date){
        Date now = new Date();
        int rnum = date.compareTo(now);
        return rnum;
    }

    /**
     * 两个时间比较(时间戳比较)
     * @param
     * @return
     */
    public static int compareDateWithNow(long date){
        long now = dateToUnixTimestamp();
        if(date>now){
            return 1;
        }else if(date<now){
            return -1;
        }else{
            return 0;
        }
    }


    /**
     * 将指定的日期转换成Unix时间戳
     * @param date 需要转换的日期 yyyy-MM-dd HH:mm:ss
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(DATE_FULL_STR).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    /**
     * 将指定的日期转换成Unix时间戳
     * @param  date 需要转换的日期 yyyy-MM-dd
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp(String date, String dateFormat) {
        long timestamp = 0;
        try {
            timestamp = new SimpleDateFormat(dateFormat).parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 将当前日期转换成Unix时间戳
     * @return long 时间戳
     */
    public static long dateToUnixTimestamp() {
        long timestamp = new Date().getTime();
        return timestamp;
    }

    /**
     * 将Unix时间戳转换成日期
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDate(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_STR);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * 将Unix时间戳转换成日期
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String unixTimestampToDateMin(long timestamp) {
        SimpleDateFormat sd = new SimpleDateFormat(DATE_FULL_MIN);
        sd.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return sd.format(new Date(timestamp));
    }

    /**
     * 将Unix时间戳转换成日期
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2Date(long timestamp,String dateFormat){
        String date = new SimpleDateFormat(dateFormat).format(new Date(timestamp));
        return date;
    }


    //============================================================

    public static String formatStringDate(long dateStr){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String date = sdf.format(new Date(dateStr*1000L));
        return  date;
    }
    public static String formatNormalStringDate(long dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date(dateStr*1000L));
        return  date;
    }
    /**
     * 将Unix时间戳转换成日期 yyyy-MM-dd HH:mm:ss
     * @param timestamp 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2Date(long timestamp){
        SimpleDateFormat sdr = new SimpleDateFormat(DATE_FULL_STR);
//        long lcc = Long.valueOf(time);
//        int i = Integer.parseInt(time);
        String date = sdr.format(new Date(timestamp * 1000L));
        return date;
    }

    /**
     * 将Unix时间戳转换成日期  HH:mm:ss
     * @param dateStr 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2DateHHMM(long dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String date = sdf.format(new Date(dateStr*1000L));
        return  date;
    }

    /**
     * 将字符串时间戳转换成日期  yyyy-MM-dd
     * @param dateStr 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2DateYYYYMMDD(String dateStr){
        String date = DateUtils.formatStringDate(StrngToLong(dateStr));
        return  date;
    }

    /**
     * 将字符串时间戳转换成日期  yyyy-MM-dd HH:mm:ss
     * @param dateStr 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2DateYYYYMMDDHHmmss(String dateStr){
        String date = DateUtils.formatNormalStringDate(StrngToLong(dateStr));
        return  date;
    }

    /**
     * 将long时间戳转换成日期  yyyy-MM-dd
     * @param dateStr 时间戳
     * @return String 日期字符串
     */
    public static String TimeStamp2DateYYYYMMDD(long dateStr){
        String date = DateUtils.formatStringDate(dateStr);
        return  date;
    }

    /**
     * 将字符串时间戳转换成long时间戳
     */
    public static long StrngToLong(String dateStr){
        long date = Long.parseLong(dateStr);
        return date;
    }

    /**
     * 判断是否同一天
     * */
    public static boolean isTheSameDay(String loginTime){
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String sp_time = sf.format(StrngToLong(loginTime));
        String current_time = sf.format(timeMillis);
        System.out.println("===========================DateUtils 上次登录时间 sp_time = " + sp_time + "，本次登录时间 current_time = " +current_time);
        if(!sp_time.equals(current_time)){
            // 不同一天
            //存储登录时间
//            MyApplication.getInstance().setLast_start_time(timeMillis+"");
            return false;
        }else{
            return true;
        }
    }

}

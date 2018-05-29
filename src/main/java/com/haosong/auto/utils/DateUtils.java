package com.haosong.auto.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期处理的工具类
 * @author songhao
 * @since 2018-01-29
 */
public class DateUtils {

    //常规化几种常用的格式信息
    public static final String Y_M_D_FORMAT = "yyyy-MM-dd";
    public static final String YMDHmss_FORMAT = "yyyyMMddHHmmssSSS";
    public static final String YMDHms_FORMAT = "yyyyMMddHHmmss";
    public static final String YMD_FORMAT = "yyyyMMdd";
    public static final String YM_FORMAT = "yyyyMM";
    public static final String Hms_FORMAT = "HHmmss";

    public static final long DAY_MILLISECONDS = 24 * 60 * 60 * 1000;

    /**
     * 获取当前时间的毫秒
     * @return
     */
    public static long getNowMilliseconds(){
        return getMillisecondsByDate(new Date());
    }

    /**
     * 根据时间取毫秒值
     * @param date
     * @return
     */
    public static long getMillisecondsByDate(Date date){
        return date.getTime();
    }

    /**
     * 获取当前时间的Date对象
     * @return
     */
    public static Date getNowDate(){
        return new Date();
    }

    /**
     * 根据用户输入的日期字符串，转换成生日
     * @param birthDay
     * @param format
     * @return
     */
    public static int ageOfNow(String birthDay,String format) throws Exception {
        Date date = getDateByValue(birthDay,format);
        //首先将其
        return ageOfNow(date);
    }

    /**
     * 传入一个年龄的Date对象，返回年龄的int型变量
     * @param birthDay
     * @return
     */
    public static int ageOfNow(Date birthDay) {
        return age(birthDay, getNowDate());
    }

    /**
     * 获取年龄
     * @param birthDay
     * @param dateToCompare
     * @return
     */
    private static int age(Date birthDay, Date dateToCompare) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateToCompare);
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(String.format("Birthday is after date %s!",dateToCompare));
        } else {
            int year = cal.get(1);
            int month = cal.get(2);
            int dayOfMonth = cal.get(5);
            cal.setTime(birthDay);
            int age = year - cal.get(1);
            int monthBirth = cal.get(2);
            if (month == monthBirth) {
                int dayOfMonthBirth = cal.get(5);
                if (dayOfMonth < dayOfMonthBirth) {
                    --age;
                }
            } else if (month < monthBirth) {
                --age;
            }

            return age;
        }
    }

    /**
     * 获取对应时间的毫秒，注意格式必须与值的格式是对应的
     * @param format 格式是"yyyy-MM-dd"
     * @param value 值是"2018-01-29"
     * @return
     */
    public static long getMillisecondsByValue(String value,String format) throws Exception {
        if(format == null || value == null){
            throw new Exception("格式和值都不允许为空!");
        }
        if(format.length() != value.length()){
            throw new Exception("格式和值不对应!");
        }
        Date date = getDateByValue(value,format);
        return date.getTime();
    }

    /**
     * 根据日期字符串和格式转换成Date对象
     * @param dateValue
     * @param format
     * @return
     */
    public static Date getDateByValue(String dateValue,String format) throws Exception{
        DateFormat df = new SimpleDateFormat(format);
        return df.parse(dateValue);
    }

    /**
     * 获取日期的指定格式,如果date为空就取当前时间
     * @param format
     * @param date
     * @return
     */
    public static String getValueByDate(Date date,String format){
        if(date == null){
            date = new Date();
        }
        DateFormat dateFormat = new SimpleDateFormat(format);
        String value = dateFormat.format(date);
        return value;
    }

    /**
     * 根据时间戳获取日期
     * @param value
     * @param format
     * @return
     */
    public static String getValueByTime(Long value,String format){
        Date date = new Date(value);
        return getValueByDate(date,format);
    }


    //-----------------------------start：根据某个日期，取当天的日期-------------------------------------------------------

    /**
     * 根据参数日期，取当天最小的时间
     * @param dateValue
     * @param format
     * @return
     */
    public static long getMinTimeBysameDay(String dateValue,String format) throws Exception{
        Date date = getDateByValue(dateValue,format);
        return date.getTime();
    }


    /**
     * 根据参数日期，取当天最大的时间
     * @param dateValue
     * @param format
     * @return
     */
    public static long getMaxTimeBysameDay(String dateValue,String format) throws Exception{
        Date date = getDateByValue(dateValue,format);
        Date newDate = getIntervalByDay(date,1);
        return newDate.getTime();
    }


    //-----------------------------start：取间隔时间的日期--------------------------------------------------------
    /**
     * 获取指定日期的间隔年数的日期
     * @param date
     * @param years
     * @return
     */
    public static Date getIntervalByYear(Date date,int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR,years);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的间隔月数的日期
     * @param date
     * @param months
     * @return
     */
    public static Date getIntervalByMonth(Date date,int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH,months);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的间隔天数的日期
     * @param date
     * @param days
     * @return
     */
    public static Date getIntervalByDay(Date date,int days){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR,days);
        return calendar.getTime();
    }

    /**
     * 获取指定日期的间隔小时的日期
     * @param date
     * @param hours
     * @return
     */
    public static Date getIntervalByHour(Date date,int hours){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR,hours);
        return calendar.getTime();
    }

    /**
     * 获取两个日期的间隔天数
     * @param start
     * @param end
     * @return
     */
    public static int getIntervalDays(long start,long end){
        int interval = (int)((end - start) / DAY_MILLISECONDS);
        return interval;
    }


    /**
     * 判断一个日期是否是某个月的最后一天
     * @param date
     * @return
     */
    public static boolean isLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
        if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
            return true;
        }
        return false;
    }

    /**
     * 根据日期获取对应的年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 根据日期获取对应的月份
     * @param date
     * @return
     */
    public static int getMonth(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 根据日期获取对应的日
     * @param date
     * @return
     */
    public static int getDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    public static void main(String[] args) throws Exception{
        /*System.out.println(DateUtils.getValueByDate(new Date(1514764800000l), DateUtils.YMDHms_FORMAT));

        String start = "20180327";
        String end = "20180419";
        while(true){
            Date startDate = DateUtils.getDateByValue(start, DateUtils.YMD_FORMAT);
            System.out.println(start + " === " + startDate.getTime());
            Date endDate = DateUtils.getIntervalByDay(startDate,1);
            if(start.equals(end)){
                break;
            }
            start = DateUtils.getValueByDate(endDate, DateUtils.YMD_FORMAT);
        }*/

        Date date = new Date();
        System.out.println(getYear(date));
        System.out.println(getMonth(date));
        System.out.println(getDay(date));

        Date date2 = getDateByValue("20180228",DateUtils.YMD_FORMAT);
        System.out.println(isLastDayOfMonth(date2));
    }
}
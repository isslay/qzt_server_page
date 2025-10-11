package com.qzt.common.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class DateUtil {
    /**
     * 定义日期时间格式的中间符号，可以是"-"或"/"或":"。日期默认为"-"时间默认为":"
     */
    private static final String formatDateSign = "-";

    private static final String formatDandTSign = "/";

    private static final String formatTimeSign = ":";

    private static final String simpleDateFormat = "yyyy" + formatDateSign + "MM" + formatDateSign + "dd";

    private static final String simpleTimeFormat = simpleDateFormat + " HH" + formatTimeSign + "mm" + formatTimeSign + "ss";

    public static SimpleDateFormat chineseDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat dateTimeFormatSSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static Random random = new Random();

    /**
     * 计算传入与当前时间差
     *
     * @param date
     * @return java.lang.Long 差值为秒
     * @author Xiaofei
     * @date 2019-11-14
     */
    public static Long computingTimeDifference(Date date) {
        Long result = 0L;
        if (date != null) {
            Long time1 = date.getTime();
            Long time2 = new Date().getTime();
            result = (time1 - time2) / 1000;
            result = result < 0 ? 0 : result;
        }
        return result;
    }

    /**
     * 获取某日期所属月份第一天
     *
     * @param date
     * @author Xiaofei
     * @date 2019-06-11
     */
    public static String getDayFirst(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Date theDate = calendar.getTime();
        GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
        gcLast.setTime(theDate);
        gcLast.set(Calendar.DAY_OF_MONTH, 1);//设置为第一天
        String day_first = chineseDateFormat.format(gcLast.getTime());
        return day_first;
    }

    /**
     * 获取某日期所属月份最后一天
     *
     * @param date
     * @author Xiaofei
     * @date 2019-06-11
     */
    public static String getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();//获取Calendar
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(calendar.DATE));//设置日期为本月最大日期
        String day_last = chineseDateFormat.format(calendar.getTime());
        return day_last;
    }

    /**
     * 获取某时间所处于的周一或周日日期
     *
     * @param date
     * @param type 获取周一日期0、获取周日日期1
     * @return java.lang.String
     * @author Xiaofei
     * @date 2019-06-06
     */
    public static String getOnMondayOrSunday(Date date, String type) {
        String retdate = "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (dayWeek == 1) {
            dayWeek = 8;
        }
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - dayWeek);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        if ("0".equals(type)) {
            Date mondayDate = cal.getTime();
            retdate = chineseDateFormat.format(mondayDate);
        } else if ("1".equals(type)) {
            cal.add(Calendar.DATE, 4 + cal.getFirstDayOfWeek());
            Date sundayDate = cal.getTime();
            retdate = chineseDateFormat.format(sundayDate);
        }
        return retdate;
    }

    /**
     * 功能描述：返回yyyyMMddHHmmss格式化的时间字符串
     *
     * @param date
     * @return
     */
    public static String getDatetimeString(Date date, String format) {
        SimpleDateFormat dataFormat = new SimpleDateFormat(format);
        return dataFormat.format(date);
    }

    /**
     * 获取某时间多少分钟后的时间
     *
     * @param date   某时间
     * @param amount 多少分钟
     * @return
     * @throws ParseException
     */
    public static Date getTimingTimeMinute(Date date, Integer amount) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.MINUTE, amount);
        String dateStr = dateTimeFormat.format(now.getTimeInMillis());
        return dateTimeFormat.parse(dateStr);
    }

    /**
     * 获取某时间多少小时后的时间
     *
     * @param date   某时间
     * @param amount 多少小时
     * @return
     * @throws ParseException
     */
    public static Date getTimingTime(Date date, Integer amount) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.HOUR_OF_DAY, amount);
        String dateStr = dateTimeFormat.format(now.getTimeInMillis());
        return dateTimeFormat.parse(dateStr);
    }

    /**
     * 获取某时间N天后的时间
     *
     * @param date   某时间
     * @param amount 多少天
     * @return
     * @throws ParseException
     */
    public static Date getTimingDay(Date date, Integer amount) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.add(Calendar.DATE, amount);
        String dateStr = dateTimeFormat.format(now.getTimeInMillis());
        return dateTimeFormat.parse(dateStr);
    }

    /**
     * 获取某时间N天前的时间
     *
     * @param date   某时间
     * @param amount 多少天
     * @return
     * @throws ParseException
     */
    public static Date getSomeDaysAgo(Date date, Integer amount) throws ParseException {
        Calendar now = Calendar.getInstance();
        now.setTime(date);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - amount);
        String dateStr = dateTimeFormat.format(now.getTimeInMillis());
        return dateTimeFormat.parse(dateStr);
    }

    /**
     * 获取格式化时间 yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getFormatDate(Date date) {
        return dateTimeFormat.format(date);
    }

    /**
     * 格式化时间
     *
     * @param date   字符串时间
     * @param format 指定格式
     * @return
     */
    public static String timeFormatting(String date, String format) {
        try {
            if (date == null || "".equals(date) || format == null || "".equals(format)) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.format(sdf.parse(date));
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 指定String时间格式化为Date时间
     *
     * @param date   字符串时间
     * @param format 指定格式
     * @return
     */
    public static Date stringDateToDate(String date, String format) {
        try {
            if (date == null || "".equals(date) || format == null || "".equals(format)) {
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取格式化时间
     *
     * @param date
     * @return
     */
    public static String getFormatDateTime(Date date) {
        if (date != null) return dateTimeFormat.format(date);
        return "";
    }

    /**
     * 获取当前时间之后多少分钟的时间
     *
     * @param time1
     * @return
     */
    public static Date getNextMinutesTime(Date time1, long minutes) {
        return new Date(time1.getTime() + 1000 * 60 * minutes + (random.nextInt(59) + 1) * 1000);
    }

    /**
     * 获取当前时间之后多少分钟的时间
     *
     * @param time1
     * @return
     */
    public static String getNextMinutesTimeStr(Date time1, long minutes) {
        Date time2 = new Date(time1.getTime() + 1000 * 60 * minutes);
        return dateTimeFormat.format(time2);
    }

    /**
     * @param time 要格式化的日期字符串: 10位 yyyy/MM/dd或yyyy-MM-dd或yyyy:MM:dd
     * @return 返回格式化后的日期
     * @函数名称：dateTo8 @功能描述：10位yyyy/MM/dd,yyyy-MM-dd,yyyy:MM:dd 转换为8位yyyyMMdd
     */

    public static String timeNumberTodate(String time) {
        int len = time.length();
        return time.substring(0, len - 4) + formatDateSign
                + time.substring(len - 4, len - 2) + formatDateSign
                + time.substring(len - 2, len);
    }

    public static String dateTo8(String date) {
        if (date == null)
            return "";
        if (date.trim().length() != 10) {
            return date;
        }
        return date.substring(0, 4) + date.substring(5, 7)
                + date.substring(8, 10);
    }


    /**
     * @param date 要格式化的日期字符串: 8位yyyyMMdd
     * @return 返回格式化后的日期
     * @函数名称：dateTo8 @功能描述：8位yyyyMMdd 转换为yyy/MM/dd,yyyy-MM-dd,yyyy:MM:dd
     */
    public static String dateTo10(String date) {
        if (date == null)
            return "";
        if (date.trim().length() != 8)
            return "";
        return date.substring(0, 4) + formatDateSign + date.substring(4, 6)
                + formatDateSign + date.substring(6, 8);
    }

    public static String dateTo19(String date) {
        int len = date.length();
        if (len != 14)
            return date;
        return date.substring(0, 4) + formatDateSign + date.substring(4, 6)
                + formatDateSign + date.substring(6, 8) + formatDandTSign
                + date.substring(8, 10) + formatTimeSign
                + date.substring(10, 12) + formatTimeSign
                + date.substring(12, 14);
    }

    /**
     * @param date 要格式化的日期字符串: 8位yyyyMMdd
     * @return 返回格式化后的日期
     * @函数名称：dateTo14 @功能描述：8位yyyyMMdd 转换为yyy/MM/dd,yyyy-MM-dd,yyyy:MM:dd
     */
    public static String dateTo14(String date) {
        if (date == null)
            return "";
        if (date.trim().length() != 19)
            return date;
        return date.substring(0, 4) + date.substring(5, 7)
                + date.substring(8, 10) + date.substring(11, 13)
                + date.substring(14, 16) + date.substring(17);
    }

    /**
     * @param date 要格式化的日期字符串:9位yy-mmm-dd
     * @return 返回格式化后的日期
     * @函数名称：oracleDateTo8 @功能描述：9位yy-mmm-dd 转换为yyyy-MM-dd
     * @write :wujs
     * @date :2007-08-30
     */
    public static String oracleDateTo8(String date) {
        if (date == null)
            return "";
        if (date.trim().length() != 9)
            return date;
        String month = "";
        String smonth = date.substring(3, 6);
        if (smonth.equals("JAN"))
            month = "01";
        else if (smonth.equals("FEB"))
            month = "02";
        else if (smonth.equals("MAR"))
            month = "03";
        else if (smonth.equals("APR"))
            month = "04";
        else if (smonth.equals("MAY"))
            month = "05";
        else if (smonth.equals("JUN"))
            month = "06";
        else if (smonth.equals("JUL"))
            month = "07";
        else if (smonth.equals("AUG"))
            month = "08";
        else if (smonth.equals("SEP"))
            month = "09";
        else if (smonth.equals("OCT"))
            month = "10";
        else if (smonth.equals("NOV"))
            month = "11";
        else if (smonth.equals("DEC"))
            month = "12";
        return "20" + date.substring(7, 9) + formatDateSign + month
                + formatDateSign + date.substring(0, 2);
    }

    /**
     * 时间格式化。 <br>
     * 8位(HH:mm:ss)或7位(H:mm:ss)的时间转换为6位(HHmmss)或5位(Hmmss) <br>
     * 时间的分隔字符可以是任意字符，一般为冒号(:)
     *
     * @param time -要格式化的时间字符串:8位(HH:mm:ss)或7位(H:mm:ss)
     * @return String - 返回格式化后的时间 <br>
     * 若time长度不为8或7，即格式不为8位(HH:mm:ss)或7位(H:mm:ss)形式的时间，则直接返回date。 <br>
     * 若time为null, 则返回""
     */
    public static String timeTo6(String time) {
        int len = time.length();
        if (len < 7 || len > 8)
            return "";
        return time.substring(0, len - 6) + time.substring(len - 5, len - 3)
                + time.substring(len - 2, len);
    }

    /**
     * 时间格式化。 <br>
     * 6位(HHmmss)或5位(Hmmss)的时间转换为8位(HH:mm:ss)或7位(H:mm:ss)
     *
     * @param -要格式化的时间字符串: 6位(HHmmss)或5位(Hmmss)
     * @return String - 返回格式化后的时间 <br>
     * 若time长度不为5或6，即格式不为6位(HHmmss)或5位(Hmmss)形式的时间，则直接返回date。 <br>
     * 若time为null, 则返回""
     */
    public static String timeTo8(String time) {
        int len = time.length();
        if (len < 5 || len > 6)
            return "";
        return time.substring(0, len - 4) + formatTimeSign
                + time.substring(len - 4, len - 2) + formatTimeSign
                + time.substring(len - 2, len);
    }

    /**
     * @param str
     * @return
     * @函数名称：stringToSqlDate
     * @功能描述：将String型的日期格式转换为Sql型的日期格式
     */
    public static java.sql.Date stringToSqlDate(String str) {
        if (stringToUtilDate(str) == null || str.length() < 1)
            return null;
        else
            return new java.sql.Date(stringToUtilDate(str).getTime());
    }

    /**
     * @param str
     * @return
     * @函数名称：stringToUtilDate
     * @功能描述：将String型的日期格式转换为Util型的日期格式
     */
    public static Date stringToUtilDate(String str) {
        if (null != str && str.length() > 0) {
            try {
                // 对两种时间格式进行转化。
                if (str.length() <= simpleDateFormat.length()) { // 只包含日期。
                    return (new SimpleDateFormat(simpleDateFormat)).parse(str);
                } else { // 日期和时间都有
                    return (new SimpleDateFormat(simpleTimeFormat)).parse(str);
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
                return null;
            }
        } else
            return null;
    }

    /**
     * @param date
     * @return
     * @函数名称：utilDateToSql
     * @功能描述：将Util型的日期格式转换为Sql型的日期格式
     */
    public static java.sql.Date utilDateToSql(Date date) {
        return new java.sql.Date(date.getTime());
    }

    /**
     * @param date
     * @return
     * @函数名称：sqlDateToUtil
     * @功能描述：将Sql型的日期格式转换为Util型的日期格式
     */
    public static Date sqlDateToUtil(java.sql.Date date) {
        return new Date(date.getTime());
    }

    /**
     * @param d
     * @return
     * @函数名称：toDateTimeString
     * @功能描述：将Sql型的带时间日期格式转换为String型的日期格式
     */
    public static String toDateTimeString(java.sql.Date d) {
        if (d == null) {
            return null;
        } else {
            return (new SimpleDateFormat(simpleTimeFormat)).format(d);
        }
    }

    /**
     * @param d
     * @return
     * @函数名称：toDateTimeString
     * @功能描述：将Util型的带时间日期格式转换为String型的日期格式
     */
    public static String toDateTimeString(Date d) {
        if (d == null) {
            return null;
        } else {
            return (new SimpleDateFormat(simpleTimeFormat)).format(d);
        }
    }

    /**
     * @param d
     * @return
     * @函数名称：toDateString
     * @功能描述：将Sql型的只带日期格式转换为String型的日期格式
     */
    public static String toDateString(java.sql.Date d) {
        if (d == null) {
            return null;
        } else {
            return (new SimpleDateFormat(simpleDateFormat)).format(d);
        }
    }

    /**
     * @param d
     * @return
     * @函数名称：toDateString
     * @功能描述：将Sql型的只带日期格式转换为String型的日期格式
     */
    public static String toDateString(Date d) {
        if (d == null) {
            return null;
        } else {
            return (new SimpleDateFormat(simpleDateFormat)).format(d);
        }
    }

    /**
     * @return
     * @函数名称：getCurrentDate
     * @功能描述：获取当前日期和时间
     */
    public static java.sql.Date getCurrentDateTime() {
        Calendar newcalendar = null;
        newcalendar = Calendar.getInstance();

        String year = String.valueOf(newcalendar.get(Calendar.YEAR));
        String month = String.valueOf(newcalendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(newcalendar.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(newcalendar.get(Calendar.HOUR_OF_DAY));
        String min = String.valueOf(newcalendar.get(Calendar.MINUTE));
        String sec = String.valueOf(newcalendar.get(Calendar.MINUTE));

        return stringToSqlDate(year + formatDateSign + month + formatDateSign
                + day + " " + hour + formatTimeSign + min + formatTimeSign
                + sec);
    }

    /**
     * @return
     * @函数名称：getCurrentDate
     * @功能描述：获取当前日期(只带日期)
     */
    public static java.sql.Date getCurrentDate() {
        Calendar newcalendar = null;
        newcalendar = Calendar.getInstance();

        String year = String.valueOf(newcalendar.get(Calendar.YEAR));
        String month = String.valueOf(newcalendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(newcalendar.get(Calendar.DAY_OF_MONTH));

        return stringToSqlDate(year + formatDateSign + month + formatDateSign
                + day);
    }

    /**
     * @return
     * @函数名称：getCurrentTime
     * @功能描述：获取当前时间(只带时间)
     */
    public static String getCurrentTime() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("HHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeTo8(timeString);
    }

    /**
     * @return
     * @函数名称：getCurrentTime
     * @功能描述：获取当前时间(只带时间)
     */
    public static String getCurrentDateTimeStr() {
        Date date = new Date();
        return dateTimeFormat.format(date);
    }

    public static String getCurrentDateTimeStr1() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }


    public static String getDateAddMinutes(Date time1, int minutes) {
        Date time2 = new Date(time1.getTime() + 1000 * 60 * minutes);
        return dateTimeFormat.format(time2);
    }

    public static String getDateAddDays(Date time1, int days) {
        Date time2 = new Date(time1.getTime() + 1000 * 3600 * 24 * days);
        return chineseDateFormat.format(time2);
    }

    public static Date getDateTimeAddDays(Date time1, int days) {
        return new Date(time1.getTime() + 1000 * 3600 * 24 * days);
    }


    public static String getDateTimeAddHourStr(Date time1, long h) {
        Date nowDate = new Date(time1.getTime() + 1000 * 3600 * h);

        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String timeString = dataFormat.format(nowDate);
        return timeString;
    }

    public static String getDateTimeByMinutes(long minutes) {
        Date nowDate = new Date();
        nowDate.setTime(minutes * 1000);
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String timeString = dataFormat.format(nowDate);
        return timeString;
    }

    public static String getDateAddMonths(Date dNow, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dNow);
        calendar.add(calendar.MONTH, months);
        Date dBefore = calendar.getTime();
        String defaultStartDate = dateTimeFormat.format(dBefore);
        return defaultStartDate;
    }

    /**
     * 功能描述：返回yyyyMMddHHmmss格式化的时间字符串
     *
     * @param date
     * @return
     */
    public static String getCurrentDateTimeStr1(Date date) {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = dataFormat.format(date);
        return timeString;
    }

    public static String getCurrentDateStr() {
        Date date = new Date();
        return chineseDateFormat.format(date);
    }

    public static String getCurrentDateStr1() {
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String timeString = dataFormat.format(date);
        return timeString;
    }

    public static String getCurrentDateStr(String formatStr) {
        SimpleDateFormat dataFormat = new SimpleDateFormat(formatStr);
        Date date = new Date();
        return dataFormat.format(date);
    }


    public static String getWeekStr() {
        String s = "";
        int week = getWeek();
        switch (week) {
            case 1:
                s = "星期一";
                break;
            case 2:
                s = "星期二";
                break;
            case 3:
                s = "星期三";
                break;
            case 4:
                s = "星期四";
                break;
            case 5:
                s = "星期五";
                break;
            case 6:
                s = "星期六";
                break;
            case 7:
                s = "星期天";
                break;
            default:
                break;
        }
        return s;
    }

    /**
     * 获取当前是星期几。 <br>
     * 0为星期天、1为星期一、以此类推。
     *
     * @return String - 返回当前星期几
     */
    public static int getWeek() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int posOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        posOfWeek--; // Calendar格式转化成普通格式 0星期天， 1 星期一...
        return posOfWeek;
    }

    /**
     * @param beginTime
     * @return
     * @函数名称：addYear
     * @功能描述：
     */
    public static java.sql.Date addYear(java.util.Date beginTime, int i) {
        Calendar date = Calendar.getInstance();
        date.setTime(beginTime);
        date.add(Calendar.YEAR, i);
        return utilDateToSql(date.getTime());
    }

    /**
     * @param beginTime
     * @return
     * @函数名称：addMonth
     * @功能描述：
     */
    public static java.sql.Date addMonth(java.sql.Date beginTime, int i) {
        Calendar date = Calendar.getInstance();
        date.setTime(beginTime);
        date.add(Calendar.MONTH, i);
        return utilDateToSql(date.getTime());
    }

    /**
     * @param beginTime
     * @return
     * @函数名称：addMonth
     * @功能描述：
     */
    public static Date addMonth(Date beginTime, int i) {
        Calendar date = Calendar.getInstance();
        date.setTime(beginTime);
        date.add(Calendar.MONTH, i);
        return date.getTime();
    }

    /**
     * @param beginTime
     * @return
     * @函数名称：addDay
     * @功能描述：
     */
    public static java.sql.Date addDay(java.sql.Date beginTime, int i) {
        Calendar date = Calendar.getInstance();
        date.setTime(beginTime);
        date.add(Calendar.DAY_OF_MONTH, i);
        return utilDateToSql(date.getTime());
    }

    /**
     * @param beginTime
     * @param endTime
     * @return
     * @函数名称：compareYear
     * @功能描述：比较年
     */
    public static int compareYear(java.sql.Date beginTime, java.sql.Date endTime) {
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.setTime(beginTime);
        end.setTime(endTime);
        int compareYear = end.get(Calendar.YEAR) - begin.get(Calendar.YEAR);
        return compareYear;
    }

    /**
     * @param beginTime
     * @param endTime
     * @return
     * @函数名称：compareMonth
     * @功能描述：比较月
     */
    public static int compareMonth(java.sql.Date beginTime,
                                   java.sql.Date endTime) {
        int compareYear = compareYear(beginTime, endTime);
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        begin.setTime(beginTime);
        end.setTime(endTime);
        int compareMonth = compareYear * 12
                + (end.get(Calendar.MONTH) - begin.get(Calendar.MONTH));
        return compareMonth;
    }

    /**
     * @param beginTime
     * @param endTime
     * @return
     * @函数名称：compareDay
     * @功能描述：比较天
     */
    public static Integer compareDayCeil(Date beginTime, Date endTime) {
        long compare = (endTime.getTime() - beginTime.getTime())
                / (1000 * 3600 * 24);
        String aa = String.valueOf(compare);
        return Integer.parseInt(aa);
    }

    public static Integer compareDayCeilNew(Date beginTime, Date endTime) {
        double baseValue = 1000 * 3600 * 24;
        double compare = (endTime.getTime() - beginTime.getTime())
                / baseValue;

        String aa = String.valueOf((int) Math.ceil(compare));
        return Integer.parseInt(aa);
    }

    /**
     * @param beginTime
     * @param endTime
     * @return
     * @函数名称：compareDay
     * @功能描述：比较天
     */
    public static int compareDay(java.sql.Date beginTime, java.sql.Date endTime) {
        long compare = (endTime.getTime() - beginTime.getTime())
                / (1000 * 3600 * 24);
        String compareStr = String.valueOf(compare);
        return Integer.parseInt(compareStr);
    }

    /**
     * @param date
     * @param date2
     * @return
     * @函数名称：compareHour
     * @功能描述：比较小时
     */
    public static int compareHour(Date date, Date date2) {
        long compare = (date2.getTime() - date.getTime()) / (1000 * 3600);
        String compareStr = String.valueOf(compare);
        return Integer.parseInt(compareStr);
    }


    /**
     * @param beginTime
     * @param endTime
     * @return
     * @函数名称：compareHour
     * @功能描述：比较小时
     */
    public static int compareHourByTimestamp(String beginTime, String endTime) {
        Long beginTimestamp = Long.parseLong(beginTime);
        Long endTimestamp = Long.parseLong(endTime);
        return compareHour(new Date(beginTimestamp),
                new Date(endTimestamp));
    }

    /**
     * 获取一个月的最后一天。 <br>
     * 例如20030111,即一月份的最后一天是20030131
     *
     * @param date -指定日期(yyyyMMdd)
     * @return String - 返回计算后的日期
     */
    public static String getLastDayOfMonth(String date) {
        if (date.length() != 8)
            return "";
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(4, 6));
        int day = getDayNumOfMonth(year, month);
        String newDate = "" + year;
        if (month < 10) {
            newDate += "0" + month + day;
        } else {
            newDate += "" + month + day;
        }
        return newDate;
    }

    /**
     * 获取每月的天数
     *
     * @param month
     * @return
     */
    public static int getDayNumOfMonth(int year, int month) {
        int day = 0;
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
                || month == 10 || month == 12) {
            day = 31;
        } else if (month == 2) {
            if (((year % 4) == 0) && ((year % 100) != 0)) {
                day = 29;
            } else {
                day = 28;
            }
        } else {
            day = 30;
        }
        return day;
    }

    public static String getWeek(String sDate) {
        final String dayNames[] = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
                "星期六"};

        SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        Date date = new Date();

        try {
            date = sdfInput.parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayNames[dayOfWeek - 1];
    }


    public static boolean compareAWithB(Date a, Date b) {
        return a.before(b);
    }

    /**
     * 比较String类型的时间大小
     *
     * @param a
     * @param b
     * @return true 表示a<b
     */
    public static boolean compareString(String a, String b) {
        Date x = stringToUtilDate(a);
        Date y = stringToUtilDate(b);
        return compareAWithB(x, y);
    }

    /**
     * 比较当前时间是否超出指定时间固定的毫秒数
     *
     * @param a    String类型且yyyy-MM-dd HH:mm:ss
     * @param time 比对的秒数
     * @return
     */
    public static boolean compareWithNow(String a, String time) {
        Date x = stringToUtilDate(a);
        int interval = Integer.parseInt(time);
        Date y = new Date(System.currentTimeMillis() - interval
                * 1000);
        return compareAWithB(x, y);
    }

    /**
     * @return
     * @函数名称：getCurrentYearMonthStr
     * @功能描述：获取当前日期(只带年月) 格式 yyMM
     */
    public static String getCurrentYearMonthStr() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");
        Date date = new Date();
        String dateString = dateFormat.format(date);
        return dateString;
    }


    public static String formateDate(String engDate, String timeDiff) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy, K:mm a", Locale.US);
        String dateCH = "";
        try {
            Date d2 = sdf.parse(engDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(d2);
            ca.add(Calendar.HOUR, Integer.parseInt(timeDiff));
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateCH = dataFormat.format(ca.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateCH.equals("")) {
            sdf = new SimpleDateFormat("MMMM d, yyyy");
            try {
                Date d2 = sdf.parse(engDate);
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateCH = dataFormat.format(d2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return dateCH;
    }

    public static String formateDate2(String engDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
        String dateCH = "";
        try {
            Date d2 = sdf.parse(engDate);
            Calendar ca = Calendar.getInstance();
            ca.setTime(d2);
            ca.add(Calendar.HOUR, 0);
            SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateCH = dataFormat.format(ca.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dateCH.equals("")) {
            sdf = new SimpleDateFormat("MMMM d, yyyy");
            try {
                Date d2 = sdf.parse(engDate);
                SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateCH = dataFormat.format(d2);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateCH;
    }

    /**
     * 获取所有的时区编号. <br>
     * 排序规则:按照ASCII字符的正序进行排序. <br>
     * 排序时候忽略字符大小写.
     *
     * @return 所有的时区编号(时区编号已经按照字符[忽略大小写]排序).
     */
    public static String[] fecthAllTimeZoneIds() {
        Vector v = new Vector();
        String[] ids = TimeZone.getAvailableIDs();
        for (int i = 0; i < ids.length; i++) {
            v.add(ids[i]);
        }
        java.util.Collections.sort(v, String.CASE_INSENSITIVE_ORDER);
        v.copyInto(ids);
        v = null;
        return ids;
    }

    /**
     * 日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @param aDate    java.util.Date类的实例.
     * @return 日期转化后的字符串.
     */
    public static String date2String(String formater, Date aDate) {
        if (formater == null || "".equals(formater))
            return null;
        if (aDate == null)
            return null;
        return (new SimpleDateFormat(formater)).format(aDate);
    }

    /**
     * 当前日期(时间)转化为字符串.
     *
     * @param formater 日期或时间的格式.
     * @return 日期转化后的字符串.
     */
    public static String date2String(String formater) {
        return date2String(formater, new Date());
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcFormater   待转化的日期时间的格式.
     * @param srcDateTime   待转化的日期时间.
     * @param dstFormater   目标的日期时间的格式.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     */
    public static String string2Timezone(String srcFormater,
                                         String srcDateTime, String dstFormater, String dstTimeZoneId) {
        if (srcFormater == null || "".equals(srcFormater))
            return null;
        if (srcDateTime == null || "".equals(srcDateTime))
            return null;
        if (dstFormater == null || "".equals(dstFormater))
            return null;
        if (dstTimeZoneId == null || "".equals(dstTimeZoneId))
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(srcFormater);
        try {
            int diffTime = getDiffTimeZoneRawOffset(dstTimeZoneId);
            Date d = sdf.parse(srcDateTime);
            long nowTime = d.getTime();
            long newNowTime = nowTime - diffTime;
            d = new Date(newNowTime);
            return date2String(dstFormater, d);
        } catch (ParseException e) {
            return null;
        } finally {
            sdf = null;
        }
    }

    /**
     * 获取系统当前默认时区与UTC的时间差.(单位:毫秒)
     *
     * @return 系统当前默认时区与UTC的时间差.(单位 : 毫秒)
     */
    private static int getDefaultTimeZoneRawOffset() {
        return TimeZone.getDefault().getRawOffset();
    }

    /**
     * 获取指定时区与UTC的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 指定时区与UTC的时间差.(单位 : 毫秒)
     */
    private static int getTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 获取系统当前默认时区与指定时区的时间差.(单位:毫秒)
     *
     * @param timeZoneId 时区Id
     * @return 系统当前默认时区与指定时区的时间差.(单位 : 毫秒)
     */
    private static int getDiffTimeZoneRawOffset(String timeZoneId) {
        return TimeZone.getDefault().getRawOffset()
                - TimeZone.getTimeZone(timeZoneId).getRawOffset();
    }

    /**
     * 将日期时间字符串根据转换为指定时区的日期时间.
     *
     * @param srcDateTime   待转化的日期时间.
     * @param dstTimeZoneId 目标的时区编号.
     * @return 转化后的日期时间.
     * @see #string2Timezone(String, String, String, String)
     */
    public static String string2TimezoneDefault(String srcDateTime,
                                                String dstTimeZoneId) {
        return string2Timezone("yyyy-MM-dd HH:mm:ss", srcDateTime,
                "yyyy-MM-dd HH:mm:ss", dstTimeZoneId);
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static String getSpecifiedDayBefore(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = chineseDateFormat.parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);

        return chineseDateFormat.format(c.getTime());
    }

    /**
     * 查询给定时间和当前时间的间隔小时数是否大于24小时
     *
     * @param time1
     * @return
     */
    public static String ifDiff24Hours(Date time1) {
        double hours = ((double) time1.getTime() - new Date().getTime()) / (1000 * 60 * 60);
        if (hours > 12.0) {
            return "Y";
        }
        return "N";
    }

    public static long ifDiffSecond(Date time1, Date nowDate) {
        long minu = time1.getTime() - nowDate.getTime();
        return minu / 1000;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     */
    public static String getSpecifiedDayAfter(String specifiedDay) {
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = chineseDateFormat.parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);

        return chineseDateFormat.format(c.getTime());
    }

    public static String after24Hours(Date time1) {
        Date time2 = new Date(time1.getTime() + 1000 * 60 * 60 * 12);
        return dateTimeFormat.format(time2);
    }

    public static void main(String[] args) {
//      System.out.println(compareDayCeilNew(new Date(), stringToUtilDate("2019-05-23 21:00:35")));
//    	String[] ids = fecthAllTimeZoneIds();
//    	String nowDateTime =formateDate("January 20, 2017, 2:00 PM EST","-13");
//    	System.out.println(nowDateTime);
//    	StatsDClient statsd = new NonBlockingStatsDClient("my.prefix", "175.25.16.181", 8125);
//    	statsd.incrementCounter("bar");
//    	statsd.recordGaugeValue("baz", 100);
//    	statsd.recordExecutionTime("bag", 25);
//    	statsd.recordSetEvent("qux", "two");
//    	System.out.println("ssss==="+compareDayCeil(stringToUtilDate("2017-06-04 21:00:35"),new Date()));
//      System.out.println(System.currentTimeMillis() + "ssss===" + getFormatDateTime(new Date(1508831099234l)));
    }

}

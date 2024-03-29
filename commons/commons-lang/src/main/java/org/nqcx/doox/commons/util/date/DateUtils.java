/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.date;

import org.nqcx.doox.commons.lang.enums.TimeUnitEO;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.nqcx.doox.commons.util.date.DateFormatUtils.DATE_FORMATS;
import static org.nqcx.doox.commons.util.date.DateFormatUtils.TIME;

/**
 * @author naqichuan 2014年8月14日 上午11:48:35
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * <p>
     * Returns current date
     * </p>
     *
     * @return java Date
     */
    public static Date date() {
        return new Date();
    }

    /**
     * 当前日期 增加/减少 amount 天后的日期
     *
     * @param amount 增加/减少 天数
     * @return java Date
     */
    public static Date date(int amount) {
        return addDays(date(), amount);
    }

    /**
     * atStartOfDay
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-12 16:20
     */
    public static Date atStartOfDay(Date date) {
        if (date == null)
            return null;
        return Date.from(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
                .atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 一天中的第一时间（精确到秒）
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-12 16:00
     */
    public static Date dayBegin(Date date) {
        try {
            if (date != null)
                return TIME.parse(DateFormatUtils.DAY_BEGIN.format(date));
        } catch (ParseException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 一天的结束时间（精确到秒）
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-12 16:00
     */
    public static Date dayEnd(Date date) {
        try {
            if (date != null)
                return TIME.parse(DateFormatUtils.DAY_END.format(date));
        } catch (ParseException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * 字符串转日期
     *
     * @param str str
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:41
     */
    public static Date parseDate(String str) {
        try {
            if (str != null)
                return parseDate(str, DATE_FORMATS);
        } catch (ParseException e) {
            logger.warn("'{}' can not convert to type 'java.util.Date',just support timestamp(type of long) and following date format({})",
                    str,
                    StringUtils.join(DATE_FORMATS, ","));
        }
        return null;
    }

    /**
     * 字符串转日期指定格式日期
     *
     * @param str     str
     * @param pattern pattern
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:41
     */
    public static Date parseDate(String str, String pattern) {
        try {
            if (str != null && pattern != null)
                return parseDate(str, new String[]{pattern});
        } catch (ParseException e) {
            logger.warn("'{}' can not convert to type 'java.util.Date',just support timestamp(type of long) and following date format({})",
                    str,
                    pattern);
        }
        return null;
    }

    /**
     * atStartOfYear
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-12 17:20
     */
    public static Date atStartOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);

        return atStartOfDay(cal.getTime());
    }

    /**
     * 当前月份最后一天
     *
     * @return String
     */
    public static String lastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date());
        return lastDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }

    /**
     * 取得月份最后一天
     *
     * @param year
     * @param month
     * @return MM-dd
     */
    public static String lastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return lastDayOfMonth(cal);
    }

    /**
     * 取得月份最后一天
     *
     * @param calendar calendar
     * @return MM-dd
     */
    private static String lastDayOfMonth(Calendar calendar) {
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar
                .getActualMaximum(Calendar.DAY_OF_MONTH));
        return (month.length() == 1 ? "0" + month : month) + "-"
                + (day.length() == 1 ? "0" + day : day);
    }

    /**
     * 获取月份最后一天 (YYYY-MM-dd)
     *
     * @return YYYY-MM-dd
     */
    @Deprecated
    public static String lastDayAndYearOfMonth() {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = String.valueOf(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        return (year + "-" + (month.length() == 1 ? "0" + month : month) + "-"
                + (day.length() == 1 ? "0" + day : day));
    }

    /**
     * 获取日期对就月分第一天
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:36
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        return cal.getTime();
    }

    /**
     * 获取当前月份第一天(YYYY-MM-dd)
     *
     * @return
     */
    @Deprecated
    public static String firstDayAndYearOfMonth() {
        Calendar cal = Calendar.getInstance();
        String year = String.valueOf(cal.get(Calendar.YEAR));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String day = String.valueOf(cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return (year + "-" + (month.length() == 1 ? "0" + month : month) + "-"
                + (day.length() == 1 ? "0" + day : day));
    }

    /**
     * 取得当前月份第一天
     *
     * @return MM-dd
     */
    @Deprecated
    public static String firstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date());
        return firstDayOfMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH));
    }

    /**
     * 取得月份第一天
     *
     * @param year  year
     * @param month month
     * @return MM-dd
     */
    public static String firstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        return firstDayOfMonth(cal);
    }

    /**
     * 取得月份第一天（MM-dd）
     *
     * @param calendar calendar
     * @return MM-dd
     */
    private static String firstDayOfMonth(Calendar calendar) {
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar
                .getActualMinimum(Calendar.DAY_OF_MONTH));
        return (month.length() == 1 ? "0" + month : month) + "-"
                + (day.length() == 1 ? "0" + day : day);
    }

    /**
     * 取得日期所在周数
     *
     * @param date date
     * @return 格式 "2011-52"
     */
    public static String weekOfYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setMinimalDaysInFirstWeek(7);
        cal.setTime(date);
        String week = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

        return cal.get(Calendar.YEAR) + "-"
                + (week.length() == 1 ? "0" + week : week);
    }

    /**
     * 取得指定周的第一天
     *
     * @param yearWeek yearWeek
     * @return Date
     */
    public static Date firstDayOfWeek(String yearWeek) {
        String[] yws;
        if (yearWeek == null || (yws = yearWeek.split("-")).length != 2)
            return null;

        int year = Integer.parseInt(yws[0]);
        int week = Integer.parseInt(yws[1]);

        return firstDayOfWeek(year, week);
    }

    /**
     * firstDayOfWeek
     *
     * @param year year
     * @param week week
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:40
     */
    private static Date firstDayOfWeek(int year, int week) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DATE, 1);

        Calendar cal1 = (Calendar) cal.clone();
        cal1.add(Calendar.DATE, week * 7);

        return firstDayOfWeek(cal1.getTime());
    }

    /**
     * 取得指定日期所在周的第一天
     *
     * @param date date
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:40
     */
    public static Date firstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 取得指定周的最后一天
     *
     * @param yearWeek yearWeek
     * @return {@link Date}
     * @author naqichuan 2021-03-26 09:41
     */
    public static Date lastDayOfWeek(String yearWeek) {
        return addDays(firstDayOfWeek(yearWeek), 6);
    }

    /**
     * 取得指定周的最后一天
     *
     * @param year year
     * @param week week
     * @return Date
     */
    public static Date lastDayOfWeek(int year, int week) {
        return addDays(firstDayOfWeek(year, week), 6);
    }

    /**
     * 取得指定日期所在周的最后一天
     *
     * @param date date
     * @return Date
     */
    public static Date lastDayOfWeek(Date date) {
        return addDays(firstDayOfWeek(date), 6);
    }

    /**
     * 根据时间单位和给定时间，取得给定时间在时间单位内的起止时间，[startAtDate, beforeThisDate)，左闭区间，右开区间
     *
     * @param timeUnit timeUnit
     * @param date     date
     * @return {@link Date[]} [startAtDate, beforeThisDate)
     * @author naqichuan 2021-03-26 10:12
     */
    public static Date[] getDatePeriod(TimeUnitEO timeUnit, Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null)
            cal.setTime(date);

        // before this date
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date beforeThisDate = atStartOfDay(cal.getTime()); // 日期右开区间
        // start at this date
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date startAtDate = atStartOfDay(cal.getTime()); // 日期左闭区间

        if (TimeUnitEO.WEEK.is(timeUnit)) {
            cal.add(Calendar.DAY_OF_MONTH, 7);
            beforeThisDate = atStartOfDay(firstDayOfWeek(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH, -7);
            startAtDate = atStartOfDay(firstDayOfWeek(cal.getTime()));
        } else if (TimeUnitEO.MONTH.is(timeUnit)) {
            cal.add(Calendar.MONTH, 1);
            beforeThisDate = atStartOfDay(firstDayOfMonth(cal.getTime()));
            cal.add(Calendar.MONTH, -1);
            startAtDate = atStartOfDay(firstDayOfMonth(cal.getTime()));
        } else if (TimeUnitEO.YEAR.is(timeUnit)) {
            cal.add(Calendar.YEAR, 1);
            beforeThisDate = atStartOfYear(cal.getTime());
            cal.add(Calendar.YEAR, -1);
            startAtDate = atStartOfDay(cal.getTime());
        }

        return new Date[]{startAtDate, beforeThisDate};
    }


    public static void main(String[] args) {
//        System.out.println(firstDayOfWeek("2012-10"));
//        System.out.println(lastDayOfWeek("2012-10"));
//
//        System.out.println(firstDayOfMonth(2012, 2));
//        System.out.println(lastDayOfMonth(2012, 2));
//        System.out.println(lastDayAndYearOfMonth());
//        System.out.println(firstDayAndYearOfMonth());
//
//        System.out.println(atStartOfDay(date()));
//        System.out.println(atStartOfYear(date()));
//
//        Calendar currentCal = Calendar.getInstance();
//        currentCal.add(Calendar.DAY_OF_MONTH, -100);
//        for (int i = 0; i < 100; i++) {
//            currentCal.add(Calendar.DAY_OF_MONTH, 1);
//            Date currentDate = currentCal.getTime();
//            // ######################################
//
//            Integer archiveYear = 2020; // 存档数据所在年份，为空时默认取当前日期减一个月的前一天对应日期的年份
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(currentDate);
//            System.out.print("Current Date: [" + TIME.format(currentDate) + "]");
//            System.out.print(", Current year: " + cal.get(Calendar.YEAR));
//
//            // exec year
//            cal.add(Calendar.MONTH, -1);
//            int execYear = cal.get(Calendar.YEAR); // 执行存档年份，默认取当前日期减一个月的当天对应日期
//            //
//            System.out.print(", Exec year: " + execYear);
//
//            // archive before this date
//            Date archiveBeforeThisDate = atStartOfDay(cal.getTime()); // 存档数据右开区间
//            // archive year
//            cal.add(Calendar.DAY_OF_MONTH, -1);
//            if (archiveYear == null || archiveYear == 0)
//                archiveYear = cal.get(Calendar.YEAR);
//            //
//            System.out.print(", Archive year: " + archiveYear);
//            //
//            if (archiveYear > execYear) {
//                //
//                System.out.println();
//                return;
//            }
//            // archive start at date
//            Date archiveStartAtDate = atStartOfYear(cal.getTime()); // 存档数据左闭区间
//            // delete start at date
//            Calendar delCal = Calendar.getInstance();
//            delCal.setTime(archiveBeforeThisDate);
//            delCal.add(Calendar.MONTH, -1);
//            int delYear = delCal.get(Calendar.YEAR);
//            //
//            System.out.print(", Del year: " + delYear);
//            Date deleteStartAtDate = null;
//            Date deleteBeforeThisDate = null;
//            if (delYear == archiveYear) {
//                // delete before this date
//                deleteStartAtDate = atStartOfYear(delCal.getTime());
//                deleteBeforeThisDate = delCal.getTime();
//            }
//
//            if (archiveYear < execYear) {
//                // 执行存档数据所在年份和执行存档年份不想同时，需要重新设置"存档数据右开区间"
//                Calendar cal1 = Calendar.getInstance();
//                cal1.set(Calendar.YEAR, archiveYear);
//                cal1.add(Calendar.YEAR, 1);
//                archiveBeforeThisDate = atStartOfYear(cal1.getTime());
//
//                cal1 = Calendar.getInstance();
//                cal1.setTime(archiveBeforeThisDate);
//                cal1.add(Calendar.DAY_OF_MONTH, -1);
//                archiveStartAtDate = atStartOfYear(cal1.getTime());
//
//                // delete before this date
//                delCal = Calendar.getInstance();
//                delCal.setTime(archiveBeforeThisDate);
//                delCal.add(Calendar.MONTH, -1);
//                deleteStartAtDate = atStartOfYear(delCal.getTime());
//                deleteBeforeThisDate = delCal.getTime();
//            }
//            //
//            //
//            System.out.print(", Archive in: [" + TIME.format(archiveStartAtDate) + ", " + TIME.format(archiveBeforeThisDate) + ")");
//            if (deleteStartAtDate != null && deleteBeforeThisDate != null)
//                System.out.print(", Del in: [" + TIME.format(deleteStartAtDate) + ", " + TIME.format(deleteBeforeThisDate) + ")");
//
//            System.out.println();
//
//        }

//        System.out.println(atStartOfDay(firstDayOfMonth(date(-30))));
//        System.out.println(atStartOfDay(firstDayOfWeek(date())));

        System.out.println(DateFormatUtils.DATETIME.format(date()));
        System.out.println(DateFormatUtils.DATETIME_ZZ.format(date()));
        System.out.println(DateFormatUtils.DATETIME_S.format(date()));
        System.out.println(DateFormatUtils.DATETIME_S_ZZ.format(date()));
        System.out.println(DateFormatUtils.DATETIME_SSS.format(date()));
        System.out.println(DateFormatUtils.DATETIME_SSS_ZZ.format(date()));
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.date;

import org.apache.commons.lang3.time.FastDateFormat;

/**
 * @author naqichuan 2014年8月14日 上午11:48:25
 */
public class DateFormatUtils extends org.apache.commons.lang3.time.DateFormatUtils {

    public static final String YEAR_FORMAT = "yyyy";
    public static final String MONTH_FORMAT = "yyyy-MM";
    public static final String MONTHONLY_FORMAT = "MM";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String MONTH_DAY_FORMAT = "MM-dd";
    public static final String DAY_FORMAT = "dd";
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIMEONLY_FORMAT = "HH:mm:ss";

    public static final String DAY_BEGIN_FORMAT = "yyyy-MM-dd 00:00:00";
    public static final String DAY_END_FORMAT = "yyyy-MM-dd 23:59:59";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String DATETIME_ZZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZZ";
    public static final String DATETIME_S_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.S";
    public static final String DATETIME_S_ZZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SZZ";
    public static final String DATETIME_SSS_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String DATETIME_SSS_ZZ_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

    public final static String[] DATE_FORMATS =
            new String[]{
                    DATETIME_FORMAT,
                    DATETIME_ZZ_FORMAT,
                    DATETIME_S_FORMAT,
                    DATETIME_S_ZZ_FORMAT,
                    DATETIME_SSS_FORMAT,
                    DATETIME_SSS_ZZ_FORMAT,
                    DATE_FORMAT,
                    "yyyy-MM-dd HH:mm",
                    TIME_FORMAT,
                    TIME_FORMAT + ".S",
                    "yyyy.MM.dd",
                    "yyyy.MM.dd HH:mm",
                    "yyyy.MM.dd HH:mm:ss",
                    "yyyy.MM.dd HH:mm:ss.S",
                    "yyyy/MM/dd",
                    "yyyy/MM/dd HH:mm",
                    "yyyy/MM/dd HH:mm:ss",
                    "yyyy/MM/dd HH:mm:ss.S"};

    public static final FastDateFormat DATETIME= FastDateFormat.getInstance(DATETIME_FORMAT);
    public static final FastDateFormat DATETIME_ZZ = FastDateFormat.getInstance(DATETIME_ZZ_FORMAT);
    public static final FastDateFormat DATETIME_SSS = FastDateFormat.getInstance(DATETIME_SSS_FORMAT);
    public static final FastDateFormat DATETIME_SSS_ZZ = FastDateFormat.getInstance(DATETIME_SSS_ZZ_FORMAT);
    public static final FastDateFormat DATETIME_S = FastDateFormat.getInstance(DATETIME_S_FORMAT);
    public static final FastDateFormat DATETIME_S_ZZ = FastDateFormat.getInstance(DATETIME_S_ZZ_FORMAT);

    /**
     * Nqcx formatter for year without time zone. The format used is <tt>yyyy</tt>.
     */
    public static final FastDateFormat YEAR = FastDateFormat.getInstance(YEAR_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_YEAR_FORMAT = YEAR;

    /**
     * Nqcx formatter for year-moth without time zone. The format used is <tt>yyyy-MM</tt>.
     */
    public static final FastDateFormat MONTH = FastDateFormat.getInstance(MONTH_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_MONTH_FORMAT = MONTH;

    /**
     * Nqcx formatter for moth only without time zone. The format used is <tt>MM</tt>.
     */
    public static final FastDateFormat MONTHONLY = FastDateFormat.getInstance(MONTHONLY_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_MONTHONLY_FORMAT = MONTHONLY;
    /**
     * Nqcx formatter for date without time zone. The format used is <tt>yyyy-MM-dd</tt>.
     */
    public static final FastDateFormat DATE = FastDateFormat.getInstance(DATE_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_DATE_FORMAT = DATE;
    /**
     * Nqcx formatter for date without time zone. The format used is <tt>MM-dd</tt>.
     */
    public static final FastDateFormat MONTH_DAY = FastDateFormat.getInstance(MONTH_DAY_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_MONTH_DAY_FORMAT = MONTH_DAY;

    /**
     * Nqcx formatter for day without time zone. The format used is <tt>dd</tt>.
     */
    public static final FastDateFormat DAY = FastDateFormat.getInstance(DAY_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_DAY_FORMAT = DAY;

    /**
     * Nqcx formatter for time without time zone. The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>.
     */
    public static final FastDateFormat TIME = FastDateFormat.getInstance(TIME_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_TIME_FORMAT = TIME;

    /**
     * Nqcx formatter for time only without time zone. The format used is <tt>HH:mm:ss</tt>.
     */
    public static final FastDateFormat TIMEONLY = FastDateFormat.getInstance(TIMEONLY_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_TIMEONLY_FORMAT = TIMEONLY;


    public static final FastDateFormat DAY_BEGIN = FastDateFormat.getInstance(DAY_BEGIN_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_DAY_BEGIN_FORMAT = DAY_BEGIN;

    public static final FastDateFormat DAY_END = FastDateFormat.getInstance(DAY_END_FORMAT);
    @Deprecated
    public static final FastDateFormat NQCX_DAY_END_FORMAT = DAY_END;
}

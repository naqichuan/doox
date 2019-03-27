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

    /**
     * Nqcx formatter for year without time zone. The format used is <tt>yyyy</tt>.
     */
    public static final FastDateFormat NQCX_YEAR_FORMAT = FastDateFormat.getInstance(YEAR_FORMAT);

    /**
     * Nqcx formatter for year-moth without time zone. The format used is <tt>yyyy-MM</tt>.
     */
    public static final FastDateFormat NQCX_MONTH_FORMAT = FastDateFormat.getInstance(MONTH_FORMAT);

    /**
     * Nqcx formatter for moth only without time zone. The format used is <tt>MM</tt>.
     */
    public static final FastDateFormat NQCX_MONTHONLY_FORMAT = FastDateFormat.getInstance(MONTHONLY_FORMAT);
    /**
     * Nqcx formatter for date without time zone. The format used is <tt>yyyy-MM-dd</tt>.
     */
    public static final FastDateFormat NQCX_DATE_FORMAT = FastDateFormat.getInstance(DATE_FORMAT);
    /**
     * Nqcx formatter for date without time zone. The format used is <tt>MM-dd</tt>.
     */
    public static final FastDateFormat NQCX_MONTH_DAY_FORMAT = FastDateFormat.getInstance(MONTH_DAY_FORMAT);

    /**
     * Nqcx formatter for day without time zone. The format used is <tt>dd</tt>.
     */
    public static final FastDateFormat NQCX_DAT_FORMAT = FastDateFormat.getInstance(DAY_FORMAT);

    /**
     * Nqcx formatter for time without time zone. The format used is <tt>yyyy-MM-dd HH:mm:ss</tt>.
     */
    public static final FastDateFormat NQCX_TIME_FORMAT = FastDateFormat.getInstance(TIME_FORMAT);

    /**
     * Nqcx formatter for time only without time zone. The format used is <tt>HH:mm:ss</tt>.
     */
    public static final FastDateFormat NQCX_TIMEONLY_FORMAT = FastDateFormat.getInstance(TIMEONLY_FORMAT);


    public static final FastDateFormat NQCX_DAY_BEGIN_FORMAT = FastDateFormat.getInstance(DAY_BEGIN_FORMAT);
    public static final FastDateFormat NQCX_DAY_END_FORMAT = FastDateFormat.getInstance(DAY_END_FORMAT);
}

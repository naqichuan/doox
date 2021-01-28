/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.consts;

/**
 * 时间段常量，以秒为单位
 *
 * @author naqichuan 17/1/10 08:45
 */
public class PeriodConst {

    public final static int THOUSAND = 1000; // 一千, for millisecond

    public final static int ONE_MINUTES = 60; // Unit second
    public final static int TEN_MINUTES = 10 * ONE_MINUTES; // Unit second
    public final static int THIRTY_MINUTES = 30 * ONE_MINUTES; // Unit second
    public final static int ONE_HOUR = 60 * ONE_MINUTES; // Unit second
    public final static int ONE_DAY = 24 * ONE_HOUR; // Unit second
    public final static int ONE_WEEK = 7 * ONE_DAY; // Unit second
}

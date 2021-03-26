/*
 * Copyright 2021 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.enums;

/**
 * @author naqichuan 2021-03-26 10:08
 */
public enum TimeUnitEO {

    DAY(0, "天"), MONTH(1, "月"), YEAR(2, "年"), WEEK(3, "周");

    private int value;
    private String text;

    TimeUnitEO(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    /**
     * 判断自身是否与参数里的枚举相等
     *
     * @param eo eo
     * @return boolean
     */
    public boolean is(TimeUnitEO eo) {
        return this == eo;
    }

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(TimeUnitEO[] eos) {
        if (eos == null || eos.length == 0)
            return false;

        for (TimeUnitEO n : eos) {
            if (n == this)
                return true;
        }

        return false;
    }

    /**
     * 通过 value 取得枚举实例
     *
     * @param value value
     * @return TimeUnitEO
     */
    public static TimeUnitEO of(int value) {
        for (TimeUnitEO e : TimeUnitEO.values()) {
            if (e.getValue() == value)
                return e;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}

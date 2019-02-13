/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.enums;

/**
 * Bool enum object
 *
 * @author naqichuan 2014年8月14日 上午11:05:32
 */
public enum BoolEO {

    FALSE(0, false, "否"), TRUE(1, true, "是");

    private int value;
    private boolean bool;
    private String text;

    BoolEO(int value, boolean bool, String text) {
        this.value = value;
        this.bool = bool;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public boolean isTrue() {
        return bool;
    }

    /**
     * 判断自身是否与参数里的枚举相等
     *
     * @param boolEO BoolEO
     * @return boolean
     */
    public boolean is(BoolEO boolEO) {
        return this == boolEO;
    }

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(BoolEO[] eos) {
        return EOUtils.contain(eos, this);
    }

    /**
     * 通过 value 取得 eo
     *
     * @param value value
     * @return eo
     */
    public static BoolEO of(int value) {
        for (BoolEO p : BoolEO.values()) {
            if (p.getValue() == value)
                return p;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}

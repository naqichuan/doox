/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.enums;

/**
 * Gender enum object
 *
 * @author naqichuan 14-10-11 9:23
 */
public enum GenderEO {

    NONE(0, ""), MALE(1, "男"), FEMALE(2, "女");

    private int value;
    private String text;

    GenderEO(int value, String text) {
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
    public boolean is(GenderEO eo) {
        return this == eo;
    }


    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(GenderEO[] eos) {
        return EOUtils.contain(eos, this);
    }

    /**
     * @param value value
     * @return boolean
     */
    public static GenderEO of(int value) {
        for (GenderEO p : GenderEO.values()) {
            if (p.getValue() == value)
                return p;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.enums;

/**
 * @author naqichuan 17/4/4 13:48
 */
public enum StatusEO {

    NONE(0, "无"), AVAILABLE(1, "可用"), DISABLE(2, "不可用"), DELETED(3, "已删除");

    private int value;
    private String text;

    StatusEO(int value, String text) {
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
    public boolean is(StatusEO eo) {
        return this == eo;
    }

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(StatusEO[] eos) {
        return EOUtils.contain(eos, this);
    }

    /**
     * 通过 value 取得枚举实例
     *
     * @param value
     * @return
     */
    public static StatusEO of(int value) {
        for (StatusEO p : StatusEO.values()) {
            if (p.getValue() == value)
                return p;
        }
        throw new IllegalArgumentException("unknown value:" + value);
    }
}

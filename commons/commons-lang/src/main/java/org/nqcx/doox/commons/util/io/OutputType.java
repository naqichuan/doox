/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

/**
 * @author naqichuan 14/12/3 10:45
 */
public enum OutputType {

    BYTE, CHAR;

    /**
     * 判断自身是否与参数里的枚举相等
     *
     * @param eo eo
     * @return boolean
     */
    public boolean is(OutputType eo) {
        return this == eo;
    }

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(OutputType[] eos) {
        if (eos == null || eos.length == 0)
            return false;

        for (OutputType n : eos) {
            if (n == this)
                return true;
        }

        return false;
    }
}

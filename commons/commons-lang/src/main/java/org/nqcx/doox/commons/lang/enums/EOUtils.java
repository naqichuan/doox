/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.enums;

/**
 * Enum object utils
 *
 * @author naqichuan 15/11/2 10:58
 */
public class EOUtils {

    /**
     * 判断指定的 eo 是否在 eos 数组中
     *
     * @param eos
     * @param eo
     * @param <T>
     * @return
     */
    public static <T> boolean contain(T[] eos, T eo) {
        if (eos == null || eos.length == 0 || eo == null)
            return false;

        for (T n : eos) {
            if (n != null && n == eo)
                return true;
        }

        return false;
    }
}

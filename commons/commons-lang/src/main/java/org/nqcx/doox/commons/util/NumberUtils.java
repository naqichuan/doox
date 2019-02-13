/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util;

/**
 * @author naqichuan 2014年9月9日 下午12:46:12
 */
public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

    public static void main(String[] args) {
        String testString = "12";

        System.out.println(NumberUtils.isCreatable(testString));
    }
}

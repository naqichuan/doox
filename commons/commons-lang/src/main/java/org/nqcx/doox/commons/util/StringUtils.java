/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util;

import java.util.Random;

/**
 * @author naqichuan 2014年8月14日 上午11:47:53
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 生成随机字符和数字组合串
     *
     * @param length
     * @return
     */
    public static String randomCharAndNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            if ("char".equalsIgnoreCase(charOrNum)) {
                int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char) (choice + random.nextInt(26));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }

    /**
     * 生成指定位随机数字的字符串
     *
     * @param length
     * @return
     */
    public static String randomNum(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        int val = 0;
        Random rand = new Random();

        int tmp = Math.abs(rand.nextInt());
        val = tmp % (max - min + 1) + min;

        return String.valueOf(val);
    }

}

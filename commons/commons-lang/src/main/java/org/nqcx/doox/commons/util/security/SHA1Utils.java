/*
 * Copyright 2020 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.security;

import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author naqichuan 2020-12-02 12:31
 */
public class SHA1Utils {

    /**
     * SHA1签名生成
     *
     * @param text text
     * @return string
     */
    public static String sha1(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(text.getBytes());
            byte[] digest = md.digest();

            StringBuilder hexstr = new StringBuilder();
            String shaHex;
            for (byte b : digest) {
                shaHex = Integer.toHexString(b & 0xFF);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }
                hexstr.append(shaHex);
            }
            return hexstr.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String[] array = new String[]{"JDWeWAfaEDt4kbuihyJH1rigiNad3CgE", "1682494825", "1606882916"};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (String s : array) {
            sb.append(s);
        }

        System.out.println(sha1(sb.toString()));
    }
}

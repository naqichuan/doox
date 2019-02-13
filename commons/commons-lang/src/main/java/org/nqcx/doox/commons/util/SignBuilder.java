/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util;

import org.nqcx.doox.commons.util.security.MD5Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

public class SignBuilder {

    private final static Logger LOGGER = LoggerFactory.getLogger(SignBuilder.class);
    private final static String CHARESET = "UTF-8";

    /**
     * 根据数组取签名
     *
     * @param params
     * @param key
     * @param charset
     * @return
     * @author 黄保光 Oct 12, 2013 11:17:21 AM
     */
    public static String buildSign(Map<String, Object> params, String key, String charset) {
        if (charset == null || charset.length() == 0)
            charset = CHARESET;
        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串再与安全校验码直接连接起来，最后取签名
        try {
            return MD5Utils.md5Hex((createLinkString(params) + key).getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn(e.getMessage());
        }

        return null;
    }


    /**
     * 根据字符串取签名
     *
     * @param text
     * @param key
     * @param charset
     * @return
     */
    public static String buildSign(String text, String key, String charset) {
        if (charset == null || charset.length() == 0)
            charset = CHARESET;
        // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串再与安全校验码直接连接起来，最后取签名
        try {
            if (text != null)
                return MD5Utils.md5Hex((text + key).getBytes(charset));
        } catch (UnsupportedEncodingException e) {
            LOGGER.warn(e.getMessage());
        }

        return null;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     * @return
     * @author 黄保光 Oct 12, 2013 11:11:46 AM
     */
    public static String createLinkString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuffer sb = new StringBuffer();

        for (String key : keys) {
            if (key.equals("") || key == null
                    || key.equalsIgnoreCase("signature") || key.equalsIgnoreCase("_signature"))
                continue;

            if (sb.length() > 0) // 拼接时，不包括最后一个&字符
                sb.append("&");

            sb.append(key);
            sb.append("=");

            Object param = params.get(key);
            if (param instanceof String[])
                sb.append(arrayToString((String[]) param));
            else if (Object[].class.isAssignableFrom(param.getClass())) {
                Object[] objectArray = (Object[]) param;
                String[] objectString = new String[objectArray.length];
                for (int i = 0; i < objectArray.length; i++) {
                    objectString[i] = String.valueOf(objectArray[i]);
                }
                sb.append(arrayToString(objectString));
            } else {
                sb.append(null2blank(param == null ? "" : param.toString()));
            }
        }

        return sb.toString();
    }

    /**
     * 字符串转成数组
     *
     * @param input
     * @return
     * @author 黄保光 Oct 28, 2013 5:30:18 PM
     */
    private static String arrayToString(String[] input) {
        StringBuffer sb = new StringBuffer();
        Arrays.sort(input);
        for (int i = 0; i < input.length; i++) {
            if (sb.length() > 0)
                sb.append(",");
            sb.append(null2blank(input[i]));
        }
        return sb.toString();
    }

    /**
     * @param input
     * @return
     * @author 黄保光 Oct 12, 2013 4:57:00 PM
     */
    private static String null2blank(String input) {
        return input == null ? "" : input;
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("identity", "user1identity");
        map.put("apiName", "myTestApi2");
        // map.put("pa", "1");
        // map.put("pb", "2");
        map.put("abcd", new Integer[]{1, 3, 4});

        System.out.println(SignBuilder.buildSign(map, "user1key", "GBK"));
    }
}

/*
 * Copyright 2015 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.http;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author 黄保光 Nov 4, 2013 2:06:52 PM
 */
public class HttpMap {

    private final static Logger logger = LoggerFactory.getLogger(HttpMap.class);

    private Map<String, Object> params = new LinkedHashMap<>();

    private HttpMap() {

    }

    public static HttpMap newInstance() {
        return new HttpMap();
    }

    /**
     * 返回一个新的 map 对象
     *
     * @return
     * @author 黄保光 Nov 5, 2013 4:21:21 PM
     */
    public Map<String, Object> getMap() {
        return params;
    }

    /**
     * @param key
     * @param value
     * @return
     * @author 黄保光 Nov 5, 2013 3:15:29 PM
     */
    public HttpMap add(final String key, final Object value) {
        Object newValue;
        if (params.containsKey(key)) {
            Object[] original;
            if (params.get(key) != null
                    && Object[].class.isAssignableFrom(params.get(key)
                    .getClass()))
                original = (Object[]) params.get(key);
            else
                original = new Object[]{params.get(key)};

            Object[] newArray;
            if (value != null
                    && Object[].class.isAssignableFrom(value.getClass())) {
                newArray = new Object[original.length
                        + ((Object[]) value).length];
                System.arraycopy(newArray, 0, original, 0, original.length);
                System.arraycopy(newArray, original.length, value, 0,
                        ((Object[]) value).length);
            } else {
                newArray = new Object[original.length + 1];
                System.arraycopy(original, 0, newArray, 0, newArray.length - 1);
                newArray[newArray.length - 1] = value;
            }
            newValue = newArray;
        } else {
            newValue = value;
        }
        params.put(key, newValue);
        return this;
    }

    public HttpMap add(Map<String, Object> values) {
        for (Entry<String, Object> entry : values.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public HttpMap put(final String key, final Object value) {
        params.put(key, value);
        return this;
    }

    public HttpMap put(Map<String, Object> values) {
        params.putAll(values);
        return this;
    }

    /**
     * 构建 为 list
     *
     * @return
     * @author 黄保光 Nov 5, 2013 2:35:54 PM
     */
    public List<NameValuePair> buildList() {
        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                if (entry == null || entry.getValue() == null)
                    continue;

                if (Object[].class
                        .isAssignableFrom(entry.getValue().getClass())) {
                    Object[] array = (Object[]) entry.getValue();
                    for (Object value : array) {
                        nvps.add(new BasicNameValuePair(entry.getKey(),
                                null2blank(value)));
                    }
                } else {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry
                            .getValue().toString()));
                }
            }
        }
        return nvps;
    }

    /**
     * 返回一个 & 分隔的字符串，格式为 key1=value1&key2=value2
     *
     * @return
     * @author 黄保光 Nov 4, 2013 2:51:08 PM
     */
    public String buildString() {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                if (entry == null || entry.getValue() == null)
                    continue;

                if (Object[].class
                        .isAssignableFrom(entry.getValue().getClass())) {
                    Object[] array = (Object[]) entry.getValue();
                    for (Object value : array) {
                        if (sb.length() > 0)
                            sb.append("&");
                        sb.append(entry.getKey()).append("=").append(encode(null2blank(value), HttpConfig.CHARESET));
                    }
                } else {
                    if (sb.length() > 0)
                        sb.append("&");
                    sb.append(entry.getKey()).append("=").append(encode(null2blank(entry.getValue()), HttpConfig.CHARESET));
                }
            }
        }
        return sb.toString();
    }

    /**
     * @param input
     * @return
     * @author 黄保光 Nov 5, 2013 2:33:50 PM
     */
    private String null2blank(Object input) {
        return input == null ? "" : input.toString();
    }

    /**
     * 对指定字符串进行 url encode
     *
     * @param input
     * @param chareset
     * @return
     */
    private String encode(String input, String chareset) {
        try {
            if (StringUtils.isNotEmpty(input) && StringUtils.isNotEmpty(chareset))
                input = URLEncoder.encode(input, chareset);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }

        return input;
    }
}

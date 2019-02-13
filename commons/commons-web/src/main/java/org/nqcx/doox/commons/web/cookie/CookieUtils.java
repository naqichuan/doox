/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * 系统中所有用到的cookie 都需要通过 CookieUtils操作，在配置文件中通过用 cookieName 作为 key 注册到cookieMap
 *
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class CookieUtils {

    private static Map<String, NqcxCookie> cookieMap;

    public static String getCookieValue(HttpServletRequest request, String name) {
        NqcxCookie nCookie = cookieMap.get(name);
        if (nCookie == null)
            return null;

        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0)
            return null;

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName()))
                return nCookie.getValue(cookie.getValue());
        }
        return null;
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param cookie
     */
    public static void setCookie(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        NqcxCookie nCookie = cookieMap.get(name);
        if (nCookie == null)
            return;
        response.addCookie(nCookie.newCookie(name, value));
    }

    /**
     * 添加cookie
     *
     * @param response
     * @param name
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int expiry) {
        NqcxCookie nCookie = cookieMap.get(name);
        if (nCookie == null)
            return;
        response.addCookie(nCookie.newCookie(name, value, expiry));
    }

    /**
     * 跨域 cookie，需要需要注册到cookieMap中
     *
     * @param response
     * @param name
     * @param value
     * @param httpOnly
     */
    public static void crossCookie(HttpServletResponse response, String name, String value, boolean httpOnly) {

        NqcxCookie nCookie = cookieMap.get(name);
        if (nCookie == null)
            return;

        // 跨域用的 获取持久化cookie
        response.setHeader("P3P",
                "CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

        StringBuilder cross = new StringBuilder(380);
        cross.append(name).append("=").append(value).append(";");
        if (nCookie.getDomain() != null && nCookie.getDomain().length() > 0)
            cross.append(" Domain=").append(nCookie.getDomain()).append(";");
        if (nCookie.getExpiry() >= 0) {
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.add(Calendar.SECOND, nCookie.getExpiry());
            DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.US);
            df.setTimeZone(TimeZone.getTimeZone("GMT"));
            String expires = df.format(cal.getTime());
            if (nCookie.getExpiry() == 0) { // 清除cookie
                expires = "Thu, 01-Jan-1970 00:00:10 GMT";
            }
            cross.append(" Expires=").append(expires).append(";");
        }
        cross.append(" Path=").append(nCookie.getPath());
        if (httpOnly) {
            cross.append(";");
            cross.append(" HttpOnly");
        }

        response.addHeader("Set-Cookie", cross.toString());
    }

    /**
     * 删除cookie，需要需要注册到cookieMap中
     *
     * @param request
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        removeCookie(request, response, name, false);
    }

    /**
     * 删除cookie，需要需要注册到cookieMap中
     *
     * @param request
     * @param response
     * @param name
     * @param isCrossCookie
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isCrossCookie) {
        NqcxCookie nCookie = cookieMap.get(name);
        if (nCookie == null)
            return;

        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (nCookie.getName().equals(cookie.getName())) {
                    if (isCrossCookie) {
                        cookie.setMaxAge(0);
                        cookie.setValue(null);
                        cookie.setPath(nCookie.getPath());
                        if (nCookie.getDomain() != null && nCookie.getDomain().length() > 0)
                            cookie.setDomain(nCookie.getDomain());
                        response.addCookie(cookie);
                    } else
                        setCookie(response, nCookie.getName(), null, 0);
                    break;
                }
            }
        }
    }

    public void setCookieMap(Map<String, NqcxCookie> _cookieMap) {
        cookieMap = _cookieMap;
    }
}

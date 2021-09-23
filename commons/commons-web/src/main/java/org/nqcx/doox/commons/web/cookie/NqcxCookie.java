/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.cookie;


import org.nqcx.doox.commons.util.StringUtils;

import javax.servlet.http.Cookie;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class NqcxCookie {

    /**
     * Cookie 加解密工具
     */
    private CookieCipherTools cookieCipherTools;
    /**
     * cookie名称
     */
    private String name;
    /**
     * cookie域名
     */
    private String domain;
    /**
     * cookie路径
     */
    private String path;
    /**
     * cookie默认时限
     */
    private int expiry = -1;
    /**
     * http only
     */
    private boolean httpOnly = false;
    /**
     * cookie键
     *
     * @see #encrypt
     */
    private String key;
    /**
     * 是否加密cookie
     *
     * @see #key
     */
    private boolean encrypt;

    public Cookie newCookie(String value) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String value, boolean httpOnly) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String value, int expiry) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String value, int expiry, boolean httpOnly) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String name, String value) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String name, String value, boolean httpOnly) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String name, String value, int expiry) {
        return newCookie(name, value, expiry, httpOnly);
    }

    public Cookie newCookie(String name, String value, int expiry, boolean httpOnly) {
        return newCookie(name, value, expiry, domain, path, httpOnly);
    }

    /**
     * 创建cookie
     *
     * @param name
     * @param value
     * @param expiry
     * @param domain
     * @param path
     * @param httpOnly
     * @return
     */
    public Cookie newCookie(String name, String value, int expiry, String domain, String path, boolean httpOnly) {
        String newValue;
        if (!StringUtils.isEmpty(value))
            newValue = isEncrypt() ? cookieCipherTools.encrypt(value, getKey()) : value;
        else
            newValue = value;

        Cookie cookie = new Cookie(name, newValue);
        if (!StringUtils.isBlank(domain))
            cookie.setDomain(domain);
        if (!StringUtils.isBlank(path))
            cookie.setPath(path);
        cookie.setMaxAge(expiry);
        cookie.setHttpOnly(httpOnly);

        return cookie;
    }

    public String getValue(String value) {
        // 非原始文件
        return getValue(value, true);
    }

    /**
     * 取 value 值，指定是否返回原始文本内容
     *
     * @param value       value
     * @param notOriginal 为 true 表示非原始文本，按 cookie 配置返回
     * @return {@link String}
     * @author naqichuan 9/23/21 1:17 PM
     */
    public String getValue(String value, boolean notOriginal) {
        if (!StringUtils.isBlank(value)) {
            return notOriginal && isEncrypt() ? cookieCipherTools.decrypt(value, getKey()) : value;
        } else {
            return value;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }

    public boolean isHttpOnly() {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public void setCookieCipherTools(CookieCipherTools cookieCipherTools) {
        this.cookieCipherTools = cookieCipherTools;
    }
}

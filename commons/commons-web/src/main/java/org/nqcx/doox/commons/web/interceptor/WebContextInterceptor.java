/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.nqcx.doox.commons.lang.consts.LoggerConst;
import org.nqcx.doox.commons.web.WebContext;
import org.nqcx.doox.commons.web.WebSupport;
import org.nqcx.doox.commons.web.cookie.CookieUtils;
import org.nqcx.doox.commons.web.cookie.NqcxCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 该拦截器用于记录访问日志，所有系统都需要引入该拦截器
 *
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContextInterceptor extends WebSupport implements HandlerInterceptor {

    private final static Logger ACCESS_LOGGER = LoggerFactory.getLogger(LoggerConst.LOGGER_ACCESS_NAME);
    private final static Logger LOGGER = LoggerFactory.getLogger(WebContextInterceptor.class);

    protected static final String XHR_OBJECT_NAME = "XMLHttpRequest";
    protected static final String HEADER_REQUEST_WITH = "x-requested-with";
    protected static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    protected static final String HEADER_REAL_IP = "X-Real-IP";

    protected LocaleResolver localeResolver;
    protected NqcxCookie identityCookie;

    /*
     * ========================================================================
     * ===================          以下是拦截器方法       ======================
     * ========================================================================
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebContext.remove();
        WebContext wc = getWebContext().start();

        // 取 scheme
        wc.setScheme(request.getScheme());
        // 取 secure
        wc.setSecure(request.isSecure());
        // 取 server name
        wc.setServerName(request.getServerName());
        // 取 port
        wc.setPort(request.getServerPort());
        // 取 contextPath
        wc.setContextPath(request.getContextPath());
        // 取 servletPath
        wc.setServletPath(request.getServletPath());
        // 取 requestURI
        wc.setRequestURI(request.getRequestURI());
        // 取 params
        wc.setParams(getParamsFromRequest(request));
        // 取 requestURL
        wc.setRequestURL(request.getRequestURL());

        // 取 realPath
        wc.setRealPath(request.getSession().getServletContext().getRealPath("/"));

        // 取 remoteAddr
        wc.setRemoteAddr(this.getRemoteAddrFromRequest(request));
        // 取 method
        wc.setMethod(request.getMethod());
        // 判断是否 ajax 访问
        wc.setAjax(this.isAjaxFromRequest(request));
        // 取 locale
        wc.setLocale(localeResolver == null ? null : localeResolver.resolveLocale(request));
        // 取 sessionId
        wc.setSessionId(request.getRequestedSessionId());
        // 取 url
        StringBuffer url = new StringBuffer(request.getRequestURL());
        if (request.getQueryString() != null)
            url.append("?").append(request.getQueryString());
        wc.setUrl(url.toString());
        // 取 referer
        wc.setReferer(request.getHeader("referer"));
        // 取 userAgent
        wc.setUserAgent(request.getHeader("User-Agent"));

        // 处理 identity
        processIdentity(wc, request, response);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        getWebContext().post();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        WebContext wc = getWebContext().end();
        if (requireAccessLog(wc)) {
            ACCESS_LOGGER.info("\"start\": \"{}\", \"post\": \"{}\",  \"end\": \"{}\", \"remoteAddr\": \"{}\"," +
                            " \"serverName\": \"{}\", \"method\": \"{}\", \"scheme\": \"{}\", \"secure\": \"{}\"," +
                            " \"isAjax\": \"{}\", \"uri\": \"{}\", \"locale\": \"{}\", \"sessionId\": \"{}\"," +
                            " \"url\": \"{}\", \"referer\": \"{}\", \"params\": \"{}\"," +
                            " \"data\": \"{}\", \"User-Agent\": \"{}\"",
                    wc.getStart(), wc.getPost(), wc.getEnd(), wc.getRemoteAddr(),
                    wc.getServerName(), wc.getMethod(), wc.getScheme(), wc.isSecure(),
                    wc.isAjax(), wc.getRequestURI(), wc.getLocale(), StringUtils.trimToEmpty(wc.getSessionId()),
                    wc.getUrl(), StringUtils.trimToEmpty(wc.getReferer()), StringUtils.trimToEmpty(string2Json(wc.getParams())),
                    StringUtils.trimToEmpty(string2Json(wc.getData())), StringUtils.trimToEmpty(string2Json(wc.getUserAgent())));
        }
    }

    /**
     * 处理 特殊字符
     *
     * @param input input
     * @return String
     */
    private static String string2Json(String input) {
        if (input == null)
            return null;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {

            char c = input.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 是否需要输出 access log，如果应用中需要特殊处理访问日志，覆盖该方法
     *
     * @param webContext WebContext
     * @return 返回 true 为需要输出日志
     */
    protected boolean requireAccessLog(WebContext webContext) {
        return true;
    }

    /*
     * ========================================================================
     * ===================     以下是 identity 处理方法    ======================
     * ========================================================================
     */

    /**
     * 处理用户唯一标识，用于追踪用户，当前情况下尽量避免同一系统使用多个一级域名的情况，这会导致追踪不准
     * 如果应用中需要特殊处理，覆盖该方法
     *
     * @param request request
     */
    protected void processIdentity(WebContext webContext, HttpServletRequest request, HttpServletResponse response) {
        if (identityCookie == null || webContext == null)
            return;

        String identity = CookieUtils.getCookieValue(request, identityCookie.getName());
        if (org.nqcx.doox.commons.util.StringUtils.isBlank(identity)) {
            // 发放新 cookie
            CookieUtils.setCookie(response, identityCookie.getName(), (identity = newIdentitye()));
        }
        webContext.appendData("identity: " + identity);
    }

    /**
     * 生成新的 identity
     *
     * @return String
     */
    public static String newIdentitye() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /*
     * ========================================================================
     * ===================       以下是 request 处理方法       ==================
     * ========================================================================
     */

    /**
     * @param request request
     * @return boolean
     * @author naqichuan Oct 14, 2013 4:04:03 PM
     */
    protected boolean isAjaxFromRequest(HttpServletRequest request) {
        return XHR_OBJECT_NAME.equals(request.getHeader(HEADER_REQUEST_WITH));
    }

    /**
     * 取得 IP
     *
     * @param request
     * @return
     * @author naqichuan Oct 14, 2013 3:54:51 PM
     */
    protected String getRemoteAddrFromRequest(HttpServletRequest request) {
        String ip = request.getHeader(HEADER_FORWARDED_FOR);
        if (org.nqcx.doox.commons.util.StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader(HEADER_REAL_IP);

        if (org.nqcx.doox.commons.util.StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();

        if (org.nqcx.doox.commons.util.StringUtils.isNotBlank(ip) && ip.contains(","))
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();

        return ip;
    }

    /**
     * 取参数表
     *
     * @param request request
     * @return string
     */
    protected String getParamsFromRequest(HttpServletRequest request) {
        StringBuilder query = new StringBuilder();

        // 解析来源于 form 表单（post）或 get 方式传送的参数
        Map<String, String[]> originParams = new HashMap<String, String[]>();
        if (request.getParameterMap() != null)
            originParams.putAll(request.getParameterMap());

        appendParamsString(query, originParams, false, false);

        if (query.length() > 0 && query.indexOf("&") == 0)
            query.replace(0, 1, "");

        return query.toString();
    }

    /**
     * 生成参数字符串
     *
     * @param query       query
     * @param map         map
     * @param ignoreEmpty 忽略空值（""）
     * @param isEncode    是否进行编码
     */
    protected void appendParamsString(StringBuilder query, Map<String, String[]> map, boolean ignoreEmpty,
                                      boolean isEncode) {
        if (map == null || query == null)
            return;

        for (Map.Entry<String, String[]> entry : map.entrySet()) {
            final String key = entry.getKey();
            String[] value = entry.getValue();
            if (value == null)
                continue;

            for (final String v : value) {
                appendParamsString(query, key, v, ignoreEmpty, isEncode);
            }
        }
    }

    /**
     * 生成参数字符串
     *
     * @param query       query
     * @param key         key
     * @param value       value
     * @param ignoreEmpty 忽略空值（""）
     * @param isEncode    是否进行编码
     */
    protected void appendParamsString(StringBuilder query, String key, String value, boolean ignoreEmpty,
                                      boolean isEncode) {
        if (value == null)
            return;

        String v = String.valueOf(value);
        if (ignoreEmpty && org.nqcx.doox.commons.util.StringUtils.isBlank(v))
            return;

        query.append("&").append(key).append("=").append(isEncode ? encodeValue(v, Charset.forName(DEFAULT_CHARSET_NAME)) : v);
    }

    /**
     * 字符串进行 url 编码
     *
     * @param value   value
     * @param charset charset
     * @return string
     */
    protected String encodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLEncoder.encode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
            LOGGER.warn(e.getMessage());
        }
        return value;
    }

    /*
     * ========================================================================
     * ===================        以下是注入处理方法       ======================
     * ========================================================================
     */

    /**
     * 用于配置文件中配置注入
     *
     * @param localeResolver localeResolver
     */
    public void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    /**
     * 用于配置文件中配置注入
     *
     * @param identityCookie identityCookie
     */
    public void setIdentityCookie(NqcxCookie identityCookie) {
        this.identityCookie = identityCookie;
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web;

import java.util.Date;
import java.util.Locale;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class WebContext {

    private final static ThreadLocal<WebContext> holder = ThreadLocal.withInitial(WebContext::new);

    private String scheme;
    private boolean secure;
    private String serverName;
    private int port;
    private String contextPath;
    private String servletPath;
    private String requestURI;
    private String params;
    private StringBuffer requestURL;

    private String realPath;

    private String remoteAddr;
    private String method;
    private boolean ajax = false;
    private Locale locale;
    private String sessionId;
    private String url;
    private String referer;
    private String userAgent;

    private long start; // 开始时间，before HandlerAdapter invokes the handler
    private long post; // 业务时间，Before the DispatcherServlet renders the view
    private long end; //结束时间，After rendering the view
    private String data; // 其它数据

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getServletPath() {
        return servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public StringBuffer getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(StringBuffer requestURL) {
        this.requestURL = requestURL;
    }

    public String getRealPath() {
        return realPath;
    }

    public void setRealPath(String realPath) {
        this.realPath = realPath;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isAjax() {
        return ajax;
    }

    public void setAjax(boolean ajax) {
        this.ajax = ajax;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public long getPost() {
        return post;
    }

    public void setPost(long post) {
        this.post = post;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void appendData(String data) {
        if (data == null || data.length() == 0)
            return;

        if (this.data != null && this.data.length() > 0)
            this.data += ", ";
        else
            this.data = "";
        this.data += data;
    }

    public static void setWebContext(WebContext webContext) {
        holder.set(webContext);
    }

    public static WebContext getWebContext() {
        return holder.get();
    }

    public WebContext start() {
        this.start = (new Date()).getTime();
        return this;
    }

    public WebContext post() {
        this.post = (new Date()).getTime();
        return this;
    }

    public WebContext end() {
        this.end = (new Date()).getTime();
        return this;
    }

    public static void remove() {
        holder.remove();
    }

}

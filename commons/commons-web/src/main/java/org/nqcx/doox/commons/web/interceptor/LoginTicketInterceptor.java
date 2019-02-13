/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.interceptor;

import org.nqcx.doox.commons.util.StringUtils;
import org.nqcx.doox.commons.web.cookie.CookieUtils;
import org.nqcx.doox.commons.web.cookie.NqcxCookie;
import org.nqcx.doox.commons.web.login.LoginTicket;
import org.nqcx.doox.commons.web.login.LoginTicketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginTicketInterceptor extends WebContextInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginTicketInterceptor.class);

    protected NqcxCookie ticketCookie;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            parseTicketCookie(request, response);
        } catch (Exception e) {
            logger.warn("parse login ticket cookie error! " + e.getMessage());
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // ignore
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // ignore
    }

    /**
     * 解析 ticket cookie
     *
     * @param request
     * @param response
     */
    protected void parseTicketCookie(HttpServletRequest request, HttpServletResponse response) {
        LoginTicket.remove();

        if (ticketCookie == null)
            return;

        String cookieValue = CookieUtils.getCookieValue(request, ticketCookie.getName());
        if (StringUtils.isNotBlank(cookieValue)) {
            try {
                LoginTicket.setTicket(LoginTicketUtils.getLoginTicket(cookieValue, ticketCookie.getKey()));
                if (LoginTicket.getTicket() != null)
                    return;
                logger.info("ticket error or ticket expired!");
            } catch (Exception e) {
                logger.warn("decrypt ticket cookie error!" + e.getMessage());
            }

            LoginTicket.remove();
            removeTicketCookie(request, response);
        }
    }

    /**
     * 删除 ticket cookie
     *
     * @param request
     * @param response
     */
    protected void removeTicketCookie(HttpServletRequest request, HttpServletResponse response) {
        if (ticketCookie == null)
            return;

        CookieUtils.removeCookie(request, response, ticketCookie.getName(), true);
    }

    /**
     * 用于配置文件中配置注入
     *
     * @param ticketCookie
     */
    public void setTicketCookie(NqcxCookie ticketCookie) {
        this.ticketCookie = ticketCookie;
    }
}

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
import org.nqcx.doox.commons.web.login.LoginContext;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.nqcx.doox.commons.lang.consts.PeriodConst.THIRTY_MINUTES;
import static org.nqcx.doox.commons.lang.consts.PeriodConst.THOUSAND;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginContextInterceptor extends WebContextInterceptor {

    protected final static long SESSION_TIMEOUT_MILLISECOND = THIRTY_MINUTES * THOUSAND; // 30 minutes
    protected final static int RATE = 2;

    /**
     * 需要注入
     */
    protected NqcxCookie loginCookie;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        LoginContext.remove();

        if (loginCookie == null)
            return true;

        LoginContext lctx = getLoginContext(CookieUtils.getCookieValue(request, loginCookie.getName()), response);
        if (lctx == null || StringUtils.isBlank(lctx.getAcco())) {
            // 没有 login cookie，直接返回
            removeLoginCookie(request, response);
            LoginContext.remove();
            return true;
        }

        LoginContext.setLoginContext(lctx);

        long expires = lctx.getExpires(); //过期时间
        if (expires == -1)
            return true;

        long created = lctx.getCreated(); //创建时间戳
        long current = System.currentTimeMillis(); //当前时间戳

        // 如果没有设置过期时间，则使用默认的
        long timeout = expires == 0 ? SESSION_TIMEOUT_MILLISECOND : expires - created;

        // 如果已过期 login cookie，直接返回
        if (current - created >= timeout) {
            removeLoginCookie(request, response);
            LoginContext.remove();
            return true;
        }

        // 如果新cookie或是更改了登录或剩下的时间只有2/3，就需要重新派发cookie
        if ((current - created) * 3 / RATE > timeout) {
            // 写最后一次访问的cookie
            lctx.setCreated(current);
            if (expires != 0)
                lctx.setTimeout(timeout);

            // 这里添加一个步骤用于应用中填充 cookie
            this.fillLoginContext(lctx);

            CookieUtils.setCookie(response, loginCookie.getName(), lctx.toCookieValue());
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
     * 默认情况下，LoginContext 的值来源于解析登录 cookieValue
     * <p/>
     * 特殊情况下可以覆盖该方法，通过其它途径取得 LoginContext，比如用 TicketContext 生成 LoginContext
     *
     * @param cookieValue value
     * @param response    response
     * @return
     */
    protected LoginContext getLoginContext(String cookieValue, HttpServletResponse response) {
        return this.getLoginContext(cookieValue);
    }

    /**
     * 解析 cookieValue 并生成 LoginContext
     *
     * @param cookieValue
     * @return
     */
    protected LoginContext getLoginContext(String cookieValue) {
        return StringUtils.isBlank(cookieValue) ? null : LoginContext.parse(cookieValue);
    }

    /**
     * 用于应用中修改或添加 cookie 属性，非必须
     *
     * @param loginContext
     */
    protected void fillLoginContext(LoginContext loginContext) {
    }

    /**
     * 删除 login cookie
     *
     * @param request
     * @param response
     */
    protected void removeLoginCookie(HttpServletRequest request, HttpServletResponse response) {
        if (loginCookie == null)
            return;

        CookieUtils.removeCookie(request, response, loginCookie.getName());
    }


    /**
     * 用于配置文件中配置注入
     *
     * @param loginCookie
     */
    public void setLoginCookie(NqcxCookie loginCookie) {
        this.loginCookie = loginCookie;
    }
}

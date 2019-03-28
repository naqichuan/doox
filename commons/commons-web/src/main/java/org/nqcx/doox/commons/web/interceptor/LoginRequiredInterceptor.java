/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.interceptor;

import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.lang.url.UrlBuilder;
import org.nqcx.doox.commons.web.login.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginRequiredInterceptor extends WebContextInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(LoginRequiredInterceptor.class);

    protected UrlBuilder homeUrl;
    protected UrlBuilder authorizeUrl;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginContext loginContext = getLoginContext();

        // 取得用户请求方式 request with
        if (!checkLogin(response, loginContext)) {
            if (isAjax()) {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from ajax check login false!");

                responseJsonResult(response, buildJsonResult(new DTO().putResult("2", "需要登录")));
            } else {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from normal way check login false!");

                response.sendRedirect(getAuthorizeUrl(request));
            }

            return false;
        }

        logger.info("Account [" + loginContext.getAccount() + "] check login true!");

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
     * 通过验证 LoginContext，判断用户是否登录，如果需要更新数据库，要重写该方法
     *
     * @param context
     * @return
     */
    protected boolean checkLogin(HttpServletResponse response, LoginContext context) {
        if (context == null || !context.isLogin()) {
            // 没登录
            return false;
        }

        return true;
    }

    /**
     * 取出登录的信息
     *
     * @return
     */
    protected LoginContext getLoginContext() {
        return LoginContext.getLoginContext();
    }


    /**
     * 使用 getAuthorizeUrl() 方法
     *
     * @param request
     * @return
     */
    protected String getAuthorizeUrl(HttpServletRequest request) {
        return getAuthorizeUrl(request, null);
    }

    /**
     * @param request
     * @return
     */
    protected String getAuthorizeUrl(HttpServletRequest request, Map<String, String> params) {
        if (homeUrl == null)
            return "";
        UrlBuilder.Builder currentUrlBuilder = homeUrl.forPath(request.getServletPath());
        currentUrlBuilder.put(request.getParameterMap());

        if (authorizeUrl == null)
            return "";
        UrlBuilder.Builder authorizeUrlBuilder = authorizeUrl.forPath();

        if (params != null && params.size() > 0)
            authorizeUrlBuilder.put(params);
        authorizeUrlBuilder.put("redirectUrl", generateRedirectUrl(currentUrlBuilder));

        return authorizeUrlBuilder.build();
    }

    /**
     * 对当前的 url 进行处理，生成符合预期的 redirectUrl
     * <p/>
     * 该方法用于子类扩展和重构 redirectUrl
     *
     * @param currentUrlBuilder currentUrlBuilder
     * @return
     */
    protected String generateRedirectUrl(UrlBuilder.Builder currentUrlBuilder) {
        if (currentUrlBuilder == null)
            return null;
        return currentUrlBuilder.build();
    }

    /**
     * 用于配置文件中配置注入
     *
     * @param homeUrl
     */
    public void setHomeUrl(UrlBuilder homeUrl) {
        this.homeUrl = homeUrl;
    }

    /**
     * 用于配置文件中配置注入，用 setAuthorizeUrl() 代替
     *
     * @param authorizeUrl
     */
    @Deprecated
    public void setLoginUrl(UrlBuilder authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    /**
     * 用于配置文件中配置注入
     *
     * @param authorizeUrl
     */
    public void setAuthorizeUrl(UrlBuilder authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }
}

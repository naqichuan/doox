/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.interceptor;

import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.lang.o.NErrorCode;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        LoginContext loginContext = getLoginContext();

        // 取得用户请求方式 request with
        if (!checkLogin(response, loginContext)) {
            if (isAjax()) {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from ajax check login false!");

                responseJson(response, buildJsonResult(new DTO().putError(NErrorCode.E11.error())));
            } else {
                logger.info("RemoteAddr [" + request.getRemoteAddr() + "] from normal way check login false!");

                response.sendRedirect(buildInternalAuthorizeUrl(request, true));
            }

            return false;
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
     * 生成 url 不包含 contextPath
     *
     * @param request request
     * @return String
     */
    protected String buildInternalAuthorizeUrl(HttpServletRequest request) {
        return buildInternalAuthorizeUrl(request, false);
    }

    /**
     * @param request request
     * @return String
     */
    protected String buildInternalAuthorizeUrl(HttpServletRequest request, boolean containContextPath) {
        return new UrlBuilder().forPath(authorizeUri(containContextPath ? request.getContextPath() : ""))
                .put("redirectUri", buildInternalRedirectUri(request.getServletPath(), request.getParameterMap()))
                .buildUri();
    }

    /**
     * @param prefix
     * @return
     */
    private String authorizeUri(String prefix) {
        return prefix + authorizeUri();
    }

    /**
     * @return 验证登录状态的 uri
     */
    protected String authorizeUri() {
        return "/account/login";
    }

    /**
     * 生成符合预期的 redirectUri，该 url 不包含 contextPath
     * <p/>
     * 如果其它需求，子类扩展和重载
     *
     * @param servletPath servletPath
     * @param params      params
     * @return 生成不包含 contextPath 的 url
     */
    protected String buildInternalRedirectUri(String servletPath, Map<String, ?> params) {
        return new UrlBuilder().forPath(servletPath)
                .put(params)
                .buildUri();
    }
}

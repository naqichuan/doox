/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.handler;

import org.nqcx.doox.commons.web.login.LoginContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class NqcxExceptionResolver extends SimpleMappingExceptionResolver {

    protected final static Logger logger = LoggerFactory.getLogger("_CONTROLLER_ERROR");

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception exception) {

        ModelAndView mv = super.doResolveException(request, response, handler, exception);
        logError(request, handler, exception);

        return mv;
    }

    protected void logError(HttpServletRequest request, Object handler, Exception exception) {
        LoginContext context = LoginContext.getLoginContext();
        if (context != null && context.getAccount() != null && context.getAccount().length() > 0) {
            logger.error(
                    "Account [" + context.getAccount() + "] has a error for request url [" + request.getRequestURI()
                            + "]", exception);
        } else {
            logger.error(
                    "RemoteAddr [" + request.getRemoteAddr() + "] has a error for request url ["
                            + request.getRequestURI() + "]", exception);
        }
    }
}

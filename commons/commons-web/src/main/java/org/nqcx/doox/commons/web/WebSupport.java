/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.nqcx.doox.commons.lang.o.*;
import org.nqcx.doox.commons.lang.url.UrlBuilder;
import org.nqcx.doox.commons.util.MapBuilder;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public abstract class WebSupport {

    private final static Logger logger = LoggerFactory.getLogger(WebSupport.class);

    public final static TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT+08:00");

    @SuppressWarnings("WeakerAccess")
    protected final static String SUCCESS = "success";
    @SuppressWarnings("WeakerAccess")
    protected final static String ERROR_CODE = "errorCode";
    @SuppressWarnings("WeakerAccess")
    protected final static String ERROR_TEXT = "errorText";
    @SuppressWarnings("unused")
    protected final static String NOT_FOUND = "NOT FOUND";
    protected final static String DEFAULT_CHARSET_NAME = "UTF-8";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired(required = false)
    protected MessageSource messageSource;


    {
        this.configFeatures(objectMapper);
    }

    // ========================================================================

    protected void configFeatures(ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        objectMapper.setTimeZone(DEFAULT_TIME_ZONE);
    }

    // ========================================================================

    /**
     * getPropertyValue
     *
     * @param code           code
     * @param args           args
     * @param defaultMessage defaultMessage
     * @return {@link String}
     * @author naqichuan 22-5-17 下午6:10
     */
    protected String getPropertyValue(String code, Object[] args, String defaultMessage) {
        return this.getPropertyValue(code, args, defaultMessage, getLocale());
    }

    /**
     * 从 properties 中取值
     *
     * @param code   code
     * @param args   args
     * @param locale locale
     * @return String
     */
    protected String getPropertyValue(String code, Object[] args, String defaultMessage, Locale locale) {
        String rv = null;
        try {
            if (locale == null)
                locale = getLocale();

            rv = messageSource == null ? null : messageSource.getMessage(code, args, defaultMessage, locale);

        } catch (NoSuchMessageException e) {
            logger.warn("WebSupport.getPropertyValue ," + e.getMessage());
        }
        return rv == null ? code : rv;
    }

    // ========================================================================

    /**
     * getWebContext
     *
     * @return {@link WebContext}
     * @author naqichuan 22-5-18 下午6:59
     */
    protected WebContext getWebContext() {
        return WebContext.getWebContext();
    }

    /**
     * getScheme
     *
     * @return {@link String}
     * @author naqichuan 22-5-18 下午6:59
     */
    protected String getScheme() {
        return getWebContext().getScheme();
    }

    /**
     * getServerName
     *
     * @return {@link String}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected String getServerName() {
        return getWebContext().getServerName();
    }

    /**
     * getRemoteAddr
     *
     * @return {@link String}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected String getRemoteAddr() {
        return getWebContext().getRemoteAddr();
    }

    /**
     * getContextPath
     *
     * @return {@link String}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected String getContextPath() {
        return getWebContext().getContextPath();
    }

    /**
     * getServletPath
     *
     * @return {@link String}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected String getServletPath() {
        return getWebContext().getServletPath();
    }

    /**
     * isAjax
     *
     * @return {@link boolean}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected boolean isAjax() {
        return getWebContext().isAjax();
    }

    /**
     * getLocale
     *
     * @return {@link Locale}
     * @author naqichuan 22-5-18 下午6:58
     */
    protected Locale getLocale() {
        return getWebContext().getLocale();
    }

    // ========================================================================

    /**
     * 构建返回结果，返回 map 类型
     *
     * @param dto dto
     * @return {@link Map<?,?>}
     * @author naqichuan 22-5-18 下午6:59
     */
    @Deprecated
    protected Map<?, ?> buildResult(DTO dto) {
        return dto2map(dto);
    }

    /**
     * 构建返回结果，返回 String 类型
     *
     * @param dto      dto
     * @param features features
     * @return String
     */
    @Deprecated
    protected String buildJsonResult(DTO dto, SerializerFeature... features) {
        return JSON.toJSONString(dto2map(dto), features);
    }

    /**
     * dto2Json
     *
     * @param dto dto
     * @return {@link String}
     * @author naqichuan 22-5-20 下午12:07
     */
    protected String dto2Json(DTO dto) {
        try {
            return objectMapper.writeValueAsString(dto2map(dto));
        } catch (JsonProcessingException e) {
            logger.warn(e.getMessage());
        }
        return null;
    }


    /**
     * buildDTO
     *
     * @param dto dto
     * @return {@link Map<?,?>}
     * @author naqichuan 22-5-20 下午12:09
     */
    protected Map<?, ?> dto2map(DTO dto) {
        if (dto == null)
            // 这里的 value 只做说明，最终返回以 gmsg.properties 中 key 对应的配置为准
            dto = new DTO().putError(NErrorCode.E6.buildError());

        MapBuilder mb = MapBuilder.instance()
                .put(SUCCESS, dto.isSuccess());

        this.buildDtoResult(mb, dto.getResults());

        if (dto.isSuccess()) {
            // 1. 解析对象
            this.buildDtoObject(mb, dto.getObject());
            // 2. 解析列表
            this.buildDtoList(mb, dto.getList());
            // 3. 解析分页
            this.buildDtoNpage(mb, dto.getNpage());
            // 4. 解析排序
            this.buildDtoNsort(mb, dto.getNsort());

        } else
            this.buildDtoErrors(mb, dto.getErrors());


        return mb.build();
    }

    /**
     * dto.object to map
     *
     * @param dto dto
     * @return {@link Map<?,?>}
     * @author naqichuan 22-5-20 下午12:29
     */
    protected Map<?, ?> object2map(DTO dto) {
        if (dto == null)
            // 这里的 value 只做说明，最终返回以 gmsg.properties 中 key 对应的配置为准
            dto = new DTO().putError(NErrorCode.E6.buildError());

        MapBuilder mb = MapBuilder.instance()
                .put(SUCCESS, dto.isSuccess());

        if (dto.isSuccess() && dto.getObject() != null) {
            Class<?> clazz = dto.getObject().getClass();

            if (isPrimitiveOrWrapper(clazz)                         // primitive or wrapper
                    || String.class.isAssignableFrom(clazz)         // string
                    || clazz.isArray()                              // array
                    || Collection.class.isAssignableFrom(clazz)     // collection
                    || clazz.isEnum()                               // enum
            ) {
                mb.put("object", dto.getObject());
            } else if (Map.class.isAssignableFrom(clazz)) {
                mb.putMap(dto.getObject());
            } else {
                Map<String, Object> omap = new HashMap<>();
                BeanMap beanMap = BeanMap.create(dto.getObject());
                for (Object key : beanMap.keySet()) {
                    omap.put(String.valueOf(key), beanMap.get(key));
                }
                // root 根下直接存储对象 field 用于直接映射成 object
                mb.putMap(omap);
            }
        } else
            this.buildDtoErrors(mb, dto.getErrors());

        return mb.build();
    }

    /**
     * buildDtoResult
     *
     * @param mb  mb
     * @param map map
     * @author naqichuan 22-5-17 下午9:24
     */
    private void buildDtoResult(final MapBuilder mb, Map<String, Object> map) {
        if (mb == null)
            return;

        Optional.ofNullable(map).ifPresent(x -> mb.put("results", x));
    }

    /**
     * 处理 object
     *
     * @param mb     mb
     * @param object object
     * @author naqichuan 22-5-17 下午9:54
     */
    private void buildDtoObject(final MapBuilder mb, Object object) {
        if (mb == null)
            return;

        Optional.ofNullable(object).ifPresent(x -> mb.put("object", object));
    }

    /**
     * 处理 list
     *
     * @param mb   mb
     * @param list list
     * @author naqichuan 22-5-17 下午9:50
     */
    private void buildDtoList(final MapBuilder mb, List<?> list) {
        if (mb == null)
            return;

        Optional.ofNullable(list).ifPresent(x -> mb.put("list", x));
    }

    /**
     * 设置返回分页的结果
     *
     * @param mb   mb
     * @param page page
     * @author naqichuan 22-5-17 下午9:46
     */
    private void buildDtoNpage(final MapBuilder mb, NPage page) {
        if (mb == null)
            return;

        Optional.ofNullable(page).ifPresent(x -> mb.put("npage", MapBuilder.instance()
                .put("page", x.getPage())
                .put("totalCount", x.getTotalCount())
                .put("pageSize", x.getPageSize())
                .put("totalPage", x.getTotalPage())
                .put("offset", x.getOffset()).build())
        );
    }

    /**
     * buildDtoSort
     *
     * @param mb   mb
     * @param sort sort
     * @author naqichuan 22-5-17 下午10:01
     */
    private void buildDtoNsort(final MapBuilder mb, NSort sort) {
        if (mb == null)
            return;

        Optional.ofNullable(sort).ifPresent(x -> mb.put("nsort", x));
    }

    /**
     * 处理多个错误，errorCode 使用默认错误码“10”
     * <p/>
     * 参数:
     * <pre>
     *      entrySet {key="1", value="错误1"}
     *      entrySet {key="2", value="错误2"}
     *      entrySet {key="3", value="错误3"}
     * </pre>
     * 转换结果:
     * <pre>
     * {
     *     success: false,
     *     errorCode: "1",
     *     errorText: "错误1",
     *     errors: [
     *         {
     *             errorCode: "1",
     *             errorText: "错误1"
     *         },
     *         {
     *             errorCode: "2",
     *             errorText: "错误2"
     *         },
     *         {
     *             errorCode: "3",
     *             errorText: "错误3"
     *         }
     *     ]
     * }
     * </pre>
     *
     * @param mb     mb
     * @param errors errors
     * @author naqichuan 22-5-17 下午10:04
     */
    private void buildDtoErrors(final MapBuilder mb, Map<NError, Object[]> errors) {
        if (mb == null)
            return;

        Optional.ofNullable(errors).ifPresent(x -> {
            if (x.size() == 0)
                errors.put(NErrorCode.E0.buildError(), null);

            // 设置 error
            this.buildDtoErrorCode(mb, errors.entrySet().iterator().next());

            // 解析 errors
            if (errors.size() > 1)
                this.buildDtoErrorCodes(mb, errors.entrySet());
        });
    }


    /**
     * 处理单个错误
     *
     * @param mb    mb
     * @param error error
     */
    protected void buildDtoErrorCode(final MapBuilder mb, Map.Entry<NError, Object[]> error) {
        if (mb == null)
            return;

        Optional.ofNullable(error).ifPresent(x -> {
            NError nerror = error.getKey();
            Object[] args = error.getValue();

            mb.put(ERROR_CODE, nerror.getErrorCode())
                    .put(ERROR_TEXT, getPropertyValue(nerror.fullErrorCode(), args,
                            IErrorCode.fullErrorText(nerror.getErrorText(), args)));
        });
    }

    /**
     * buildDtoErrorCodes
     *
     * @param mb     mb
     * @param errors errors
     * @author naqichuan 22-5-18 下午7:00
     */
    protected void buildDtoErrorCodes(final MapBuilder mb, Set<Map.Entry<NError, Object[]>> errors) {
        if (mb == null)
            return;

        mb.put("errors", errors.stream().map(x -> new NError(
                x.getKey().getErrorCode(),
                getPropertyValue(x.getKey().fullErrorCode(), x.getValue(),
                        IErrorCode.fullErrorText(x.getKey().getErrorText(), x.getValue()))))
                .collect(Collectors.toList()));
    }

    // ========================================================================

    /**
     * 通过 response 直接返回 ContentType 为 application/json 格式字符串
     *
     * @param response response
     * @param json     json
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */
    protected void responseJson(HttpServletResponse response, String json) {
        response.setContentType("application/json; charset=UTF-8");
        response(response, json);
    }

    /**
     * 通过 response 直接返回 ContentType 为 text/html 格式字符串
     *
     * @param response response
     * @param html     html
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */
    protected void responseHtml(HttpServletResponse response, String html) {
        response.setContentType("text/html; charset=UTF-8");
        response(response, html);
    }

    /**
     * 通过 response 直接返回字符串
     *
     * @param response response
     * @param text     text
     * @author naqichuan Sep 26, 2013 3:02:32 PM
     */
    protected void response(HttpServletResponse response, String text) {
        response.setCharacterEncoding(DEFAULT_CHARSET_NAME);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(text);
        } catch (IOException e) {
            logger.warn("WebSupport.responseResult, " + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    // ========================================================================

    /**
     * 跳转到错误页
     *
     * @param response response
     * @param error    error
     */
    protected void sendRedirectErrorPage(HttpServletResponse response, NError error) {
        if (error == null || error.getErrorCode() == null || error.getErrorCode().length() == 0)
            error = NErrorCode.E12.buildError();

        try {
            response.sendRedirect(getContextPath() + "/error/" + error.getErrorCode());
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * 跳转到普通页
     *
     * @param response response
     * @param uri      uri
     */
    protected void sendRedirectNormalPage(HttpServletResponse response, String uri) {
        if (uri == null)
            uri = "";

        try {
            if (UrlBuilder.containProtocol(uri))
                response.sendRedirect(uri);
            else
                response.sendRedirect((getContextPath() == null ? "" : getContextPath()) + (uri.startsWith("/") ? uri : "/" + uri));
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

    // ========================================================================

    /**
     * 读取 request body 内容为字符串
     *
     * @param request request
     * @return String
     */
    protected String requestBody(HttpServletRequest request) {
        return requestBody(request, DEFAULT_CHARSET_NAME);
    }

    /**
     * 读取 request body 内容为字符串
     *
     * @param request     request
     * @param charsetName charsetName
     * @return String
     */
    protected String requestBody(HttpServletRequest request, String charsetName) {
        StringBuffer sb = new StringBuffer();

        try {
            InputStream is = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, charsetName);
            BufferedReader br = new BufferedReader(isr);
            String s;
            while ((s = br.readLine()) != null)
                sb.append(s);
        } catch (IOException e) {
            logger.warn("requestBody error, {}", e.getMessage());
        }

        return sb.toString();
    }

    /**
     * 从 request 中取原始参数表。
     * <p/>
     * 参数来源 url, header, request body 等。
     *
     * @param request request
     * @return map map
     */
    protected Map<String, String[]> parseParamsFromRequest(HttpServletRequest request) {
        String contentType = request.getContentType();
        if (contentType != null)
            contentType = contentType.trim();

        // 解析来源于 form 表单（post）或 get 方式传送的参数
        Map<String, String[]> originParams = new HashMap<String, String[]>();
        if (request.getParameterMap() != null)
            originParams.putAll(request.getParameterMap());

        // GET 请求直接结束方法，不需要解析 request body
        if ("GET".equalsIgnoreCase(request.getMethod())
                || ("POST".equalsIgnoreCase(request.getMethod()) && contentType != null && contentType.contains("application/x-www-form-urlencoded")))
            return originParams;

        // 其它方式的请求需要解析 responseBody 的参数
        String responseBody = requestBody(request);
        if (StringUtils.isBlank(responseBody))
            return originParams;

        // json 格式，不支持复杂的 json 格式，两层以下如果是对象，直接转类字符串，如果是数组对象也转，字符串数组
        if (contentType != null && contentType.contains("application/json")) {
            // 解析 Json
            appendRequestBodyJsonParamsToMap(originParams, JSON.parseObject(responseBody, Map.class));
        } else {
            // 解析用 & 拼接的 key=value 字符串
            appendRequestBodyParamsToMap(originParams, responseBody);
        }

        return originParams;
    }

    /**
     * 将 json 格式参数转成的 Map<String, Object> 添加到 paramMap 中
     *
     * @param paramMap   paramMap
     * @param bodyParams bodyParams
     */
    private void appendRequestBodyJsonParamsToMap(Map<String, String[]> paramMap, Map<String, Object> bodyParams) {
        if (paramMap == null || bodyParams == null || bodyParams.size() == 0)
            return;

        for (Map.Entry<String, Object> entry : bodyParams.entrySet()) {
            final String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null)
                continue;

            if (value instanceof Object[]) {
                for (final Object v : (Object[]) value) {
                    appendParamMap(paramMap, key, v);
                }
            } else if (value instanceof Collection) {
                for (final Object v : (Collection<?>) value) {
                    appendParamMap(paramMap, key, v);
                }
            } else
                appendParamMap(paramMap, key, value);
        }
    }


    /**
     * 用 & 拼接的 key=value 字符串解析成 Map<String, String[]>，并添加到 paramMap 中
     *
     * @param paramMap    paramMap
     * @param requestBody requestBody String
     */
    private void appendRequestBodyParamsToMap(Map<String, String[]> paramMap, String requestBody) {
        if (paramMap == null || StringUtils.isBlank(requestBody) || !requestBody.contains("="))
            return;

        String[] paramKeyValue;
        String paramKey;

        String[] params = requestBody.split("&");
        for (String param : params) {
            if (StringUtils.isBlank(param) || !param.contains("=")
                    || (paramKeyValue = param.split("=")).length < 1
                    || StringUtils.isBlank(paramKey = paramKeyValue[0]))
                continue;

            appendParamMap(paramMap, paramKey, (paramKeyValue.length == 1 ? null : paramKeyValue[1]));
        }
    }

    /**
     * 追回参数表
     *
     * @param paramMap paramMap
     * @param key      key
     * @param value    value
     */
    private void appendParamMap(Map<String, String[]> paramMap, String key, Object value) {
        String paramValue;
        String[] existValues;

        if (value == null) {
            paramValue = "";
        } else
            paramValue = String.valueOf(value).trim();

        String[] newKeyValue;
        if ((existValues = paramMap.get(key)) != null) {
            System.arraycopy(existValues, 0, (newKeyValue = new String[existValues.length + 1]), 0, existValues.length);
            newKeyValue[newKeyValue.length - 1] = paramValue;
        } else
            newKeyValue = new String[]{paramValue};

        paramMap.put(key, newKeyValue);
    }
}

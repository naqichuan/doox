/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.url;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支持泛协议，该特性用于http 和 https 自适应，如：//nqcx.org，forPath() 操作之前需要填充协议
 * <p/>
 * 支持 baseUrl 占位符，该特性用于泛域名的应用，如：http://$baseUrl$ or //$baseUrl$ or http://$baseUrl$，forPath() 操作之前需要填充 baseUrl
 * <p/>
 * 支持 ajax 匿名函数，如：http://nqcx.org?xx=xx&callback=?
 *
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class UrlBuilder implements Cloneable {

    public final static Pattern URL_PATTERN = Pattern.compile("^(file|gopher|news|nntp|telnet|http|ftp|https|ftps|sftp){0,1}:{0,1}//{0,1}(.*)");
    public final static Pattern URL_PROTOCOL_PATTERN = Pattern.compile("file|gopher|news|nntp|telnet|http|ftp|https|ftps|sftp");

    private final static Pattern URL_BASE_PATTERN = Pattern.compile("(\\$\\s*baseUrl\\s*\\$)");
    private final static Pattern PARAM_PLACEHOLDER_PATTERN = Pattern.compile("\\{\\s*\\d+\\s*\\}");

    // 泛协议，默认 http 协议，如果调用 setProtocol() 进行个赋值，设置该属性不空，再调用 setBaseUrl() 时不对 protocol 进行更改
    private final ThreadLocal<String> protocol = ThreadLocal.withInitial(() -> null);

    // 支持泛协议，支持 baseUrl 占位符，//$baseUrl$
    private final ThreadLocal<String> baseUrl = ThreadLocal.withInitial(() -> null);

    // 生成 url 时根据 index 对应的值替换占位符
    private final ThreadLocal<List<String>> values = ThreadLocal.withInitial(() -> new ArrayList<>(50));

    // 原始 url 字符串
    private final String originalUrl;
    // 原始 Url 对象
    private final URL url;

    private final Charset charset;
    private final boolean ignoreEmpty;

    // 参数表
    private final Map<String, Object> queryMap;

    /**
     * 默认构造函数
     */
    public UrlBuilder() {
        this("//$baseUrl$");
    }

    /**
     * 构造函数
     *
     * @param _originalUrl originalUrl
     */
    public UrlBuilder(final String _originalUrl) {
        this(_originalUrl, Charset.defaultCharset().name());
    }

    /**
     * 构造函数
     *
     * @param _originalUrl originalUrl
     * @param _charsetName charsetName
     */
    public UrlBuilder(final String _originalUrl, final String _charsetName) {
        this(_originalUrl, _charsetName, true);
    }

    /**
     * 构造函数
     *
     * @param _originalUrl originalUrl
     * @param _charsetName charsetName
     * @param _ignoreEmpty ignoreEmpty
     */
    public UrlBuilder(final String _originalUrl, final String _charsetName, final boolean _ignoreEmpty) {
        this.clean();

        // 检查 originalUrl 是否符合要求
        if (_originalUrl == null || _originalUrl.length() == 0)
            throw new RuntimeException("originalUrl 不允许空!");

        Matcher matcher = URL_PATTERN.matcher(_originalUrl);
        if (!matcher.matches())
            throw new RuntimeException("originalUrl 格式不匹配!");

        // 原始 url
        this.originalUrl = _originalUrl;

        // charsetName
        if (_charsetName == null || _charsetName.length() == 0)
            this.charset = Charset.defaultCharset();
        else
            this.charset = Charset.forName(_charsetName);

        // ignoreEmpty
        this.ignoreEmpty = _ignoreEmpty;


        String protocol = "http";
        if (matcher.groupCount() >= 1 && matcher.group(1) != null)
            protocol = matcher.group(1);

        String noProtocolUrl = this.originalUrl;
        if (matcher.groupCount() >= 2 && matcher.group(2) != null)
            noProtocolUrl = matcher.group(2);


        // url & queryMap
        try {
            url = new URL(protocol + "://" + noProtocolUrl);

            queryMap = new LinkedHashMap<>();
            if (isNotBlank(url.getQuery()))
                queryMap.putAll(parseQuery(url.getQuery()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        if (url == null)
            throw new RuntimeException("originalUrl 格式不匹配!");
    }

    /**
     * 清除原来的变量
     */
    private void clean() {
        protocol.remove();
        baseUrl.remove();
        values.remove();
    }

    @Override
    public UrlBuilder clone() {
        UrlBuilder builder = null;
        try {
            builder = (UrlBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            // nothing to do
        }
        return builder;
    }

    // ========================================================================

    /**
     * 为 protocol 赋值
     *
     * @param _protocol protocol
     * @return UrlBuilder
     */
    public UrlBuilder setProtocol(final String _protocol) {
        if (isNotBlank(_protocol) && URL_PROTOCOL_PATTERN.matcher(_protocol).matches())
            this.protocol.set(_protocol);

        return this;
    }

    /**
     * 为 baseUrl 赋值
     *
     * @param _baseUr baseUr
     * @return UrlBuilder
     */
    public UrlBuilder setBaseUrl(final String _baseUr) {
        // 检查 baseUr 是否符合要求
        if (_baseUr == null || _baseUr.length() == 0)
            return this;

        // protocol 未设置，并且 baseUr 包含 protocol，更新 protocol
        Matcher matcher = URL_PATTERN.matcher(_baseUr);
        if (matcher.matches() && (protocol.get() == null || protocol.get().length() == 0)
                && matcher.groupCount() > 0
                && matcher.group(1) != null && matcher.group(1).length() > 0)
            protocol.set(matcher.group(1));

        if (matcher.matches() && matcher.groupCount() >= 2 && matcher.group(2) != null)
            this.baseUrl.set(matcher.group(2));
        else
            this.baseUrl.set(_baseUr);

        return this;
    }

    /**
     * 最多允许 50 个占位符
     *
     * @param value value
     * @return UrlBuilder
     */
    public UrlBuilder setValue(int index, String value) {
        if (index > 50 || index < 0)
            throw new RuntimeException("占位符个数不允许超过50");

        if (values.get().size() > index)
            values.get().set(index, value);
        else {
            for (int i = values.get().size(); i <= index; i++) {
                values.get().add("{" + i + "}");
                if (i == index)
                    values.get().set(i, value);
            }
        }

        return this;
    }

    /**
     * 最多允许 50 个占位符
     *
     * @param map map
     * @return UrlBuilder
     */
    public UrlBuilder setValues(Map<String, String> map) {
        if (map == null || map.size() == 0)
            return this;

        for (Entry<String, String> entry : map.entrySet()) {
            if (entry == null)
                continue;
            try {
                setValue(Integer.parseInt(entry.getKey()), entry.getValue());
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
        }

        return this;
    }

    // ========================================================================

    /**
     * 检查 url 是否包含 protocol
     *
     * @param originalUrl originalUrl
     * @return boolean
     */
    public static boolean containProtocol(String originalUrl) {
        Matcher matcher = URL_PATTERN.matcher(originalUrl);

        return matcher.matches() && matcher.groupCount() >= 1 && matcher.group(1) != null;
    }

    /**
     * @param query query
     * @return map
     */
    public static Map<String, Object> parseQuery(String query) {
        String[] params = query.split("&");
        Map<String, Object> map = new LinkedHashMap<String, Object>(params.length);
        for (String param : params) {
            String[] strings = param.split("=");
            String name = strings[0];
            String value = null;
            if (strings.length > 1) {
                // 原始 url 中的参数值不进行 decode
                value = strings[1];
            }
            map.put(name, value);
        }
        return map;
    }

    /**
     * 处理 originalUrl 中的占位符
     *
     * @param originalUrl originalUrl
     * @return url
     */
    public static String replaceBaseUrl(String originalUrl, String baseUrl) {
        if (originalUrl == null || originalUrl.length() == 0
                || baseUrl == null || baseUrl.length() == 0)
            return originalUrl;

        return URL_BASE_PATTERN.matcher(originalUrl).replaceAll(baseUrl);
    }

    /**
     * 字符串进行 url 解码
     *
     * @param value value
     * @return string
     */
    public static String decodeValue(String value) {
        return decodeValue(value, Charset.defaultCharset());
    }

    /**
     * 字符串进行 url 解码
     *
     * @param value   value
     * @param charset charset
     * @return string
     */
    public static String decodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLDecoder.decode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
        }
        return value;
    }

    /**
     * 字符串进行 url 编码
     *
     * @param value value
     * @return string
     */
    public static String encodeValue(String value) {
        return encodeValue(value, Charset.defaultCharset());
    }

    /**
     * 字符串进行 url 编码
     *
     * @param value   value
     * @param charset charset
     * @return string
     */
    public static String encodeValue(String value, Charset charset) {
        try {
            if (value != null && value.length() > 0)
                return URLEncoder.encode(value, charset == null ? Charset.defaultCharset().name() : charset.name());
        } catch (UnsupportedEncodingException e) {
            // Nothing to do
        }
        return value;
    }

    /**
     * 检查值是否含有占位符
     *
     * @param value value
     * @return boolean
     */
    public static boolean hasPlaceholder(String value) {
        return value != null && value.length() > 0 && PARAM_PLACEHOLDER_PATTERN.matcher(value).matches();
    }

    private static boolean isNotBlank(String v) {
        return !isBlank(v);
    }

    private static boolean isBlank(String v) {
        return v == null || v.length() == 0;
    }

    // ========================================================================

    /**
     * @return builder
     */
    public Builder forPath() {
        return forPath(null);
    }

    /**
     * @param path path
     * @return builder
     */
    public Builder forPath(String path) {
        try {
            StringBuffer result = new StringBuffer();
            result.append(isBlank(protocol.get()) ? url.getProtocol() : protocol.get());
            result.append(":");
            if (url.getAuthority() != null && url.getAuthority().length() > 0) {
                result.append("//");
                result.append(url.getAuthority());
            }
            if (url.getPath() != null)
                result.append(url.getPath());

            return new Builder(new URL(replaceBaseUrl(result.toString(), this.baseUrl.get())),
                    path, charset, ignoreEmpty, queryMap, values.get());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * ========================================================================
     * ===================          Builder 类       ==========================
     * ========================================================================
     */
    public static class Builder {

        final URL baseUrl;
        String path;
        Charset charset;
        boolean ignoreEmpty;
        // 原始参数构建 url 时不进行 encode
        final Map<String, Object> queryMap;
        // 非原始参数构建 url 时需要进行 encode
        final Map<String, Object> urlParams = new LinkedHashMap<>();
        final List<String> values;

        Builder(URL _baseUrl, String _path, Charset _charset, boolean _ignoreEmpty, Map<String, Object> _queryMap, List<String> _values) {
            this.baseUrl = _baseUrl;
            this.path = _path;
            this.charset = _charset;
            this.ignoreEmpty = _ignoreEmpty;
            this.queryMap = _queryMap;
            this.values = _values;
        }

        /**
         * 扩展 path
         *
         * @param path path
         * @return builder
         */
        public Builder apendPath(String path) {
            if (this.path == null)
                this.path = path;
            else if (path != null)
                this.path = this.path + path;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setCharsetName(String _charsetName) {
            if (_charsetName != null && _charsetName.length() > 0)
                this.charset = Charset.forName(_charsetName);
            return this;
        }

        public Builder setIgnoreEmpty(boolean ignoreEmpty) {
            this.ignoreEmpty = ignoreEmpty;
            return this;
        }

        /**
         * 取得参数表
         *
         * @return map
         */
        public Map<String, Object> getParamMap() {
            return new HashMap<>(this.urlParams);
        }

        /**
         * 执行构建 url
         *
         * @return String
         */
        public String build() {
            String path = prefixPath(baseUrl.getPath(), this.path);
            int port = baseUrl.getPort();
            if (baseUrl.getPort() == baseUrl.getDefaultPort())
                port = -1;

            String host = baseUrl.getHost();
            if (isNotBlank(host) && host.startsWith("."))
                host = host.substring(1);

            final StringBuilder builder;
            try {
                builder = new StringBuilder(new URL(baseUrl.getProtocol(), host, port, path).toString());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            StringBuilder query = new StringBuilder();
            // 添加原始参数表
            appendQueryString(query, this.queryMap, false);
            // 添加参数表
            appendQueryString(query, this.urlParams, true);
            if (query.length() > 0)
                query.replace(0, 1, "?");
            builder.append(query);

            // 进行占位符替换
            if (values != null && values.size() > 0)
                return MessageFormat.format(builder.toString(), values.toArray());

            return builder.toString();
        }

        /**
         * @param query    query
         * @param map      map
         * @param isEncode 是否进行编码
         */
        private void appendQueryString(StringBuilder query, Map<String, Object> map, boolean isEncode) {
            if (map == null || query == null)
                return;

            for (Entry<String, Object> entry : map.entrySet()) {
                final String key = entry.getKey();
                Object value = entry.getValue();
                if (value == null)
                    continue;

                if (value instanceof Object[]) {
                    for (final Object v : (Object[]) value) {
                        appendQueryString(query, key, v, isEncode);
                    }
                } else if (value instanceof Collection) {
                    for (final Object v : (Collection<?>) value) {
                        appendQueryString(query, key, v, isEncode);
                    }
                } else
                    appendQueryString(query, key, value, isEncode);
            }
        }

        /**
         * @param query query
         * @param key   key
         * @param value value
         */
        private void appendQueryString(StringBuilder query, String key, Object value, boolean isEncode) {
            if (value == null)
                return;

            String v = String.valueOf(value);
            if (ignoreEmpty && isBlank(v))
                return;

            query.append("&").append(key).append("=").append(isEncode && !hasPlaceholder(v) ? encodeValue(v, charset) : v);
        }


        /**
         * @param contextPath contextPath
         * @param path        path
         * @return string
         */
        private String prefixPath(String contextPath, String path) {
            if (path == null && contextPath == null)
                return "/";
            else if (path == null)
                return contextPath;
            else if (contextPath == null)
                return path;
            else if (path.startsWith("/") && contextPath.endsWith("/"))
                return contextPath + path.substring(1);
            else
                return contextPath + path;
        }

        /**
         * @param container container
         * @param o         o
         */
        private void append(List<Object> container, Object o) {
            if (o instanceof Object[]) {
                Collections.addAll(container, (Object[]) o);
            } else if (o instanceof Collection)
                container.addAll((Collection<?>) o);
            else
                container.add(o);
        }

        /**
         * 向参数表中增加一个参数
         * <p/>
         * 不覆盖原有参数值，允许多个同名参数
         *
         * @param key   key
         * @param value value
         * @return Builder
         */
        public Builder add(final String key, final Object value) {
            if (isNotBlank(key)) {
                Object newValue;
                if (urlParams.containsKey(key)) {
                    Object o = urlParams.get(key);
                    if (o == null) {
                        newValue = value;
                    } else {
                        List<Object> container = new LinkedList<>();
                        append(container, o);
                        append(container, value);
                        newValue = container;
                    }
                } else {
                    newValue = value;
                }
                urlParams.put(key, newValue);
            }
            return this;
        }

        /**
         * 向参数表中增加多个参数
         * <p/>
         * 不覆盖原有参数值，允许多个同名参数
         *
         * @param values values
         * @return Builder
         */
        public Builder add(final Map<String, ?> values) {
            if (values != null && values.size() > 0) {
                for (Entry<String, ?> entry : values.entrySet()) {
                    add(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        /**
         * 向参数表中设置一个参数
         * <p/>
         * 覆盖原有参数值
         *
         * @param key   key
         * @param value value
         * @return Builder
         */
        public Builder put(final String key, final Object value) {
            if (isNotBlank(key)) {
                if (queryMap != null)
                    queryMap.remove(key);

                urlParams.put(key, value);
            }

            return this;
        }

        /**
         * 向参数表中设置多个参数
         * <p/>
         * 覆盖原有参数值
         *
         * @param values values
         * @return Builder
         */
        public Builder put(final Map<String, ?> values) {
            if (values != null && values.size() > 0) {
                for (Entry<String, ?> entry : values.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }
    }

    /**
     * main
     *
     * @param args args
     */
    public static void main(String[] args) {
//        UrlBuilder ub = new UrlBuilder("//yun.$baseUrl$/{0}?param1={1}&param2={2}&callback=?");
        UrlBuilder ub = new UrlBuilder();
        ub.setProtocol("https");
        ub.setBaseUrl("nqcx.org");
        ub.setValue(0, "i/x");
        ub.setValue(1, "0");
        ub.setValue(2, UrlBuilder.encodeValue("黄保光"));
        ub.setValue(3, "nqcx");
        ub.setValue(4, "wq");

        System.out.println(ub.forPath("/{3}").add("account", "{4}").build());

        System.out.println(containProtocol("https://a"));
    }
}

/*
 * Copyright 2015 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nqcx.doox.commons.lang.consts.LoggerConst;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.*;

/**
 * @author nqcx 2013-10-22 下午2:13:57
 */
public class HttpRequest {

    private final static Logger logger = LoggerFactory.getLogger(HttpRequest.class);
    private final static Logger http_logger = LoggerFactory.getLogger(LoggerConst.LOGGER_HTTP_NAME);

    /**
     * get
     *
     * @param uri uri
     * @return {@link String}
     * @author naqichuan 10/27/21 5:04 PM
     */
    public static String get(String uri) {
        return get(uri, HttpMap.newInstance(), Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri     uri
     * @param httpMap httpMap
     * @return {@link String}
     * @author naqichuan 10/27/21 8:06 PM
     */
    public static String get(String uri, HttpMap httpMap) {
        return get(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri uri
     * @param map map
     * @return {@link String}
     * @author naqichuan 10/27/21 8:06 PM
     */
    public static String get(String uri, Map<String, Object> map) {
        return get(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri               uri
     * @param map               map
     * @param chareset          chareset
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:06 PM
     */
    public static String get(String uri, Map<String, Object> map,
                             String chareset, int connectionTimeout, int socketTimeout) {
        return get(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * get
     *
     * @param uri               uri
     * @param httpMap           httpMap
     * @param chareset          chareset
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:05 PM
     */
    public static String get(String uri, HttpMap httpMap, String chareset,
                             int connectionTimeout, int socketTimeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String paramString = null;
        if (httpMap != null)
            paramString = httpMap.buildString();

        http_logger.info("reqeust: get, uri: " + uri + ", params: [" + paramString + "]");

        if (paramString != null && paramString.length() > 0)
            uri += "?" + paramString;

        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(HttpConfig.config(connectionTimeout, socketTimeout));
        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return getResponseBody(response, chareset);
        } catch (IOException e) {
            logger.error("response: get, error: ", e);
        }

        return null;
    }

    /**
     * post
     *
     * @param uri uri
     * @return {@link String}
     * @author naqichuan 10/27/21 8:00 PM
     */
    public static String post(String uri) {
        return post(uri, HttpMap.newInstance(), Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri uri
     * @param map map
     * @return {@link String}
     * @author naqichuan 10/27/21 8:00 PM
     */
    public static String post(String uri, Map<String, Object> map) {
        return post(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri     uri
     * @param httpMap httpMap
     * @return {@link String}
     * @author naqichuan 10/27/21 8:00 PM
     */
    public static String post(String uri, HttpMap httpMap) {
        return post(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri               uri
     * @param map               map
     * @param chareset          chareset
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:00 PM
     */
    public static String post(String uri, Map<String, Object> map,
                              String chareset, int connectionTimeout, int socketTimeout) {
        return post(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * post
     *
     * @param uri               uri
     * @param httpMap           httpMap
     * @param chareset          chareset
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:00 PM
     */
    public static String post(String uri, HttpMap httpMap,
                              String chareset, int connectionTimeout, int socketTimeout) {
        List<NameValuePair> nvps = new ArrayList<>();
        if (httpMap != null)
            nvps.addAll(httpMap.buildList());

        try {
            http_logger.info("reqeust: post, uri:" + uri + ", params: " + nvps);

            return post(uri, chareset, null, new UrlEncodedFormEntity(nvps, chareset),
                    connectionTimeout, socketTimeout);
        } catch (UnsupportedEncodingException e) {
            logger.error("", e);
        }
        return null;
    }

    /**
     * post
     *
     * @param uri               uri
     * @param chareset          chareset
     * @param headers           headers
     * @param requestEntity     requestEntity
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:01 PM
     */
    private static String post(String uri, String chareset, Map<String, Object> headers, HttpEntity requestEntity,
                               int connectionTimeout, int socketTimeout) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(uri);
        // config
        httpPost.setConfig(HttpConfig.config(connectionTimeout, socketTimeout));
        // header
        Optional.ofNullable(headers).ifPresent(x -> x.forEach((k, v) -> {
            if (StringUtils.isNotBlank(k) && Objects.nonNull(v))
                httpPost.setHeader(k, v.toString());
        }));
        // endity
        httpPost.setEntity(requestEntity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return getResponseBody(response, chareset);
        } catch (Exception e) {
            logger.error("response: post, error: ", e);
        }
        return null;
    }

    /**
     * postJson
     *
     * @param uri  uri
     * @param json json
     * @return {@link String}
     * @author naqichuan 10/27/21 8:05 PM
     */
    public static String postJson(String uri, String json) {
        return postJson(uri, Consts.UTF_8.toString(), json, HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * postJson
     *
     * @param uri               uri
     * @param chareset          chareset
     * @param json              json
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/27/21 8:05 PM
     */
    public static String postJson(String uri, String chareset, String json,
                                  int connectionTimeout, int socketTimeout) {

        StringEntity jsonEntity = new StringEntity(json, chareset);
        jsonEntity.setContentEncoding(chareset);

        http_logger.info("reqeust: post, uri:" + uri + ", json body: " + json);

        return post(uri, chareset, HttpMap.newInstance().add("Content-type", "application/json").getMap(), jsonEntity,
                connectionTimeout, socketTimeout);
    }

    /**
     * postBinaryBody
     *
     * @param uri           uri
     * @param params        params
     * @param fileParamName fileParamName
     * @param fileName      fileName
     * @return {@link String}
     * @author naqichuan 10/28/21 9:21 AM
     */
    public static String postBinaryBody(String uri, HttpMap params, String fileParamName, String fileName) {
        return postBinaryBody(uri, Consts.UTF_8.toString(), null, params,
                fileParamName, fileName,
                HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);

    }

    /**
     * postBinaryBody
     *
     * @param uri               uri
     * @param chareset          chareset
     * @param headers           headers
     * @param params            params
     * @param fileParamName     fileParamName
     * @param fileName          fileName
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @return {@link String}
     * @author naqichuan 10/28/21 9:21 AM
     */
    public static String postBinaryBody(String uri, String chareset, HttpMap headers, HttpMap params,
                                        String fileParamName, String fileName,
                                        int connectionTimeout, int socketTimeout) {

        Charset chset = Consts.UTF_8;
        try {
            if (chareset != null)
                chset = Charset.forName(chareset);
        } catch (UnsupportedCharsetException ignore) {
        }

        final String boundary = "------" + StringUtils.randomCharAndNum(32);

        if (headers == null)
            headers = HttpMap.newInstance();

        headers.put("Content-Type", "multipart/form-data; boundary=" + boundary);

        //HttpEntity builder
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        //字符编码
        builder.setCharset(chset);
        //模拟浏览器
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        //boundary
        builder.setBoundary(boundary);
        //其他参数
        final ContentType textType = ContentType.create("text/plain", chset);
        if (params != null) {
            params.getMap().forEach((k, v) -> builder.addTextBody(k, String.valueOf(v), textType));
        }

        //// multipart/form-data
        //builder.addPart("multipartFile", new FileBody(file));
        // binary
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            try {
                builder.addBinaryBody("name=\"" + fileParamName + "\"; filename=\"" + file.getName() + "\"",
                        new FileInputStream(file), ContentType.MULTIPART_FORM_DATA, file.getName());// 文件流
            } catch (FileNotFoundException e) {
                logger.warn(e.getMessage());
            }
        }

        return post(uri, chset.toString(), headers.getMap(), builder.build(),
                connectionTimeout, socketTimeout);
    }

    /**
     * getResponseBody
     *
     * @param response response
     * @param chareset chareset
     * @return {@link String}
     * @author naqichuan 10/27/21 8:05 PM
     */
    private static String getResponseBody(CloseableHttpResponse response, String chareset) throws IOException {
        String responseBody;
        HttpEntity entity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200) {
            responseBody = EntityUtils.toString(entity, chareset);
            EntityUtils.consume(entity);
            return responseBody;
        }

        EntityUtils.consume(entity);
        responseBody = "HttpStatus " + response.getStatusLine().getStatusCode();

        return responseBody;
    }
}

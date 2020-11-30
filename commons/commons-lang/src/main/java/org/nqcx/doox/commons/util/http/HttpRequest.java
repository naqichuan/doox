/*
 * Copyright 2015 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.nqcx.doox.commons.lang.consts.LoggerConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
     * @param uri
     * @param httpMap
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String get(String uri, HttpMap httpMap) {
        return get(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri
     * @param map
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String get(String uri, Map<String, Object> map) {
        return get(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * get
     *
     * @param uri
     * @param map
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author 黄保光 Nov 6, 2013 3:40:06 PM
     */
    public static String get(String uri, Map<String, Object> map,
                             String chareset, int connectionTimeout, int socketTimeout) {
        return get(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * @param uri
     * @param httpMap
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author nqcx 2013-10-22 下午2:13:57
     * @author 黄保光 Nov 6, 2013 3:40:09 PM
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
     * @param uri
     * @param map
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String post(String uri, Map<String, Object> map) {
        return post(uri, map, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri
     * @param httpMap
     * @return
     * @author 黄保光 Nov 6, 2013 3:43:46 PM
     */
    public static String post(String uri, HttpMap httpMap) {
        return post(uri, httpMap, Consts.UTF_8.toString(), HttpConfig.CONNECTION_TIMEOUT, HttpConfig.SOCKET_TIMEOUT);
    }

    /**
     * post
     *
     * @param uri
     * @param map
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     */
    public static String post(String uri, Map<String, Object> map,
                              String chareset, int connectionTimeout, int socketTimeout) {
        return post(uri, HttpMap.newInstance().put(map), chareset,
                connectionTimeout, socketTimeout);
    }

    /**
     * post
     *
     * @param uri
     * @param httpMap
     * @param chareset
     * @param connectionTimeout
     * @param socketTimeout
     * @return
     * @author 黄保光 Nov 6, 2013 3:40:06 PM
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
     * @param uri
     * @param chareset
     * @param headers
     * @param requestEntity
     * @param connectionTimeout
     * @param socketTimeout
     * @return
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
     * post json
     *
     * @param uri
     * @param chareset
     * @param json
     * @param connectionTimeout
     * @param socketTimeout
     * @return String
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
     * @param response
     * @param chareset
     * @return
     * @throws IOException
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

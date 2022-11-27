/*
 * Copyright 2021 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.http;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.tika.Tika;
import org.nqcx.doox.commons.lang.consts.LoggerConst;
import org.nqcx.doox.commons.util.StringUtils;
import org.nqcx.doox.commons.util.io.Output;
import org.nqcx.doox.commons.util.io.OutputType;
import org.nqcx.doox.commons.util.io.Resource;
import org.nqcx.doox.commons.util.io.ResourceStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author naqichuan 10/28/21 3:49 PM
 */
public class HttpDownload {

    private final static Logger logger = LoggerFactory.getLogger(HttpDownload.class);
    private final static Logger http_logger = LoggerFactory.getLogger(LoggerConst.LOGGER_HTTP_NAME);


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
    public static void postBinaryBody(String uri, HttpMap params, String fileParamName, String fileName, String outputFileName) {
        postBinaryBody(uri, Consts.UTF_8.toString(), null, params,
                fileParamName, fileName, outputFileName,
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
     * @param outputFileName    outputFileName
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @author naqichuan 10/28/21 4:46 PM
     */
    public static void postBinaryBody(String uri, String chareset, HttpMap headers, HttpMap params,
                                      String fileParamName, String fileName, String outputFileName,
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
                String mimeType = new Tika().detect(file);
                ContentType contentType = ContentType.getByMimeType(mimeType);
                if (contentType == null)
                    contentType = ContentType.DEFAULT_BINARY;

                builder.addBinaryBody(fileParamName,
                        new FileInputStream(file), contentType, file.getName());// 文件流
            } catch (Exception e) {
                logger.warn(e.getMessage());
            }
        }

        post(uri, chset.toString(), headers.getMap(), builder.build(), outputFileName,
                connectionTimeout, socketTimeout);
    }

    /**
     * post
     *
     * @param uri               uri
     * @param chareset          chareset
     * @param headers           headers
     * @param requestEntity     requestEntity
     * @param outputFileName    outputFileName
     * @param connectionTimeout connectionTimeout
     * @param socketTimeout     socketTimeout
     * @author naqichuan 10/28/21 4:46 PM
     */
    private static void post(String uri, String chareset, Map<String, Object> headers,
                             HttpEntity requestEntity,
                             String outputFileName,
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

            Resource rr = new ResourceStream(response.getEntity().getContent());
            rr.out(Output.of(Paths.get(outputFileName), OutputType.BYTE));

        } catch (Exception e) {
            logger.error("response: post, error: ", e);
        }
    }
}

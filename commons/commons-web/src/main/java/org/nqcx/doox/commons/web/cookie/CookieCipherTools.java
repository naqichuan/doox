/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.cookie;

import org.nqcx.doox.commons.util.StringUtils;
import org.nqcx.doox.commons.util.security.Base32;
import org.nqcx.doox.commons.util.security.DESCoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class CookieCipherTools {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String charsetName;

    public String encrypt(String value, String key) {
        try {
            byte[] data;
            if (!StringUtils.isEmpty(charsetName)) {
                try {
                    data = value.getBytes(charsetName);
                } catch (Exception e1) {
                    logger.error("charset " + charsetName + " Unsupported!", e1);
                    data = value.getBytes();
                }
            } else {
                data = value.getBytes();
            }
            byte[] bytes = encrypt(key, data);
            return encoding(bytes);
        } catch (Exception e) {
            logger.error("encrypt error", e);
            return null;
        }
    }

    private String encoding(byte[] bytes) throws Exception {
        return Base32.encode(bytes);
    }

    private byte[] decoding(String value) throws Exception {
        return Base32.decode(value);
    }

    private byte[] encrypt(String key, byte[] data) throws Exception {
        return DESCoder.encrypt(data, key);
    }

    private byte[] decrypt(String key, byte[] data) throws Exception {
        return DESCoder.decrypt(data, key);
    }

    public String decrypt(String value, String key) {
        try {
            byte[] data = decoding(value);
            byte[] bytes = decrypt(key, data);
            if (!StringUtils.isEmpty(charsetName)) {
                try {
                    return new String(bytes, charsetName);
                } catch (UnsupportedEncodingException e1) {
                    logger.error("charset " + charsetName + " Unsupported!", e1);
                    return new String(bytes);
                }
            } else {
                return new String(bytes);
            }
        } catch (Exception e) {
            logger.error("encrypt error", e);
            return null;
        }
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }
}

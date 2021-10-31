/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * @author naqichuan 2014年8月14日 上午11:49:18
 */
@SuppressWarnings("restriction")
public class DESCoder {

    private final static String DEFAULT_KEY = "_nqcx_";

    public static byte[] encrypt(byte[] bytes) {
        return encrypt(bytes, DEFAULT_KEY);
    }

    public static byte[] encrypt(byte[] bytes, String key) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;

    }

    public static byte[] decrypt(byte[] bytes) {
        return decrypt(bytes, DEFAULT_KEY);
    }

    public static byte[] decrypt(byte[] bytes, String key) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(key));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }

    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static byte[] decryptBASE64(String key) throws IOException {
        return Base64.decodeBase64(key);
    }

    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }

    private static Key getKey(String key) {
        if (key == null || key.length() == 0)
            key = DEFAULT_KEY;
        try {
            DESKeySpec dks = new DESKeySpec(decryptBASE64(key));

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(dks);

            return secretKey;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        byte[] value = null;
        try {
            value = DESCoder.encrypt("nqcx".getBytes("UTF-8"), "123111111111111");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        System.out.println(value);

        value = DESCoder.decrypt(value, "123111111111111");

        System.out.println(new String(value));
    }
}

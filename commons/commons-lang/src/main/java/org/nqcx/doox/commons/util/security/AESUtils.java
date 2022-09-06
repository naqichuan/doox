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
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;

/**
 * @author naqichuan 2014年8月14日 上午11:49:38
 */
public class AESUtils {

    public final static String DEFAULT_CHARSET = "UTF-8";
    private final static String DEFAULT_KEY = "HP1ozhw3WrhSIo2X";

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encrypt(String text) {
        return encrypt(text, DEFAULT_KEY);
    }

    /**
     * 加密
     *
     * @param text
     * @param key
     * @return
     */
    public static String encrypt(String text, String key) {
        try {
            return HexUtils.toHexString(encrypt(text.getBytes(DEFAULT_CHARSET), key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encryptBase64(String text) {
        return encryptBase64(text, DEFAULT_KEY);
    }

    /**
     * 加密
     *
     * @param text
     * @param key
     * @return
     */
    public static String encryptBase64(String text, String key) {
        try {
            return Base64.encodeBase64String(encrypt(text.getBytes(DEFAULT_CHARSET), key));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     *
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes) {
        return encrypt(bytes, DEFAULT_KEY);
    }


    /**
     * 加密
     *
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] bytes, String key) {
        return (bytes == null || key == null) ? null : encrypt(bytes, key.getBytes());
    }

    /**
     * 加密
     *
     * @param bytes
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] bytes, byte[] keyBytes) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(keyBytes));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteFina;
    }


    /**
     * 解密
     *
     * @param text
     * @return
     */
    public static String decrypt(String text) {
        return decrypt(text, DEFAULT_KEY);
    }

    /**
     * 解密
     *
     * @param text
     * @param key
     * @return
     */
    public static String decrypt(String text, String key) {
        byte[] b = decrypt(HexUtils.toByteArray(text), key);
        return b == null ? null : new String(b);
    }

    /**
     * 解密
     *
     * @param text
     * @return
     */
    public static String decryptBase64(String text) {
        return decryptBase64(text, DEFAULT_KEY);
    }

    /**
     * 解密
     *
     * @param text
     * @param key
     * @return
     */
    public static String decryptBase64(String text, String key) {
        try {
            byte[] b = decrypt(Base64.decodeBase64(text), key);
            if (b != null)
                return new String(b, DEFAULT_CHARSET);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param bytes
     * @return
     */
    public static byte[] decrypt(byte[] bytes) {
        return decrypt(bytes, DEFAULT_KEY);
    }


    /**
     * 解密
     *
     * @param bytes
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] bytes, String key) {
        return (bytes == null || key == null) ? null : decrypt(bytes, key.getBytes());
    }

    /**
     * 解密
     *
     * @param bytes
     * @param keyBytes
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] bytes, byte[] keyBytes) {
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(keyBytes));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return byteFina;
    }

    /**
     * @param keyBytes keyBytes
     * @return kye
     */
    private static Key getKey(byte[] keyBytes) {
        if (keyBytes == null || keyBytes.length == 0)
            keyBytes = DEFAULT_KEY.getBytes();
        try {
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
//        HashMap<String, Object> map = new HashMap<String, Object>();
//        map.put("bookId", "1111112");
//        map.put("bs", "basic");
//        map.put("filetype", "TXT");
//        map.put("timestamp", new Date().getTime());
//        String json = JsonUtils.mapToJson(map);
//        String idEncrypt = encrypt(json);
//        System.out.println(idEncrypt);
//        String idDecrypt = decrypt(idEncrypt);
//        System.out.println(idDecrypt);


        String bb = encryptBase64("1111111111111111111111111111111111dfa1", "1IphGIeSwIOPRLlX");
        System.out.println(bb);
        String aa = decryptBase64("HjBJmW/HNCPn/layAatb4g==", "1IphGIeSwIOPRLlX");
        System.out.println(aa);
    }
}

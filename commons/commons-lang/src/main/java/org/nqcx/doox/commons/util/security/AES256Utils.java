/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.security;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.Security;

/**
 * @author naqichuan 17/6/1 17:19
 */
public class AES256Utils {

    public final static String DEFAULT_CHARSET = "UTF-8";
    private final static String DEFAULT_KEY = "HP1ozhw3WrhSIo2X";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param text
     * @return
     */
    public static String encryptHex(String text) {
        return encryptHex(text, DEFAULT_KEY);
    }

    /**
     * 加密
     *
     * @param text
     * @param key
     * @return
     */
    public static String encryptHex(String text, String key) {
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
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(key));
            byteFina = cipher.doFinal(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cipher = null;
        }
        return byteFina;
    }


    /**
     * 解密
     *
     * @param text
     * @return
     */
    public static String decryptHex(String text) {
        return decryptHex(text, DEFAULT_KEY);
    }

    /**
     * 解密
     *
     * @param text
     * @param key
     * @return
     */
    public static String decryptHex(String text, String key) {
        return new String(decrypt(HexUtils.toByteArray(text), key));
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
            return new String(decrypt(Base64.decodeBase64(text), key), DEFAULT_CHARSET);
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
        byte[] byteFina = null;
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
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
     * @param key
     * @return
     */
    private static Key getKey(String key) {
        if (key == null || key.length() == 0)
            key = DEFAULT_KEY;
        try {
            return new SecretKeySpec(key.getBytes(DEFAULT_CHARSET), "AES");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String bb = encryptBase64("149578270125927d52db2c72", "206d01af40403a09e782bafd82f1f320");
        System.out.println(bb);
        String aa = decryptBase64("KUqxTSqZ8bBvTLLhMSLI2EWHxSVHHbb44AQ+DoPIV28=", "206d01af40403a09e782bafd82f1f320");
        System.out.println(aa);
    }
}

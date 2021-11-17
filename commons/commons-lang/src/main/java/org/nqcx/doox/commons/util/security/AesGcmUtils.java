/*
 * Copyright 2021 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.security;

import org.nqcx.doox.commons.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author naqichuan 11/16/21 12:13 PM
 */
public class AesGcmUtils {


    private static final int KEY_LENGTH_BYTE = 32;
    private static final int TAG_LENGTH_BIT = 128;

    /**
     * 加密
     *
     * @param aad        aad
     * @param nonce      nonce
     * @param cipherText cipherText
     * @param key        key
     * @return {@link String}
     * @author naqichuan 11/16/21 12:26 PM
     */
    public static String decryptToString(String aad, String nonce, String cipherText, String key)
            throws GeneralSecurityException {

        aad = StringUtils.trimToEmpty(aad);
        nonce = StringUtils.trimToEmpty(nonce);
        cipherText = StringUtils.trimToEmpty(cipherText);

        if (key.length() != KEY_LENGTH_BYTE)
            throw new IllegalArgumentException("key，长度必须为32个字节");

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec noceSpec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.DECRYPT_MODE, keySpec, noceSpec);
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));

            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }


    /**
     * encryptToString
     *
     * @param aad   aad
     * @param nonce nonce
     * @param text  text
     * @return {@link String}
     * @author naqichuan 7/12/21 10:48 PM
     */
    public static String encryptToString(String aad, String nonce, String text, String key)
            throws GeneralSecurityException {

        aad = StringUtils.trimToEmpty(aad);
        nonce = StringUtils.trimToEmpty(nonce);
        text = StringUtils.trimToEmpty(text);

        if (key.length() != KEY_LENGTH_BYTE)
            throw new IllegalArgumentException("key，长度必须为32个字节");

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec nonceSpec = new GCMParameterSpec(TAG_LENGTH_BIT, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, nonceSpec);
            cipher.updateAAD(aad.getBytes(StandardCharsets.UTF_8));

            return new String(Base64.getEncoder().encode(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8))),
                    StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * main
     *
     * @param args args
     * @author naqichuan 7/12/21 10:48 PM
     */
    public static void main(String[] args) throws GeneralSecurityException {
        String text = "ldsf;asd;kldfs;lkasd;kldfs;kljdasf;kjldasf;jkldsaf;jkldsfa;jklsdaf;jklasdf;jlk";


        String key = StringUtils.randomCharAndNum(32);

        String encryptText = encryptToString("xxx", "abcd", text, key);

        System.out.println(encryptText);

        String decryptText = decryptToString("xxx", "abcd", encryptText, key);
        System.out.println(decryptText);
    }
}

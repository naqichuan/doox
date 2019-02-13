/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.login;

import org.nqcx.doox.commons.util.date.DateUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Date;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginTicketUtils {

    public final static String ENCODING = "UTF-16LE";

    public static String generateTickBolob(String random, String account, String userdata, Date issueDate, Date expires, String key)
            throws Exception {
        return new String(encrypt(
                createTickBolob(random.getBytes(),
                        account.getBytes(ENCODING),
                        userdata.getBytes(ENCODING),
                        dateToByteArray(issueDate),
                        dateToByteArray(expires)), key));
    }

    private static byte[] createTickBolob(byte[] random, byte[] account, byte[] userData, byte[] issueDate, byte[] expires)
            throws Exception {
        if (random.length != 8)
            throw new Exception("random");
        int bufferLength = 4;
        bufferLength += random.length;
        bufferLength += account.length;
        bufferLength += userData.length;
        bufferLength += issueDate.length;
        bufferLength += expires.length;

        byte[] buffer = new byte[bufferLength];
        int pos = 0;

        System.arraycopy(random, 0, buffer, pos, random.length);
        pos += random.length;
        System.arraycopy(account, 0, buffer, pos, account.length);
        pos += account.length;
        buffer[pos++] = 0;
        buffer[pos++] = 0;
        System.arraycopy(userData, 0, buffer, pos, userData.length);
        pos += userData.length;
        buffer[pos++] = 0;
        buffer[pos++] = 0;
        System.arraycopy(issueDate, 0, buffer, pos, issueDate.length);
        pos += issueDate.length;
        System.arraycopy(expires, 0, buffer, pos, expires.length);
        pos += expires.length;

        return buffer;
    }

    public static LoginTicket getLoginTicket(String desc, String key) throws Exception {
        return parseTickBolob(decrypt(desc, key));
    }

    public static LoginTicket parseTickBolob(byte[] ticketBytes) throws Exception {

        LoginTicket ticket = null;
        int pos = 8;

        byte[] account = readUtf16le(ticketBytes, pos);
        pos += account.length + 2;

        byte[] userData = readUtf16le(ticketBytes, pos);
        pos += userData.length + 2;

        byte[] issueDate = new byte[8];
        System.arraycopy(ticketBytes, pos, issueDate, 0, issueDate.length);
        pos += issueDate.length;

        byte[] expires = new byte[8];
        System.arraycopy(ticketBytes, pos, expires, 0, expires.length);
        pos += expires.length;

        String _account = new String(account, ENCODING);
        String _userData = new String(userData, ENCODING);
        Date _issueDate = byteArrayToDate(issueDate);
        Date _expires = byteArrayToDate(expires);
        ticket = new LoginTicket(_account, _userData, null, _issueDate, _expires, 0, false);

        return ticket;
    }

    public static String encrypt(byte[] encrypted, String key) throws Exception {
        if (null == encrypted || encrypted.length < 1) {
            return null;
        }

        byte[] keybytes = LoginTicketUtils.hexToByte(key);
        byte[] ivbytes = new byte[16];

        SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");

        IvParameterSpec iv = new IvParameterSpec(ivbytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

        byte[] original = cipher.doFinal(encrypted);

        String originalString = LoginTicketUtils.byte2hex(original);
        return originalString;

    }

    public static byte[] decrypt(String str, String key) throws Exception {
        if (null == str || str.trim().length() < 1) {
            return null;
        }

        byte[] keybytes = hexToByte(key);
        byte[] ivbytes = new byte[16];

        SecretKeySpec skeySpec = new SecretKeySpec(keybytes, "AES");

        IvParameterSpec iv = new IvParameterSpec(ivbytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

        byte[] encrypted1 = hexToByte(str);

        byte[] original = cipher.doFinal(encrypted1);

        return original;
    }

    public static byte[] hexToByte(String s) throws IOException {
        int i = s.length() / 2;
        byte abyte0[] = new byte[i];
        int j = 0;
        if (s.length() % 2 != 0)
            throw new IOException("hexadecimal string with odd number of characters");
        for (int k = 0; k < i; k++) {
            char c = s.charAt(j++);
            int l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
            if (l == -1)
                throw new IOException("hexadecimal string contains non hex character");
            int i1 = (l & 0xf) << 4;
            c = s.charAt(j++);
            l = "0123456789abcdef0123456789ABCDEF".indexOf(c);
            i1 += l & 0xf;
            abyte0[k] = (byte) i1;
        }

        return abyte0;
    }

    /**
     * 转换成十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";

        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;

        }
        return hs.toUpperCase();
    }

    private static byte[] readUtf16le(byte[] ticketBytes, int start) {
        int end = checkUtf16leEnd(ticketBytes, start);
        if (end < 0) {
            return new byte[0];
        } else {
            int len = end - start;
            byte[] desc = new byte[len];
            System.arraycopy(ticketBytes, start, desc, 0, len);
            return desc;
        }
    }

    private static int checkUtf16leEnd(byte[] ticketBytes, int start) {
        int end = ticketBytes.length - start;

        for (int i = start; i < ticketBytes.length - 1; i += 2) {
            byte i1 = ticketBytes[i];
            byte i2 = ticketBytes[i + 1];
            if (i1 == 0 && i2 == 0) {
                end = i;
                break;
            }
        }
        return end;
    }

    public static byte[] dateToByteArray(Date date) {
        long longDate = date.getTime();
        longDate *= 10000;
        longDate += 116444736000000000L;
        return longToByteArray(longDate);
    }

    public static Date byteArrayToDate(byte[] bytes) throws Exception {
        if (bytes.length != 8)
            throw new Exception("must be 8 bytes");
        long date = byteArrayToLong(bytes);
        return new Date((date - 116444736000000000L) / 10000);
    }

    public static byte[] longToByteArray(long l) {
        byte[] bArray = new byte[8];
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        lBuffer.put(0, l);
        return bArray;
    }

    public static long byteArrayToLong(byte[] bArray) {
        ByteBuffer bBuffer = ByteBuffer.wrap(bArray);
        bBuffer.order(ByteOrder.LITTLE_ENDIAN);
        LongBuffer lBuffer = bBuffer.asLongBuffer();
        long l = lBuffer.get(0);
        return l;
    }

    public static void main(String[] args) {
        String key = "0123456789abcdef0123456789abcdef";
        byte[] testByte = null;
        try {
            testByte = createTickBolob("123iudf8".getBytes(),
                    "123".getBytes(ENCODING),
                    "ad".getBytes(ENCODING),
                    LoginTicketUtils.dateToByteArray(new Date()),
                    LoginTicketUtils.dateToByteArray(DateUtils.addMinutes(new Date(), 30)));
            String encrypt = encrypt(testByte, key);
            System.out.println(encrypt);

            byte[] originalByte = decrypt(encrypt, key);
            LoginTicket ticket = parseTickBolob(originalByte);

            System.out.println(ticket.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import java.io.*;

/**
 * @author naqichuan 14/12/3 10:44
 */
public class Output {

    protected final static int BUF_LEN = 1024;

    /**
     * 输出流
     */
    protected OutputStream outputStream;

    /**
     * 适配类型
     */
    protected OutputType outputType = OutputType.BYTE;

    /**
     * Output
     *
     * @author naqichuan 11/8/21 2:27 PM
     */
    public Output() {
        outputStream = System.out;
    }

    public Output(OutputType outputType) {
        outputStream = System.out;
        this.outputType = outputType;
    }

     public Output(File file) throws FileNotFoundException {
        this(new FileOutputStream(file));
    }

    public Output(File file, OutputType outputType) throws FileNotFoundException {
        this(new FileOutputStream(file), outputType);
    }

    public Output(OutputStream outputStream) {
        this(outputStream, OutputType.BYTE);
    }

    public Output(OutputStream outputStream, OutputType outputType) {
        this.outputStream = outputStream;
        if (outputType != null)
            this.outputType = outputType;
    }



    public void out(InputStream in) throws IOException {
        if (OutputType.CHAR.is(this.outputType))
            writeChar(BUF_LEN, in, outputStream);

        writeByte(BUF_LEN, in, outputStream);
    }

    /**
     * 按照缓冲长度大小，将输入字符流内数据读出后写到输出字符流中
     *
     * @param buflen       buflen
     * @param inputStream  inputStream
     * @param outputStream outputStream
     * @author naqichuan 11/8/21 2:29 PM
     */
    protected void writeChar(int buflen, InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(outputStream));
        char[] data = new char[buflen];
        int len;
        while ((len = in.read(data)) > -1) {
            out.write(data, 0, len);
            out.flush();
        }

        in.close();
        out.close();
    }

    /**
     * 按照缓冲长度大小，将输入流内数据读出后写到输出流中
     *
     * @param buflen       缓冲长度
     * @param inputStream  读取输入流
     * @param outputStream 写出输出流
     * @throws IOException
     */
    protected void writeByte(int buflen, InputStream inputStream, OutputStream outputStream) throws IOException {
        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedOutputStream out = new BufferedOutputStream(outputStream);
        byte[] data = new byte[buflen];
        int len;
        while ((len = in.read(data)) > -1) {
            out.write(data, 0, len);
            out.flush();
        }

        in.close();
        out.close();
    }
}

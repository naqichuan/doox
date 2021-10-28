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

    public Output() {
        outputStream = System.out;
    }

    public Output(OutputType outputType) {
        outputStream = System.out;
        this.outputType = outputType;
    }

    public Output(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public Output(OutputStream outputStream, OutputType outputType) {
        this.outputStream = outputStream;
        this.outputType = outputType;
    }

    public void out(InputStream in) throws IOException {
        if (OutputType.CHAR == this.outputType)
            writeChar(BUF_LEN, in, outputStream);
        else
            writeByte(BUF_LEN, in, outputStream);
    }

    /**
     * 按照缓冲长度大小，将输入字符流内数据读出后写到输出字符流中
     *
     * @param buflen
     * @param inputStream
     * @param outputStream
     * @throws IOException
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

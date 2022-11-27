/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author naqichuan 14/12/3 10:44
 */
public class Output {

    private static final int BUFFER_SIZE = 8192;

    /**
     * 输出流
     */
    protected OutputStream out;

    /**
     * 适配类型
     */
    protected OutputType outputType = OutputType.BYTE;

    /**
     * 字符集
     */
    private final Charset charset = StandardCharsets.UTF_8;

    /**
     * Output
     *
     * @author naqichuan 11/8/21 2:27 PM
     */
    private Output(OutputStream out) {
        this.out = out;
    }

    /**
     * Output
     *
     * @author naqichuan 11/8/21 2:27 PM
     */
    private Output(OutputStream out, OutputType outputType) {
        this.out = out;
        if (outputType != null)
            this.outputType = outputType;
    }

    public static Output of() {
        return of(System.out);
    }

    public static Output of(OutputStream outputStream) {
        return new Output(outputStream, OutputType.BYTE);
    }

    public static Output of(OutputStream outputStream, OutputType outputType) {
        return new Output(outputStream, outputType);
    }

    public static Output of(Path path) {
        return of(path, OutputType.BYTE);
    }

    public static Output of(Path path, OutputType outputType) {
        try {
            return new Output(Files.newOutputStream(path), outputType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Output out(InputStream in) {
        try {
            if (OutputType.CHAR.is(this.outputType))
                writeChar(in, out);
            else
                writeByte(in, out);

            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Output out(Path path) {
        try {
            Files.copy(path, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public Output out(byte[] bytes) {
        Objects.requireNonNull(bytes);

        try {
            int len = bytes.length;
            int rem = len;
            while (rem > 0) {
                int n = Math.min(rem, BUFFER_SIZE);
                out.write(bytes, (len - rem), n);

                rem -= n;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public void close() {
        try {
            if (out != null)
                out.close();
        } catch (IOException ignore) {
        }
    }


    /**
     * 按照缓冲长度大小，将输入字符流内数据读出后写到输出字符流中
     *
     * @param source source
     * @param sink   sink
     * @author naqichuan 2022/11/3 下午2:19
     */
    protected static void writeChar(InputStream source, OutputStream sink)
            throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(source));
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sink));

        char[] chars = new char[BUFFER_SIZE];
        int n;
        while ((n = in.read(chars)) > -1) {
            out.write(chars, 0, n);
        }
    }


    protected static void writeByte(InputStream source, OutputStream sink)
            throws IOException {
        long nread = 0L;
        byte[] buf = new byte[BUFFER_SIZE];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
        }
    }
}

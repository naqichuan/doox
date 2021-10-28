/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author naqichuan 14/12/2 16:48
 */
public class FileUtils {

    public final static String[] SPECIAL_EXTENSIONS = {"tar.gz"};

    /**
     * Java文件操作 获取文件名及扩展名
     *
     * @param fileName
     * @return 返回长度为2的字符串数组，第一位为文件名，第二位为扩展名
     */
    public static String[] getFileNames(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            String[] fileNameSplit = new String[2];

            int dot = indexOfSpecialExtensions(fileName);
            if (dot == -1 && (dot = fileName.lastIndexOf('.')) == -1) {
                fileNameSplit[0] = fileName;
                fileNameSplit[1] = null;
            } else {

                fileNameSplit[0] = fileName.substring(0, dot);
                fileNameSplit[1] = fileName.substring(dot + 1);
            }

            return fileNameSplit;
        }
        return null;
    }


    /**
     * 判断是否有扩展名，如果有刚返回扩展名位置
     *
     * @param fileName
     * @return -1 表示没有扩展名
     */
    private static int indexOfSpecialExtensions(String fileName) {
        if (fileName == null || fileName.length() == 0 || SPECIAL_EXTENSIONS == null || SPECIAL_EXTENSIONS.length == 0)
            return -1;

        for (String se : SPECIAL_EXTENSIONS) {
            if (se != null && se.length() > 0 && fileName.endsWith("." + se))
                return fileName.indexOf("." + se);
        }

        return -1;
    }

    /**
     * 取扩展名
     *
     * @param fileName fileName
     * @return {@link String}
     * @author naqichuan 8/18/21 5:04 PM
     */
    public static String getFileExtName(String fileName) {
        String[] names = getFileNames(fileName);
        return names == null ? null : names[1];
    }

    /**
     * readFile
     *
     * @param fileName fileName
     * @return {@link List <String>}
     * @author naqichuan 7/29/21 1:53 PM
     */
    static List<String> readFile(String fileName) {
        return readFile(new File(fileName));
    }

    /**
     * readFile
     *
     * @param file file
     * @return {@link List<String>}
     * @author naqichuan 7/29/21 1:53 PM
     */
    public static List<String> readFile(File file) {
        List<String> lines = new ArrayList<>();
        if (file == null || !file.exists() || !file.isFile()) {
            System.out.printf("读取文件 [%s] 失败!\n", file == null ? "[]" : file.getPath());
            return null;
        }

        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String str;
            while ((str = br.readLine()) != null) {
                lines.add(str);
            }
        } catch (IOException e) {
            throw new RuntimeException("File exists.", e);
        }

        return lines;
    }

    /**
     * writeFile
     *
     * @param fileName fileName
     * @param lines    lines
     * @author naqichuan 7/29/21 1:53 PM
     */
    static void writeFile(String fileName, List<String> lines) {
        writeFile(new File(fileName), lines);
    }

    /**
     * writeFile
     *
     * @param file  file
     * @param lines lines
     * @author naqichuan 7/29/21 1:53 PM
     */
    public static void writeFile(File file, List<String> lines) {
        writeFile(file, lines, false);
    }

    /**
     * writeFile
     *
     * @param file  file
     * @param lines lines
     * @author naqichuan 7/29/21 1:53 PM
     */
    public static void writeFile(File file, List<String> lines, boolean appending) {
        if (file.exists() && !appending && !file.delete())
            throw new RuntimeException("删除文件失败");

        if (lines == null)
            return;

        try (FileWriter fw = new FileWriter(file, appending)) {
            for (String line : lines) {
                fw.write(line + "\n");
            }
            fw.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String[] aa = getFileNames("aa.tar");
        System.out.println(aa);
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.file;

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

    public static void main(String[] args) {
        String[] aa = getFileNames("aa.tar");
        System.out.println(aa);
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.image;

import magick.CompressionType;
import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import java.awt.*;
import java.io.File;

/**
 * @author naqichuan 14/12/20 23:28
 */
public class ImageUtils {
    /**
     * 处理图片
     */
    public static void processImage(File source, File dest, double width, double height)
            throws MagickException {
        System.setProperty("jmagick.systemclassloader", "no");

        MagickImage sourceImage = null;
        MagickImage destImage = null;
        try {
            ImageInfo info = new ImageInfo(source.getPath());
            //设置图片质量
//            info.setQuality(IMG_QUALITY);
            info.setQuality(200);
            sourceImage = new MagickImage(info);
            Dimension dimension = sourceImage.getDimension();

            Double sourceWidth = dimension.getWidth();
            Double sourceHeight = dimension.getHeight();
            Double destWidth = null;
            Double destHeight = null;

            if (!dest.getParentFile().exists())
                dest.getParentFile().mkdirs();

            if (width == 0 && height == 0) {
                width = sourceWidth;
                height = sourceHeight;
            }
            if (sourceWidth > sourceHeight) {
                destWidth = width;
                destHeight = width / sourceWidth * sourceHeight;
                if (destHeight > height && height != 0) {
                    destHeight = height;
                    destWidth = height / sourceHeight * sourceWidth;
                }
            } else {
                destHeight = height;
                destWidth = height / sourceHeight * sourceWidth;
                if (destWidth > width || height == 0) {
                    destWidth = width;
                    destHeight = width / sourceWidth * sourceHeight;
                }
            }
            destImage = sourceImage.zoomImage(destWidth.intValue(), destHeight.intValue());
            // .sharpenImage(SHARPEN_IMG_RADUIS, SHARPEN_IMG_SIGMA);
            //水平分辨率DPI
//            destImage.setXResolution(IMG_XRES);
            destImage.setXResolution(100);
            //垂直分辨率DPI
//            destImage.setYResolution(IMG_YRES);
            destImage.setYResolution(100);
            destImage.profileImage("*", null);
            //设置图片压缩
            destImage.setCompression(CompressionType.JPEGCompression);
            //设置对比度
            destImage.contrastImage(true);
            destImage.setFileName(dest.getPath());
            destImage.writeImage(info);
            info = null;
        } finally {
            if (destImage != null) {
                destImage.destroyImages();
            }
            if (sourceImage != null) {
                sourceImage.destroyImages();
            }
        }
    }
}

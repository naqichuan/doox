/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author naqichuan 14/12/3 10:47
 */
public class ResourceFile extends Resource {

//    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected String pathName;
    private File file;
    private boolean exist = false;
    private long length = 0;

    public ResourceFile(String pathName) {
        this.pathName = pathName;
        if (StringUtils.isNotBlank(pathName)) {
            file = new File(this.pathName);
            exist = file.exists();
            if (exist)
                length = file.length();
        }
    }

    @Override
    protected InputStream getIn() throws IOException {
        if (this.isExist())
            return new FileInputStream(file);
        return null;
    }

    public boolean isExist() {
        return exist;
    }

    public long getLength() {
        return length;
    }

//    public static void main(String[] args) {
//        Resource rr = new ResourceFile("abcd.aa");
//        try {
//            rr.out(new Output(new FileOutputStream("aa.abcd"), OutputType.BYTE));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}

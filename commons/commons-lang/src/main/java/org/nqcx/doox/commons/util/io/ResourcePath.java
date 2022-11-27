/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author naqichuan 14/12/3 10:47
 */
public class ResourcePath implements Resource {

    private final Path path;

    public ResourcePath(Path path) {
        if (path == null)
            throw new RuntimeException("path can't be null.");

        this.path = path;
    }

    public boolean isExist() {
        return Files.exists(path);
    }

    public long getLength() {
        return isExist() ? path.toFile().length() : 0L;
    }

    @Override
    public Output out(Output output) {
        return output.out(path);
    }

    public static void main(String[] args) {
        Resource rr = new ResourcePath(Paths.get("abcd.aa"));
        rr.out(Output.of(Paths.get("aa.abcd"), OutputType.BYTE));
    }
}

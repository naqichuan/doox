/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

/**
 * @author naqichuan 14/12/3 10:47
 */
public class ResourceByte implements Resource {

    private final byte[] bytes;

    public ResourceByte(byte[] bytes) {
        if (bytes == null)
            throw new RuntimeException("bytes can't be null.");
        this.bytes = bytes;
    }

    @Override
    public Output out(Output output) {
        return output.out(bytes);
    }
}

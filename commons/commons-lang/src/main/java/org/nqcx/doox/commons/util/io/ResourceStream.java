/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import java.io.InputStream;

/**
 * @author naqichuan 14/12/3 10:47
 */
public class ResourceStream implements Resource {
    private final InputStream inputStream;

    public ResourceStream(InputStream inputStream) {
        if (inputStream == null)
            throw new RuntimeException("path can't be null.");

        this.inputStream = inputStream;
    }

    @Override
    public Output out(Output output) {
        return output.out(inputStream);
    }
}

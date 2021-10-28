/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author naqichuan 14/12/3 10:47
 */
public class ResourceStream extends Resource {
    private InputStream inputStream;

    public ResourceStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    protected InputStream getIn() throws IOException {
        return inputStream;
    }
}

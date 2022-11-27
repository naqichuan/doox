/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.io;

/**
 * @author naqichuan 14/12/3 10:44
 */
public interface Resource {


//    /**
//     * getIn
//     *
//     * @return {@link InputStream}
//     * @author naqichuan 11/8/21 2:25 PM
//     */
//    protected abstract InputStream getIn() throws IOException;

    /**
     * 执行资源输出
     *
     * @param output output
     * @author naqichuan 11/8/21 2:24 PM
     */
    Output out(Output output);
}

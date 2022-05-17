/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.door.core;

import org.nqcx.doox.commons.lang.o.DTO;

/**
 * 门接口，用于构造门和实现开门方法，主要用于动态代理
 *
 * @author naqichuan 17/8/14 18:13
 */
public interface IDoor {

    /**
     * 开门
     *
     * @param dto DTO
     * @return DTO
     */
    DTO open(DTO dto);
}

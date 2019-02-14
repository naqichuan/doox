/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.tree;

/**
 * Node Wrapper interface
 *
 * @author naqichuan Dec 15, 2013
 */
public interface INode<ID> {

    /**
     * 取包装节点 ID
     *
     * @return ID
     */
    ID getNodeId();

    /**
     * 取父包装节点 ID
     *
     * @return ID
     */
    ID getParentNodeId();
}

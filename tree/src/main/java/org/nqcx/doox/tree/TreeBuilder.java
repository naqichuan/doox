/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.tree;

import java.io.Serializable;
import java.util.*;

/**
 * tree builder
 *
 * @param <O>
 * @author huangbaoguang 2013-3-27 下午5:02:48
 */
public class TreeBuilder<ID, O extends INode<ID>> implements Serializable {

    private final Map<ID, NodeFacade<ID, O>> nodeMap = new LinkedHashMap<>();

    /**
     * @param o o
     */
    public void put(O o) {
        if (o == null || containsNodeId(o.getNodeId()))
            return;

        // 放入列表
        nodeMap.put(o.getNodeId(), new NodeFacade<>(o));
        // 关联上级
        relateParentNode(nodeMap.get(o.getNodeId()));
        // 关联下级
        relateChildNodes(nodeMap.get(o.getNodeId()));
    }

    /**
     * @param nodeId ID
     * @return O
     */
    public Optional<O> get(ID nodeId) {
        Optional<NodeFacade<ID, O>> nodeFacade = getNode(nodeId);
        if (nodeFacade.isPresent())
            return nodeFacade.get().getObject();
        return Optional.empty();
    }

    /**
     * @param node node
     */
    private void relateParentNode(NodeFacade<ID, O> node) {
        getNode(node.getParentNodeId()).ifPresent(parent -> {
            parent.addChildNode(node);
            node.setParentNode(parent);
        });
    }

    /**
     * @param node node
     */
    private void relateChildNodes(NodeFacade<ID, O> node) {
        // 关联下级
        Set<ID> nodeIds = nodeMap.keySet();
        for (ID nodeId : nodeIds) {
            NodeFacade<ID, O> dataNodeTmp = nodeMap.get(nodeId);
            if (node.getNodeId().equals(dataNodeTmp.getParentNodeId())) {
                node.addChildNode(dataNodeTmp);
                dataNodeTmp.setParentNode(node);
            }
        }

    }

    /**
     * @param nodeId ID
     * @return boolean
     */
    public boolean containsNodeId(ID nodeId) {
        return nodeMap.containsKey(nodeId);
    }

    /**
     *
     */
    public void clean() {
        nodeMap.clear();
    }

    /**
     * @return int
     */
    public int size() {
        return nodeMap.size();
    }

    /**
     * @return boolean
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * @param nodeId ID
     * @return NodeFacade
     */
    public Optional<NodeFacade<ID, O>> getNode(ID nodeId) {
        return Optional.ofNullable(nodeMap.get(nodeId));
    }

    /**
     * @param nodeId ID
     * @return List
     */
    public List<NodeFacade<ID, O>> listChildNodes(ID nodeId) {
        List<NodeFacade<ID, O>> list = new ArrayList<>();

        if (nodeId != null) {
            nodeMap.forEach((k, v) -> {
                if (k.equals(v.getParentNodeId())) {
                    list.add(v);
                }

            });
        }
        return list;
    }
}

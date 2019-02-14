/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Node facade
 *
 * @author naqichuan Dec 15, 2013
 */
public class NodeFacade<ID, O extends INode<ID>> implements INode<ID>, Serializable {

    /**
     * 当前节点，是一个具体的对象
     */
    private O object;

    /**
     * 父节点
     */
    private NodeFacade<ID, O> parentNode;

    /**
     * 所有子节点
     */
    private final List<NodeFacade<ID, O>> childNodes = new ArrayList<>(0);

    // ========================================================================

    public NodeFacade() {

    }

    public NodeFacade(O o) {
        this.object = o;
    }

    // ========================================================================

    /**
     * 从当前节点的具体对象返回节点ID
     */
    @Override
    public ID getNodeId() {
        return object == null ? null : object.getNodeId();
    }

    /**
     * 从当前节点的具体对象返回父节点ID
     */
    @Override
    public ID getParentNodeId() {
        return object == null ? null : object.getParentNodeId();
    }

    // ========================================================================

    public Optional<O> getObject() {
        return Optional.ofNullable(object);
    }

    /**
     * 取得父节点的具体对象
     *
     * @return O
     */
    public Optional<O> getParent() {
        return hasParent() ? parentNode.getObject() : Optional.empty();
    }

    /**
     * 取得子节点的具体对象列表
     *
     * @return List
     */
    public List<O> getChilds() {
        List<O> newList = new ArrayList<>();

        this.childNodes.forEach(n -> {
            if (n != null) {
                n.getObject().ifPresent(newList::add);
            }
        });

        return newList;
    }

    // ========================================================================

    public Optional<NodeFacade<ID, O>> getParentNode() {
        return Optional.ofNullable(parentNode);
    }

    public List<NodeFacade<ID, O>> getChildNodes() {
        return childNodes;
    }

    // ========================================================================

    public void setObject(O o) {
        this.object = o;
    }

    public void setParentNode(NodeFacade<ID, O> parentNode) {
        if (parentNode == null || !parentNode.hasObject())
            return;

        this.parentNode = parentNode;
    }

    public void setChildNodes(List<NodeFacade<ID, O>> childNodes) {
        if (childNodes == null || childNodes.size() == 0)
            return;

        this.childNodes.addAll(childNodes);
    }

    /**
     * 添加子节点
     *
     * @param node node
     */
    public void addChildNode(NodeFacade<ID, O> node) {
        if (node == null || !node.hasObject())
            return;

        childNodes.add(node);
    }

    // ========================================================================

    /**
     * has object
     *
     * @return boolean
     */
    public boolean hasObject() {
        return object != null;
    }

    /**
     * has parent
     *
     * @return boolean
     */
    public boolean hasParent() {
        return parentNode != null && parentNode.hasObject();
    }

    /**
     * has child
     *
     * @return boolean
     */
    public boolean hasChilds() {
        return childNodes.size() > 0;
    }
}

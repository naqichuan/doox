/*
 * Copyright 2020 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.dao;

import java.util.List;

/**
 * @author naqichuan 2020/3/29 22:37
 */
public interface IAspect<PO> {
    /**
     * 保存之前处理
     *
     * @param po po
     * @return PO
     */
    default PO beforeSave(PO po) {
        return po;
    }

//    /**
//     * 保存之前处理
//     *
//     * @param pos pos
//     * @return List
//     */
//    default List<PO> beforeSave(List<PO> pos) {
//        if (pos != null && pos.size() > 0)
//            pos.forEach(this::beforeSave);
//        return pos;
//    }

    /**
     * 保存之后处理
     *
     * @param po po
     * @return PO
     */
    default PO afterSave(PO po) {
        return po;
    }

//    /**
//     * 保存之后处理
//     *
//     * @param pos pos
//     * @return List
//     */
//    default List<PO> afterSave(List<PO> pos) {
//        if (pos != null && pos.size() > 0)
//            pos.forEach(this::afterSave);
//        return pos;
//    }

    /**
     * 修改之前处理
     *
     * @param po po
     * @return PO
     */
    default PO beforeModify(PO po) {
        return po;
    }

//    /**
//     * 修改之前处理
//     *
//     * @param pos pos
//     * @return List
//     */
//    default List<PO> beforeModify(List<PO> pos) {
//        if (pos != null && pos.size() > 0)
//            pos.forEach(this::beforeModify);
//        return pos;
//    }

    /**
     * 修改之后处理
     *
     * @param po po
     * @return PO
     */
    default PO afterModify(PO po) {
        return po;
    }

//    /**
//     * 修改之后处理
//     *
//     * @param pos pos
//     * @return List
//     */
//    default List<PO> afterModify(List<PO> pos) {
//        if (pos != null && pos.size() > 0)
//            pos.forEach(this::afterModify);
//        return pos;
//    }

    /**
     * 查询一条数据后处理
     *
     * @param po po
     * @return PO
     */
    default PO afterFoud(PO po) {
        return po;
    }

    /**
     * 查询数据列表后处理
     *
     * @param pos pos
     * @return pos
     */
    default List<PO> afterFoud(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::afterFoud);
        return pos;
    }

    /**
     * 删除之前处理
     *
     * @param po po
     */
    default void beforeDelete(PO po) {
        // nothing to do
    }

    /**
     * 删除之前处理
     *
     * @param pos pos
     */
    default void beforeDelete(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::beforeDelete);
    }

    /**
     * 删除之后处理
     *
     * @param po po
     */
    default PO afterDelete(PO po) {
        return po;
    }

    /**
     * 删除之后处理
     *
     * @param pos pos
     */
    default List<PO> afterDelete(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::afterDelete);

        return pos;
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.service;

import org.nqcx.doox.commons.lang.o.DTO;

import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午10:52:21
 */
public interface IService<PO, ID> {

    /**
     * 保存之前处理
     *
     * @param po po
     * @return PO
     */
    default PO beforeSave(PO po) {
        return po;
    }

    /**
     * 保存数据
     *
     * @param po po
     * @return t
     */
    PO save(PO po);


    /**
     * 保存之后处理
     *
     * @param po po
     * @return PO
     */
    default PO afterSave(PO po) {
        return po;
    }

    /**
     * 保存之前处理
     *
     * @param pos pos
     * @return List
     */
    default List<PO> beforeSave(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::beforeSave);
        return pos;
    }

    /**
     * 保存多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> saveAll(List<PO> pos);

    /**
     * 保存多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> saveAll(PO... pos);

    /**
     * 保存之后处理
     *
     * @param pos pos
     * @return List
     */
    default List<PO> afterSave(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::afterSave);
        return pos;
    }

    /**
     * 修改之前处理
     *
     * @param po po
     * @return PO
     */
    default PO beforeModify(PO po) {
        return po;
    }

    /**
     * 修改数据
     *
     * @param po po
     * @return po
     */
    PO modify(PO po);

    /**
     * 修改之后处理
     *
     * @param po po
     * @return PO
     */
    default PO afterModify(PO po) {
        return po;
    }

    /**
     * 修改之前处理
     *
     * @param pos pos
     * @return List
     */
    default List<PO> beforeModify(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::beforeModify);
        return pos;
    }

    /**
     * 修改多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> modifyAll(List<PO> pos);

    /**
     * 修改多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> modifyAll(PO... pos);

    /**
     * 修改之后处理
     *
     * @param pos pos
     * @return List
     */
    default List<PO> afterModify(List<PO> pos) {
        if (pos != null && pos.size() > 0)
            pos.forEach(this::afterModify);
        return pos;
    }

    /**
     * Retrieves an po by its id
     *
     * @param id id
     * @return t
     */
    PO findById(ID id);

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
     * Return all by ids
     *
     * @param ids id
     * @return t
     */
    List<PO> findAllByIds(List<ID> ids);

    /**
     * Return all by ids
     *
     * @param ids id
     * @return t
     */
    List<PO> findAllByIds(ID... ids);


    /**
     * 执行查询
     *
     * @param dto dto
     * @return List
     */
    List<PO> listAll(DTO dto);

    /**
     * 执行查询
     *
     * @param dto dto
     * @return DTO
     */
    DTO findAll(DTO dto);

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
     * 取得 count
     *
     * @param dto dto
     * @return long
     */
    long getCount(DTO dto);

    /**
     * 删除数据
     *
     * @param id id
     */
    void deleteById(ID id);

    /**
     * 删除多条数据
     *
     * @param ids ids
     */
    void deleteByIds(List<ID> ids);

    /**
     * 删除多条数据
     *
     * @param ids ids
     */
    void deleteByIds(ID... ids);
}

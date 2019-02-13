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
import java.util.Optional;

/**
 * @author naqichuan 2014年8月14日 上午10:52:21
 */
public interface IService<DO, PO, ID> {

    /**
     * 保存数据
     *
     * @param pos pos
     * @return t
     */
    PO save(PO pos);

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
     * 修改数据
     *
     * @param pos pos
     * @return pos
     */
    PO modify(PO pos);

    /**
     * 修改多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> modifyAll(List<PO> pos);

    /**
     * 保存多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> modifyAll(PO... pos);

    /**
     * Retrieves an pos by its id
     *
     * @param id id
     * @return t
     */
    Optional<DO> findById(ID id);

    /**
     * Return all by ids
     *
     * @param ids id
     * @return t
     */
    List<DO> findAllByIds(List<ID> ids);

    /**
     * Return all by ids
     *
     * @param ids id
     * @return t
     */
    List<DO> findAllByIds(ID... ids);


    /**
     * 执行查询
     *
     * @param dto dto
     * @return List
     */
    List<DO> listAll(DTO dto);

    /**
     * 执行查询
     *
     * @param dto dto
     * @return DTO
     */
    DTO findAll(DTO dto);

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

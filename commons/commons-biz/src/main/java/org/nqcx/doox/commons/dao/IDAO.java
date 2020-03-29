/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.dao;

import org.nqcx.doox.commons.lang.o.DTO;

import java.util.List;

/**
 * @author naqichuan 2014年8月14日 上午10:41:33
 */
public interface IDAO<PO, ID> {


    /**
     * 保存数据
     *
     * @param po po
     * @return PO
     */
    PO save(PO po);


    /**
     * 保存多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> saveAll(List<PO> pos);


    /**
     * 修改数据
     *
     * @param po po
     * @return PO
     */
    PO modify(PO po);


    /**
     * 修改多条数据
     *
     * @param pos pos
     * @return list
     */
    List<PO> modifyAll(List<PO> pos);

    /**
     * Retrieves an entity by its id
     *
     * @param id id
     * @return t
     */
    PO findById(ID id);

    /**
     * Return all by ids
     *
     * @param ids id
     * @return t
     */
    List<PO> findAllByIds(List<ID> ids);

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
    PO deleteById(ID id);


    /**
     * 删除多条数据
     *
     * @param ids ids
     */
    List<PO> deleteByIds(List<ID> ids);
}

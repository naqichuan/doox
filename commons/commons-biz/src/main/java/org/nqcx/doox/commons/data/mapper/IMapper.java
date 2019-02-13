/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.data.mapper;

import java.util.List;
import java.util.Map;

/**
 * @author naqichuan 2018/12/3 10:12
 */
public interface IMapper<PO, ID> {

    /**
     * 插入数据
     *
     * @param po
     * @return
     */
    int save(PO po);

    /**
     * 更新数据
     *
     * @param po
     * @return
     */
    int update(PO po);

    /**
     * 根据ID取得详情
     *
     * @param id
     * @return
     */
    PO findById(ID id);

    /**
     * 根据 ids 删除多条记录
     *
     * @param ids
     * @return
     */
    List<PO> findByIds(List<ID> ids);

    /**
     * 执行查询
     *
     * @param map
     * @return
     */
    List<PO> findAll(Map<String, Object> map);

    /**
     * 查询总数
     *
     * @param map map
     * @return long
     */
    long getCount(Map<String, Object> map);

    /**
     * 根据 id 删除单条记录
     *
     * @param id id
     * @return int
     */
    int deleteById(ID id);

    /**
     * 根据 ids 删除多条记录
     *
     * @param ids ids
     * @return id
     */
    int deleteByIds(List<ID> ids);
}

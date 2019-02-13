/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.data.mapper;

import org.nqcx.doox.commons.dao.IDAO;
import org.nqcx.doox.commons.lang.o.DTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author naqichuan 2018/12/3 10:13
 */
public abstract class MapperSupport<Mapper extends IMapper<PO, ID>, PO, ID> implements IDAO<PO, ID> {

    protected final Mapper mapper;

    public MapperSupport(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public PO save(PO po) {
        mapper.save(po);

        return po;
    }

    @Override
    public PO modify(PO po) {
        mapper.update(po);

        return po;
    }

    @Override
    public List<PO> saveAll(List<PO> pos) {
        if (pos == null)
            return null;

        pos.forEach(this::save);

        return pos;
    }

    @Override
    public List<PO> modifyAll(List<PO> pos) {
        if (pos == null)
            return null;

        pos.forEach(this::modify);

        return pos;
    }

    @Override
    public Optional<PO> findById(ID id) {
        return Optional.ofNullable(mapper.findById(id));
    }

    @Override
    public List<PO> findAllByIds(List<ID> ids) {
        return mapper.findByIds(ids);
    }

    @Override
    public List<PO> listAll(DTO dto) {
        return mapper.findAll(parseParams(dto == null ? new DTO() : dto));
    }

    @Override
    public DTO findAll(DTO dto) {
        if (dto == null)
            dto = new DTO();

        if (dto.getPage() != null)
            dto.getPage().setTotalCount(this.getCount(dto));

        dto.setList(mapper.findAll(parseParams(dto)));

        return dto.setSuccess(true);
    }

    @Override
    public long getCount(DTO dto) {
        return mapper.getCount(parseParams(dto == null ? new DTO() : dto));
    }

    @Override
    public void deleteById(ID id) {
        mapper.deleteById(id);
    }

    @Override
    public void deleteByIds(List<ID> ids) {
        mapper.deleteByIds(ids);
    }

    /**
     * 解析参数，返回 mapper 需要的参数
     *
     * @param dto dto
     * @return map
     */
    public static Map<String, Object> parseParams(DTO dto) {
        Map<String, Object> map = new HashMap<>();

        if (dto != null && dto.getParamsMap() != null)
            map.putAll(dto.getParamsMap());
        if (dto != null && dto.getSort() != null)
            map.put("_order_", "ORDER BY " + dto.getSort().orderString());

        if (dto != null && dto.getPage() != null)
            map.put("_page_", "LIMIT " + dto.getPage().getStartIndex() + ", " + dto.getPage().getPageSize());

        return map;
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.data.mapper;

import org.apache.ibatis.reflection.property.PropertyNamer;
import org.nqcx.doox.commons.dao.IDAO;
import org.nqcx.doox.commons.lang.o.DTO;

import javax.persistence.Column;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author naqichuan 2018/12/3 10:13
 */
public abstract class MapperSupport<Mapper extends IMapper<PO, ID>, PO, ID> implements IDAO<PO, ID> {

    protected final Map<String, String> fieldMapping = new LinkedHashMap<>();
    protected final Mapper mapper;

    public MapperSupport(Mapper mapper) {
        Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) t).getActualTypeArguments();
            Class<PO> classpo = (Class<PO>) types[1];
            Method[] methods;
            if (classpo != null && (methods = classpo.getMethods()) != null) {
                for (Method m : methods) {
                    Column c = m.getAnnotation(Column.class);
                    if (c == null)
                        continue;

                    fieldMapping.put(PropertyNamer.methodToProperty(m.getName()), c.name().trim());
                }
            }
        }

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
        return mapper.findAll(parseParams(dto == null ? new DTO() : dto, fieldMapping));
    }

    @Override
    public DTO findAll(DTO dto) {
        if (dto == null)
            dto = new DTO();

        if (dto.getPage() != null)
            dto.getPage().setTotalCount(this.getCount(dto));

        dto.setList(mapper.findAll(parseParams(dto, fieldMapping)));

        return dto.setSuccess(true);
    }

    @Override
    public long getCount(DTO dto) {
        return mapper.getCount(parseParams(dto == null ? new DTO() : dto, fieldMapping));
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
        return parseParams(dto, null);
    }

    /**
     * 解析参数，返回 mapper 需要的参数
     *
     * @param dto          dto
     * @param fieldMapping field mapping
     * @return map
     */
    public static Map<String, Object> parseParams(DTO dto, Map<String, String> fieldMapping) {
        Map<String, Object> map = new HashMap<>();

        if (dto != null && dto.getParamsMap() != null)
            dto.getParamsMap().forEach(map::put);

        if (dto != null && dto.getSort() != null)
            map.put("_order_", "ORDER BY " + dto.getSort().orderString(fieldMapping));

        if (dto != null && dto.getPage() != null)
            map.put("_page_", "LIMIT " + dto.getPage().getStartIndex() + ", " + dto.getPage().getPageSize());

        return map;
    }
}

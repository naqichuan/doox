/*
 * Copyright 2020 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.dao;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.nqcx.doox.commons.data.mapper.IMapper;
import org.nqcx.doox.commons.lang.consts.PeriodConst;
import org.nqcx.doox.commons.lang.o.DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 默认使用 mybatis
 *
 * @author naqichuan 2020/03/31 10:13
 */
public abstract class DAOSupport<Mapper extends IMapper<PO, ID>, PO, ID> implements IDAO<PO, ID>, IAspect<PO, ID> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DAOSupport.class.getName());

    // Po field 与 table column 对应关系
    protected final Map<String, String> fieldMapping = new LinkedHashMap<>();
    // Po filed 与 method 对应关系
    protected final Map<String, Method> setMethodMapping = new HashMap<>();
    protected final Map<String, Method> getMethodMapping = new HashMap<>();

    protected final Map<String, KO> KOS = new HashMap<>();

    protected final Class<PO> clazz;
    // mapper
    protected final Mapper mapper;

    public DAOSupport(Mapper mapper) {
        Type t = getClass().getGenericSuperclass();
        if (t instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) t).getActualTypeArguments();
            clazz = (Class<PO>) types[1];
            Method[] methods;
            if (clazz != null && (methods = clazz.getMethods()) != null) {
                for (Method m : methods) {
                    Column c = m.getAnnotation(Column.class);
                    if (c != null)
                        fieldMapping.put(PropertyNamer.methodToProperty(m.getName()), c.name().trim());

                    if (PropertyNamer.isGetter(m.getName()))
                        getMethodMapping.put(PropertyNamer.methodToProperty(m.getName()), m);
                    else if (PropertyNamer.isSetter(m.getName()))
                        setMethodMapping.put(PropertyNamer.methodToProperty(m.getName()), m);
                }
            }
        } else
            throw new RuntimeException("Class not found");

        this.mapper = mapper;
    }

    @Override
    public PO save(PO po) {
        if (po == null)
            return null;

        mapper.save(beforeSave(po));

        return afterSave(po);
//        mapper.save(po);
//
//        return po;
    }

    @Override
    public PO modify(PO po) {
        if (po == null)
            return null;

        mapper.update(beforeModify(po));

        return afterModify(po);

//        mapper.update(po);
//
//        return po;
    }

    @Override
    public List<PO> saveAll(List<PO> pos) {
        if (pos == null)
            return new ArrayList<>(0);

        pos.forEach(this::save);

        return pos;
    }

    @Override
    public List<PO> modifyAll(List<PO> pos) {
        if (pos == null)
            return new ArrayList<>(0);

        pos.forEach(this::modify);

        return pos;
    }

    @Override
    public PO findById(ID id) {
        return afterFoud(mapper.findById(id));
    }

    @Override
    public List<PO> findAllByIds(List<ID> ids) {
        List<PO> list;
        return afterFoud((list = mapper.findByIds(ids)) == null ? new ArrayList<>(0) : list);
    }

    @Override
    public List<PO> listAll(DTO dto) {
        List<PO> list;
        return (list = mapper.findAll(parseParams(dto == null ? new DTO() : dto, fieldMapping))) == null ? new ArrayList<>(0) : list;
    }

    @Override
    public DTO findAll(DTO dto) {
        if (dto == null)
            dto = new DTO();

        if (dto.getPage() != null)
            dto.getPage().setTotalCount(this.getCount(dto));

        List<PO> list;
        dto.setList((list = mapper.findAll(parseParams(dto, fieldMapping))) == null ? new ArrayList<>(0) : list);

        return dto.setSuccess(true);
    }

    @Override
    public long getCount(DTO dto) {
        return mapper.getCount(parseParams(dto == null ? new DTO() : dto, fieldMapping));
    }

    @Override
    public PO deleteById(ID id) {
        PO po = mapper.findById(id);

        this.beforeDelete(po);
        mapper.deleteById(id);

        return this.afterDelete(po);
    }

    @Override
    public List<PO> deleteByIds(List<ID> ids) {
        List<PO> pos = Optional.ofNullable(mapper.findByIds(ids))
                .orElse(Collections.emptyList());

        this.beforeDelete(pos);
        mapper.deleteByIds(ids);

        return this.afterDelete(pos);
    }

    // =========================================================================

    /**
     * @param po           po
     * @param isEmptyCache boolean
     * @return PO
     */
    protected PO putCache(PO po, boolean isEmptyCache) {
        if (po == null)
            return null;

        AtomicReference<PO> npo = new AtomicReference<>(po);

        KOS.values().forEach(ko -> {
            // 从对象里找值
            try {
                String fieldValue = String.valueOf(getMethodMapping.get(ko.field()).invoke(po));
                if (isEmptyCache) {
                    if (!fieldValue.equals("null") && !fieldValue.equals("")) {
                        // 设置空缓存，防止缓存穿透
                        putCache(ko.key(fieldValue), "", PeriodConst.ONE_MINUTES);
                        npo.set(null);
                    }
                } else
                    // 从对象里找值
                    putCache(ko.key(fieldValue), JSON.toJSONString(po), ko.expire());
            } catch (Exception e) {
                LOGGER.error("Put cache fail", e);
            }
        });

        return npo.get();
    }

    /**
     * @param key    key
     * @param value  value
     * @param expire expire
     */
    abstract void putCache(String key, String value, int expire);

    /**
     * @param po dooxPO
     */
    protected PO delCache(PO po) {
        Optional.ofNullable(po).ifPresent(p -> KOS.values().forEach(ko -> {
                    try {
                        delCache(ko.key((String) getMethodMapping.get(ko.field()).invoke(p, new Object[0])));
                    } catch (Exception e) {
                        LOGGER.error("Del cache fail", e);
                    }
                })
        );

        return po;
    }

    /**
     * @param key key
     */
    abstract void delCache(String key);

    /**
     * @param value value
     * @return String
     */
    protected PO fromCache(String value) {
        if (value != null && value.length() > 0)
            return JSON.parseObject(value, clazz);
        return null;
    }

    /**
     * key object 类
     */
    public static class KO {
        private final String schema;
        private final String po;
        private final String field;
        private final int expire;

        public KO(String schema, String po, String field, int expire) {
            this.schema = schema;
            this.po = po;
            this.field = field;
            this.expire = expire;
        }

        /**
         * 生成 key String
         *
         * @param value value
         * @return String
         */
        public String key(String value) {
            return (this.schema + ":" + this.po + ":" + this.field + ":").toUpperCase() + value;
        }

        public String field() {
            return this.field;
        }

        public int expire() {
            return this.expire;
        }
    }

    // =========================================================================

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

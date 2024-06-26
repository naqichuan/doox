/*
 * Copyright 2020 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.dao;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.reflection.property.PropertyNamer;
import org.nqcx.doox.commons.data.mapper.IMapper;
import org.nqcx.doox.commons.lang.consts.LoggerConst;
import org.nqcx.doox.commons.lang.consts.PeriodConst;
import org.nqcx.doox.commons.lang.o.DTO;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * 默认使用 mybatis
 *
 * @author naqichuan 2020/03/31 10:13
 */
public abstract class DAOSupport<Mapper extends IMapper<PO, ID>, PO, ID> implements IDAO<PO, ID>, IAspect<PO> {

    public final static Logger CACHE_LOGGER = LoggerFactory.getLogger(LoggerConst.LOGGER_CACHE_NAME);

    private final static Logger LOGGER = LoggerFactory.getLogger(DAOSupport.class);

    // Po field 与 table column 对应关系
    protected final Map<String, String> fieldMapping = new LinkedHashMap<>();
    // Po filed 与 method 对应关系
    protected final Map<String, Method> poFieldSetters = new HashMap<>();
    protected final Map<String, Method> poFieldGetters = new HashMap<>();
    // Key Object s
    protected final Map<String, KO> KOS = new HashMap<>();
    // PO class
    protected final Class<PO> clazz;
    // mapper
    protected final Mapper mapper;
    // redisTemplate
    protected final RedisTemplate<String, String> redisTemplate;

    public DAOSupport(Mapper mapper) {
        this(mapper, null);
    }

    public DAOSupport(Mapper mapper, RedisTemplate<String, String> redisTemplate) {
        Class<?> cz = getClass();
        while (cz != null && cz.getSuperclass() != DAOSupport.class) {
            cz = cz.getSuperclass();
        }

        Type t;
        if (cz != null && (t = cz.getGenericSuperclass()) instanceof  ParameterizedType) {
            Type[] types = ((ParameterizedType) t).getActualTypeArguments();
            clazz = (Class<PO>) types[1];
            Method[] methods;
            if (clazz != null && (methods = clazz.getMethods()) != null) {
                for (Method m : methods) {
                    Column c = m.getAnnotation(Column.class);
                    if (c != null)
                        fieldMapping.put(PropertyNamer.methodToProperty(m.getName()), "`" + c.name().trim() + "`");

                    if (PropertyNamer.isGetter(m.getName()))
                        poFieldGetters.put(PropertyNamer.methodToProperty(m.getName()), m);
                    else if (PropertyNamer.isSetter(m.getName()))
                        poFieldSetters.put(PropertyNamer.methodToProperty(m.getName()), m);
                }
            }
        } else
            throw new RuntimeException("Class not found");

        // extend field mapping
        Optional.ofNullable(this.extendFieldMapping()).ifPresent(xs -> Arrays.asList(xs).forEach(x -> {
            Method[] methods;
            if (x != null && (methods = x.getMethods()) != null) {
                Table table = (Table) x.getDeclaredAnnotation(Table.class);
                for (Method m : methods) {
                    Column c = m.getAnnotation(Column.class);
                    if (c == null)
                        continue;

                    fieldMapping.put(x.getSimpleName() + "." + PropertyNamer.methodToProperty(m.getName()),
                            (table == null ? "" : "`" + table.name().trim() + "`.") + "`" + c.name().trim() + "`");
                }
            }
        }));

        this.mapper = mapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * id field name of po
     *
     * @return String
     */
    protected String idField() {
        return "id";
    }

    /**
     * exend fileld mapping
     *
     * @return mapping
     */
    protected Class[] extendFieldMapping() {
        return new Class[0];
    }

    @Override
    public List<PO> saveAll(List<PO> pos) {
        if (pos == null)
            return new ArrayList<>(0);

        pos.forEach(this::save);

        return pos;
    }

    @Override
    public PO save(PO po) {
        if (po == null)
            return null;

        mapper.save(beforeSave(po));

        return afterSave(po);
    }

    @Override
    public PO afterSave(PO po) {
        return putSaveAndModifyCache(po);
    }

    @Override
    public List<PO> modifyAll(List<PO> pos) {
        if (pos == null)
            return new ArrayList<>(0);

        pos.forEach(this::modify);

        return pos;
    }

    @Override
    public PO beforeModify(PO po) {
        Optional.ofNullable(KOS.get(idField())).map(ko -> {
            try {
                return mapper.findById((ID) poFieldGetters.get(ko.fieldStr()).invoke(po));
            } catch (Exception e) {
                LOGGER.error("beforeModify fail", e);
            }
            return null;
        }).ifPresent(p -> KOS.values().forEach(ko -> {
                    // 从对象里找值
                    try {
                        String[] fieldValues = Arrays.stream(ko.fields).map(x -> {
                            try {
                                return String.valueOf(poFieldGetters.get(x).invoke(po));
                            } catch (Exception e) {
                                LOGGER.error("Delete cache fail", e);
                            }
                            return null;
                        }).filter(Objects::nonNull).toArray(String[]::new);

                        // 设置一个 5 秒缓存，防止有做为缓存 key 的值有变更
                        expireCache(ko.key(fieldValues), 5);
                    } catch (Exception e) {
                        LOGGER.error("beforeModify fail", e);
                    }
                })
        );

        return po;
    }

    @Override
    public PO modify(PO po) {
        if (po == null)
            return null;

        mapper.update(beforeModify(po));

        return afterModify(po);
    }


    @Override
    public PO afterModify(PO po) {
        return putSaveAndModifyCache(po);
    }

    /**
     * @param po po
     * @return PO
     */
    protected PO putSaveAndModifyCache(PO po) {
        return putCache(Optional.ofNullable(KOS.get(idField())).map(ko -> {
            try {
                return mapper.findById((ID) poFieldGetters.get(ko.fieldStr()).invoke(po));
            } catch (Exception e) {
                LOGGER.error("Put save and modify cache fail", e);
            }
            return null;
        }).orElse(po), false);
    }

    @Override
    public PO findById(ID id) {
        if (Objects.isNull(id))
            return null;

        AtomicReference<String> value = new AtomicReference<>(null);
        Optional.ofNullable(KOS.get(idField())).ifPresent(ko -> {
            try {
                value.set(fromCache(ko.key(String.valueOf((id)))));
            } catch (Exception e) {
                LOGGER.error("findById fail", e);
            }
        });

        if ("".equals(value.get()))
            return null;

        PO po;
        if (value.get() != null && (po = cache2po(value.get())) != null)
            return po;

        if ((po = mapper.findById(id)) != null)
            return afterFoud(po);

        try {
            // 缓存和数据源中都找不到相应数据时，生成一个只有缓存项的对象用于设置空缓存
            PO finalPo = clazz.newInstance();
            Optional.ofNullable(KOS.get(idField())).ifPresent(ko -> {
                try {
                    poFieldSetters.get(ko.fieldStr()).invoke(finalPo, id);
                } catch (Exception e) {
                    LOGGER.error("findById fail", e);
                }
            });

            return putCache(finalPo, true);
        } catch (Exception e) {
            LOGGER.error("findById fail", e);
        }

        return null;
    }

    @Override
    public List<PO> findAllByIds(List<ID> ids) {
        if (ids == null)
            return null;
        if (ids.size() == 0)
            return Collections.emptyList();

        List<PO> list;
        return afterFoud((list = mapper.findByIds(ids)) == null ? new ArrayList<>(0) : list);
    }

    @Override
    public PO afterFoud(PO po) {
        return putCache(po, false);
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

        if (dto.getNpage() != null)
            dto.getNpage().setTotalCount(this.getCount(dto));

        List<PO> list;
        dto.setList((list = mapper.findAll(parseParams(dto, fieldMapping))) == null ? new ArrayList<>(0) : list);

        return dto.setSuccess(true);
    }

    @Override
    public long getCount(DTO dto) {
        return mapper.getCount(parseParams(dto == null ? new DTO() : dto, fieldMapping));
    }

    @Override
    public List<PO> deleteByIds(List<ID> ids) {
        if (ids == null)
            return null;
        if (ids.size() == 0)
            return Collections.emptyList();

        List<PO> pos = Optional.ofNullable(mapper.findByIds(ids))
                .orElse(Collections.emptyList());

        this.beforeDelete(pos);
        mapper.deleteByIds(ids);

        return this.afterDelete(pos);
    }

    @Override
    public PO deleteById(ID id) {
        if (Objects.isNull(id))
            return null;

        PO po = mapper.findById(id);

        this.beforeDelete(po);
        mapper.deleteById(id);

        if (po == null) {
            try {
                PO finalPo = clazz.newInstance();
                Optional.ofNullable(KOS.get(idField())).ifPresent(ko -> {
                    try {
                        poFieldSetters.get(ko.fieldStr()).invoke(finalPo, id);
                    } catch (Exception e) {
                        LOGGER.error("deleteById fail", e);
                    }
                });
                this.delCache(finalPo);
            } catch (Exception e) {
                LOGGER.error("deleteById fail", e);
            }
            return null;
        } else
            return this.afterDelete(po);
    }

    @Override
    public PO afterDelete(PO po) {
        return delCache(po);
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
                String[] fieldValues = Arrays.stream(ko.fields).map(x -> {
                    try {
                        return String.valueOf(poFieldGetters.get(x).invoke(po));
                    } catch (Exception e) {
                        LOGGER.error("Put cache fail", e);
                    }
                    return null;
                }).filter(Objects::nonNull).toArray(String[]::new);

                if (isEmptyCache) {
                    if (ko.fields.length == fieldValues.length) {
                        // 设置空缓存，防止缓存穿透
                        putCache(ko.key(fieldValues), "", PeriodConst.ONE_MINUTES);
                        npo.set(null);
                    }
                } else if (ko.fields.length == fieldValues.length) {
                    // 从对象里找值
                    putCache(ko.key(fieldValues), JSON.toJSONString(po), ko.expire());
                }
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
    protected void putCache(String key, String value, int expire) {
        Optional.ofNullable(redisTemplate).ifPresent(c -> {
                    c.opsForValue().set(key, value);
                    c.expire(key, expire, TimeUnit.SECONDS);
                }
        );
    }

    /**
     * @param key    key
     * @param expire expire
     */
    protected void expireCache(String key, int expire) {
        Optional.ofNullable(redisTemplate).ifPresent(c -> c.expire(key, expire, TimeUnit.SECONDS));
    }


    /**
     * @param po po
     */
    protected PO delCache(PO po) {
        Optional.ofNullable(po).ifPresent(p -> KOS.values().forEach(ko -> {
                    try {
                        String[] fieldValues = Arrays.stream(ko.fields).map(x -> {
                            try {
                                return String.valueOf(poFieldGetters.get(x).invoke(po));
                            } catch (Exception e) {
                                LOGGER.error("Delete cache fail", e);
                            }
                            return null;
                        }).filter(Objects::nonNull).toArray(String[]::new);

                        delCache(ko.key(fieldValues));
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
    protected void delCache(String key) {
        Optional.ofNullable(redisTemplate).ifPresent(c -> c.delete(key));
    }

    /**
     * @param value value
     * @return String
     */
    protected PO cache2po(String value) {
        if (value != null && value.length() > 0)
            return JSON.parseObject(value, clazz);
        return null;
    }

    /**
     * @param key key
     * @return String
     */
    protected String fromCache(String key) {
        return getFromCache(redisTemplate, key);
    }

    /**
     * @param redisTemplate JedisCluster
     * @param key           key
     * @return String
     */
    public static String getFromCache(RedisTemplate<String, String> redisTemplate, String key) {
        if (redisTemplate == null || key == null)
            return null;

        String value = redisTemplate.opsForValue().get(key);

        CACHE_LOGGER.info("From String, KEY: {}, HIT: {}", key, value != null && value.length() > 0);

        return value;
    }

    // =========================================================================

    /**
     * key object 类
     */
    public static class KO {
        private final String schema;
        private final String biz;
        private final String[] fields;
        private final int expire;

        public KO(String schema, String biz, int expire, String... fields) {
            this.schema = schema;
            this.biz = biz;
            this.expire = expire;

            if (fields == null)
                throw new RuntimeException("The fields can't be null!");

            this.fields = fields;
        }

        /**
         * 生成 key String
         *
         * @param values values
         * @return String
         */
        public String key(String... values) {
            if (values != null && values.length > 0 && values.length != fields.length)
                throw new RuntimeException("The values can't be null or the length must equal to fields length!");

            StringBuilder kb = new StringBuilder((this.schema + ":" + this.biz + ":" + fieldStr()).toUpperCase());

            if (values != null && values.length > 0) {
                kb.append(":");
                kb.append(StringUtils.join(values, "-"));
            }

            return kb.toString();
        }

        public String[] fields() {
            return this.fields;
        }

        public String fieldStr() {
            return StringUtils.join(this.fields, "-");
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

        if (dto != null && dto.getParams() != null)
            dto.getParams().forEach(map::put);

        if (dto != null && dto.getNsort() != null)
            map.put("_order_", "ORDER BY " + dto.getNsort().orderString(fieldMapping));

        if (dto != null && dto.getNpage() != null)
            map.put("_page_", "LIMIT " + dto.getNpage().getStartIndex() + ", " + dto.getNpage().getPageSize());

        return map;
    }

    // =========================================================================

    /**
     * @param supplier supplier
     * @return PO
     */
    protected final PO findFromDB(Supplier<PO> supplier) {
        return supplier.get();
    }

    /**
     * 执行 po setter
     *
     * @param po      po
     * @param setters setters
     * @param fields  fields
     * @param values  values
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected final PO invokeSetters(PO po, final Map<String, Method> setters, final String[] fields, final Object[] values)
            throws InvocationTargetException, IllegalAccessException {
        if (po == null || setters == null || fields == null || values == null)
            throw new NullPointerException();

        if (values.length != fields.length)
            throw new IllegalArgumentException();

        for (int index = 0; index < fields.length; index++) {
            Method m = setters.get(fields[index]);
            if (m == null)
                continue;
            m.invoke(po, values[index]);
        }

        return po;
    }
}

///*
// * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
// * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
// * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
// */
//
//package org.nqcx.doox.commons.data.mapper;
//
//import org.apache.ibatis.reflection.property.PropertyNamer;
//import org.nqcx.doox.commons.dao.IDAO;
//import org.nqcx.doox.commons.lang.o.DTO;
//
//import javax.persistence.Column;
//import java.lang.reflect.Method;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.*;
//
///**
// * @author naqichuan 2018/12/3 10:13
// */
//public abstract class MapperSupport<Mapper extends IMapper<PO, ID>, PO, ID> implements IDAO<PO, ID> {
//
//    // Po field 与 table column 对应关系
//    protected final Map<String, String> fieldMapping = new LinkedHashMap<>();
//    // Po filed 与 method 对应关系
//    protected final Map<String, Method> poFieldSetters = new HashMap<>();
//    protected final Map<String, Method> poFieldGetters = new HashMap<>();
//    // mapper
//    protected final Mapper mapper;
//
//    public MapperSupport(Mapper mapper) {
//        Type t = getClass().getGenericSuperclass();
//        if (t instanceof ParameterizedType) {
//            Type[] types = ((ParameterizedType) t).getActualTypeArguments();
//            Class<PO> classpo = (Class<PO>) types[1];
//            Method[] methods;
//            if (classpo != null && (methods = classpo.getMethods()) != null) {
//                for (Method m : methods) {
//                    Column c = m.getAnnotation(Column.class);
//                    if (c != null)
//                        fieldMapping.put(PropertyNamer.methodToProperty(m.getName()), c.name().trim());
//
//                    if (PropertyNamer.isGetter(m.getName()))
//                        poFieldGetters.put(PropertyNamer.methodToProperty(m.getName()), m);
//                    else if (PropertyNamer.isSetter(m.getName()))
//                        poFieldSetters.put(PropertyNamer.methodToProperty(m.getName()), m);
//                }
//            }
//        }
//
//        this.mapper = mapper;
//    }
//
//    @Override
//    public PO save(PO po) {
//        if (po == null)
//            return null;
//
////        mapper.save(beforeSave(po));
////
////        return afterSave(po);
//        mapper.save(po);
//
//        return po;
//    }
//
//    @Override
//    public PO modify(PO po) {
//        if (po == null)
//            return null;
//
////        mapper.update(beforeModify(po));
////
////        return afterModify(po);
//
//        mapper.update(po);
//
//        return po;
//    }
//
//    @Override
//    public List<PO> saveAll(List<PO> pos) {
//        if (pos == null)
//            return new ArrayList<>(0);
//
//        pos.forEach(this::save);
//
//        return pos;
//    }
//
//    @Override
//    public List<PO> modifyAll(List<PO> pos) {
//        if (pos == null)
//            return new ArrayList<>(0);
//
//        pos.forEach(this::modify);
//
//        return pos;
//    }
//
//    @Override
//    public PO findById(ID id) {
//        return mapper.findById(id);
//    }
//
//    @Override
//    public List<PO> findAllByIds(List<ID> ids) {
//        List<PO> list;
//        return (list = mapper.findByIds(ids)) == null ? new ArrayList<>(0) : list;
//    }
//
//    @Override
//    public List<PO> listAll(DTO dto) {
//        List<PO> list;
//        return (list = mapper.findAll(parseParams(dto == null ? new DTO() : dto, fieldMapping))) == null ? new ArrayList<>(0) : list;
//    }
//
//    @Override
//    public DTO findAll(DTO dto) {
//        if (dto == null)
//            dto = new DTO();
//
//        if (dto.getPage() != null)
//            dto.getPage().setTotalCount(this.getCount(dto));
//
//        List<PO> list;
//        dto.setList((list = mapper.findAll(parseParams(dto, fieldMapping))) == null ? new ArrayList<>(0) : list);
//
//        return dto.setSuccess(true);
//    }
//
//    @Override
//    public long getCount(DTO dto) {
//        return mapper.getCount(parseParams(dto == null ? new DTO() : dto, fieldMapping));
//    }
//
//    @Override
//    public PO deleteById(ID id) {
//        Optional<PO> po = Optional.ofNullable(mapper.findById(id));
//
////        po.ifPresent(this::beforeDelete);
//        mapper.deleteById(id);
////        po.ifPresent(this::afterDelete);
//
//        return po.orElse(null);
//    }
//
//    @Override
//    public List<PO> deleteByIds(List<ID> ids) {
//        List<PO> pos = Optional.ofNullable(mapper.findByIds(ids))
//                .orElse(Collections.emptyList());
//
////        pos.forEach(this::beforeDelete);
//        mapper.deleteByIds(ids);
////        pos.forEach(this::afterDelete);
//
//        return pos;
//    }
//
//    /**
//     * 解析参数，返回 mapper 需要的参数
//     *
//     * @param dto dto
//     * @return map
//     */
//    public static Map<String, Object> parseParams(DTO dto) {
//        return parseParams(dto, null);
//    }
//
//    /**
//     * 解析参数，返回 mapper 需要的参数
//     *
//     * @param dto          dto
//     * @param fieldMapping field mapping
//     * @return map
//     */
//    public static Map<String, Object> parseParams(DTO dto, Map<String, String> fieldMapping) {
//        Map<String, Object> map = new HashMap<>();
//
//        if (dto != null && dto.getParamsMap() != null)
//            dto.getParamsMap().forEach(map::put);
//
//        if (dto != null && dto.getSort() != null)
//            map.put("_order_", "ORDER BY " + dto.getSort().orderString(fieldMapping));
//
//        if (dto != null && dto.getPage() != null)
//            map.put("_page_", "LIMIT " + dto.getPage().getStartIndex() + ", " + dto.getPage().getPageSize());
//
//        return map;
//    }
//}

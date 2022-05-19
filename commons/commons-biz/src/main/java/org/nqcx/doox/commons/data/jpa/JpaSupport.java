///*
// * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
// * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
// * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
// */
//
//package org.nqcx.doox.commons.data.jpa;
//
//import org.nqcx.doox.commons.dao.IDAO;
//import org.nqcx.doox.commons.data.converter.DTOConverter;
//import org.nqcx.doox.commons.lang.o.DTO;
//import org.nqcx.doox.commons.util.orika.Orika;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.NoRepositoryBean;
//import org.springframework.util.Assert;
//
//import java.lang.reflect.Array;
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
///**
// * @author naqichuan 2018/12/3 10:17
// */
//@NoRepositoryBean
//public abstract class JpaSupport<JPA extends IJpa<PO, ID>, PO, ID> implements IDAO<PO, ID> {
//
//    protected final Class<PO> clazz;
//    protected final JPA jpa;
//
//    public JpaSupport(JPA jpa) {
//        Type t = getClass().getGenericSuperclass();
//        if (t instanceof ParameterizedType) {
//            Type[] types = ((ParameterizedType) t).getActualTypeArguments();
//
//            this.clazz = (Class<PO>) types[1];
//        } else
//            throw new RuntimeException("Class not found");
//
//        this.jpa = jpa;
//    }
//
//    @Override
//    public PO save(PO po) {
//        Assert.notNull(po, "po must be not null.");
//
////        return afterSave(jpa.save(beforeSave(po)));
//        return jpa.save(po);
//    }
//
//    @Override
//    public PO modify(PO po) {
//        Assert.notNull(po, "po must be not null.");
//
////        return afterModify(jpa.save(beforeModify(po)));
//        return jpa.save(po);
//    }
//
//    @Override
//    public List<PO> saveAll(List<PO> pos) {
//        Assert.notNull(pos, "pos must be not null.");
//
////        return jpa.saveAll(pos.stream().map(this::beforeSave).collect(Collectors.toList()))
////                .stream().map(this::afterSave).collect(Collectors.toList());
//        return jpa.saveAll(pos);
//    }
//
//    @Override
//    public List<PO> modifyAll(List<PO> pos) {
//        Assert.notNull(pos, "pos must be not null.");
//
////        return jpa.saveAll(pos.stream().map(this::beforeModify).collect(Collectors.toList()))
////                .stream().map(this::afterModify).collect(Collectors.toList());
//        return jpa.saveAll(pos);
//    }
//
//    @Override
//    public PO findById(ID id) {
//        Assert.notNull(id, "ID must be not null.");
//
//        return jpa.findById(id).orElse(null);
//    }
//
//    @Override
//    public List<PO> findAllByIds(List<ID> ids) {
//        Assert.notNull(ids, "ID must be not null.");
//
//        return jpa.findAllById(ids);
//    }
//
//    @Override
//    public List<PO> listAll(DTO dto) {
//        Assert.notNull(dto, "DTO must be not null.");
//
//        Sort sort = DTOConverter.toSort(dto.getNsort());
//
//        Example<PO> example = getExample(dto, this.clazz);
//        if (example == null)
//            return jpa.findAll(sort);
//
//        return jpa.findAll(example, sort);
//    }
//
//    @Override
//    public DTO findAll(DTO dto) {
//        Assert.notNull(dto, "DTO must be not null.");
//
//        PageRequest request = DTOConverter.toPageRequest(dto);
//
//        Example<PO> example = getExample(dto, this.clazz);
//        if (example == null)
//            return DTOConverter.toDto(jpa.findAll(request));
//
//        return DTOConverter.toDto(jpa.findAll(example, request));
//    }
//
//    @Override
//    public long getCount(DTO dto) {
//        Example<PO> example = getExample(dto, this.clazz);
//        if (example == null)
//            return jpa.count();
//
//        return jpa.count(example);
//    }
//
//    /**
//     * @param id id
//     */
//    @Override
//    public PO deleteById(ID id) {
//        Assert.notNull(id, "ID must be not null.");
//
//        Optional<PO> po = jpa.findById(id);
//
////        po.ifPresent(this::beforeDelete);
//        jpa.deleteById(id);
////        po.ifPresent(this::afterDelete);
//
//        return po.orElse(null);
//    }
//
//    /**
//     * @param ids ids
//     */
//    @Override
//    public List<PO> deleteByIds(List<ID> ids) {
//        Assert.notNull(ids, "IDS must be not null.");
//
//        List<PO> list = new ArrayList<>();
//
//        for (ID id : ids)
//            list.add(this.deleteById(id));
//
//        return list;
//    }
//
//    /**
//     * 解析参数，返回 Example 需要的参数
//     *
//     * @param dto dto
//     * @return Example
//     */
//    public static <T> Example<T> getExample(DTO dto, Class<T> clazz) {
//        if (dto.getParamsMap() != null && dto.getParamsMap().size() > 0)
//            return Example.of(Orika.o2o(dto.getParamsMap(), clazz));
//
//        return null;
//    }
//}

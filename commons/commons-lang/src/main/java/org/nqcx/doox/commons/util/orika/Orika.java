/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.orika;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import java.util.List;
import java.util.Optional;

/**
 * @author naqichuan 2018/9/20 17:37
 */
public class Orika {

    //    private final static MapperFactory mapperFactory = new DefaultMapperFactory.Builder()
    //            .unenhanceStrategy(new HibernateUnenhanceStrategy()).build();
    private final static MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

    public static synchronized MapperFactory instance() {
        return mapperFactory;
    }

    public static MapperFacade getMapper() {
        return instance().getMapperFacade();
    }

    /**
     * @param o     o
     * @param clazz class
     * @param <O>   Original
     * @param <T>   Target
     * @return Target instance
     */
    public static <O, T> T o2o(O o, Class<T> clazz) {
        return o == null ? null : getMapper().map(o, clazz);
    }

    /**
     * @param o   Original object
     * @param t   Target object
     * @param <O> Original
     * @param <T> Target
     * @return Target instance
     */
    public static <O, T> T o2o(O o, T t) {
        getMapper().map(o, t);
        return t;
    }

    /**
     * @param o     o
     * @param clazz class
     * @param <O>   Original
     * @param <T>   Target
     * @return Target instance
     */
    public static <O, T> Optional<T> o2oo(O o, Class<T> clazz) {
        return Optional.ofNullable(o).map(t -> o2o(t, clazz));
    }

    /**
     * @param o   Original object
     * @param t   Target object
     * @param <O> Original
     * @param <T> Target
     * @return Target instance
     */
    public static <O, T> Optional<T> o2oo(O o, T t) {
        getMapper().map(o, t);
        return Optional.ofNullable(t);
    }

    /**
     * @param list  list
     * @param clazz class
     * @param <O>   Original
     * @param <T>   Target
     * @return Target instance list
     */
    public static <O, T> List<T> list2list(List<O> list, Class<T> clazz) {
        return l2l(list, clazz);
    }

    /**
     * @param list  list
     * @param clazz class
     * @param <O>   Original
     * @param <T>   Target
     * @return Target instance list
     */
    public static <O, T> List<T> l2l(List<O> list, Class<T> clazz) {
        return getMapper().mapAsList(list, clazz);
    }
}

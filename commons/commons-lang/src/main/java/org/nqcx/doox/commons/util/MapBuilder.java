/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author naqichuan 2014年8月14日 上午11:47:41
 */
public class MapBuilder {

    private final Map<Object, Object> map;

    private MapBuilder(){
        map = new LinkedHashMap<>();
    }

    private MapBuilder(Map<?, ?> map){
        this();
        Optional.ofNullable(map).ifPresent(this.map::putAll);
    }

    public static MapBuilder instance() {
        return new MapBuilder();
    }

    public static MapBuilder instance(Map<?, ?> map) {
        return new MapBuilder(map);
    }

    /**
     * 将 value 放到 map
     *
     * @param key
     * @param value
     * @return
     */
    public MapBuilder put(Object key, Object value) {
        this.map.put(key, value);
        return this;
    }

    /**
     * 将 map 对象添加到 map
     *
     * @param map
     * @return
     */
    public MapBuilder putMap(Map<?, ?> map) {
        this.map.putAll(map);
        return this;
    }

    /**
     * 将 list 数组添加到 map
     *
     * @param key
     * @param list
     * @return
     */
    public MapBuilder pubArray(Object key, List<?> list) {
        this.map.put(key, list);
        return this;
    }

    public Map<?, ?> build() {
        return map;
    }
}

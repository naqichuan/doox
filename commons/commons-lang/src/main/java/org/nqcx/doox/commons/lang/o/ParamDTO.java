/*
 * Copyright 2022 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author naqichuan 22-5-17 下午8:04
 */
public class ParamDTO implements Serializable {

    // 调用参数表
    protected Map<String, Object> params;

    public ParamDTO setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

    public Map<String, Object> getParams() {
        return this.params;
    }

    /**
     * putParam
     *
     * @param name  name
     * @param value value
     * @return {@link ParamDTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public ParamDTO putParam(String name, Object value) {
        Map<String, Object> params = this.getParams();
        if (params == null)
            params = new LinkedHashMap<>();

        params.put(name, value);

        return this.setParams(params);
    }

    /**
     * putParamWhen
     *
     * @param name      name
     * @param value     value
     * @param predicate predicate
     * @return {@link ParamDTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public <T> ParamDTO putParamWhen(String name, T value, Predicate<T> predicate) {
        if (predicate != null && predicate.test(value))
            return putParam(name, value);

        return this;
    }

    /**
     * putParamWith
     *
     * @param name      name
     * @param value     value
     * @param predicate predicate
     * @return {@link ParamDTO}
     * @author naqichuan 22-5-17 下午4:30
     */
    @Deprecated
    public <T> ParamDTO putParamWith(String name, T value, Predicate<T> predicate) {
        return putParamWhen(name, value, predicate);
    }

    /**
     * removeParam
     *
     * @param name name
     * @author naqichuan 22-5-17 下午4:31
     */
    public void removeParam(String name) {
        Optional.ofNullable(this.getParams()).ifPresent(x -> x.remove(name));
    }

    /**
     * param
     *
     * @param name name
     * @return {@link T}
     * @author naqichuan 22-5-17 下午4:35
     */
    public <T> T param(String name) {
        return this.getParams() == null ? null : (T) this.getParams().get(name);
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object
 *
 * @author naqichuan 2014年8月14日 上午10:58:13
 */
public class DTO implements Serializable {

    /**
     * 成功标记
     */
    private boolean success = false;

    /**
     * 实体对象
     */
    protected Object object;

    /**
     * 实例对象列表
     */
    protected List<?> list;

    /**
     * 参数列表
     */
    protected Map<String, Object> paramsMap;

    /**
     * 返回结果
     */
    protected Map<String, Object> resultMap;

    /**
     * 分页
     */
    protected INPage page;

    /**
     * 排序
     */
    protected NSort sort;

    public DTO() {

    }

    public DTO(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public DTO setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    /**
     * 取实体对象
     *
     * @return object
     */
    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T) object;
    }

    public DTO setObject(Object object) {
        this.object = object;
        return this;
    }

    /**
     * 取实体对象列表
     *
     * @return 实体对象的List
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return (List<T>) list;
    }

    public DTO setList(List<?> list) {
        this.list = list;
        return this;
    }

    public Map<String, Object> getParamsMap() {
        return paramsMap;
    }

    public DTO setParamsMap(Map<String, Object> paramsMap) {
        this.paramsMap = paramsMap;
        return this;
    }

    public DTO putParam(String key, Object value) {
        if (this.paramsMap == null)
            this.paramsMap = new LinkedHashMap<String, Object>();
        this.paramsMap.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getParam(String key) {
        return this.paramsMap == null ? null : (T) this.paramsMap.get(key);
    }

    public void removeParam(String key) {
        if (this.paramsMap != null)
            this.paramsMap.remove(key);
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public DTO setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
        return this;
    }

    public DTO putResult(String key, Object value) {
        if (this.resultMap == null)
            this.resultMap = new LinkedHashMap<String, Object>();
        this.resultMap.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getResult(String key) {
        return this.resultMap == null ? null : (T) this.resultMap.get(key);
    }

    public void removeResult(String key) {
        if (this.resultMap != null)
            this.resultMap.remove(key);
    }

    public INPage getPage() {
        return page;
    }

    public DTO setPage(INPage page) {
        this.page = page;
        return this;
    }

    public long getTotalCount() {
        return getPage() == null ? 0 : getPage().getTotalCount();
    }

    public NSort getSort() {
        return sort;
    }

    public DTO setSort(NSort sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public String toString() {
        return "DTO{" +
                "success=" + success +
                ", object=" + object +
                ", list=" + list +
                ", paramsMap=" + paramsMap +
                ", resultMap=" + resultMap +
                ", page=" + page +
                ", sort=" + sort +
                '}';
    }
}

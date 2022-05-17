/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * Data transfer object
 *
 * @author naqichuan 2014年8月14日 上午10:58:13
 */
public class DTO implements Serializable {

    // 调用参数表
    protected Map<String, Object> params;

    // 调用成功标记（默认false：调用失败；true：调用成功）
    protected boolean success = false;
    // 调用失败，返回错误码及错误值
    protected Map<NError, Object[]> errors;

    // 调用成功，返回的实体对象
    protected Object object;
    // 调用成功，返回的实体对象列表
    protected List<?> list;
    // 调用成功，分页
    protected NPage page;
    // 调用成功，排序
    protected NSort sort;

    // 调用成功或失败，返回key-value 的结果
    protected Map<String, Object> results;

    public DTO() {
    }

    public DTO(boolean success) {
        this.success = success;
    }

    // ========================================================================

    public DTO setParams(Map<String, Object> params) {
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
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public DTO putParam(String name, Object value) {
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
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public <T> DTO putParamWhen(String name, T value, Predicate<T> predicate) {
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
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:30
     */
    @Deprecated
    public <T> DTO putParamWith(String name, T value, Predicate<T> predicate) {
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

    // ========================================================================

    /**
     * isSuccess
     *
     * @return {@link boolean}
     * @author naqichuan 22-5-17 下午5:04
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * setSuccess
     *
     * @param success success
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:04
     */
    public DTO setSuccess(boolean success) {
        this.success = success;

        return this;
    }

    /**
     * getErrors
     *
     * @return {@link Map< NError, String>}
     * @author naqichuan 22-5-17 下午5:03
     */
    public Map<NError, Object[]> getErrors() {
        return this.errors;
    }

    /**
     * setErrors
     *
     * @param errors errors
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public DTO setErrors(Map<NError, Object[]> errors) {
        this.errors = errors;

        return this;
    }

    /**
     * putError
     *
     * @param error error
     * @param args  args
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public DTO putError(NError error, Object... args) {
        Map<NError, Object[]> errors = this.getErrors();
        if (errors == null)
            errors = new LinkedHashMap<>();

        errors.put(error, args);

        return this.setErrors(errors);
    }

    /**
     * putError
     *
     * @param error error
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public DTO putError(NError error) {
        return this.putError(error, null);
    }

    /**
     * removeError
     *
     * @param error error
     * @author naqichuan 22-5-17 下午5:03
     */
    public void removeError(NError error) {
        Optional.ofNullable(this.getErrors()).ifPresent(x -> {
            x.remove(error);
        });
    }

    // ========================================================================

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

    public <T> DTO setObjectWhen(T t, Predicate<T> predicate) {
        if (predicate != null && predicate.test(t))
            this.setObject(t);
        return this;
    }

    @Deprecated
    public <T> DTO setObjectWith(T t, Predicate<T> predicate) {
        return setObjectWhen(t, predicate);
    }

    // ========================================================================

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

    // ========================================================================

    public NPage getPage() {
        return page;
    }

    public DTO setPage(NPage page) {
        this.page = page;
        return this;
    }

    public Long totalCount() {
        return getPage() == null ? null : getPage().getTotalCount();
    }

    public DTO newPageWith(Long page) {
        return newPageWith(page, null);
    }

    public DTO newPageWith(Long page, Long pageSize) {
        return newPageWith(page, pageSize, null);
    }

    public DTO newPageWith(Long page, Long pageSize, Long totalCount) {
        if (page == null) {
            this.page = null;
            return this;
        }

        this.page = new NPage(page, pageSize == null ? 0 : pageSize)
                .setTotalCount(totalCount == null ? -1 : totalCount);

        return this;
    }

    // ========================================================================

    public NSort getSort() {
        return sort;
    }

    public DTO setSort(NSort sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Sort fields and directions array, e.g. ["field1,asc", "field2,desc", ...]
     * When array length equals 1, can sort more than one with expressions "field1,asc;field2,desc;..."
     *
     * @param sorts array
     * @return DTO
     */
    public DTO newSortsWith(String... sorts) {
        if (sorts == null || sorts.length == 0)
            this.sort = null;
        else
            this.sort = NSort.parse(sorts);

        return this;
    }

    // ========================================================================

    /**
     * setResults
     *
     * @param results results
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:44
     */
    public DTO setResults(Map<String, Object> results) {
        this.results = results;

        return this;
    }

    /**
     * getResults
     *
     * @return {@link Map<String, Object>}
     * @author naqichuan 22-5-17 下午4:44
     */
    public Map<String, Object> getResults() {
        return results;
    }

    /**
     * putResult
     *
     * @param name  name
     * @param value value
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public DTO putResult(String name, Object value) {
        Map<String, Object> results = this.getResults();
        if (results == null)
            results = new LinkedHashMap<>();

        results.put(name, value);

        return this.setResults(results);
    }

    /**
     * putResultWhen
     *
     * @param name      name
     * @param value     value
     * @param predicate predicate
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:29
     */
    public <T> DTO putResultWhen(String name, T value, Predicate<T> predicate) {
        if (predicate != null && predicate.test(value))
            return putResult(name, value);

        return this;
    }

    /**
     * putResultWith
     *
     * @param name      name
     * @param value     value
     * @param predicate predicate
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午4:30
     */
    @Deprecated
    public <T> DTO putResultWith(String name, T value, Predicate<T> predicate) {
        return putResultWhen(name, value, predicate);
    }

    /**
     * removeResult
     *
     * @param name name
     * @author naqichuan 22-5-17 下午4:31
     */
    public void removeResult(String name) {
        Optional.ofNullable(this.getResults()).ifPresent(x -> x.remove(name));
    }

    /**
     * result
     *
     * @param name name
     * @return {@link T}
     * @author naqichuan 22-5-17 下午4:35
     */
    public <T> T result(String name) {
        return this.getResults() == null ? null : (T) this.getResults().get(name);
    }

    // ========================================================================

    @Override
    public String toString() {
        return new StringJoiner(", ", DTO.class.getSimpleName() + "[", "]")
                .add("params=" + params)
                .add("success=" + success)
                .add("errors=" + errors)
                .add("object=" + object)
                .add("list=" + list)
                .add("page=" + page)
                .add("sort=" + sort)
                .add("results=" + results)
                .toString();
    }

//    public static void main(String[] args) {
//        DTO d = new DTO().putParam("id", "1")
//                .putError(NErrorCode.E6.error()).putError(NErrorCode.Ex.error())
//                .putParam("date_gt", "2021-07-22")
//                 .setPage(new NPage(1, 20))
//                .newPageWith(1L, 20L)
//                .setSort(NSort.by(new NSort.NOrder(NSort.NDirection.DESC, "id")))
//                .newSortsWith("id,desc");
//    }
}

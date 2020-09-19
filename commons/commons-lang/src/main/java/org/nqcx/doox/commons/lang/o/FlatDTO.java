/*
 * Copyright 2015 ChineseAll Inc. All right reserved. This software is the
 * confidential and proprietary information of ChineseAll Inc. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with ChineseAll Inc..
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 在SDK中，用来存放接口返回值
 *
 * @author nqcx 2013-10-24 下午2:22:56
 */
public class FlatDTO<T> implements Serializable {

    /**
     * 对应接口返回值的success字段
     */
    private boolean success = false;

    /**
     * 对应接
     * 口返回值的object字段
     */
    private T object;

    /**
     * 对应接口返回值的list字段
     */
    private List<T> list;

    /**
     * 对应接口返回值的result字段
     */
    private Map<String, Object> result;

    /**
     * 对应接口返回值的page字段
     */
    private Long page = 0L;

    /**
     * 对应接口返回值的pageSize字段
     */
    private Long pageSize = 0L;

    /**
     * 对应接口返回值的totalPage字段
     */
    private Long totalPage = 0L;

    /**
     * 对应接口返回值的totalCount字段
     */
    private Long totalCount = 0L;

    /**
     * 对应接口返回值的errorText字段
     */
    private String errorText;

    /**
     * 对应接口返回值的errorCode字段
     */
    private String errorCode;

    /**
     * 多个错
     */
    private List<MultipleError> multipleErrors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getObject() {
        return (T) object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public List<T> getList() {
        return (List<T>) list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Long totalPage) {
        this.totalPage = totalPage;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public List<MultipleError> getMultipleErrors() {
        return multipleErrors;
    }

    public void setMultipleErrors(List<MultipleError> multipleErrors) {
        this.multipleErrors = multipleErrors;
    }

    static class MultipleError {

        private String multipleErrorCode;
        private String multipleErrorText;

        public String getMultipleErrorCode() {
            return multipleErrorCode;
        }

        public void setMultipleErrorCode(String multipleErrorCode) {
            this.multipleErrorCode = multipleErrorCode;
        }

        public String getMultipleErrorText() {
            return multipleErrorText;
        }

        public void setMultipleErrorText(String multipleErrorText) {
            this.multipleErrorText = multipleErrorText;
        }
    }
}

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
public class FlatDTO implements Serializable {

    /**
     * 对应接口返回值的success字段
     */
    private boolean success = false;

    /**
     * 对应接
     * 口返回值的object字段
     */
    private Object object;

    /**
     * 对应接口返回值的list字段
     */
    private List<Object> list;

    /**
     * 对应接口返回值的result字段
     */
    private Map<String, Object> result;

    /**
     * 对应接口返回值的page字段
     */
    private int page = 0;

    /**
     * 对应接口返回值的pageSize字段
     */
    private int pageSize = 0;

    /**
     * 对应接口返回值的totalPage字段
     */
    private int totalPage = 0;

    /**
     * 对应接口返回值的totalCount字段
     */
    private int totalCount = 0;

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

    @SuppressWarnings("unchecked")
    public <T> T getObject() {
        return (T) object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getList() {
        return (List<T>) list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
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

    public static class MultipleError {

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

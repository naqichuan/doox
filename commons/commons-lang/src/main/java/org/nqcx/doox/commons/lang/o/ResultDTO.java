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

/**
 * @author naqichuan 22-5-17 下午4:07
 */
public class ResultDTO implements Serializable {

    // 调用成功标记（默认false：调用失败；true：调用成功）
    protected boolean success = false;
    // 调用失败，返回错误码及错误值
    protected Map<NError, String> errors;

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
    public ResultDTO setSuccess(boolean success) {
        this.success = success;

        return this;
    }

    /**
     * getErrors
     *
     * @return {@link Map< NError, String>}
     * @author naqichuan 22-5-17 下午5:03
     */
    public Map<NError, String> getErrors() {
        return this.errors;
    }

    /**
     * setErrors
     *
     * @param errors errors
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public ResultDTO setErrors(Map<NError, String> errors) {
        this.errors = errors;

        return this;
    }

    /**
     * putError
     *
     * @param error          error
     * @param finalErrorText finalErrorText
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public ResultDTO putError(NError error, String finalErrorText) {
        Map<NError, String> errors = this.getErrors();
        if (errors == null)
            errors = new LinkedHashMap<>();

        errors.put(error, finalErrorText);

        return this.setErrors(errors);
    }

    /**
     * putError
     *
     * @param error error
     * @return {@link DTO}
     * @author naqichuan 22-5-17 下午5:03
     */
    public ResultDTO putError(NError error) {
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
}

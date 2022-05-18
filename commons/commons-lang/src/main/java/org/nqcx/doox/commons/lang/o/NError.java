/*
 * Copyright 2022 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * @author naqichuan 22-5-17 上午10:10
 */
public class NError implements Serializable {

    private String errorCode;
    private String errorText;
    private String codePrefix = "";

    public NError() {
    }

    public NError(String errorCode, String errorText) {
        this.errorCode = errorCode;
        this.errorText = errorText;
    }

    public NError(String errorCode, String errorText, String codePrefix) {
        this(errorCode, errorText);
        this.codePrefix = Optional.ofNullable(codePrefix).orElse("");
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String codePrefix() {
        return this.codePrefix;
    }

    public String codePrefix(String codePrefix) {
        this.codePrefix = Optional.ofNullable(codePrefix).orElse("");
        return this.codePrefix;
    }

    public String fullErrorCode() {
        return this.codePrefix + this.errorCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NError nError = (NError) o;
        return Objects.equals(errorCode, nError.errorCode) &&
                Objects.equals(errorText, nError.errorText) &&
                Objects.equals(codePrefix, nError.codePrefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorCode, errorText, codePrefix);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NError.class.getSimpleName() + "[", "]")
                .add("errorCode='" + errorCode + "'")
                .add("errorText='" + errorText + "'")
                .add("codePrefix='" + codePrefix + "'")
                .toString();
    }
}

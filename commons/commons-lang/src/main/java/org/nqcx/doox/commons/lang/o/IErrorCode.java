/*
 * Copyright 2022 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.text.MessageFormat;

/**
 * @author naqichuan 22-5-17 上午11:47
 */
public interface IErrorCode {

    String getCode();

    String getText();

    default String codePrefix() {
        return "error.";
    }

    /**
     * build the NError object
     *
     * @return {@link NError}
     * @author naqichuan 22-5-18 下午7:15
     */
    default NError buildError() {
        return new NError(codePrefix() + getCode(), getText());
    }

    /**
     * build full error text
     *
     * @param errorText         errorText
     * @param placeHolderValues placeHolderValues
     * @return {@link String}
     * @author naqichuan 22-5-18 下午7:16
     */
    static String fullErrorText(String errorText, Object... placeHolderValues) {
        return (errorText == null || placeHolderValues == null) ? errorText : MessageFormat.format(errorText, placeHolderValues);
    }
}

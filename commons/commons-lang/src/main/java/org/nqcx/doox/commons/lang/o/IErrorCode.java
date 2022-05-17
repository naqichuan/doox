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

    default NError error() {
        return new NError(getCode(), getText());
    }

    static String finalErrorText(String errorText, Object... placeHolderValues) {
        return (errorText == null || placeHolderValues == null) ? errorText : MessageFormat.format(errorText, placeHolderValues);
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.validate;

import org.nqcx.doox.commons.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author naqichuan 16/10/12 16:37
 */
public class NValidator {


    /**
     * Email pattern
     */
    public final static String EMAIL_PATTERN = "\\b(^['_A-Za-z0-9-]+(\\.['_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";

    /**
     * 手机号码
     * 移动：134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通：130,131,132,145,152,155,156,1709,171,176,185,186
     * 电信：133,134,153,1700,177,180,181,189
     */
    public final static String MOBILE_PATTERN = "\\b(^1([38][0-9]|4[579]|5[0-35-9]|6[6]|7[0135678])\\d{8}$)\\b";
//    /**
//     * 中国移动：China Mobile
//     * 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
//     */
//    public final static String MOBILE_CM_PATTERN = "\\b(^1(3[4-9]|4[7]|5[0-27-9]|7[0]|7[8]|8[2-478])\\d{8}$)\\b";
//    /**
//     * 中国联通：China Unicom
//     * 130,131,132,145,152,155,156,1709,171,176,185,186
//     */
//    public final static String MOBILE_CU_PATTERN = "\\b(^1(3[0-2]|4[5]|5[56]|709|7[1]|7[6]|8[56])\\d{8}$)\\b";
//    /**
//     * 中国电信：China Telecom
//     * 133,134,153,1700,177,180,181,189
//     */
//    public final static String MOBILE_CT_PATTERN = "\\b(^1(3[34]|53|77|700|8[019])\\d{8}$)\\b";

    /**
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {
        return validate(value, EMAIL_PATTERN, false, true);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isMobile(String value) {
        return validate(value, MOBILE_PATTERN);
    }

//    /**
//     * @param value
//     * @return
//     */
//    public static boolean isMobileCM(String value) {
//        return validate(value, MOBILE_CM_PATTERN);
//    }
//
//    /**
//     * @param value
//     * @return
//     */
//    public static boolean isMobileCU(String value) {
//        return validate(value, MOBILE_CU_PATTERN);
//    }
//
//    /**
//     * @param value
//     * @return
//     */
//    public static boolean isMobileCT(String value) {
//        return validate(value, MOBILE_CT_PATTERN);
//    }

    /**
     * validate
     *
     * @param value
     * @return
     */
    public static boolean validate(String value, String pattern) {
        return validate(value, pattern, true, false);
    }

    /**
     * validate
     *
     * @param value
     * @param pattern
     * @param caseSensitive
     * @param trim
     * @return
     */
    public static boolean validate(String value, String pattern, boolean caseSensitive, boolean trim) {

        if (StringUtils.isBlank(value) || StringUtils.isBlank(pattern))
            return false;

        Pattern p = (caseSensitive ? Pattern.compile(pattern) : Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));

        if (trim)
            value = value.trim();

        return p.matcher(value).matches();
    }
}

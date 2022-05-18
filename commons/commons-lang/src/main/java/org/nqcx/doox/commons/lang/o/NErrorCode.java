/*
 * Copyright 2022 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.lang.o;

import java.util.Arrays;

/**
 * @author naqichuan 22-5-17 下午5:28
 */
public enum NErrorCode implements IErrorCode {

    E0("0", "未知错误"), //
    E1("1", "参数异常"), //
    E2("2", "数据异常"), //
    E3("3", "数据为空或不存在"), //
    E4("4", "数据不完整"), //
    E5("5", "无权限"), //
    E6("6", "运行时异常"), //
    E7("7", "调用方法或函数不存在"), //
    E8("8", "调用方法或函数异常"), //
    E10("10", "不是一个有效的访问来源"), //
    E11("11", "需要验证身份"), //
    E12("12", "访问的内容不存在"), //
    E13("13", "循环重定向");

    private String code;
    private String text;

    NErrorCode(String code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getText() {
        return this.text;
    }

    /**
     * 判断自身是否与参数里的枚举相等
     *
     * @param eo eo
     * @return boolean
     */
    public boolean is(NErrorCode eo) {
        return this == eo;
    }

    /**
     * 判断 this 是否在 eos 数组中
     *
     * @param eos eos
     * @return EOOUtils.contain(eos, this);
     */
    public boolean in(NErrorCode[] eos) {
        if (eos == null || eos.length == 0)
            return false;

        for (NErrorCode n : eos) {
            if (n == this)
                return true;
        }

        return false;
    }

    /**
     * error
     *
     * @return {@link NError}
     *
     * @author naqichuan 22-5-17 下午8:14
     */
    public NError error() {
        return new NError(this.getCode(),this.text);
    }

    /**
     * 通过 code 取得枚举实例
     *
     * @param code code
     * @return NErrorCode
     */
    public static NErrorCode of(String code) {
        for (NErrorCode e : NErrorCode.values()) {
            if (e.code.equals(code))
                return e;
        }
        throw new IllegalArgumentException("unknown code:" + code);
    }

    /**
     * print all
     *
     * @author naqichuan 22-5-17 下午5:36
     */
    public static void print() {
        Arrays.stream(NErrorCode.values()).forEach(x -> {
            System.out.println("error." + x.code + "=" + x.getText());
        });
    }

    public static void main(String[] args) {
        print();
    }
}

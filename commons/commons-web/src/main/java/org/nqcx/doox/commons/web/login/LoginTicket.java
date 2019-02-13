/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.login;

import java.io.Serializable;
import java.util.Date;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginTicket implements Serializable {

    private final static ThreadLocal<LoginTicket> holder = new ThreadLocal<LoginTicket>();

    /**
     * 指定的版本号
     */
    private int _version = 0;

    /**
     * account
     */
    private String _account;
    /**
     * 用户data
     */
    private String _userData;
    /**
     * 登录写入的路径
     */
    private String _appPath;
    /**
     * 过期日期
     */
    private Date _expires;
    /**
     * 发布日期
     */
    private Date _issueDate;

    private boolean _isPersistent = false;

    public int getVersion() {
        return _version;
    }

    public String getAccount() {
        return _account;
    }

    public String getUserData() {
        return _userData;
    }

    public String getAppPath() {
        return _appPath;
    }

    public Date getExpires() {
        return _expires;
    }

    public Date getIssueDate() {
        return _issueDate;
    }

    public boolean isPersistent() {
        return _isPersistent;
    }

    /**
     * 判断cookie是不是过期
     *
     * @return true 过期。false 未过期
     */
    public boolean isExpired() {
        return (new Date()).after(_expires);
    }

    public LoginTicket(String _account) {
        this(_account, null, null, null, null, 0, false);
    }

    public LoginTicket(String account, String userdata, String appPath, Date issued, Date expires, int version,
                       boolean isPersistent) {
        if (account == null || account.length() == 0)
            throw new RuntimeException("account is empty");
        else
            _account = account;
        if (userdata == null)
            _userData = "";
        else
            _userData = userdata;
        if (appPath == null)
            _appPath = "/";
        else if (!appPath.startsWith("/"))
            _appPath = "/" + appPath;
        else
            _appPath = appPath;
        if (issued == null)
            _issueDate = new Date();
        else
            _issueDate = issued;
        if (expires == null)
            _expires = new Date(System.currentTimeMillis() + 30 * 1000 * 60); // 30
            // mins
        else
            _expires = expires;
        if (version > 0)
            _version = version;
        else
            _version = 1;

    }

    public String toString() {
        return "version=" + _version + "," + "account=" + _account + "," + "userData=" + _userData + "," + "appPath="
                + _appPath + "," + "isPersistent=" + _isPersistent + "," + "issueDate=" + _issueDate + "," + "expires="
                + _expires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        LoginTicket ticket = (LoginTicket) o;

        if (_isPersistent != ticket._isPersistent)
            return false;
        if (_version != ticket._version)
            return false;
        if (_appPath != null ? !_appPath.equals(ticket._appPath) : ticket._appPath != null)
            return false;
        if (_expires != null ? !_expires.equals(ticket._expires) : ticket._expires != null)
            return false;
        if (_issueDate != null ? !_issueDate.equals(ticket._issueDate) : ticket._issueDate != null)
            return false;
        if (_userData != null ? !_userData.equals(ticket._userData) : ticket._userData != null)
            return false;
        if (_account != null ? !_account.equals(ticket._account) : ticket._account != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = _version;
        result = 31 * result + (_account != null ? _account.hashCode() : 0);
        result = 31 * result + (_userData != null ? _userData.hashCode() : 0);
        result = 31 * result + (_appPath != null ? _appPath.hashCode() : 0);
        result = 31 * result + (_expires != null ? _expires.hashCode() : 0);
        result = 31 * result + (_issueDate != null ? _issueDate.hashCode() : 0);
        result = 31 * result + (_isPersistent ? 1 : 0);
        return result;
    }

    /**
     * 实际上是将LoginTicket放到了actionContext中
     *
     * @param ticket 对象
     */
    public static void setTicket(LoginTicket ticket) {
        holder.set(ticket);
    }

    /**
     * 取出ticket的上下文
     *
     * @return null 如果没有的话
     */
    public static LoginTicket getTicket() {
        return holder.get();
    }

    /**
     * 删除上下文、其实一般不用删除
     */
    public static void remove() {
        holder.remove();
    }
}

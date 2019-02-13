/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.web.login;

import org.apache.commons.beanutils.BeanUtils;
import org.nqcx.doox.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * @author naqichuan 2014年8月14日 上午11:50:15
 */
public class LoginContext implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(LoginContext.class);

    private final static ThreadLocal<LoginContext> holder = new ThreadLocal<LoginContext>() {

        @Override
        protected LoginContext initialValue() {
            return new LoginContext();
        }
    };

    /**
     * id
     */
    private long id;
    /**
     * 账号
     */
    private String account;

    /**
     * 显示名称
     */
    private String nick;

    /**
     * login cookie的checksum
     */
    private int checksum;

    /**
     * 创建时间 默认为当前时间
     */
    private long created = System.currentTimeMillis();

    /**
     * 过期时间 如果没有指定，就使用拦截器默认的
     */
    private long expires;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * Method getNick returns the nick of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @return the nick (type String) of this LoginContext object.
     */
    public String getNick() {
        return nick;
    }

    /**
     * Method setNick sets the nick of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @param nick the nick of this LoginContext object.
     */
    public void setNick(String nick) {
        this.nick = nick;
    }


    /**
     * Method setChecksum sets the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @param checksum the checksum of this LoginContext object.
     */
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    /**
     * Method getChecksum returns the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @return the checksum (type int) of this LoginContext object.
     */
    public int getChecksum() {
        return checksum;
    }

    /**
     * Method getCreated returns the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @return the created (type long) of this LoginContext object.
     */
    public long getCreated() {
        return created;
    }

    /**
     * 创建日期
     *
     * @return 创建日期
     */
    public Date getCreatedDate() {
        return new Date(created);
    }

    /**
     * Method setCreated sets the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @param created the created of this LoginContext object.
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * Method setCreatedDate sets the createdDate of this LoginContext object.
     *
     * @param created the createdDate of this LoginContext object.
     */
    public void setCreatedDate(Date created) {
        this.created = created.getTime();
    }

    /**
     * 设置创建时间等于当前日期
     */
    public void setCreated() {
        this.created = System.currentTimeMillis();
    }

    /**
     * Method getExpires returns the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @return the expires (type long) of this LoginContext object.
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Method getExpiresDate returns the expiresDate of this LoginContext object.
     *
     * @return the expiresDate (type Date) of this LoginContext object.
     */
    public Date getExpiresDate() {
        return new Date(expires);
    }

    /**
     * Method setExpires sets the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @param expires the expires of this LoginContext object.
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * Method setExpiresDate sets the expiresDate of this LoginContext object.
     *
     * @param expires the expiresDate of this LoginContext object.
     */
    public void setExpiresDate(Date expires) {
        this.expires = expires.getTime();
    }

    /**
     * 设置cookie的过期时间，单位：毫秒
     *
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.expires = this.created + timeout;
    }

    /**
     * 实际上是将loginContext放到了actionContext中
     *
     * @param loginContext 对象
     */
    public static void setLoginContext(LoginContext loginContext) {
        holder.set(loginContext);
    }

    /**
     * 取出登录的上下文
     *
     * @return null 如果没有的话
     */
    public static LoginContext getLoginContext() {
        return holder.get();
    }

    /**
     * 删除上下文、其实一般不用删除
     */
    public static void remove() {
        holder.remove();
    }

    /**
     * 反向构造上下文。
     *
     * @param value 需要反向构造的串。形式如下：mid=123,account=yangsy,nick=杨思勇
     * @return 上下文
     * @see #toCookieValue()
     */
    public static LoginContext parse(String value) {
        LoginContext context = new LoginContext();
        setValue(value, context);
        return context;
    }

    /**
     * Method setValue ...
     *
     * @param value   of type String
     * @param context of type LoginContext
     */
    protected static void setValue(String value, LoginContext context) {
        if (StringUtils.isNotEmpty(value)) {
            String[] fields = value.split(",");
            for (String keyValues : fields) {
                String[] keyValue = keyValues.split("=");
                if (keyValue.length == 2) {
                    try {
                        String field = keyValue[0];
                        if (StringUtils.isNotBlank(field)) {
                            BeanUtils.setProperty(context, field, keyValue[1]);
                        }
                    } catch (Exception e) {
                        logger.error("praser error!", e);
                    }
                }
            }
        }
    }

    /**
     * 将实体系列化成字符串。
     *
     * @return 字符串。形式：字段1=值1,字段2=值2。该方法不会返回空
     * @see #parse(String)
     */
    public String toCookieValue() {
        final StringBuilder sb = new StringBuilder();
        if (id != 0)
            sb.append(",id=").append(id);

        if (account != null)
            sb.append(",account=").append(account);

        if (nick != null)
            sb.append(",nick=").append(nick);

        if (created != 0)
            sb.append(",created=").append(created);

        if (checksum != 0)
            sb.append(",checksum=").append(checksum);

        if (expires != 0)
            sb.append(",expires=").append(expires);

        return sb.length() > 0 ? sb.substring(1) : "";
    }

    /**
     * 判断是否登录。标准：account.trim().length() > 0
     *
     * @return true 已经登录 false 没有登录
     */
    public boolean isLogin() {
        return account != null && account.trim().length() > 0;
    }
}

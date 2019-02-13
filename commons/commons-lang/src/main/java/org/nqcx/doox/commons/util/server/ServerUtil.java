/*
 * Copyright 2019 nqcx.org All right reserved. This software is the
 * confidential and proprietary information of nqcx.org ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with nqcx.org.
 */

package org.nqcx.doox.commons.util.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author naqichuan 2014年8月14日 上午11:50:00
 */
public class ServerUtil {

    private final static Logger logger = LoggerFactory.getLogger(ServerUtil.class);

    /**
     * 取服务器IP
     *
     * @return
     */
    public static String serverIp() {
        String serverip = "0.0.0.0";
        try {
            Set<String> ips = HostAddress.ipv4All();
            if (!ips.isEmpty())
                return ips.iterator().next();
        } catch (Exception e) {
            logger.error("", e);
        }
        return serverip;
    }

    /**
     * 取服务器IPS
     *
     * @return
     */
    public static Set<String> serverIps() {
        return HostAddress.ipv4All();
    }

    public static void main(String[] args) {
        System.out.println(ServerUtil.serverIp());
        System.out.println(ServerUtil.serverIps());
    }
}

/*
 * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
 * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
 */

package org.nqcx.doox.commons.util.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by naqichuan 2017/12/15 23:49
 */
public class HostAddress {

    private final static Logger LOGGER = LoggerFactory.getLogger(HostAddress.class);

    public final static String IPV4_LOCAL = "127.0.0.1";
    public final static String IPV4_ALL = "127.0.0.1";
    public final static String IPV6_LOCAL = "0:0:0:0:0:0:0:1";
    public final static String IPV6_ALL = "0:0:0:0:0:0:0:0";

    /**
     * 取得全部IP地址
     *
     * @param ipv4 ipv4
     * @param ipv6 ipv6
     * @return set
     */
    private static Set<String> all(boolean ipv4, boolean ipv6) {
        Set<String> all = new HashSet<String>();
        try {
            for (Enumeration<NetworkInterface> netWorks = NetworkInterface.getNetworkInterfaces(); netWorks.hasMoreElements(); ) {
                NetworkInterface network = netWorks.nextElement();

                for (Enumeration<InetAddress> addresses = network.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress address = addresses.nextElement();

                    if (address == null)
                        continue;

                    if (ipv4 && address instanceof Inet4Address && !address.getHostAddress().equals(IPV4_LOCAL))
                        all.add(address.getHostAddress());
                    else if (ipv6 && address instanceof Inet6Address && !address.getHostAddress().startsWith(IPV6_LOCAL))
                        all.add(address.getHostAddress());
                }

            }
        } catch (SocketException e) {
            LOGGER.error("", e);
        }

        return all;
    }

    /**
     * all
     *
     * @return set
     */
    public static Set<String> all() {
        return all(true, true);
    }

    /**
     * ipv4 first
     *
     * @return
     */
    public static String ipv4() {
        Set<String> ips = ipv4All();
        if (ips.isEmpty())
            return IPV4_LOCAL;
        return ips.iterator().next();
    }

    /**
     * ipv4 all
     *
     * @return set
     */
    public static Set<String> ipv4All() {
        return all(true, false);
    }

    /**
     * ipv4 all string
     *
     * @return
     */
    public static String ipv4AllString() {
        StringBuilder ipsBuilder = new StringBuilder();

        Iterator<String> it = ipv4All().iterator();
        while (it.hasNext()) {
            if (ipsBuilder.length() != 0)
                ipsBuilder.append(",");
            ipsBuilder.append(it.next());
        }

        return ipsBuilder.length() == 0 ? IPV4_LOCAL : ipsBuilder.toString();
    }

    /**
     * ipv6 all
     *
     * @return set
     */
    public static Set<String> ipv6All() {
        return all(false, true);
    }


    public static void main(String[] args) {
        System.out.println("all: " + all());
        System.out.println("IPv4 all: " + ipv4All());
        System.out.println("IPv4 all string: " + ipv4AllString());
        System.out.println("IPv4: " + ipv4());
        System.out.println("IPv6 all: " + ipv6All());
    }

}

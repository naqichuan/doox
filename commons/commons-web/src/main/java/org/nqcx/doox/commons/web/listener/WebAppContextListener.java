///*
// * Copyright 2019 nqcx.org All right reserved. This software is the confidential and proprietary information
// * of nqcx.org ("Confidential Information"). You shall not disclose such Confidential Information and shall use
// * it only in accordance with the terms of the license agreement you entered into with nqcx.org.
// */
//
//package org.nqcx.doox.commons.web.listener;
//
//import org.nqcx.doox.commons.util.server.HostAddress;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
///**
// * Created by naqichuan 2018/4/4 11:46
// */
//public class WebAppContextListener implements ServletContextListener {
//
//    @Override
//    public void contextInitialized(ServletContextEvent sce) {
//        // 用于标识是哪个服务器输出的日志
//        // 服务器 ip 地址，当前只取 ipv4 的一个地址
//        System.setProperty("SERVER_ADDR", HostAddress.ipv4());
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent sce) {
//        // ignore
//    }
//}

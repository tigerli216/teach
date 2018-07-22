/*
 * @(#)WarStartUp.java 2016年9月6日下午1:54:03
 * Copyright 2016 gbtsoft, Inc. All rights reserved.
 */
package com.wst;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.SpringUtils;

/**
 * 打包成war的启动入口
 * 
 * @author liqg
 * @date 2016年9月6日下午1:54:03
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = { "com" })
public class WarStartUp extends SpringBootServletInitializer {

    private static HiwiLog log = HiwiLogFactory.getLogger(WarStartUp.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        log.info("===========start configure===========");
        return application.sources(WarStartUp.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.boot.context.web.SpringBootServletInitializer#
     * onStartup(javax.servlet.ServletContext)
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext rootAppContext = createRootApplicationContext(servletContext);

        if (rootAppContext != null) {
            SpringUtils.setCtx(rootAppContext);
            servletContext.addListener(new ContextLoaderListener(rootAppContext) {
                @Override
                public void contextInitialized(ServletContextEvent event) {
                    // 初始化数据
                    log.info("===========启动成功===========");
                }
            });
        } else {
            log.info("No ContextLoaderListener registered, as " + "createRootApplicationContext() did not "
                    + "return an application context");
        }
    }
}

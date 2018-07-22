package com.wst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.hiwi.common.util.SpringUtils;

/**
 * jar包的启动入口
 * 
 * @author liqg
 * @date 2016年9月6日下午2:55:27
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = { "com" })
public class StartUp {

    private static HiwiLog log = HiwiLogFactory.getLogger(StartUp.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StartUp.class);
        app.setWebEnvironment(true);

        // 初始化IOC容器
        ApplicationContext ctx = app.run(args);
        SpringUtils.setCtx(ctx);

        // 初始化基础参数
        initBaseParam();

        log.info("===========启动成功===========");

    }

    /**
     * 初始化基本参数
     */
    public static void initBaseParam() {
    }

}

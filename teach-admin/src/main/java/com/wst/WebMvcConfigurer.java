package com.wst;
/*
 * @(#)WebMvcConfigurer.java 2016年9月6日下午2:30:26
 * Copyright 2016 gbtsoft, Inc. All rights reserved.
 */


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.catalina.Context;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.wst.base.interceptor.BaseInterceptor;
import com.wst.constant.ApplicationConstant;

/**
 * mvc配置处理
 * 
 * @author liqg
 * @date 2016年9月6日下午2:30:26
 * @version 1.0
 */

@Configuration
@ServletComponentScan(basePackages = { "com.hiwi" }) // 自动扫描servlet、filter
// 自动扫描servlet、filter
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 7)
// 启用spring-redis-session，共享session（7天失效）
@ImportResource(locations = { "classpath:config.xml" })
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static HiwiLog log = HiwiLogFactory.getLogger(WebMvcConfigurer.class);

    /**
     * 应用信息
     */
    public static final ApplicationConstant APPLICATION = ApplicationConstant.TEACH_CORE;
    
    /**
     * 配置拦截器 独立出来是为了避免与config.xml冲突
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BaseInterceptor()).addPathPatterns("/d-admin/**");
        log.info("===========开启配置拦截器===========");
    }

    /**
     * 使用fastjson
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        MediaType textMedia = MediaType.parseMediaType("text/html;charset=UTF-8");
        MediaType jsonMedia = MediaType.parseMediaType("application/json;charset=UTF-8");
        fastConverter.setSupportedMediaTypes(new ArrayList<MediaType>(Arrays.asList(textMedia, jsonMedia)));

        SerializerFeature[] features = new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat };
        
        fastConverter.getFastJsonConfig().setSerializerFeatures(features);
        converters.add(fastConverter);
        
        ParserConfig.getGlobalInstance().addAccept("com.wst");
    }

    /**
     * 配置错误返回页面
     * 
     * @author liqg @creationDate. 2016年8月29日 下午5:48:43
     * @return
     */
    @Bean
    public EmbeddedServletContainerFactory containerCustomizer() {
        log.info("===========开启配置欢迎页===========");
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        // 配置欢迎页面
        TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addWelcomeFile("/index.html");
            }
        };
        factory.addContextCustomizers(contextCustomizer);
        return factory;
    }

    /**
     * 配置错误返回页面
     * 
     * @return
     */
    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/common/500.html");
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/common/500.html");
                ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/common/403.html");
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/common/404.html");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/common/500.html");

                container.addErrorPages(error400Page, error401Page, error403Page, error404Page, error500Page);
            }
        };
    }

}

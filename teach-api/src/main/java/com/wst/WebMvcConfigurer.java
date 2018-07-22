/*
 * @(#)WebMvcConfigurer.java 2016年9月6日下午2:30:26
 * Copyright 2016 gbtsoft, Inc. All rights reserved.
 */
package com.wst;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.catalina.Context;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;
import com.wst.base.interceptor.AppInterceptor;
import com.wst.base.interceptor.ThirdInterceptor;
import com.wst.third.InterceptorNeedBean;

/**
 * mvc配置处理
 * 
 * @author liqg
 * @date 2016年9月6日下午2:30:26
 * @version 1.0
 */

@Configuration
@ServletComponentScan // 自动扫描servlet、filter
@ImportResource(locations = { "classpath:config.xml" })
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60 * 60 * 24 * 90)
// 启用spring-redis-session，共享session（90天失效）
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static HiwiLog log = HiwiLogFactory.getLogger(WebMvcConfigurer.class);

    @Resource
    private InterceptorNeedBean interceptorNeedBean;

    /**
     * 配置拦截器 独立出来是为了避免与config.xml冲突
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AppInterceptor()).addPathPatterns("/d-app/API/**");
        registry.addInterceptor(new ThirdInterceptor(interceptorNeedBean)).addPathPatterns("/d-third/API/**");
        log.info("===========开启配置拦截器===========");
    }

    /**
     * 使用fastjson
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new MyFastJsonHttpMessageConverter();

        MediaType textMedia = MediaType.parseMediaType("text/html;charset=UTF-8");
        MediaType jsonMedia = MediaType.parseMediaType("application/json;charset=UTF-8");
        fastConverter.setSupportedMediaTypes(new ArrayList<MediaType>(Arrays.asList(textMedia, jsonMedia)));

        converters.add(fastConverter);

        ParserConfig.getGlobalInstance().addAccept("com.wst.");
    }

    class MyFastJsonHttpMessageConverter extends FastJsonHttpMessageConverter {

        SerializerFeature[] features = new SerializerFeature[] { SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat };

        @Override
        protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
                throws IOException, HttpMessageNotWritableException {
            OutputStream out = outputMessage.getBody();
            String text = JSON.toJSONString(obj, new ValueFilter() {
                // 自定义数据过滤，如果为null，则返回""
                @Override
                public Object process(Object object, String name, Object value) {
                    if (value == null)
                        return "";
                    return value;
                }

            }, features);
            byte[] bytes = text.getBytes("UTF-8");
            out.write(bytes);
        }
    }

    /**
     * 配置错误返回页面及欢迎页面
     * 
     * @author liqg @creationDate. 2016年8月29日 下午5:48:43
     * @return
     */
    @Bean
    public EmbeddedServletContainerFactory containerCustomizer() {
        log.info("===========开启配置欢迎页、错误页面===========");
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        // 配置欢迎页面
        TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
            @Override
            public void customize(Context context) {
                context.addWelcomeFile("/index.html");
            }
        };
        factory.addContextCustomizers(contextCustomizer);

        // 配置错误页面
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/common/500.html");
        ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/common/500.html");
        ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/common/403.html");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/common/404.html");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/common/500.html");

        factory.addErrorPages(error400Page, error401Page, error403Page, error404Page, error500Page);

        return factory;
    }

}

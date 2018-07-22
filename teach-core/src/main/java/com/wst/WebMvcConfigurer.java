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

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;

/**
 * MVC配置处理
 * 
 * @author <a href="mailto:jiangq@hiwitech.com">JiangQiang</a>
 * @version 1.0
 */
@Configuration
@ServletComponentScan(basePackages = { "com.hiwi" }) // 自动扫描servlet、filter
@ImportResource(locations = { "classpath:config.xml" })
@EnableScheduling
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

    private static HiwiLog log = HiwiLogFactory.getLogger(WebMvcConfigurer.class);

    /**
     * 配置拦截器 独立出来是为了避免与config.xml冲突
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // registry.addInterceptor(new
        // BaseInterceptor()).addPathPatterns("/d-admin/**");
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
        
        ParserConfig.getGlobalInstance().addAccept("com.wst");
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
}

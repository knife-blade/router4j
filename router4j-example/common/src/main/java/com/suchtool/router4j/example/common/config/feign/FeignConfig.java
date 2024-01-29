package com.suchtool.router4j.example.common.config.feign;

import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class FeignConfig {
    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;
 
    @Bean
    public Encoder feignEncoder() {
        return new SpringEncoder(messageConverters);
    }
 
    @Bean
    public Decoder feignDecoder() {
        return new ResponseEntityDecoder(new FeignResultDecoder(messageConverters));
    }
 
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
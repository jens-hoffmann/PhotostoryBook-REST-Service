package org.jhoffmann.photostorybookwebclient.util;

import feign.Contract;
import feign.RequestInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }


}

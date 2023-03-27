package org.jhoffmann.photostorybookwebclient.util;

import feign.Contract;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
public class FeignConfiguration {

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

}

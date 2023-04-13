package org.jhoffmann.photostorybookwebclient.util;

import feign.Logger;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.cloud.openfeign.security.OAuth2AccessTokenInterceptor;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

public class OAuthFeignConfig {

    private final OAuth2ClientProperties oAuth2ClientProperties;
    private final ObjectFactory<HttpMessageConverters> messageConverters;

    public OAuthFeignConfig(OAuth2ClientProperties oAuth2ClientProperties, ObjectFactory<HttpMessageConverters> messageConverters) {
        this.oAuth2ClientProperties = oAuth2ClientProperties;
        this.messageConverters = messageConverters;
    }

    @Bean
    @ConditionalOnBean({OAuth2AuthorizedClientService.class, ClientRegistrationRepository.class})
    @ConditionalOnMissingBean
    public OAuth2AuthorizedClientManager feignOAuth2AuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                                            OAuth2AuthorizedClientService oAuth2AuthorizedClientService) {
        return new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, oAuth2AuthorizedClientService);
    }

    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientManager.class)
    public OAuth2AccessTokenInterceptor defaultOAuth2AccessTokenInterceptor(OAuth2AuthorizedClientManager oAuth2AuthorizedClientManager) {
        return new OAuth2AccessTokenInterceptor(oAuth2ClientProperties.getRegistration().keySet().iterator().next(), oAuth2AuthorizedClientManager);
    }

    @Bean
    public Encoder feignFormEncoder () {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}

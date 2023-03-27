package org.jhoffmann.photostorybookwebclient.services;


import org.jhoffmann.photostorybookwebclient.util.OAuthFeignConfig;
import org.openapitools.client.api.PhotostoriesApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PhotostoryRestService", url = "http://localhost:8081/", configuration = OAuthFeignConfig.class)
public interface PhotostoryRestService extends PhotostoriesApi {
}

package org.jhoffmann.photostorybookwebclient.services;

import org.jhoffmann.photostorybookwebclient.util.OAuthFeignConfig;
import org.openapitools.client.api.PhotosApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "PhotoRestService", url = "http://localhost:8081/", configuration = OAuthFeignConfig.class)
public interface PhotoRestService extends PhotosApi {

}

package org.jhoffmann.photostorybookwebclient.services;


import org.jhoffmann.photostorybookwebclient.util.OAuthFeignConfig;
import org.openapitools.client.api.PhotostoriesApi;
import org.openapitools.client.model.AddPhotostoryRequest;
import org.openapitools.client.model.PhotostoryListResponse;
import org.openapitools.client.model.PhotostoryResponse;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.UUID;

@FeignClient(name = "PhotostoryRestService", url = "http://localhost:8081/", configuration = OAuthFeignConfig.class)
public interface PhotostoryRestService extends PhotostoriesApi {
}

package org.jhoffmann.photostorybookwebclient.services;

import org.jhoffmann.photostorybook.api.v1.UserApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service", url ="localhost:8080")
public interface RestUserService extends UserApi {

}

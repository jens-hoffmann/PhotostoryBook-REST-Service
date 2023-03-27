package org.jhoffmann.photostorybookwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybookwebclient.services.PhotostoryRestService;
import org.openapitools.client.model.PhotostoryListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/photostories")
public class PhotostoryController {

    private final PhotostoryRestService photostoryRestService;

    public PhotostoryController(PhotostoryRestService photostoryRestService) {
        this.photostoryRestService = photostoryRestService;
    }

    @GetMapping
    public String getPhotostories() {
        log.info("PhotostoryController: getPhotostories");

        PhotostoryListResponse response = photostoryRestService.getPhotostories();
        log.info("PhotostoryController: getPhotostories: " + response.toString());

        return "photostories";
    }
}

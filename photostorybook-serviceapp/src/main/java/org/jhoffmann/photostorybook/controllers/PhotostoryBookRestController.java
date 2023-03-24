package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.PhotostoriesApi;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.services.PhotostoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "Photostories")
@CrossOrigin(origins="http://localhost:8080")
public class PhotostoryBookRestController implements PhotostoriesApi {
    private final PhotostoryService photostoryService;

    public PhotostoryBookRestController(PhotostoryService photostoryService) {
        this.photostoryService = photostoryService;
    }

    @Override
    public ResponseEntity<PhotostoryResponse> addPhotostory(AddPhotostoryRequest addPhotostoryRequest) {
        if (addPhotostoryRequest.getStoryTitle().isEmpty())
            throw new ApiRequestException("PhotostoryBookRestController: story title of photostory is empty !");
        log.debug("Received POST Photostories with title " + addPhotostoryRequest.getStoryTitle());
        PhotostoryResponse photostoryResponse = photostoryService.addPhotostory(addPhotostoryRequest);

        return new ResponseEntity<>(photostoryResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PhotostoryListResponse> getPhotostories() {
        log.debug("Received GET Photostories");
        PhotostoryListResponse listResponse = photostoryService.getPhotostories();
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }
}

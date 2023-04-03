package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.PhotostoriesApi;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.ModifyPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.services.PhotostoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            throw new ApiRequestException("PhotostoryBookRestController: No authorized user");
        }
        PhotostoryResponse photostoryResponse = photostoryService.addPhotostory(addPhotostoryRequest, currentUserName);

        return new ResponseEntity<>(photostoryResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<PhotostoryListResponse> getPhotostories() {
        log.debug("Received GET Photostories");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            throw new ApiRequestException("PhotostoryBookRestController: No authorized user");
        }
        PhotostoryListResponse listResponse = photostoryService.getPhotostories(currentUserName);
        return new ResponseEntity<>(listResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<PhotostoryResponse> modifyPhotostory(UUID storyId, ModifyPhotostoryRequest modifyPhotostoryRequest) {
        log.debug("PhotostoryBookRestController: modifyPhotostory");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("PhotostoryBookRestController: modifyPhotostory: currentUserName " + currentUserName);
        } else {
            throw new ApiRequestException("PhotostoryBookRestController: modifyPhotostory: No authorized user");
        }
        PhotostoryResponse photostoryResponse = photostoryService.modifyPhotostory(modifyPhotostoryRequest, currentUserName, storyId);
        return new ResponseEntity<>(photostoryResponse, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<Void> deletePhotostory(UUID storyId) {
        log.debug("Received DELETE Photostories");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("DELETE deletePhotostory: currentUserName " + currentUserName);
        } else {
            throw new ApiRequestException("PhotostoryBookRestController: No authorized user");
        }
        photostoryService.deletePhotostory(currentUserName, storyId.toString());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.PhotosApi;
import org.jhoffmann.photostorybook.api.v1.model.PhotoDetailsResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryWithPhotosResponse;
import org.jhoffmann.photostorybook.services.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "Photos")
@CrossOrigin(origins="http://localhost:8080")
public class PhotosRestController implements PhotosApi {

    private final PhotoService photoService;

    public PhotosRestController(PhotoService photoService) {
        this.photoService = photoService;

    }

    @Override
    public ResponseEntity<PhotoDetailsResponse> uploadPhoto(UUID storyId, @Valid byte[] body) {
        log.info("PhotosRestController: POST uploadPhoto");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("PhotosRestController: POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String uuidFilename = photoService.upload(body, "jpg", storyId, currentUserName);
        PhotoDetailsResponse photoDetailsResponse = new PhotoDetailsResponse();
        log.info("PhotosRestController: POST uploadPhoto with UUID " + uuidFilename);
        photoDetailsResponse.setUuid(UUID.fromString(uuidFilename));
        photoDetailsResponse.setPhotoTitle("No title");

        return new ResponseEntity<>(photoDetailsResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<byte[]> downloadPhoto(
            @PathVariable("storyId") UUID storyId,
            @PathVariable("photoId") UUID photoId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("PhotosRestController: POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<byte[]> mayBeDownloaded = photoService.download(storyId, photoId, currentUserName, false);
        if (mayBeDownloaded.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok( mayBeDownloaded.get() );
    }

    @Override
    public ResponseEntity<byte[]> downloadThumbnail(UUID storyId, UUID photoId) {
        log.info("PhotosRestController:GET downloadThumbnail: photoId " + photoId.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("PhotosRestController: POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<byte[]> mayBeDownloaded = photoService.download(storyId, photoId, currentUserName, true);
        if (mayBeDownloaded.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        return ResponseEntity.ok( mayBeDownloaded.get() );
    }

    @Override
    public ResponseEntity<Void> deletePhoto(UUID storyId, UUID photoId) {
        log.info("PhotosRestController:DELETE deletePhoto: photoId " + photoId.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("PhotosRestController: DELETE deletePhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        photoService.deletePhoto(storyId, photoId, currentUserName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }
}

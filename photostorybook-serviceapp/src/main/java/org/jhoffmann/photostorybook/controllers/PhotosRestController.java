package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.PhotosApi;
import org.jhoffmann.photostorybook.api.v1.model.PhotoDetailsListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotoDetailsResponse;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.services.PhotoService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.validation.constraints.Pattern;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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

    public ResponseEntity<PhotoDetailsResponse> uploadPhoto(
            @PathVariable("storyId") UUID storyId,
            @RequestBody Resource body
    ) {
        log.info("POST uploadPhoto");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            InputStream inputStream = body.getInputStream();

            BufferedImage image = ImageIO.read(inputStream); // if not image IOExecption will be thrown
            log.info("POST uploadPhoto: "+ image.toString());

            inputStream.reset();

            String uuidFilename = photoService.upload(inputStream.readAllBytes(), "jpg", storyId, currentUserName);
            PhotoDetailsResponse photoDetailsResponse = new PhotoDetailsResponse();
            log.info("POST uploadPhoto with UUID " + uuidFilename);
            photoDetailsResponse.setUuid(UUID.fromString(uuidFilename));
            photoDetailsResponse.setPhotoTitle("No title");

            return new ResponseEntity<>(photoDetailsResponse, HttpStatus.CREATED);

        } catch (IOException e) {
            throw new ApiRequestException("Failed to upload image: " + e.getMessage());
        }

    }

    public ResponseEntity<PhotoDetailsListResponse> downloadPhotoDetailsList(@PathVariable("storyId")  UUID storyId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<PhotoDetailsResponse> responseList = photoService.downloadPhotosOfStory(storyId, currentUserName);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        PhotoDetailsListResponse photoListResponse = new PhotoDetailsListResponse();
        photoListResponse.setPhotos(responseList);
        return new ResponseEntity<>(photoListResponse, HttpStatus.OK);

    }

    public ResponseEntity<Resource> downloadPhoto(
            @PathVariable("storyId") UUID storyId,
            @PathVariable("photoId") UUID photoId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = "";
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            currentUserName = authentication.getName();
            log.info("POST uploadPhoto: currentUserName " + currentUserName);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<byte[]> mayBeDownloaded = photoService.download(storyId, photoId, currentUserName);
        if (mayBeDownloaded.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        Resource imageResource = new ByteArrayResource(mayBeDownloaded.get());
        if (imageResource.exists())
            return new ResponseEntity<>(imageResource, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}

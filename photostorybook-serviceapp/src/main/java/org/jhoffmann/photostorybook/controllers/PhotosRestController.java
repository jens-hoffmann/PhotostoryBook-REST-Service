package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.PhotosApi;
import org.jhoffmann.photostorybook.api.v1.model.PhotoIdResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.services.PhotoService;
import org.jhoffmann.photostorybook.services.PhotostoryService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "Photos")
public class PhotosRestController implements PhotosApi {

    private final PhotoService photoService;

    public PhotosRestController(PhotoService photoService) {
        this.photoService = photoService;

    }

    public ResponseEntity<PhotoIdResponse> uploadPhoto(
            @PathVariable("storyId") UUID storyId,
            @RequestBody Resource body
    ) {
        log.info("POST uploadPhoto");
        try {
            InputStream inputStream = body.getInputStream();

            BufferedImage image = ImageIO.read(inputStream); // if not image IOExecption will be thrown
            log.info("POST uploadPhoto: "+ image.toString());

            inputStream.reset();

            String uuidFilename = photoService.upload(inputStream.readAllBytes(), "png", storyId);
            PhotoIdResponse photoIdResponse = new PhotoIdResponse();
            log.info("POST uploadPhoto with UUID " + uuidFilename);
            photoIdResponse.setUuid(UUID.fromString(uuidFilename));
            return new ResponseEntity<>(photoIdResponse, HttpStatus.CREATED);

        } catch (IOException e) {
            throw new ApiRequestException("Failed to upload image: " + e.getMessage());
        }

    }
}

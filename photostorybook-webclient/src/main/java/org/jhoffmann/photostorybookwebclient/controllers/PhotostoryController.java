package org.jhoffmann.photostorybookwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybookwebclient.services.PhotoRestService;
import org.jhoffmann.photostorybookwebclient.services.PhotostoryRestService;
import org.openapitools.client.model.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.DAYS;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Slf4j
@Controller
@RequestMapping("/photostories")
public class PhotostoryController {

    private final PhotostoryRestService photostoryRestService;
    private final PhotoRestService photoRestService;
    private final ResourceLoader resourceLoader;

    public PhotostoryController(PhotostoryRestService photostoryRestService, PhotoRestService photoRestService, ResourceLoader resourceLoader) {
        this.photostoryRestService = photostoryRestService;
        this.photoRestService = photoRestService;
        this.resourceLoader = resourceLoader;
    }

    @ModelAttribute( name = "photostoryList")
    public List<PhotostoryResponse> createPhotostoryList() {
        PhotostoryListResponse listResponse = photostoryRestService.getPhotostories();
        return listResponse.getPhotostories();
    }

    @GetMapping
    public String getPhotostories() {
        log.info("PhotostoryController: getPhotostories");

        PhotostoryListResponse responseList = photostoryRestService.getPhotostories();
        log.info("PhotostoryController: getPhotostories: " + responseList.toString());
        List<PhotostoryResponse> photostories = responseList.getPhotostories();

        return "photostories";
    }

    @GetMapping("/create")
    public String getNewPhotostoryForm() {
        return "newphotostory";

    }

    @PostMapping(path = "/create", consumes = {"multipart/form-data"})
    public String processRegistration(@Valid @RequestParam("storyTitle") String storyTitle, @Valid @RequestParam("titleImage") Part titleImage) throws IOException {
        String imageContentType = titleImage.getContentType();
        log.info("CreatePhotostoryController: processRegistration " + storyTitle);
        // create Photostory
        AddPhotostoryRequest addPhotostoryRequest = new AddPhotostoryRequest();
        addPhotostoryRequest.setStoryTitle(storyTitle);
        PhotostoryResponse storyResponse = photostoryRestService.addPhotostory(addPhotostoryRequest);
        // process title image
        Path titleImagePath;
        if (imageContentType.equals(MediaType.IMAGE_JPEG_VALUE)) {
            titleImagePath = Files.createTempFile("titleImage_", ".jpg");
        } else if (imageContentType.equals(MediaType.IMAGE_PNG_VALUE)) {
            titleImagePath = Files.createTempFile("titleImage_", ".png");
        } else {
            return "redirect:/photostories";
        }
        Files.copy(
                titleImage.getInputStream(),
                titleImagePath,
                StandardCopyOption.REPLACE_EXISTING);
        log.info(" CreatePhotostoryController: temp file " + titleImagePath.toString());
        PhotoDetailsResponse detailsResponse = photoRestService.uploadPhoto(storyResponse.getUuid(), titleImage.getInputStream().readAllBytes());
        UUID photoUuid = detailsResponse.getUuid();
        log.info("CreatePhotostoryController: uploaded photo "+ photoUuid.toString());

        ModifyPhotostoryRequest modifyPhotostoryRequest = new ModifyPhotostoryRequest();
        modifyPhotostoryRequest.setPhotoId(photoUuid);
        PhotostoryResponse photostoryResponse = photostoryRestService.modifyPhotostory(storyResponse.getUuid(), modifyPhotostoryRequest);

        return "redirect:/photostories";
    }

    @PostMapping("/delete/{storyid}")
    public String deletePhotostory(@PathVariable String storyid) {
        log.info("PhotostoryController: POST deletePhotostory with storyid " + storyid);
        photostoryRestService.deletePhotostory(UUID.fromString(storyid));
        return "redirect:/photostories";
    }


    @GetMapping("/edit/{storyid}/addphoto")
    public ModelAndView addPhotoToStoryForm(Model model, @PathVariable String storyid) {
        log.info("PhotostoryController: GET addPhotoToStoryForm with storyid " + storyid);
        PhotostoryResponse photostoryResponse = photostoryRestService.getPhotostory(UUID.fromString(storyid));

        ModelAndView modelAndView = new ModelAndView("addphotoform");

        modelAndView.addObject("storyId", photostoryResponse.getUuid());
        modelAndView.addObject("storyTitle", photostoryResponse.getStoryTitle());

        return modelAndView;
    }

    @PostMapping(path = "/edit/{storyid}/addphoto", consumes = {"multipart/form-data"})
    public String addPhotoToStory(@PathVariable String storyid, @Valid @RequestParam("imageTitle") String imageTitle, @Valid @RequestParam("imageFile") Part imageFile) throws IOException {
        log.info("PhotostoryController: POST addPhotoToStoryForm with storyid " + storyid);
        PhotostoryResponse photostoryResponse = photostoryRestService.getPhotostory(UUID.fromString(storyid));
        String imageContentType = imageFile.getContentType();

        // process image
        Path imagePath;
        if (imageContentType.equals(MediaType.IMAGE_JPEG_VALUE)) {
            imagePath = Files.createTempFile("photo_", ".jpg");
        } else if (imageContentType.equals(MediaType.IMAGE_PNG_VALUE)) {
            imagePath = Files.createTempFile("photo_", ".png");
        } else {
            return "redirect:/photostories";
        }
        Files.copy(
                imageFile.getInputStream(),
                imagePath,
                StandardCopyOption.REPLACE_EXISTING);
        log.info(" CreatePhotostoryController: temp file " + imagePath.toString());
        PhotoDetailsResponse detailsResponse = photoRestService.uploadPhoto(photostoryResponse.getUuid(), imageFile.getInputStream().readAllBytes());
        UUID photoUuid = detailsResponse.getUuid();

        log.info("CreatePhotostoryController: uploaded photo "+ photoUuid.toString());

        return "redirect:/photostories/view/" + storyid;
    }


    @GetMapping("/view/{storyid}")
    public String viewPhotostory(Model model, @PathVariable String storyid) {
        log.info("PhotostoryController: viewPhotostory with id " + storyid);
        PhotostoryWithPhotosResponse photostoryWithPhotos = photostoryRestService.getPhotostoryWithPhotos(UUID.fromString(storyid));
        model.addAttribute("photostoryWithPhotos", photostoryWithPhotos);

        return "photostory_details";
    }

    @GetMapping("/view/{storyId}/photos/{photoId}")
    public ResponseEntity<Resource> showPhoto(@PathVariable("storyId") String storyId, @PathVariable("photoId") String photoId) throws IOException {
        log.info("PhotostoryController:showPhoto with id "+ photoId);

        Resource resource = new ByteArrayResource(photoRestService.downloadPhoto(UUID.fromString(storyId), UUID.fromString(photoId)));

        log.info("PhotostoryController:showPhoto with file " + resource.toString());
        final Resource photo;
        final String etag;

        CacheControl cacheControl = CacheControl.noCache();
        if ( resource != null) {
            cacheControl = CacheControl.maxAge(30, DAYS);
        } else {
            resource = new ClassPathResource("/no_photo.png");
        }
        etag = md5DigestAsHex(resource.getInputStream());
        return ResponseEntity
                .ok()
                .cacheControl(cacheControl)
                .eTag(etag)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }

    @GetMapping("/view/{storyId}/thumbnails/{photoId}")
    public ResponseEntity<Resource> showthumbnail(@PathVariable("storyId") String storyId, @PathVariable("photoId") String photoId) throws IOException {
        log.info("PhotostoryController:showPhoto with id "+ photoId);

        Resource resource = new ByteArrayResource(photoRestService.downloadThumbnail(UUID.fromString(storyId), UUID.fromString(photoId)));

        log.info("PhotostoryController:showPhoto with file " + resource.toString());
        final Resource photo;
        final String etag;

        CacheControl cacheControl = CacheControl.noCache();
        if ( resource != null) {
            cacheControl = CacheControl.maxAge(30, DAYS);
        } else {
            resource = new ClassPathResource("/no_photo.png");
        }
        etag = md5DigestAsHex(resource.getInputStream());
        return ResponseEntity
                .ok()
                .cacheControl(cacheControl)
                .eTag(etag)
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);

    }

    @PostMapping("/edit/{storyid}/deletephoto/{photoid}")
    public String deletePhoto(@PathVariable String storyid, @PathVariable String photoid) {
        log.info("PhotostoryController: DELETE deletePhoto with storyid " + storyid + " and photoid " + photoid);
        photoRestService.deletePhoto(UUID.fromString(storyid), UUID.fromString(photoid));
        return "redirect:/photostories/view/" + storyid;
    }
}

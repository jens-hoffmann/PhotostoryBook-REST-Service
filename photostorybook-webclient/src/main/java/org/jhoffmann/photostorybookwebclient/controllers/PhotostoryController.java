package org.jhoffmann.photostorybookwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybookwebclient.services.PhotoRestService;
import org.jhoffmann.photostorybookwebclient.services.PhotostoryRestService;
import org.jhoffmann.photostorybookwebclient.util.NewPhotostoryForm;
import org.openapitools.client.model.AddPhotostoryRequest;
import org.openapitools.client.model.PhotoDetailsResponse;
import org.openapitools.client.model.PhotostoryListResponse;
import org.openapitools.client.model.PhotostoryResponse;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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
    public String getPhotostories(Model model) {
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

        log.info("CreatePhotostoryController: processRegistration " + storyTitle);
        // create Photostory
        AddPhotostoryRequest addPhotostoryRequest = new AddPhotostoryRequest();
        addPhotostoryRequest.setStoryTitle(storyTitle);
        PhotostoryResponse storyResponse = photostoryRestService.addPhotostory(addPhotostoryRequest);
        // process title image
        Path titleImagePath;
        if (titleImage.getContentType().equals(MediaType.IMAGE_JPEG_VALUE)) {
            titleImagePath = Files.createTempFile("titleImage_", ".jpg");
        } else if (titleImage.getContentType().equals(MediaType.IMAGE_PNG_VALUE)) {
            titleImagePath = Files.createTempFile("titleImage_", ".png");
        } else {
            return "redirect:/photostories";
        }
        Files.copy(
                titleImage.getInputStream(),
                titleImagePath,
                StandardCopyOption.REPLACE_EXISTING);
        log.info(" CreatePhotostoryController: temp file " + titleImagePath.toString());
        PhotoDetailsResponse detailsResponse = photoRestService.uploadPhoto(storyResponse.getUuid(), titleImagePath.toFile());
        UUID photoUuid = detailsResponse.getUuid();
        log.info("CreatePhotostoryController: uploaded photo "+ photoUuid.toString());


        return "redirect:/photostories";
    }

    @PostMapping("/delete/{storyid}")
    public String deletePhotostory(@PathVariable String storyid) {
        log.info("PhotostoryController: deletePhotostory with id " + storyid);
        photostoryRestService.deletePhotostory(UUID.fromString(storyid));
        return "redirect:/photostories";
    }

    @GetMapping("/view/{storyid}")
    public String viewPhotostory(Model model, @PathVariable String storyid) {
        log.info("PhotostoryController: viewPhotostory with id " + storyid);

        return "photostory_details";
    }
}

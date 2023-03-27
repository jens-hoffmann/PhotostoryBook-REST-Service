package org.jhoffmann.photostorybookwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybookwebclient.services.PhotostoryRestService;
import org.jhoffmann.photostorybookwebclient.util.NewPhotostoryForm;
import org.openapitools.client.model.AddPhotostoryRequest;
import org.openapitools.client.model.PhotostoryListResponse;
import org.openapitools.client.model.PhotostoryResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/photostories")
public class PhotostoryController {

    private final PhotostoryRestService photostoryRestService;

    public PhotostoryController(PhotostoryRestService photostoryRestService) {
        this.photostoryRestService = photostoryRestService;
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

    @PostMapping("/create")
    public String processRegistration(@Valid @ModelAttribute("createStoryForm") NewPhotostoryForm form) {
        log.info("CreatePhotostoryController: processRegistration " + form.toString());
        AddPhotostoryRequest addPhotostoryRequest = new AddPhotostoryRequest();
        addPhotostoryRequest.setStoryTitle(form.getStoryTitle());
        PhotostoryResponse response = photostoryRestService.addPhotostory(addPhotostoryRequest);
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

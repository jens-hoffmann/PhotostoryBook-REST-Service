package org.jhoffmann.photostorybook.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.services.PhotostoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PhotostoryBookRestController.class)
public class PhotostoryControllerTest {

    private final static String PHOTOSTORIES_ENDPOINT = "/photostories";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PhotostoryService photostoryService;

    @MockBean
    private PhotostoryRepository photostoryRepository;


    private String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    @Test
    void createPhotostory_successfulPost() throws Exception{
        AddPhotostoryRequest request = new AddPhotostoryRequest();
        request.setStoryTitle("my test title");
        PhotostoryResponse response = new PhotostoryResponse();
        response.setStoryTitle("my test title");
        response.setUuid(UUID.randomUUID().toString());
        response.setTitlePhotoURI("my title image URL");
        given(photostoryService.addPhotostory(request)).willReturn(
                response);

        mockMvc.perform(
                        post(PHOTOSTORIES_ENDPOINT)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("storyTitle").value("my test title"))
                .andExpect(jsonPath("titlePhotoURI").value("my title image URL"))
                .andExpect(jsonPath("uuid").isNotEmpty());

    }

    @Test
    void getPhotostories_nonemptyResponse() throws Exception{
        mockMvc.perform(
                        get(PHOTOSTORIES_ENDPOINT))
                .andDo(print())
                .andExpect( status().isOk() );
                // TODO Check response body

    }


}

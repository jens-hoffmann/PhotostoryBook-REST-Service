package org.jhoffmann.photostorybook.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.security.SecurityConfiguration;
import org.jhoffmann.photostorybook.services.PhotostoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import({SecurityConfiguration.class})
//@ContextConfiguration(classes = SecurityConfiguration.class)
@WebAppConfiguration
@WebMvcTest(controllers = PhotostoryBookRestController.class)
public class PhotostoryControllerTest {

    private final static String PHOTOSTORIES_ENDPOINT = "/photostories";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private PhotostoryService photostoryService;

    @MockBean
    private PhotostoryRepository photostoryRepository;


    private String toJson(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }

/*    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus( 10, ChronoUnit.MINUTES ))
                .subject( "testuser" )
                .build();
  //      webtoken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Test
    @WithMockUser(roles="USER")
    void createPhotostory_successfulPost() throws Exception{
        AddPhotostoryRequest request = new AddPhotostoryRequest();
        request.setStoryTitle("my test title");
        PhotostoryResponse response = new PhotostoryResponse();
        response.setStoryTitle("my test title");
        response.setUuid(UUID.randomUUID());
        response.setTitlePhotoURI("my title image URL");
        given(photostoryService.addPhotostory(request)).willReturn(
                response);

        mockMvc.perform(
                        post(PHOTOSTORIES_ENDPOINT)
                                .accept(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, webtoken)
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
    @WithMockUser(roles="USER")
    void getPhotostories_nonemptyResponse() throws Exception{
        PhotostoryResponse response = new PhotostoryResponse();
        response.setStoryTitle("my test title");
        response.setUuid(UUID.randomUUID());
        response.setTitlePhotoURI("my title image URL");

        PhotostoryListResponse photostoryListResponse = new PhotostoryListResponse();
        photostoryListResponse.setPhotostories(List.of(response));

        given(photostoryService.getPhotostories()).willReturn(photostoryListResponse);

        mockMvc.perform(
                        get(PHOTOSTORIES_ENDPOINT)
                                .header(HttpHeaders.AUTHORIZATION, webtoken))
                .andDo(print())
                .andExpect( status().isOk() )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("photostories").isArray())
                .andExpect(jsonPath("photostories[0].storyTitle").value("my test title"))
                .andExpect(jsonPath("photostories[0].titlePhotoURI").value("my title image URL"));

    }
*/
}

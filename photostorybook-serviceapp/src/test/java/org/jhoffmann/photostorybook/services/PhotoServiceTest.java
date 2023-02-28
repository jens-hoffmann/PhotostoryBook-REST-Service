package org.jhoffmann.photostorybook.services;

import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.repositories.PSImageRepository;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.util.FileSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PhotoServiceTest {

    private static final byte[] MINIMAL_JPG = Base64.getDecoder().decode(
            "/9j/4AAQSkZJRgABAQEASABIAAD"
                    + "/2wBDAP////////////////////////////////////////////////////////////"
                    + "//////////////////////////wgALCAABAAEBAREA/8QAFBABAAAAAAAAAAAAAAAAA"
                    + "AAAAP/aAAgBAQABPxA=" );                      // https://git.io/J9GXr

    @MockBean
    FileSystem fs;

    @Autowired
    PhotostoryRepository photostoryRepository;

    @Autowired
    PhotoService photoService;

    @BeforeEach
    void setUp() {
        when( fs.load( any() ) ).thenReturn( MINIMAL_JPG );
    }

    @Test
    void successful_photo_upload() {
        PhotostoryEntity savedPhotostory = photostoryRepository.save(new PhotostoryEntity("my test story"));
        String imageName = photoService.upload( MINIMAL_JPG , ".png", UUID.fromString( savedPhotostory.getBusinesskey()) );
        assertThat( imageName ).isNotEmpty();
    }
}
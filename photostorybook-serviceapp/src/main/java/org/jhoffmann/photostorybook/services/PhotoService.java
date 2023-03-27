package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.model.PhotoDetailsResponse;
import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PSImageRepository;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.util.FileSystem;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PhotoService {

    private final FileSystem fs;
    private final PSImageRepository imageRepository;
    private final PhotostoryRepository photostoryRepository;


    public PhotoService(FileSystem fs, PSImageRepository imageRepository, PhotostoryRepository photostoryRepository) {
        this.fs = fs;
        this.imageRepository = imageRepository;
        this.photostoryRepository = photostoryRepository;
    }

    public String upload(byte[] imageBytes , String imageFormat, UUID photostoryUUID,String userId) {

        String imageName = UUID.randomUUID().toString();
        log.info("PhotoService: upload image " + imageName + " for user " + userId);
        PSImageEntity psImageEntity = new PSImageEntity(imageName +".jpg", imageName, userId);
        Optional<PhotostoryEntity> photostory = photostoryRepository.findByBusinesskeyAndUserId(photostoryUUID.toString(), userId).stream().findFirst();
        if (photostory.isEmpty())
            throw new ApiRequestException("PhotoService: upload: No photostory with id "+ photostoryUUID.toString());
        psImageEntity.setPhotostory(photostory.get());

        PSImageEntity savedEntity = imageRepository.save(psImageEntity);

        // TODO set correct file extension
        fs.store( imageName + ".jpg", imageBytes );

        return imageName;
    }

    public Optional<byte[]> download( UUID storyId, UUID photoId, String userId) {
        log.info("PhotoService: download image " + photoId.toString() + " for user " + userId);
        Optional<PSImageEntity> mayBeImageEntity = imageRepository.findByBusinesskeyAndUserId(photoId.toString(), userId).stream().findFirst();
        if (mayBeImageEntity.isEmpty())
            throw new ApiRequestException("PhotoService: download: No photo with id "+ photoId.toString() + " found !");
        PSImageEntity psImageEntity = mayBeImageEntity.get();
        PhotostoryEntity photostory = psImageEntity.getPhotostory();
        if (! storyId.equals(UUID.fromString(photostory.getBusinesskey())))
            throw new ApiRequestException("PhotoService: download: Photo not in photostory id "+ storyId.toString());
        try {
            return Optional.of( fs.load( psImageEntity.getImageUrl() ) );
        }
        catch ( UncheckedIOException e ) {
            return Optional.empty();
        }
    }

    public List<PhotoDetailsResponse> downloadPhotosOfStory(UUID storyId, String userId) {
        List<PhotoDetailsResponse> responseList = new ArrayList<>();
        Optional<PhotostoryEntity> photostory = photostoryRepository.findByBusinesskeyAndUserId(storyId.toString(), userId).stream().findFirst();
        if (photostory.isEmpty())
            throw new ApiRequestException("PhotoService: downloadPhotosOfStory: No photostory with id "+ storyId.toString());
        PhotostoryEntity photostoryEntity = photostory.get();
        List<PSImageEntity> photoEntities = photostoryEntity.getPhotos();
        for (PSImageEntity photoEntity : photoEntities) {
            PhotoDetailsResponse pResponse = new PhotoDetailsResponse();
            pResponse.setUuid(UUID.fromString(photoEntity.getBusinesskey()));
            pResponse.setPhotoTitle(photoEntity.getTitle());
            responseList.add(pResponse);

        }

        return  responseList;
    }
}

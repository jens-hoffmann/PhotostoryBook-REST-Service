package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.ModifyPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PSImageRepository;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class PhotostoryService {

    private final PhotostoryRepository photostoryRepository;
    private final PSImageRepository psImageRepository;

    public PhotostoryService(PhotostoryRepository photostoryRepository, PSImageRepository psImageRepository) {
        this.photostoryRepository = photostoryRepository;
        this.psImageRepository = psImageRepository;
    }

    private PhotostoryResponse entityToResponse(PhotostoryEntity entity) {
        PhotostoryResponse response = new PhotostoryResponse();
        if (entity.getBusinesskey().isEmpty())
            throw new ApiRequestException("PhotostoryService: PhotostoryEntity has empty businesskey !");
        response.setUuid(UUID.fromString(entity.getBusinesskey()));
        if (entity.getStoryTitle().isEmpty())
            throw new ApiRequestException("PhotostoryService: PhotostoryEntity has empty story title !");
        response.setStoryTitle(entity.getStoryTitle());
        PSImageEntity titleImage = entity.getTitleImage();
        if (titleImage != null)
            response.setTitlePhotoId(UUID.fromString(titleImage.getBusinesskey()));
        return response;
    }

    public PhotostoryListResponse getPhotostories(String userId) {
        List<PhotostoryEntity> entityList = photostoryRepository.findByUserId(userId);
        log.debug("PhotostoryService: getPhotostories " + entityList.size() + " entities from repository");
        PhotostoryListResponse photostoryListResponse = new PhotostoryListResponse();
        photostoryListResponse.setPhotostories(entityList.stream()
                .map(entity -> entityToResponse(entity))
                        .toList());

        return  photostoryListResponse;
    }

    public void deletePhotostory(String userId, String storyId) {
        List<PhotostoryEntity> entityList = photostoryRepository.findByBusinesskeyAndUserId(storyId, userId);
        log.debug("PhotostoryService: deletePhotostories " + entityList.size() + " entities from repository");
        for (PhotostoryEntity entity : entityList) {
            photostoryRepository.delete(entity);
        }
    }

    public PhotostoryResponse addPhotostory(AddPhotostoryRequest addPhotostoryRequest, String userId) {
        PhotostoryEntity entity = new PhotostoryEntity(addPhotostoryRequest.getStoryTitle(), userId);
        log.debug("PhotostoryService: addPhotostory " + entity.getStoryTitle() );
        PhotostoryEntity savedEntity = photostoryRepository.save(entity);
        return entityToResponse(savedEntity);
    }

    public PhotostoryResponse findPhotostoryByBusinesskeyAndUserId(String businesskey, String userId) {
        Optional<PhotostoryEntity> first = photostoryRepository.findByBusinesskeyAndUserId(businesskey, userId).stream().findFirst();
        if (first.isEmpty()) {
            throw new ApiRequestException("PhotostoryService: no PhotostoryEntity with businesskey "+ businesskey + "!");
        }
        return entityToResponse(first.get());
    }

    public PhotostoryResponse modifyPhotostory(ModifyPhotostoryRequest modifyPhotostoryRequest, String currentUserName, UUID storyId) {
        Optional<PhotostoryEntity> first = photostoryRepository.findByBusinesskeyAndUserId(storyId.toString(), currentUserName).stream().findFirst();
        if (first.isEmpty()) {
            throw new ApiRequestException("PhotostoryService: no PhotostoryEntity with businesskey "+ storyId.toString() + "!");
        }
        PhotostoryEntity entity = first.get();

        if (modifyPhotostoryRequest.getStoryTitle() != null) {
            entity.setStoryTitle(modifyPhotostoryRequest.getStoryTitle());
        }


        UUID photoId = modifyPhotostoryRequest.getPhotoId();
        if (photoId != null) {
            Optional<PSImageEntity> maybePSImage = psImageRepository.findByBusinesskeyAndUserId(photoId.toString(), currentUserName).stream().findFirst();
            if (maybePSImage.isEmpty()) {
                throw new ApiRequestException("PhotostoryService: no PSImage with businesskey "+ photoId.toString() + "!");
            }
            entity.setTitleImage(maybePSImage.get());

        }

        PhotostoryEntity savedEntity = photostoryRepository.save(entity);
        return entityToResponse(savedEntity);
    }
}

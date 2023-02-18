package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.model.AddPhotostoryRequest;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryListResponse;
import org.jhoffmann.photostorybook.api.v1.model.PhotostoryResponse;
import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PhotostoryService {

    private final PhotostoryRepository photostoryRepository;

    public PhotostoryService(PhotostoryRepository photostoryRepository) {
        this.photostoryRepository = photostoryRepository;
    }

    private PhotostoryResponse entityToResponse(PhotostoryEntity entity) {
        PhotostoryResponse response = new PhotostoryResponse();
        response.setUuid(entity.getBusinesskey());
        response.setStoryTitle(entity.getStoryTitle());
        PSImageEntity titleImage = entity.getTitleImage();
        if (titleImage != null)
            response.setTitlePhotoURI(titleImage.getImageUrl().toString());
        return response;
    }

    public PhotostoryListResponse getPhotostories() {
        List<PhotostoryEntity> entityList = photostoryRepository.findAll();
        log.debug("PhotostoryService: getPhotostories " + entityList.size() + " entities from repository");
        PhotostoryListResponse photostoryListResponse = new PhotostoryListResponse();
        photostoryListResponse.setPhotostories(entityList.stream()
                .map(entity -> entityToResponse(entity))
                        .toList());

        return  photostoryListResponse;
    }

    public PhotostoryResponse addPhotostory(AddPhotostoryRequest addPhotostoryRequest) {
        PhotostoryEntity entity = new PhotostoryEntity(addPhotostoryRequest.getStoryTitle());
        log.debug("PhotostoryService: addPhotostory " + entity.getStoryTitle() );
        PhotostoryEntity savedEntity = photostoryRepository.save(entity);
        return entityToResponse(savedEntity);
    }

}

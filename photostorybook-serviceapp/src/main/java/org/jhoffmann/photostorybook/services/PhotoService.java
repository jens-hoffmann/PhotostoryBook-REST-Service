package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PSImageRepository;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.jhoffmann.photostorybook.util.FileSystem;
import org.springframework.stereotype.Service;

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

    public String upload(byte[] imageBytes , String imageFormat, UUID photostoryUUID) {
        String imageName = UUID.randomUUID().toString();
        PSImageEntity psImageEntity = new PSImageEntity(imageName +".png", imageName);
        Optional<PhotostoryEntity> photostory = photostoryRepository.findByBusinesskey(photostoryUUID.toString()).stream().findFirst();
        if (photostory.isEmpty())
            throw new ApiRequestException("PhotoService: upload: No photostory with id "+ photostoryUUID.toString());
        psImageEntity.setPhotostory(photostory.get());
        PSImageEntity savedEntity = imageRepository.save(psImageEntity);

        fs.store( imageName + ".png", imageBytes );

        return imageName;
    }
}

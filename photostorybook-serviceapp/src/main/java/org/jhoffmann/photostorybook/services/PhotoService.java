package org.jhoffmann.photostorybook.services;

import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.repositories.PSImageRepository;
import org.jhoffmann.photostorybook.util.FileSystem;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
public class PhotoService {

    private final FileSystem fs;
    private final PSImageRepository imageRepository;

    public PhotoService(FileSystem fs, PSImageRepository imageRepository) {
        this.fs = fs;
        this.imageRepository = imageRepository;
    }

    public String upload(byte[] imageBytes , String imageFormat) {
        //Future<byte[]> thumbnailBytes = thumbnail.thumbnail( imageBytes );

        String imageName = UUID.randomUUID().toString();

        // store full image
        fs.store( imageName + ".png", imageBytes );
        PSImageEntity psImageEntity = new PSImageEntity(imageName);
        PSImageEntity save = imageRepository.save(psImageEntity);

      /*  // Second: store thumbnail
        try {
            log.info( "upload" );
            fs.store( imageName + "-thumb.png", thumbnailBytes.get() );
        }
        catch (InterruptedException | ExecutionException e ) {
            throw new IllegalStateException( e );
        }*/

        return imageName;
    }
}

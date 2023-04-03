package org.jhoffmann.photostorybook.services;

import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

public interface Thumbnail {

    @Async
    Future<byte[]> thumbnail(byte[] imageBytes );
}

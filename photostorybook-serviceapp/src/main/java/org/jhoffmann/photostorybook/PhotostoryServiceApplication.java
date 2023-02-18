package org.jhoffmann.photostorybook;

import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class PhotostoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotostoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(PhotostoryRepository photostoryRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				List<PSImageEntity> photoList = List.of(
						new PSImageEntity("image1", "imageUrl1"),
						new PSImageEntity("image2", "imageUrl2"));

				PhotostoryEntity photostory = new PhotostoryEntity("my first photostory");
				photostory.setPhotos(photoList);
				photostoryRepository.save(photostory);
				photostoryRepository.flush();
			}
		};
	}
}

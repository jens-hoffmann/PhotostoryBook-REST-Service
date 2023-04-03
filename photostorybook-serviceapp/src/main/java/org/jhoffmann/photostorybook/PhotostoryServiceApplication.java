package org.jhoffmann.photostorybook;

import org.jhoffmann.photostorybook.domain.PSImageEntity;
import org.jhoffmann.photostorybook.domain.PSUser;
import org.jhoffmann.photostorybook.domain.PhotostoryEntity;
import org.jhoffmann.photostorybook.repositories.PhotostoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
@EnableCaching
@EnableAsync
public class PhotostoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotostoryServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner dataLoader(PhotostoryRepository photostoryRepository) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				PSUser user = new PSUser("testuser", "password", "Test user");
				List<PSImageEntity> photoList = List.of(
						new PSImageEntity("image1",  "image1.png", UUID.randomUUID().toString()),
						new PSImageEntity("image2","image1.png", UUID.randomUUID().toString()));

				PhotostoryEntity photostory = new PhotostoryEntity("my first photostory", user.getBusinesskey());
				photostory.setPhotos(photoList);
				photostoryRepository.save(photostory);
				photostoryRepository.flush();
			}
		};
	}
}

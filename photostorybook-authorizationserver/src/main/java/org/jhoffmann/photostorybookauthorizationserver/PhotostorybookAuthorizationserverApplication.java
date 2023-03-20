package org.jhoffmann.photostorybookauthorizationserver;

import org.jhoffmann.photostorybookauthorizationserver.users.PSUser;
import org.jhoffmann.photostorybookauthorizationserver.users.PSUserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PhotostorybookAuthorizationserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotostorybookAuthorizationserverApplication.class, args);
	}

	@Bean
	public ApplicationRunner dataLoader(
			PSUserRepository repo, PasswordEncoder encoder) {
		return args -> {
			repo.save(
					new PSUser("admin", encoder.encode("password"), "Test admin", "ROLE_ADMIN"));
			repo.save(
					new PSUser("user", encoder.encode("password"), "Testuser", "ROLE_USER"));
		};
	}
}

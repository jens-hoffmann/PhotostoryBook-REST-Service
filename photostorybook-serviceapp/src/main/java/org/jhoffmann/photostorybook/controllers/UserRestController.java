package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.UserApi;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserRequest;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserResponse;
import org.jhoffmann.photostorybook.domain.PSUser;
import org.jhoffmann.photostorybook.repositories.PSUserRepository;
import org.jhoffmann.photostorybook.services.UserRepositoryUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(tags = "User")
public class UserRestController implements UserApi {

    private final PSUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryUserDetailsService userRepositoryUserDetailsService;

    public UserRestController(PSUserRepository userRepository, PasswordEncoder passwordEncoder, UserRepositoryUserDetailsService userRepositoryUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryUserDetailsService = userRepositoryUserDetailsService;
    }

    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody(required = false) CreateUserRequest createUserRequest
    ) {
        PSUser psUser = new PSUser(
                createUserRequest.getUsername(),
                passwordEncoder.encode(createUserRequest.getPassword()),
                createUserRequest.getFullName());
        log.info("UserRestController: createUser " + psUser.toString());
        PSUser savedUser = userRepository.save(psUser);

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setUsername(savedUser.getUsername());
        createUserResponse.setUuid(savedUser.getBusinesskey());

        return new ResponseEntity<>(createUserResponse, HttpStatus.CREATED);
    }

}

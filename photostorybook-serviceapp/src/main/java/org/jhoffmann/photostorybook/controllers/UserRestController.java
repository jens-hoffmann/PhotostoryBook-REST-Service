package org.jhoffmann.photostorybook.controllers;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.jhoffmann.photostorybook.api.v1.UserApi;
import org.jhoffmann.photostorybook.api.v1.model.CreateTokenRequest;
import org.jhoffmann.photostorybook.api.v1.model.CreateTokenResponse;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserRequest;
import org.jhoffmann.photostorybook.api.v1.model.CreateUserResponse;
import org.jhoffmann.photostorybook.domain.PSUser;
import org.jhoffmann.photostorybook.exceptions.ApiRequestException;
import org.jhoffmann.photostorybook.repositories.PSUserRepository;
import org.jhoffmann.photostorybook.services.UserRepositoryUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@RestController
@Api(tags = "User")
public class UserRestController implements UserApi {

    private final PSUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepositoryUserDetailsService userRepositoryUserDetailsService;
    private final JwtEncoder jwtEncoder;

    public UserRestController(PSUserRepository userRepository, PasswordEncoder passwordEncoder, UserRepositoryUserDetailsService userRepositoryUserDetailsService, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepositoryUserDetailsService = userRepositoryUserDetailsService;
        this.jwtEncoder = jwtEncoder;
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

    public ResponseEntity<CreateTokenResponse> createToken(
            @Valid @RequestBody(required = false) CreateTokenRequest createTokenRequest
    ) {
        String username = createTokenRequest.getUsername();
        log.info("UserRestController: createToken for " + username);
        UserDetails userDetails = userRepositoryUserDetailsService.loadUserByUsername(username);
        if (! passwordEncoder.matches( createTokenRequest.getPassword(), userDetails.getPassword()) )
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(now.plus( 1, ChronoUnit.HOURS ))
                .subject( username )
                .build();
        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        CreateTokenResponse createTokenResponse = new CreateTokenResponse();
        createTokenResponse.setToken(tokenValue);

        return new ResponseEntity<>(createTokenResponse, HttpStatus.CREATED);
    }
}

package org.jhoffmann.photostorybook.services;

import io.swagger.v3.oas.annotations.Parameter;
import org.jhoffmann.photostorybook.api.v1.model.CreateTokenRequest;
import org.jhoffmann.photostorybook.api.v1.model.CreateTokenResponse;
import org.jhoffmann.photostorybook.domain.PSUser;
import org.jhoffmann.photostorybook.repositories.PSUserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private final PSUserRepository userRepository;

    public UserRepositoryUserDetailsService(PSUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PSUser psUser = userRepository.findByUsername(username);
        if (psUser != null) {
            return psUser;
        } else {
            throw new UsernameNotFoundException( "User '" + username + "' not found");
        }
    }


}

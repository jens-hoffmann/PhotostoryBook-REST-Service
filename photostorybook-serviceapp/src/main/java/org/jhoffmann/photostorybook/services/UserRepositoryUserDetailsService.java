package org.jhoffmann.photostorybook.services;

import org.jhoffmann.photostorybook.domain.PSUser;
import org.jhoffmann.photostorybook.repositories.PSUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

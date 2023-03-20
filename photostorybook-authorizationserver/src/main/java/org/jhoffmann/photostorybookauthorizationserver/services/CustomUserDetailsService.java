package org.jhoffmann.photostorybookauthorizationserver.services;


import org.jhoffmann.photostorybookauthorizationserver.users.PSUser;
import org.jhoffmann.photostorybookauthorizationserver.users.PSUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final PSUserRepository userRepository;

    public CustomUserDetailsService(PSUserRepository userRepository) {
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

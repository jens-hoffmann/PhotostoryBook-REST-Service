package org.jhoffmann.photostorybookwebclient.security;

import lombok.Data;
import org.jhoffmann.photostorybookwebclient.domain.PSUser;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;

@Data
public class RegistrationForm {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String fullname;

    public PSUser toUser(PasswordEncoder passwordEncoder) {
        return new PSUser(
                username, passwordEncoder.encode(password),
                fullname);
    }

}

package org.jhoffmann.photostorybookwebclient.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Getter
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class PSUser {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private final String businesskey;

    @NotNull
    private final String username;

    @NotNull
    private final String password;

    @NotNull
    private final String fullname;


    public PSUser(String username, String password, String fullname) {
        this.businesskey = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.fullname = fullname;
    }

}

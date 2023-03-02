package org.jhoffmann.photostorybook.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(force=true, access=AccessLevel.PRIVATE)
public class PSUser implements UserDetails  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

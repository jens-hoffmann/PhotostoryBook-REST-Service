package org.jhoffmann.photostorybook.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;


@Configuration
//@EnableWebSecurity(debug=true)
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests( authorize -> authorize
                    .antMatchers("/users", "/tokens", "/h2-console/**",
                                    "/swagger-ui.html", "/swagger-ui/**", " /api-docs/**", "/v3/api-docs/**", "/webjars/**" ).permitAll()
                    .anyRequest().hasRole("USER")
                )
                    .csrf().disable()
                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RSAKey rsaKey() throws NoSuchAlgorithmException {
        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize( 2048 );
        var keyPair = generator.generateKeyPair();
        return new RSAKey.Builder( (RSAPublicKey) keyPair.getPublic() )
                .privateKey( keyPair.getPrivate() )
                .keyID( UUID.randomUUID().toString() )
                .build();
    }

    @Bean
    JwtEncoder jwtEncoder( RSAKey rsaKey ) {
        return new NimbusJwtEncoder(
                new ImmutableJWKSet<>( new JWKSet( rsaKey ))
        );
    }

    @Bean
    JwtDecoder jwtDecoder( RSAKey rsaKey ) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey( rsaKey.toRSAPublicKey() )
                .build();
    }
}
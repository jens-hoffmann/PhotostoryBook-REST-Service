package org.jhoffmann.photostorybook.security;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity(debug=true)
public class SecurityConfiguration {


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .antMatchers(HttpMethod.OPTIONS).permitAll() // needed for Angular/CORS
                        .antMatchers(HttpMethod.POST, "/photostories",  "/photostories/**")
                             .hasAuthority("SCOPE_writePhotostory")
                        .antMatchers(HttpMethod.GET, "/photostories",  "/photostories/**")
                             .hasAuthority("SCOPE_readPhotostory")
                        .antMatchers(HttpMethod.DELETE, "/photostories",  "/photostories/**")
                                .hasAuthority("SCOPE_writePhotostory")
                    .antMatchers("/**" ).permitAll()


                .and()
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                    .httpBasic()
                    .realmName("PhotostoryBook Cloud")
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .csrf()
                    .ignoringAntMatchers("/h2-console/**", "/api/**")
                // Allow pages to be loaded in frames from the same origin; needed for H2-Console
                .and()
                    .headers()
                    .frameOptions()
                    .sameOrigin();

        return http.build();

    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }*/
/*
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

 */


}
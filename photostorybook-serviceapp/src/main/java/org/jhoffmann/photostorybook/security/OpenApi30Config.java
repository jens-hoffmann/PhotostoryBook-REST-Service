package org.jhoffmann.photostorybook.security;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "My API", version = "v1"))
@SecurityScheme(
        name = "security-oauth",
        type = SecuritySchemeType.OAUTH2,
        flows =  @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:9000/realms/PhotostoryBookRealm/protocol/openid-connect/auth",
                        tokenUrl = "http://localhost:9000/realms/PhotostoryBookRealm/protocol/openid-connect/token",
                        scopes = {
                                @OAuthScope(name = "openid"),
                                @OAuthScope(name = "photostory.read"),
                                @OAuthScope(name = "photostory.write"),
                                @OAuthScope(name = "photostory.delete")

                        }
                )
        )
)
public class OpenApi30Config {
}

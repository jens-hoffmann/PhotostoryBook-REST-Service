# PhotostoryBook backend service


### Keycloak user authentication service

see docker-compose.yml

### API Specification for REST services

see module photostorybook-api

contains design first OpenAPI specification for REST API

### spring web mvc backed

see module photostorybook-serviceapp

contains RestControllers to handle requests defined by photostorybook-api

### Run REST API service

1. generate OpenApi interfaces from description
 
in directory photostorybook-api: <code>mvn clean install</code>

2. start KeyCloak service

<code>docker compose up -d</code>

check if service is up and running:

<code>docker compose logs -f</code>

3. start REST-API service

in photostorybook-serviceapp: <code>mvn spring-boot:run</code>

point your browser to:
http://localhost:8081/swagger-ui/index.html

4. start web client

in photostorybook-webclient: <code>mvn spring-boot:run</code>

point your browser to:
http://localhost:8080/

select "Login" and the "Register" to create and login a new user
# PhotostoryBook backend service


### API Specification for REST services

see module photostorybook-api

contains design first OpenAPI specification for REST API

### spring web mvc backed

see module photostorybook-serviceapp

contains RestControllers to handle requests defined by photostorybook-api

### Run REST API service

in directory photostorybook-api: <code>mvn clean install</code>

in photostorybook-serviceapp: <code>mvn spring-boot:run</code>

point your browser to:
http://localhost:8080/swagger-ui/index.html
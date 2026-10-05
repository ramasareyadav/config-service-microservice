# User & Address Microservices (Spring Boot 3, Spring Cloud)

| Module           | Port | Role                                                                   |
|------------------|------|------------------------------------------------------------------------|
| config-server    | 8888 | Config Server (reads the GitHub config repo) **and** Eureka Server      |
| address-service  | 8081 | Address CRUD, DB `address_db` (MySQL), Config Client + Eureka Client    |
| user-service     | 8082 | User CRUD, DB `user_db`, Config Client + Eureka Client, calls address-service via OpenFeign |

Only three projects. The config files live in your GitHub repo
(https://github.com/ramasareyadav/config-service-microservice), not in this project.
The local `application.yml` of address-service and user-service only has the app name and
`spring.config.import`; ports, datasource, JPA and Eureka settings come from the config repo.

## Files to put in the GitHub config repo (root of branch `main`)
File name = `spring.application.name`. `application.yml` is shared by every client.
Delete the empty `configDemo.yml`, it is not used.

**application.yml**
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8888/eureka/
  instance:
    prefer-ip-address: true
```

**address-service.yml**
```yaml
server:
  port: 8081

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/address_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
```

**user-service.yml**
```yaml
server:
  port: 8082

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/user_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
        format_sql: true
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true   # needed for the Feign fallbackFactory
```

> The repo is public - do not commit real passwords. Use encrypted values (`{cipher}...`) or a private repo.

## Prerequisites
- JDK 17+, Maven 3.8+
- MySQL on `localhost:3306` (change username/password in the config repo files; databases are auto-created)

## Run (in this order)
```bash
mvn clean install
cd config-server   && mvn spring-boot:run     # config server + Eureka dashboard: http://localhost:8888
cd address-service && mvn spring-boot:run
cd user-service    && mvn spring-boot:run
```
Check what the config server serves: `curl localhost:8888/address-service/default`
If config-server is not running (or a config file is missing in the repo) the services fail fast on startup.

## Try it
```bash
# create a user
curl -X POST localhost:8082/users -H "Content-Type: application/json" \
  -d '{"name":"Rahul","email":"rahul@example.com","phone":"9999999999"}'

# add an address (user-service -> Feign -> address-service)
curl -X POST localhost:8082/users/1/addresses -H "Content-Type: application/json" \
  -d '{"street":"Main Road","city":"Varanasi","state":"UP","zipCode":"221001","country":"India"}'

# get user together with addresses
curl localhost:8082/users/1
```

## Search
```bash
# users by their own fields (all params optional)
curl "localhost:8082/users/search?keyword=rahul"
curl "localhost:8082/users/search?name=rah&email=rahul@example.com"

# users by address (user-service -> Feign -> address-service); at least one param required
curl "localhost:8082/users/search/by-address?city=Varanasi&country=India"

# addresses directly
curl "localhost:8081/addresses/search?city=Varanasi"
curl "localhost:8081/addresses/search?keyword=221001&userId=1"
```

## Error format (both services)
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id : 99",
  "path": "/users/99"
}
```
Custom exceptions: `ResourceNotFoundException` (404), `DuplicateResourceException` (409),
`InvalidRequestException` (400, user-service), `ServiceUnavailableException` (503, user-service).

Request/response classes: `UserRequest`, `UserResponse`, `UserWithAddressResponse`, `AddressRequest`, `AddressResponse`.
Sample JSON for every endpoint: see [API-EXAMPLES.md](API-EXAMPLES.md).

## Endpoints
**user-service**: `POST /users`, `GET /users`, `GET /users/{id}` (with addresses), `PUT /users/{id}`, `DELETE /users/{id}`, `GET /users/search`, `GET /users/search/by-address`, `POST /users/{id}/addresses`

**address-service**: `POST /addresses`, `GET /addresses`, `GET /addresses/search`, `GET /addresses/{id}`, `GET /addresses/user/{userId}`, `PUT /addresses/{id}`, `DELETE /addresses/{id}`, `DELETE /addresses/user/{userId}`

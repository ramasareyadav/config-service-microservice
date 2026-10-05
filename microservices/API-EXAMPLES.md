# Sample requests and responses

All examples go through **user-service** (`:8082`) unless noted. address-service runs on `:8081`.
Header for every request with a body: `Content-Type: application/json`

## Users

### Create user — `POST /users`
Request (`UserRequest`)
```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9999999999"
}
```
Response `201 Created` (`UserResponse`)
```json
{
  "id": 1,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9999999999"
}
```

### Update user — `PUT /users/1`
Request (`UserRequest`)
```json
{
  "name": "Rahul Kumar Sharma",
  "email": "rahul@example.com",
  "phone": "8888888888"
}
```
Response `200 OK` (`UserResponse`)
```json
{
  "id": 1,
  "name": "Rahul Kumar Sharma",
  "email": "rahul@example.com",
  "phone": "8888888888"
}
```

### Get all users — `GET /users`
Response `200 OK`
```json
[
  { "id": 1, "name": "Rahul Kumar Sharma", "email": "rahul@example.com", "phone": "8888888888" }
]
```

### Get user with addresses — `GET /users/1`
(user from user-service DB, addresses fetched from address-service via Feign)

Response `200 OK` (`UserWithAddressResponse`)
```json
{
  "id": 1,
  "name": "Rahul Kumar Sharma",
  "email": "rahul@example.com",
  "phone": "8888888888",
  "addresses": [
    {
      "id": 1,
      "street": "Main Road",
      "city": "Varanasi",
      "state": "Uttar Pradesh",
      "zipCode": "221001",
      "country": "India",
      "userId": 1
    }
  ]
}
```

### Search users — `GET /users/search?keyword=rahul`
Other filters: `name`, `email` (exact), `phone` (exact). All optional.

Response `200 OK` (`List<UserResponse>`)
```json
[
  { "id": 1, "name": "Rahul Kumar Sharma", "email": "rahul@example.com", "phone": "8888888888" }
]
```

### Search users by address — `GET /users/search/by-address?city=Varanasi&country=India`
Filters: `keyword`, `city`, `state`, `country`, `zipCode` (at least one required).

Response `200 OK` (`List<UserWithAddressResponse>`, only matching addresses are included)
```json
[
  {
    "id": 1,
    "name": "Rahul Kumar Sharma",
    "email": "rahul@example.com",
    "phone": "8888888888",
    "addresses": [
      { "id": 1, "street": "Main Road", "city": "Varanasi", "state": "Uttar Pradesh",
        "zipCode": "221001", "country": "India", "userId": 1 }
    ]
  }
]
```

### Delete user — `DELETE /users/1`
Response `204 No Content` (the user's addresses are removed from address-service too)

## Addresses

### Add address to a user — `POST /users/1/addresses`
Request (`AddressRequest`, no `userId` needed)
```json
{
  "street": "Main Road",
  "city": "Varanasi",
  "state": "Uttar Pradesh",
  "zipCode": "221001",
  "country": "India"
}
```
Response `201 Created` (`AddressResponse`)
```json
{
  "id": 1,
  "street": "Main Road",
  "city": "Varanasi",
  "state": "Uttar Pradesh",
  "zipCode": "221001",
  "country": "India",
  "userId": 1
}
```

### Create address directly — `POST http://localhost:8081/addresses`
Request (`AddressRequest`, `userId` is required here)
```json
{
  "street": "Station Road",
  "city": "Lucknow",
  "state": "Uttar Pradesh",
  "zipCode": "226001",
  "country": "India",
  "userId": 1
}
```
Response `201 Created`
```json
{
  "id": 2,
  "street": "Station Road",
  "city": "Lucknow",
  "state": "Uttar Pradesh",
  "zipCode": "226001",
  "country": "India",
  "userId": 1
}
```

### Update address — `PUT http://localhost:8081/addresses/2`
Request / response have the same shape as create (response is `200 OK`).

### Search addresses — `GET http://localhost:8081/addresses/search?city=Lucknow&userId=1`
Filters: `keyword`, `city`, `state`, `country`, `zipCode`, `userId` (all optional).

Response `200 OK` (`List<AddressResponse>`)
```json
[
  { "id": 2, "street": "Station Road", "city": "Lucknow", "state": "Uttar Pradesh",
    "zipCode": "226001", "country": "India", "userId": 1 }
]
```

### Other address endpoints (address-service)
| Method | URL | Success |
|---|---|---|
| GET | `/addresses` | 200, list |
| GET | `/addresses/{id}` | 200, `AddressResponse` |
| GET | `/addresses/user/{userId}` | 200, list |
| DELETE | `/addresses/{id}` | 204 |
| DELETE | `/addresses/user/{userId}` | 204 |

## Error responses (both services)

### 400 — validation failed (e.g. `POST /users` with a bad body)
Request
```json
{ "name": "", "email": "not-an-email" }
```
Response
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/users",
  "validationErrors": {
    "name": "Name is required",
    "email": "Email should be valid"
  }
}
```

### 400 — no search criteria (`GET /users/search/by-address`)
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "At least one search criterion is required: keyword, city, state, country or zipCode",
  "path": "/users/search/by-address"
}
```

### 404 — not found (`GET /users/99`)
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id : 99",
  "path": "/users/99"
}
```

### 409 — duplicate (`POST /users` with an existing email)
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 409,
  "error": "Conflict",
  "message": "Email already registered: rahul@example.com",
  "path": "/users"
}
```

### 503 — address-service is down (`POST /users/1/addresses`)
```json
{
  "timestamp": "2026-10-05T12:00:00",
  "status": 503,
  "error": "Service Unavailable",
  "message": "Address service is currently unavailable",
  "path": "/users/1/addresses"
}
```

# Exercise Management API

This project is a Spring Boot application for managing exercises. It provides endpoints to upload exercise data via CSV, fetch all exercises, fetch an exercise by its code, and delete all exercises.

## Summary

The API exposes endpoints to manage exercises:

- Upload a CSV file containing exercise data.
- Fetch all exercises.
- Fetch an exercise by its code.
- Delete all exercises.

## API Endpoints

The application has the following APIs:

### ExerciseAPI

```html
POST /api/upload - uploads a CSV file containing exercise data
GET /api/fetchAll - retrieves all exercises
GET /api/fetchByCode/{code} - retrieves an exercise by its code
DELETE /api/deleteAll - deletes all exercises
```
JUnit test coverage is 100% as well as integration tests are available.


### Tech Stack

---
- Java 17
- Spring Boot 3
- Spring Data JPA
- Restful API
- OpenAPI documentation
- H2 In memory database
- JUnit 5

### Prerequisites

---
- Maven

### Run & Build

---
There are 2 ways of run & build the application.

#### Docker Compose

For docker compose usage, docker images already push to docker.io

You just need to run `docker-compose up` command
___
*$PORT: 8080*
```ssh
$ cd account
$ docker-compose up
```

#### Maven

For maven usage, you need to change `proxy` value in the `account-fe/package.json`
file by `"http://localhost:8080"` due to the default value has been settled for docker image network proxy.
___
*$PORT: 8080*
```ssh
$ cd account/account-api
$ mvn clean install
$ mvn spring-boot:run

$ cd account/account-fe
$ npm install
$ npm start
```

### Swagger UI will be run on this url
`http://localhost:${PORT}/swagger-ui.html`
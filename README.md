
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

___
*$PORT: 8080*
```ssh
### Swagger UI will be run on this url
`http://localhost:${PORT}/swagger-ui.html`

Due to the relatively low complexity of the project, Domain-Driven Design (DDD) has not been implemented.
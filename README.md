# User Aggregator API

## Description
User Aggregator API is a **Spring Boot** service that aggregates user data from multiple databases.
The application supports dynamic database connection configuration via **YAML** and provides a REST API to retrieve a unified list of users.

## Features
- Collect users from multiple databases.
- Flexible column mapping configuration via **YAML**.
- REST API **`GET /users`** that returns all users in JSON format.
- OpenAPI documentation.
- **Docker** and **Testcontainers** support for integration tests.

## Requirements
- **Java 17**
- **Maven 3**
- **Docker** (for containerization)

## How to Build and Run Locally

1. **Clone the repository**:
   ```sh
   git clone <repo-url>
   cd user-aggregator
   ```

2. **Run docker locally**:

3. **Build the JAR**:
   ```sh
   mvn clean package
   ```
   
4. **Run only databases from docker-compose file using command**:
   ```sh
      docker-compose up postgres1 postgres2 -d
   ```

5. **Run the application**:
   ```sh
   mvn spring-boot:run
   ```

6. **API will be available at**: `http://localhost:8899/users`

## Running with Docker
1. **Build the image**:
   ```sh
   docker build -t user-aggregator .
   ```
2. **Start the container with PostgreSQL and the application**:
   ```sh
   docker-compose up --build
   ```

3. **API will be available at**: `http://localhost:8899/users`

## Running Tests
1. **Run unit and integration tests with Testcontainers**:
   ```sh
   mvn test
   ```

## OpenAPI Documentation
After starting the API, OpenAPI documentation can be accessed at:
- Swagger UI: [http://localhost:8899/swagger-ui.html](http://localhost:8899/swagger-ui.html)
- OpenAPI JSON: [http://localhost:8899/api-docs](http://localhost:8899/api-docs)
- Swagger UI (alternative URL): [http://localhost:8899/swagger-ui/index.html](http://localhost:8899/swagger-ui/index.html)

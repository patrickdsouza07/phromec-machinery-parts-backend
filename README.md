# Phromec Machinery and Parts API

Backend REST API for Phromec's machinery and parts management system. It provides endpoints for managing machines, parts, materials, customers, orders, quotations, and users, with JWT-based authentication and role permissions.

## Features

- CRUD and paginated listing APIs for core machinery and business records.
- Login and registration endpoints that issue JWT authentication tokens.
- Role and permission based access control for protected operations.
- OpenAPI documentation with a Swagger UI for exploring and calling endpoints.
- Maven build with the Maven Wrapper included.

## Requirements

- Java 21
- MySQL 8 (or a compatible MySQL server) for the configured database

## Get started

1. Create a MySQL database:

   ```sql
   CREATE DATABASE machinery_parts_erp;
   ```

2. Configure the database connection and JWT secret. The application reads Spring properties from environment variables; for example, in PowerShell:

   ```powershell
   $env:SPRING_DATASOURCE_URL = 'jdbc:mysql://localhost:3306/machinery_parts_erp'
   $env:SPRING_DATASOURCE_USERNAME = 'root'
   $env:SPRING_DATASOURCE_PASSWORD = 'your-database-password'
   $env:JWT_SECRET = 'replace-with-a-long-random-secret'
   ```

   The defaults are in [`application.properties`](src/main/resources/application.properties). The project does not include database migrations or seed data, so ensure the schema and required role/permission records exist before using the API.

3. Start the application from the repository root:

   ```bash
   # macOS/Linux
   ./mvnw spring-boot:run

   # Windows PowerShell
   .\mvnw.cmd spring-boot:run
   ```

   The service listens on `http://localhost:8080` by default.

## Using the API

Open [Swagger UI](http://localhost:8080/swagger-ui.html) to browse the API documentation. The OpenAPI JSON is available at `http://localhost:8080/v3/api-docs`.

Authentication routes are public:

```text
POST /phromecManagement/api/v1/auth/login
POST /phromecManagement/api/v1/auth/register
```

For example, log in with a user already present in the database:

```bash
curl -X POST http://localhost:8080/phromecManagement/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"your-username","password":"your-password"}'
```

Use the returned JWT as a bearer token for protected endpoints. For example, list the first page of parts:

```bash
curl 'http://localhost:8080/phromecManagement/api/v1/parts?page=0&size=10' \
  -H 'Authorization: Bearer YOUR_JWT'
```

Available API groups include `/users`, `/customers`, `/machines`, `/machine-types`, `/parts`, `/materials`, `/orders`, and `/quotations`, all under `/phromecManagement/api/v1`. Authorization may also depend on the user's assigned permissions.

## Build and tests

```bash
./mvnw test
./mvnw package
```

On Windows, use `.\mvnw.cmd` in place of `./mvnw`.

## Help and documentation

- Browse endpoint details in the running service's Swagger UI at `/swagger-ui.html`.
- See [Spring Boot documentation](https://docs.spring.io/spring-boot/) and [Maven documentation](https://maven.apache.org/guides/).
- For project-specific questions or bug reports, open an issue in the repository hosting this project.

## Contributing and maintenance

Contributions are welcome. Open an issue to discuss a substantial change, then submit a pull request with a clear description and relevant tests. Run `./mvnw test` before submitting. There is no separate contribution guide in this repository yet.

No maintainer details or license file are currently included in the repository; check the repository hosting page for current ownership and license information.

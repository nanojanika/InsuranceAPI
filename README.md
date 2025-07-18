# InsuranceAPI

InsuranceAPI is a Spring Boot application that provides REST endpoints for listing insurance products, premium calculation, and policy creation.

## 📌 Project Overview

This API provides three main functionalities:
- listing available insurance products
- calculating premium quotes based on product, customer age, and coverage amount
- creating new insurance policies with customer information.

## 🧱 Design Decisions

### Database Interaction
The application uses Spring JDBC with SimpleJdbcCall to interact with Oracle stored procedures and functions.
This approach was chosen for:
- direct integration with existing Oracle PL/SQL packages
- efficient execution of complex business logic in the database
- clear separation between application and database layers.

### Customer Lookup
Customer lookup is handled through the following approach:
- when creating a policy, the application passes customer information (name, date of birth, email) to the database
- the database procedure (CREATE_POLICY) checks if a customer with the provided email already exists
- if found, it uses the existing customer record, otherwise it creates a new customer
- this approach simplifies the API by not requiring separate customer management endpoints
- email is used as the unique identifier for customers.

### Error Handling
The application implements the following error handling strategy:
- 404 (Not Found) is returned when a requested resource doesn't exist (e.g., product not found)
- 400 (Bad Request) is returned for other input validation errors (e.g., invalid date format, missing required fields)
- database-specific errors (like ORA-20001) are translated to fit HTTP status codes
- detailed error messages are provided to help to understand the issue.

## 🧰 Setup & Run Instructions

### Prerequisites
- Java 21 or higher
- Gradle 8.0 or higher
- Oracle Database (11g or higher)

### Database Setup
To execute the SQL scripts:

1. Connect to your Oracle database as a user with administrative privileges:
   ```
   sqlplus system/password@localhost:1521/XE
   ```

2. Run the initialization script:
   ```
   @path/to/src/main/resources/db/init.sql
   ```

Alternatively, you can use Oracle SQL Developer or another GUI tool to execute these scripts.

### Building the Application
To build the application:

```bash
./gradlew clean build
```

### Running the Application
To run the application:

```bash
./gradlew bootRun
```

or after building and having jar-file:

```bash
java -jar build/libs/insuranceapi-0.0.1-SNAPSHOT.jar
```

the application can be started on port 8080.

### Running the Application with Docker Compose

To build and run the application using Docker Compose:

1. Ensure Docker and Docker Compose are installed on your system.
2. Navigate to the project root directory where the `docker-compose.yml` file is located.
3. Run the following command to build and start the containers:
   ```bash
   docker-compose up --build
4. Verify that the services are running:
   * The Oracle database should be accessible on port 1521.
   * The insurance-api service should be accessible on port 8080.
5. To stop the containers, run:
   ```bash
    docker-compose down
    ```
6. If needing to clean up volumes and images:
   ```bash
   docker-compose down --volumes --remove-orphans
   docker system prune -af


## 🔍 Example CURL commands

### List all insurance products
```bash
curl -X GET http://localhost:8080/api/products
```

### Calculate premium quote
```bash
curl -X POST http://localhost:8080/api/quotes \
-H "Content-Type: application/json" \
-d '{"productId": 1, "customerDateOfBirth": "1990-01-01", "coverageAmount": 200000}'
```

### Create new policy
```bash
curl -X POST http://localhost:8080/api/policies \
-H "Content-Type: application/json" \
-d '{"productId": 1, "customerName": "Alice", "customerDateOfBirth": "1990-01-01", "customerEmail": "alice@example.com", "coverageAmount": 200000}'
```

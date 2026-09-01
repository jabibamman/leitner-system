# Leitner System

## Overview

Leitner System is a web-based application designed to optimize learning through the spaced repetition and self-evaluation principles of the Leitner system. It allows users to create flashcards, categorize them based on their learning progress, and engage in daily quizzes to reinforce their knowledge. This application adheres to SOLID principles, Domain-Driven Design (DDD), and Hexagonal Architecture to ensure code quality, maintainability, and extensibility.

## Features

- User authentication and registration.
- Flashcard creation with categories based on the Leitner system.
- Daily quizzes to test and reinforce learning.
- Flashcards management based on user performance.
- Tagging system for better organization of flashcards.
- Notifications to remind users of quiz times.

## Prerequisites

- Java JDK 21
- Maven 3.6 or later
- An IDE of your choice (e.g., IntelliJ IDEA, Eclipse)
- No database to install: H2 runs embedded by default (see *Configuration*)

## Installation

1. Clone the repository:

```bash
git clone https://github.com/jabibamman/leitner-system.git
cd leitner-system
```

2. Build the project using Maven:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

The application will be available at http://localhost:8080.

## Configuration

The application reads its configuration from environment variables, falling
back to sensible local defaults. Nothing needs to be set to run it locally.

| Variable                     | Default                                | Purpose                                                              |
| ---------------------------- | -------------------------------------- | -------------------------------------------------------------------- |
| `PORT`                       | `8080`                                 | HTTP port. Platforms like Render inject this and require it to be used. |
| `SPRING_DATASOURCE_URL`      | `jdbc:h2:file:./data/leitner-system`   | JDBC URL. The driver and Hibernate dialect are inferred from it.       |
| `SPRING_DATASOURCE_USERNAME` | `sa`                                   | Database user.                                                        |
| `SPRING_DATASOURCE_PASSWORD` | `sa`                                   | Database password.                                                    |
| `H2_CONSOLE_ENABLED`         | `false`                                | Exposes `/h2-console`. Local debugging only — never enable in production. |
| `PUBLIC_URL`                 | `http://localhost:8080`                | Server URL advertised in the Swagger documentation.                    |

Because the JDBC driver is derived from the URL, switching from H2 to
PostgreSQL is a matter of environment variables — no code change.

## Deployment (Render)

Render has no native Java runtime, so the service is deployed from the
included `Dockerfile` (multi-stage build, non-root runtime image).

### 1. Provision a PostgreSQL database

**Do not keep the file-based H2 database in production.** The disk of a Render
instance is ephemeral: the file is recreated empty on every deploy and on every
restart — including the restart that follows the automatic spin-down of a free
instance. Every card would be lost.

Any managed PostgreSQL works. Note that Render's own free PostgreSQL instances
expire 30 days after creation, so a provider without an expiry date (Neon,
Supabase, …) is a better fit for a long-lived deployment.

### 2. Create the web service

- **Runtime**: Docker (the repository's `Dockerfile` is picked up automatically)
- **Health check path**: `/cards`

### 3. Set the environment variables

Connection strings are usually handed out in `postgresql://user:password@host/db`
form, which JDBC does not accept. Split it up:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<database>?sslmode=require
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
PUBLIC_URL=https://<your-service>.onrender.com
```

`PORT` is injected by Render, and `H2_CONSOLE_ENABLED` must stay unset.

On first start, Hibernate creates the schema automatically
(`spring.jpa.hibernate.ddl-auto=update`).

### Notes

- CORS already allows every origin, so the front end needs no extra setup.
- A free instance spins down after 15 minutes without traffic; the next request
  pays a cold start of roughly a minute. The data itself is safe — it lives in
  PostgreSQL, not on the instance.

## API Documentation

The API documentation is automatically generated and can be accessed via Swagger UI at http://localhost:8080/swagger-ui.html.
This provides an interactive UI to send requests and view responses from the API endpoints.

## Running Tests

Execute the following command to run the unit tests:

```bash
mvn test
```

## Extending the Application

The application is designed with extensibility in mind. To add a new feature or functionality, follow the Hexagonal Architecture patterns by defining new ports and adapters for the feature, ensuring that it remains decoupled from the core application logic.

## Contributing

We welcome contributions to the Leitner System project. Please read our CONTRIBUTING.md file for guidelines on how to make a contribution.

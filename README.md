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

Any managed PostgreSQL works, but check the expiry policy of the plan.
**Render's own free PostgreSQL instances expire 30 days after creation**, then
allow a 14-day grace period before the database and all of its data are deleted;
they are not backed up either. For a database meant to last, pick a plan without
an expiry date — a paid Render instance, or a free tier that does not expire
(Neon, Supabase, …).

### 2. Create the web service

- **Language / Runtime**: Docker
- **Branch**: the one carrying the `Dockerfile`
- **Region**: the same as the database, otherwise its internal URL is unreachable
- **Dockerfile path**: `./Dockerfile` (the default)
- **Health check path**: `/cards`

The container listens on the port given by `PORT`, which Render sets to 10000;
the `EXPOSE` instruction matches it, so nothing has to be configured.

### 3. Set the environment variables

Providers hand out a connection string in `postgresql://user:password@host/db`
form. **The PostgreSQL JDBC driver does not accept that form**: it ignores
credentials embedded in the URL and expects a `jdbc:` scheme. Rewrite it as
three separate variables:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://<host>/<database>
SPRING_DATASOURCE_USERNAME=<user>
SPRING_DATASOURCE_PASSWORD=<password>
PUBLIC_URL=https://<your-service>.onrender.com
```

The port may be omitted — JDBC defaults to 5432.

`PORT` is injected by Render, and `H2_CONSOLE_ENABLED` must stay unset.

#### About SSL and the choice of host

Render exposes two connection strings for its databases, and they do not behave
the same way:

- The **internal** URL (host like `dpg-xxxxxxxx-a`, no domain, no port) is only
  reachable from another Render service in the same region. It is the one to use
  when the API runs on Render: faster and not exposed to the internet. Internal
  traffic does not go through SSL, so **do not append `?sslmode=require`** — it
  can break the connection. Leave the parameter out; the driver negotiates SSL
  when available and falls back otherwise.
- The **external** URL (fully qualified host, e.g. `.oregon-postgres.render.com`)
  is required from anywhere else, and there SSL is mandatory:
  `?sslmode=require`.

Databases hosted elsewhere (Neon, Supabase, …) are always external connections
and need `?sslmode=require`.

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

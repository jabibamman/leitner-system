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
- H2 Database (embedded mode is used for simplicity)

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

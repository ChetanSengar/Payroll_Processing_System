# Payroll Processing System

## Description

The Payroll Processing System is designed to manage and process payroll data for employees. It reads employee data
from `csv` or `txt` file and updates the located file as operations are performed. The application runs standalone
without a traditional DBMS, using a JSON file (`EmployeeData.json` located in the `data` folder) to store data shared by
the file uploaded.

## Code Quality and Design Considerations

This project is designed with the following key software engineering practices in mind:

- **Design and Architecture**: Utilizes clean, modular architecture following SOLID principles to ensure each component
  is independent and responsible for a single functionality.
- **Object-Oriented Programming**: Employs OOP principles such as encapsulation, inheritance, and polymorphism to
  enhance code maintainability and scalability.
- **Production-Quality Code**: Focuses on readability, reusability, and refactorability while minimizing technical debt.
- **Design Patterns and Principles**: Implements common design patterns where applicable to solve typical problems
  encountered in software design.
- **Test-Driven Development (TDD)**: Adopts TDD practices to write tests before actual development, ensuring a more
  reliable and bug-free codebase.
- **Exception Handling**: Robust exception handling mechanisms to deal with runtime errors and unexpected behavior
  gracefully.
- **Code Coverage**: Aims for high code coverage to ensure most code paths are tested under various scenarios.

## Features

- **Swagger Integration**: Provides detailed API documentation accessible via `/swagger-ui.html` once the application is
  running. The application will be accessible on `http://localhost:8080`.
    - Ex: `http://localhost:8080/swagger-ui/index.html`
- **Actuator**: Monitors the application health and provides system metrics at `/actuator`.
    - Ex: `http://localhost:8080/actuator/health`
- **File-Based Data Management**: Uses a JSON file to store and retrieve payroll data, making the application easy to
  set up and run independently of a database server.
    - Ex: Take the file from the `src/main/resources/dummyFileData/<txt or csv>`, upload it and result will get stored
      in
      `data/EmployeeData.json` with API `http://localhost:8080/api/payroll/upload`.

## Prerequisites

- **Java JDK 17**: Ensure Java JDK 17 is installed and properly set up in your environment.
- **Maven**: Required to build and manage dependencies.
- **Lombok Plugin**: If using an IDE like IntelliJ IDEA or Eclipse, make sure to install the Lombok plugin and enable
  annotation processing in your IDE settings. This is crucial for Lombok annotations to work correctly in the project.

## Setup

1. **Clone the repository**:
   ```bash
   git clone [repository-url]
   cd payroll-processing-system

2. **Build the project**:
   ```bash
   mvn clean install

## Running the Application

- To run the application, use the provided run.sh script:
   ```bash
   ./run.sh

- This script sets the appropriate Java options, builds the application using Maven, and starts the application. Ensure
  the script has execute permissions:
   ```bash
    chmod +x run.sh

## Data Folder

- The application utilizes a data folder at the root directory where the EmployeeData.json file is stored. This file is
  read to fetch existing records and updated with new data as the program runs. Ensure that permissions are set
  appropriately to allow read and write operations in this folder.

## Database Management

- Opted for file-based data handling using a JSON file instead of a traditional database to simplify deployment and
  ensure the application can run as a standalone unit without additional server dependencies.

## Notes

- The application's default port is set to `8081`. Ensure this port is available or modify the port setting in
  the `run.sh` script or `application.properties` file if needed.
- When running in an IDE, ensure that Lombok is configured and annotation processors are enabled to avoid compilation
  errors.

# Project Setup and Run Instructions

Follow these steps to set up and run the project:

## 1. Set Java SDK to 17

Ensure that you have Java SDK version 17 installed on your machine. You can download it from the official Oracle or OpenJDK website.

## 2. Update Local Database Details

Navigate to the `ui/resources` directory and open the `application-local.properties` file. Update the database details to match your local setup.

```properties
# ui/resources/application.properties

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```
## 3. Maven Clean

Use Maven plugin on right side of intellij or open a terminal and navigate to the project root directory. Run the following Maven command to clean the project:

```bash
mvn clean
```

## 4. Maven Install

Use Maven plugin on right side of intellij or open a terminal and navigate to the project root directory. Run the following Maven command to install the modules:
```bash
mvn install
```

## 5. Run UiApplication in UI Module

Navigate to the `ui` module and run the following command to start the Spring boot Application:

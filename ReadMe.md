# Project Setup and Run Instructions

Follow these detailed steps to set up and run the project successfully. This guide assumes that you have a basic understanding of Java, Maven, Docker, and MinIO.

# 1. Prerequisites: 
> ### 1.1 Java SDK 17:
* Ensure that you have Java SDK version 17 installed on your machine. You can download it from the official Oracle or OpenJDK [website](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

> ### 1.2 Docker:
* Install Docker on your machine. You can download Docker from official [website](https://www.docker.com/get-started).

> ### 1.3 PostgreSQL:
* Install PostgreSQL on your machine. You can download it from the official PostgreSQL [website](https://www.postgresql.org/download/).  During installation, you can choose to create a new database or use the default one named 'postgres'.

> ### 1.4  IntelliJ IDEA (Recommended IDE):
* Use IntelliJ IDEA as your integrated development environment (IDE). You can download it from the official JetBrains [website](https://www.jetbrains.com/idea/download/?section=windows).

> ### 1.5  Node.js v16.17:
* Install Node.js version 16.17 on your machine. You can download it from the official Node.js [website](https://nodejs.org/dist/v16.17.0/).


## 2. Database Configuration:

Navigate to the `ui/resources` directory and open the `application-local.properties` file. Update the database details to match your local setup.

```properties
# ui/resources/application.properties

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```
Replace your_username and your_password with the credentials you set during PostgreSQL Database installation. If you didn't set a specific password during installation, you might be able to use default password.

## 3. Update Local S3 Details

Navigate to the `ui/resources` directory and open the `application-local.properties` file. Update the database details to match your local setup.

```properties
# ui/resources/application.properties

# MinIO Configuration
minio.endpoint=http://localhost:{minio_port}
minio.port={minio_port}
minio.access-key={minio_accesskey or username}
minio.secret-key={minio_secretkey or password}
```

__NOTE__: Provided configuration files contain default credentials.

Then navigate to `./docker-compose.yml` and update the following properties
```yaml
services:
  minio:
    image: minio/minio:latest
    container_name: minio
    environment:
      MINIO_ROOT_USER: "{minio_accesskey or username}"
      MINIO_ROOT_PASSWORD: "{minio_secretkey or password}"
    volumes:
      - {minio_storage_dir}:/data
    ports:
      - {minio_port}:9000
      - {minio_console_port}:9001
    command: server /data --console-address ":9001"
```
__NOTE__: If you wish to reuse existing minio, you only need to create a `./s3` folder in project dir

__NOTE__: Your variables in both the docker-compose and application-local properties
must be similar.

__NOTE__: To confirm that MinIO Object Store is working properly, you can visit `localhost:{minio_console_port}` on your browser

__NOTE__: Set the path to `{minio_storage_dir}` and make sure that that folder is empty and exists. An example can be `./s3` within the project root directory and adding `./s3` to `.gitignore`
## 4. Running Docker Container

To  initialize the defined services, use the following command:

```bash
docker-compose up
```

__NOTE__: This command orchestrates the deployment of the multi-container application as specified in the docker-compose.yml file. It is a crucial step for launching and testing the interconnected services of the application.

## 5. Maven Install

Use Maven plugin on right side of intellij or open a terminal and navigate to the project root directory.

Before running Maven commands, make sure that the mvn executable is in your system's PATH. If you encounter an "mvn not recognized" error, follow these [steps](https://www.tutorialspoint.com/maven/maven_environment_setup.htm) to add the Maven bin directory to your PATH

Run the following Maven command to clean the project:

```bash
mvn clean
```

Then run the following Maven command to install the modules:
```bash
mvn install
```

__NOTE__: When running the application from an integrated development environment (IDE), these commands are typically executed automatically for you. 

## 6. Redis OTP Cache
Redis Docker Image will be started automatically with `docker-compose.yml`. However,
 if you want to use the existing properties, create a folder `./cache` in the root dir.

## 7. Domain Tag Initialization
Run the backend with this first query
```
curl --location 'http://localhost:8080/product-tags/batch' \
--header 'Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb3dhaGEyQGJpbGtlbnQuZWR1LnRyIiwicm9sZSI6IkJJTEtFTlRFRVIiLCJpYXQiOjE3MDI4MzY0MDEsImV4cCI6MTcwMjg3MjQwMX0.Y5EoWCRvBbd7SHdAR1DFNiN8lYbdP5psgMLcRY0i0QQ' \
--header 'Content-Type: application/json' \
--data '{
    "tagNames": [
        "Textbooks",
        "Electronics",
        "Kitchenware",
        "Instruments",
        "Bicycles",
        "Games",
        "Furniture",
        "Rentable",
        "Borrowable",
        "Donations",
        "Purchase"
    ]
}'
```

## 8. Run UiApplication in UI Module

Navigate to the `ui` module and click Run next to the Current Module option on the toolbar.

![](https://i.ibb.co/QkTFJfq/Untitled.png)



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
__NOTE__: Your variables in both the docker-compose and application-local properties
must be similar.

__NOTE__: To confirm that MinIO Object Store is working properly, you can visit `localhost:{minio_console_port}` on your browser

__NOTE__: Set the path to `{minio_storage_dir}` and make sure that that folder is empty and exists. An example can be `./s3` within the project root directory and adding `./s3` to `.gitignore`
## 4. Maven Clean

Use Maven plugin on right side of intellij or open a terminal and navigate to the project root directory. Run the following Maven command to clean the project:

```bash
mvn clean
```

## 5. Maven Install

Use Maven plugin on right side of intellij or open a terminal and navigate to the project root directory. Run the following Maven command to install the modules:
```bash
mvn install
```

## 6. Run UiApplication in UI Module

Navigate to the `ui` module and run the following command to start the Spring boot Application:

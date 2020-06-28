file-import
========

This project is a simple Java application for parsing CSV and XML files into Objects and store them in the database.

## Features

- Parse CSV or XML files using Spring Batch
- Save data to a database
- Access data  via REST endpoint

## Usage

1. Clone the repository: `https://github.com/darq37/file-import.git`
2. Provide a path (ASCII encoded) to XML or CSV file with environment variable: `app.filepath` or inject the variable into `application.properties`file.
3. Start the application 

If you want to start the application as a JAR file, make sure you have the `app.filepath` environment variable set to an absolute path to the input file.

## Input File
The application expects an absolute path to input file injected as `app.filePath`.  This file can be either an XML or a CSV file - the application uses its extension to determine the correct implementation to run.

## Configuration
Some configuration is provided by default, but you can override it using environment variables:
```
# Regular expressions used to parse contacts as email, phone, jabber or unknown type
app.emailPattern=[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}  
app.phonePattern=^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{3}$  
app.jabberPattern=^[a-zA-Z]*$

# Absolute path to the input file including extension
app.filePath=  
spring.batch.job.enabled=false
```

## Dependencies
- `spring-boot-configuration-processor` - externalized configuration
- `spring-boot-web` - to expose REST endpoint for easier access to data
- `spring-data-jpa` - to abstract database operations
- `junit-5`- unit testing
- `h2` - temporary, in-memory database. Could be easly exchanged to another database server due to abstraction of Spring Data JPA
- `spring-batch` - streamlined file processing
- `spring-oxm` - abstraction around XML/Object mapping
- `xstream` -  XML/Object serialization, custom Converter implementation
- `commons-io` - FIlenameUtils, getting an extension of a file

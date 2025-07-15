# 1GLOBAL Backend Java Challenge
This repository contains a  a REST API capable of persisting and managing device resources.



## Table of Contents

- [Installation](#installation)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Database](#database)

## Installation

Clone the repository:

git clone https://github.com/sicabralBr/1global-backend-java-challenge/tree/master

2. Install dependencies with Maven
   
    mvn clean install


## Usage

1. Start the application with Maven
2. The API will be accessible at http://localhost:8080


## API Endpoints
The API provides the following endpoints:

```markdown
GET / - Fetch all devices.

GET / {id} - Fetch a single device.

GET / {state} - Fetch devices by state.

GET / {brand} - Fetch devices by brand.

POST / - Create a new device.

PUT / - Fully and/or partially update an existing device.

DELETE / {id} - Delete a single device.
```

## Database
The project utilizes PostgresSQL as the database. The necessary database migrations are managed using Flyway.

To [install PostgresSQL])(https://www.postgresql.org/download/) you can install here.

## TO DO

Organize exception handlers in a dedicated folder to follow best practices and promote reuse.

Introduce a logging mechanism to the project for better traceability.

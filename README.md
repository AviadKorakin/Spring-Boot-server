# Spring Boot server

## Overview

This project is a sophisticated application that integrates a Spring Boot backend server with a PostGIS-enabled PostgreSQL database and an Android client. The project leverages Docker for containerization, ensuring consistent deployment across environments. The Android client uses Retrofit to facilitate seamless communication with the backend API.

## Features

### 🌱 Spring Boot Backend Server

- **RESTful API**: The backend server is developed using Spring Boot, providing a RESTful API for managing the application's data and business logic. 
- **PostGIS Integration**: The backend uses PostGIS, an extension of PostgreSQL for handling geospatial data, essential for location-based services within the application.
- **Command Design Pattern**: The server implements the Command Design Pattern to handle custom commands in a flexible and maintainable way, especially for the `MiniAppCommandService`.
- **Dockerized Environment**: The backend, including the PostGIS database, is fully containerized using Docker, ensuring a seamless and reproducible deployment process.

### 🤖 Android Client

- **Modular Architecture**: The Android application follows a modular architecture, separating concerns across different layers of the app for improved maintainability.
- **Retrofit Integration**: Retrofit is utilized for making HTTP requests to the backend API, with GSON handling JSON parsing to map API responses into Java objects.
- **MVVM Pattern**: The client app is designed using the MVVM (Model-View-ViewModel) pattern, ensuring a clean and scalable code structure.
- **Gradle Build System**: The Android project uses Gradle with Kotlin DSL for managing dependencies and build configurations, ensuring efficient project management.

### 🐳 Docker with PostGIS

- **PostGIS Database**: The Docker Compose configuration includes a PostGIS container, providing powerful spatial database capabilities.
- **Spring Boot Service**: The Spring Boot application is run in a separate Docker container, communicating with the PostGIS database through a defined network.
- **Environment Configuration**: The Docker setup allows for easy environment configuration, making it adaptable for different deployment scenarios.

## REST API Overview

The backend server exposes a set of RESTful API endpoints that the Android client consumes via Retrofit. Below are the main services and their corresponding API commands:

### 🔐 User Service

- **Store User in Database**: 
  - `POST /superapp/users`
  - Stores a new user in the database.
- **Get Specific User**: 
  - `GET /superapp/users/login/{superapp}/{email}`
  - Retrieves a user by their email and superapp identifier.
- **Update User**: 
  - `PUT /superapp/users/{superapp}/{userEmail}`
  - Updates the user information.

### ⚙️ MiniApp Command Service

- **Invoke MiniApp Command**: 
  - `POST /superapp/miniapp/{miniAppName}`
  - Executes a custom command associated with a mini app. This is where the Command Design Pattern shines, allowing for flexible command handling based on the mini app's specific needs.

### 🗄️ Object Service

- **Store Object in Database**: 
  - `POST /superapp/objects`
  - Stores a new object in the database.
- **Get Specific Object**: 
  - `GET /superapp/objects/{superapp}/{id}`
  - Retrieves a specific object by its ID.
- **Update Object**: 
  - `PUT /superapp/objects/{superapp}/{id}`
  - Updates an existing object.
- **Search by Type**: 
  - `GET /superapp/objects/search/byType/{type}`
  - Retrieves objects filtered by their type.
- **Search by Alias**: 
  - `GET /superapp/objects/search/byAlias/{alias}`
  - Retrieves objects filtered by their alias.
- **Search by Location**: 
  - `GET /superapp/objects/search/byLocation/{lat}/{lng}/{distance}`
  - Retrieves objects within a specific distance from a location.

### 🛠️ Admin Service

- **Delete All Users**: 
  - `DELETE /superapp/admin/users`
  - Deletes all users from the database.
- **Delete All Objects**: 
  - `DELETE /superapp/admin/objects`
  - Deletes all objects from the database.
- **Delete All Commands**: 
  - `DELETE /superapp/admin/miniapp`
  - Deletes all commands from the database.
- **Export All Users**: 
  - `GET /superapp/admin/users`
  - Exports all users from the database.
- **Export All Commands**: 
  - `GET /superapp/admin/miniapp`
  - Exports all mini app commands from the database.
- **Search Commands by MiniApp**: 
  - `GET /superapp/admin/miniapp/{miniApp}`
  - Searches for commands related to a specific mini app.

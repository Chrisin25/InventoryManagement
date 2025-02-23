# Inventory management system

## Overview
This is a **Spring Boot** application that provides a RESTful API for managing products and categories in an inventory system. The system supports adding, updating, deleting, and retrieving products and categories, as well as handling orders and restocking.

## Features
- Add, update, retrieve, and delete **products**
- Add, update, retrieve, and delete **categories**
- Order and restock products
- Pagination support for product retrieval
- Structured API responses with custom response messages

## Tech Stack
- **Java 17+**
- **Spring Boot 3+**
- **Spring Data JPA** (for database interactions)
- **Maven** (for dependency management)
- **Log4j** (for logging)
- **MySQL**

## Prerequisites
Ensure you have the following installed:
- Java 17 or higher
- Maven 3+
- PostgreSQL/MySQL (configured in `application.properties`)

## Setup Instructions

### Clone the Repository
```sh
git clone <repo-url>
cd inventory-management
```

### Configure Database
Modify `src/main/resources/application.properties` to match your database configuration:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventorydb
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Build and Run the Application
```sh
mvn clean install
mvn spring-boot:run
```

The application will start at `http://localhost:8080/api/v1`.

## API Endpoints

### Product Endpoints
| Method | Endpoint       | Description                          |
|--------|---------------|--------------------------------------|
| POST   | `/product`    | Add a new product                   |
| GET    | `/product`    | Retrieve products (with pagination) |
| PUT    | `/product`    | Update product details              |
| DELETE | `/product`    | Delete a product                    |

### Category Endpoints
| Method | Endpoint       | Description                          |
|--------|---------------|--------------------------------------|
| POST   | `/category`   | Add a new category                  |
| GET    | `/category`   | Retrieve categories                 |
| PUT    | `/category`   | Update category name                |
| DELETE | `/category`   | Delete a category                   |

### Order & Restock Endpoints
| Method | Endpoint      | Description                          |
|--------|--------------|--------------------------------------|
| PUT    | `/orders`    | Place an order for a product        |
| PUT    | `/restock`   | Restock a product                    |





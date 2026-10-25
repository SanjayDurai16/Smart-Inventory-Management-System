# 📦 Smart Inventory Management System

A modern, high-performance desktop inventory management solution built with **Java 21**, **JavaFX**, **Spring Boot 3**, **Spring JdbcTemplate**, and **MySQL**.

---

## 🏛️ Architecture

```
┌─────────────────────────┐
│     JavaFX Desktop      │
│  (Modern Responsive UI) │
└────────────┬────────────┘
             │ HTTP / JSON
             ▼
┌─────────────────────────┐
│  Spring Boot REST API   │
│     (inventory-api)     │
└────────────┬────────────┘
             │ Prepared Statements
             ▼
┌─────────────────────────┐
│ Spring JdbcTemplate DAO │
└────────────┬────────────┘
             │ Transactional SQL
             ▼
┌─────────────────────────┐
│      MySQL Database     │
│      (inventory_db)     │
└─────────────────────────┘
```

The desktop UI communicates exclusively via HTTP REST endpoints and JSON with the Spring Boot backend, adhering to separation of concerns and database decoupling.

---

## ✨ Features

- **📊 Real-time Dashboard**: Live metric cards displaying total products, categories, completed sales, total revenue, and critical low-stock items.
- **📦 Product Management (CRUD)**: Create, view, update, and delete products with SKU uniqueness enforcement, category associations, and configurable low-stock thresholds.
- **📁 Category Management**: Organize products into structured categories with validation preventing accidental deletion of categories containing active products.
- **💰 Sales & Transactional Stock Reduction**: Cart-based checkout that performs atomic database transactions — validating inventory, creating sales and item records, and automatically deducting quantities.
- **⚠️ Low Stock Alerts & Quick Restock**: Instant detection of products at or below their low-stock thresholds with a one-click restocking modal.
- **🎨 Modern Slate UI**: Custom CSS styling with clean sidebar navigation, high-contrast stat cards, styled data tables, and modal dialogs.

---

## 🛠️ Tech Stack

- **Language**: Java 21 (LTS)
- **UI Framework**: JavaFX 21 (FXML + CSS)
- **Backend Framework**: Spring Boot 3.3
- **Data Access**: Spring `JdbcTemplate` (Plain SQL with Prepared Statements)
- **Database**: MySQL 8.0+
- **Build Tool**: Apache Maven (Multi-module architecture)
- **JSON Processing**: Jackson Databind

---

## 📂 Project Structure

```text
smart-inventory/
├── pom.xml                               # Parent Maven POM
├── database/
│   └── schema.sql                        # Database DDL & seed data
├── inventory-api/                        # Spring Boot REST API
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/inventory/api/
│       │   │   ├── InventoryApiApplication.java
│       │   │   ├── controller/           # REST endpoints
│       │   │   ├── service/              # Business logic & @Transactional
│       │   │   ├── repository/           # JdbcTemplate repositories
│       │   │   ├── model/                # Entity models
│       │   │   ├── dto/                  # Request / Response DTOs
│       │   │   └── exception/            # Global exception handling
│       │   └── resources/
│       │       └── application.properties# MySQL DataSource configuration
│       └── test/
│           └── java/com/inventory/api/   # Automated integration tests
└── inventory-ui/                         # JavaFX Desktop Application
    ├── pom.xml
    └── src/main/
        ├── java/com/inventory/ui/
        │   ├── InventoryUiApplication.java
        │   ├── controller/               # JavaFX view controller
        │   ├── service/                  # HTTP REST API client
        │   └── model/                    # Client-side data models
        └── resources/
            ├── fxml/
            │   └── main.fxml             # Desktop view layout
            └── css/
                └── style.css             # Modern stylesheet
```

---

## 🚀 Getting Started

### Prerequisites

- **Java 21 JDK** installed (`java -version`)
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **MySQL Server** installed and running

---

### 1. Database Setup

Create the database and apply the schema:

```bash
# Log in to MySQL and run schema.sql
mysql -u root -p < database/schema.sql
```

Configure your credentials in `inventory-api/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

---

### 2. Run the Application

#### Terminal 1 — Start the Backend API:
```bash
./mvnw -pl inventory-api spring-boot:run
```
*The backend API will start on `http://localhost:8080`.*

#### Terminal 2 — Launch the Desktop UI:
```bash
./mvnw -pl inventory-ui javafx:run
```
*The JavaFX desktop application will open on your screen.*

---

## 🧪 Testing

Run integration tests across all modules:

```bash
./mvnw test
```

Tests verify:
- Category CRUD operations and constraint validations
- Product CRUD operations and low-stock detection
- Atomicity of sales and automatic stock reduction
- Dashboard aggregation metrics

---

## 🔌 API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard` | Get summary metrics and revenue |
| `GET` | `/api/categories` | List all categories |
| `POST` | `/api/categories` | Create a new category |
| `PUT` | `/api/categories/{id}` | Update category details |
| `DELETE`| `/api/categories/{id}` | Delete category |
| `GET` | `/api/products` | List all products |
| `GET` | `/api/products/low-stock` | List items below low-stock threshold |
| `POST` | `/api/products` | Create a new product |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE`| `/api/products/{id}` | Delete a product |
| `GET` | `/api/sales` | List all sales transactions |
| `POST` | `/api/sales` | Create a new sale and reduce stock atomically |

---

## 👤 Author

- **Sanjay Durai -[GitHub Profile](https://github.com/SanjayDurai16)
-  Sujith S  - [Github Profile](https://github.com/Sujith-S05)
-  Barani Sankar - 
-  Sanjai S
-  Srijan S
- ** 

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

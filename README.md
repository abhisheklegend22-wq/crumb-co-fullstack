# Crumb & Co. Bakery — College Full-Stack Project

A bakery e-commerce project with an **Angular 19 web storefront**, **Java 17 / Spring Boot REST backend**, **MySQL 8**, explicit JDBC, a traditional Jakarta Servlet, and an optional **Java Swing desktop inventory GUI**. It is designed to make the Java/OOP/database/web concepts visible for a college evaluation.

## Main features
- Responsive Angular storefront with product search, category filters, cart and checkout
- Products, orders, order items, stock changes, and newsletter subscriptions stored in MySQL
- Server-side price calculation; delivery is ₹49 below ₹799 and free from ₹799
- Transactional order creation and stock reduction
- Demo admin section to add/deactivate products and view orders
- JDBC DAO with parameterized SQL and typed `RowMapper` results
- Direct JDBC REST endpoints and a traditional Jakarta Servlet endpoint
- Asynchronous stock report on a configured thread pool
- Strategy interface for delivery pricing, domain exception inheritance, centralized exception handling, and JUnit tests
- Optional Swing desktop inventory GUI using direct JDBC to view and insert MySQL products

## Requirements
- JDK 17+
- Maven 3.9+
- Node.js 20+ and npm
- Docker Desktop (easiest) or MySQL 8+

## 1. Start MySQL
From the project root:

```bash
install MySQL and start it 
```

Docker exposes MySQL on `localhost:3306`, creates `crumb_co_bakery`, and uses the demo password `root123` for the root user. This is for local college demonstration only. For an existing MySQL installation, create the database and set environment variables.

PowerShell example:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

## 2. Run the Spring Boot backend
Open a terminal at the project root:

```bash
cd backend
mvn spring-boot:run
```

API base: `http://localhost:8080/api`

Hibernate creates/updates the entity tables and sample products are inserted if the catalogue is empty.

## 3. Run Angular frontend
Open a second terminal:

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200`. Keep MySQL and the backend running.

## 4. Optional Java Swing desktop GUI (covers the Java GUI rubric too)
Start MySQL and run the Spring Boot backend once first so Hibernate creates the tables and seeds products. In a separate terminal:

```bash
cd desktop-admin
mvn compile exec:java
```

The Swing application lists products from MySQL and lets you insert a product using JDBC. By default it uses the Docker demo credentials (`root` / `root123`). If using another MySQL account, set `DB_USERNAME`, `DB_PASSWORD`, and optionally `DB_URL` before launching it. This desktop app shares the same MySQL database as the Angular web shop.

## 5. Verify persistence
Place an order, then inspect it in MySQL Workbench:

```sql
USE crumb_co_bakery;
SHOW TABLES;
SELECT * FROM products;
SELECT * FROM customer_orders ORDER BY created_at DESC;
SELECT * FROM order_items;
SELECT * FROM newsletter_subscribers;
```

## API endpoints
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/products` | List active products through JPA repository |
| GET | `/api/products/{id}` | Product detail |
| POST | `/api/products` | Create product |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Soft-deactivate product |
| POST | `/api/orders` | Validate and save order + items; reduce stock |
| GET | `/api/orders/{id}` | Retrieve saved order |
| GET | `/api/orders` | List orders for demo admin |
| POST | `/api/newsletter` | Save newsletter subscription |
| GET | `/api/jdbc/products` | List products using explicit `JdbcTemplate` SQL |
| GET | `/api/jdbc/products/{id}` | Fetch a product using explicit JDBC |
| GET | `/api/reports/stock` | Generate a low-stock report asynchronously |
| GET | `/legacy/catalog` | Traditional Servlet returns JDBC product catalogue as JSON |

## Marking-rubric coverage checklist

| Rubric criterion | Where to demonstrate it |
|---|---|
| GUI OOP: polymorphism, inheritance, interfaces, exception handling | Swing `ProductRepository` / `JdbcProductRepository`; exception inheritance in `DatabaseUnavailableException` / `BakeryDataException`; web `DeliveryFeePolicy` strategy and `BakeryException` hierarchy |
| Collections & generics | `List<Product>`, `List<ProductStockRow>`, `GenericReadDao<T, ID>`, and typed Swing table model |
| Multithreading & synchronization | Swing `SwingWorker`, Spring `@Async` stock report, `synchronized` catalog/order methods |
| Classes for database operations | `JdbcProductRepository` and Spring `JdbcProductDao` keep SQL out of the UI/controllers |
| Database connectivity / JDBC | `DriverManager`, `Connection`, `PreparedStatement`, `ResultSet`, plus Spring `JdbcTemplate` and `RowMapper` |
| Problem understanding & solution design | Product catalogue, stock control, checkout, pricing, delivery and persisted orders; layered UI/controller/service/DAO/database architecture |
| Core Java concepts | Interfaces, implementation classes, inheritance, polymorphism, generics, collections, exceptions, records, concurrency and synchronization |
| Database integration (JDBC) | `/api/jdbc/products`, `/api/reports/stock`, and Swing inventory app read/write MySQL |
| Servlets & web integration | `LegacyBakeryServlet` registered at `/legacy/catalog`; Angular `HttpClient` → REST controllers → services/DAOs |

Use the Swing application to demonstrate the GUI-specific checklist, and use the Angular storefront plus REST/Servlet endpoints for the web-specific checklist. Final marks are determined by your instructor's interpretation and live demonstration; no code can guarantee a specific score.

## Rubric evidence / viva guide

### Java GUI-based rubric evidence
- **GUI:** `desktop-admin/.../BakeryDesktopApp.java` is a Swing `JFrame` with a product `JTable`, add-product dialog, status area, and error dialogs.
- **OOP/interfaces/polymorphism:** `ProductRepository` is an interface implemented by `JdbcProductRepository`; `CatalogService` depends on the interface. `DatabaseUnavailableException` inherits from `BakeryDataException`.
- **Collections/generics:** `List<Product>` is used for query results and the table model; `AbstractTableModel` is extended for a typed table view.
- **Multithreading:** `SwingWorker` performs database reads off the Swing Event Dispatch Thread so the interface remains responsive.
- **Synchronization:** `CatalogService.refresh()` is synchronized to serialize refreshes in this desktop client.
- **JDBC/database-operation class:** `JdbcProductRepository` uses `DriverManager`, `Connection`, `PreparedStatement`, and `ResultSet` with try-with-resources.

### OOP implementation — polymorphism, inheritance, interfaces, exceptions
- **Interface:** `OrderProcessingService` is the abstraction injected into `OrderController`; `OrderService` implements it.
- **Polymorphism / Strategy pattern:** `DeliveryFeePolicy` is the interface and `StandardDeliveryFeePolicy` is the implementation injected into `OrderService`. A different delivery policy can be supplied without rewriting order flow.
- **Inheritance:** `InsufficientStockException` extends `BakeryException`, which extends `RuntimeException`.
- **Exception handling:** `ApiExceptionHandler` maps stock conflicts and validation failures into meaningful HTTP responses. `LegacyBakeryServlet` catches data-access failures.

### Collections and generics
- APIs and JDBC DAO use type-safe `List<ProductStockRow>`, `List<CustomerOrder>`, and `List<OrderItem>` collections.
- `GenericReadDao<T, ID>` provides a generic DAO contract; `JdbcProductDao` implements `GenericReadDao<ProductStockRow, Long>`.
- Order requests use a typed list of order lines rather than raw collections.

### Multithreading and synchronization
- `StockReportService.generate()` uses `@Async("bakeryExecutor")`; `AsyncConfig` defines a bounded thread pool and named worker threads.
- `OrderService.place()` is synchronized to serialize order-processing calls inside this single application instance, while `@Transactional` keeps database changes atomic. **Synchronization is process-local**; a multi-instance production deployment should use database row locks/optimistic locking instead.

### Database-operation classes and JDBC
- `JdbcProductDao` is the dedicated data-access object and uses `JdbcTemplate`, parameterized SQL, `RowMapper`, and typed result objects.
- `JdbcProductController` demonstrates JDBC database access through REST endpoints.
- The JPA repositories remain in place for normal CRUD and entity relationships, so the project demonstrates both repository-based persistence and direct JDBC.

### Servlets and web integration
- `LegacyBakeryServlet` extends `HttpServlet`, implements `doGet`, sets JSON response headers, and sends the JDBC catalogue.
- `ServletConfig` registers it at `/legacy/catalog` using Spring Boot's embedded Servlet container.
- Angular integrates with REST endpoints through `HttpClient`; Spring controllers validate requests and call service/DAO layers.

### Problem understanding and solution design
The problem is to let a bakery present products, accept orders, prevent overselling, calculate delivery costs, manage stock, and retain customer/order data. The layers are: **Angular UI → REST controller / Servlet → service and pricing strategy → JPA repository or JDBC DAO → MySQL**. Order creation recalculates prices from the database and saves order lines and stock changes transactionally.

## Tests
Run backend unit tests:

```bash
cd backend
mvn test
```

The delivery-policy tests cover the ₹799 free-delivery boundary and empty-cart case.

## Example order payload
```json
{
  "customerName": "Aarav Sharma",
  "email": "aarav@example.com",
  "phone": "+91 9876543210",
  "address": "12 Sample Road, Sector 10",
  "city": "Delhi",
  "items": [{ "productId": 1, "quantity": 2 }]
}
```

The client sends only product IDs and quantities. The server retrieves prices from MySQL and calculates the total.

## Important demo note
This is a local college project, not a production payment platform. It does not collect online payments. The demo admin panel and order-list endpoint are unauthenticated for presentation convenience. Do not expose it publicly without authentication/authorization, secure secrets, rate limiting, privacy controls, schema migrations, and database locking appropriate to multiple app instances.

## Project Security Notes
- Keep database credentials in environment variables.
- Do not expose the admin panel without authentication.
- Validate requests before displaying order information.
- Keep sensitive configuration files out of version control.

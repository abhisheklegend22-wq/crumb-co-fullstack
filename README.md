# Crumb & Co. Bakery — Full-stack College Project

A bakery e-commerce project built with **Java 17, Spring Boot 3.4, Spring Data JPA, MySQL 8, and Angular 19**. Product data, orders/order items, stock changes, and newsletter signups are persisted in MySQL.

## Features
- Responsive bakery storefront with product search and category filters
- Products loaded from the Java REST API, not hardcoded in the frontend
- Shopping cart and checkout form
- Server-side price calculation; delivery is ₹49 below ₹799 and free from ₹799
- Validated checkout, stock checks, transactional order save and stock decrement
- Saved order receipt with order number, amount and status
- Newsletter subscription saved to database
- Demo admin section: add/deactivate products and view recent orders
- Product stock visible on product cards

## Requirements
- JDK 17 or newer
- Maven 3.9+
- Node.js 20+ and npm
- MySQL 8+, or Docker Desktop to run the included MySQL container

## 1. Start MySQL (easy option)
From the project root:

```bash
install MySQL and start it 
```

This starts MySQL on `localhost:3306`, creates database `crumb_co_bakery`, and configures the demo root password `root123`. These credentials are for a local college demo only; do not use them in production.

If using an existing local MySQL install, create the database manually:

```sql
CREATE DATABASE crumb_co_bakery;
```

Then set credentials using environment variables. Defaults are `DB_USERNAME=root` and an empty password. Example (PowerShell):

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
```

## 2. Start Java backend
Open a terminal:

```bash
cd backend
mvn spring-boot:run
```

The API is at `http://localhost:8080/api`. Hibernate creates/updates tables automatically on startup and inserts the sample bakery products when the products table is empty.

## 3. Start Angular frontend
Open a second terminal:

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200` in your browser. Keep both backend and MySQL running.

## 4. Verify that data is actually saved
1. Place an order from the storefront using a name, email, phone and address.
2. The confirmation shows an order number and total returned by the API.
3. Open the **Admin / Orders** section to see saved orders; add a product to test product inserts.
4. In MySQL Workbench or a terminal, run:

```sql
USE crumb_co_bakery;
SHOW TABLES;
SELECT * FROM products;
SELECT * FROM customer_orders ORDER BY created_at DESC;
SELECT * FROM order_items;
SELECT * FROM newsletter_subscribers;
```

Tables are `products`, `customer_orders`, `order_items`, and `newsletter_subscribers`.

## API overview
| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/products` | List active products |
| GET | `/api/products/{id}` | Product details |
| POST | `/api/products` | Create a product |
| PUT | `/api/products/{id}` | Update a product |
| DELETE | `/api/products/{id}` | Soft-deactivate a product |
| POST | `/api/orders` | Validate and persist an order |
| GET | `/api/orders/{id}` | Retrieve a saved order |
| GET | `/api/orders` | List orders for the demo admin |
| POST | `/api/newsletter` | Save an email subscription |

### Example order request
```json
{
  "customerName": "Aarav Sharma",
  "email": "aarav@example.com",
  "phone": "+91 9876543210",
  "address": "12 Sample Road, Sector 10",
  "city": "Delhi",
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```

The client sends product IDs and quantities only. The server fetches current prices from MySQL and computes totals itself.

## Database relationship
- `products`: product catalogue, current price and available stock
- `customer_orders`: customer delivery details, totals, order status and creation time
- `order_items`: product, quantity, unit price snapshot and line total for each order
- `newsletter_subscribers`: unique email addresses and subscription timestamp

## College presentation notes
Explain the architecture as Angular UI → REST API → Spring Boot service → Spring Data JPA/Hibernate → MySQL. `OrderService.place()` runs in a transaction, validates current product availability, calculates totals on the server, reduces stock, and saves the order plus order items together.

## Important scope / security note
This is a college demonstration, not a production-ready payment platform. It does not collect online payments. The demo admin panel and `GET /api/orders` are intentionally unauthenticated for easy presentation; **do not deploy publicly** without authentication/authorization, rate limiting, privacy protections, migrations, and security configuration. For production, use a restricted database user and environment-managed secrets, not the sample Docker credentials.

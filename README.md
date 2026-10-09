# ✨ LathikaMart — Multi-Seller E-Commerce Marketplace

**Java Servlets · JDBC · HikariCP · H2 DB · Apache Tomcat**  
*Anna University R2025 Specification — Semester 3 E-Commerce Project*

---

## 📌 Project Overview
**LathikaMart** is a production-grade, multi-seller e-commerce marketplace web application. It enables sellers to list products, manage inventory, and track sales revenue, while buyers can browse products using multi-facet filtering (category + budget slider + dynamic sorting), manage shopping carts, place orders, and track real-time delivery status. An administrator governs user roles, moderates seller listings, and reviews audit logs.

An embedded rule-based **AI Shopping Assistant Chatbot** is integrated directly into the storefront to guide users on product recommendations and return policies.

---

## 🏛️ System Architecture

Layered MVC pattern over Servlets (Front Controller Pattern):

```
Browser (HTML5 / Vanilla CSS / Modular JavaScript + AJAX Fetch)
                     │
                     ▼
  Filter Layer (AuthFilter, EncodingFilter, MdcLoggingFilter)
                     │
                     ▼
  Front Controller (ProductServlet, OrderServlet, AdminServlet, ChatServlet...)
                     │
                     ▼
  Service Layer (ProductService, OrderService, UserService, ChatService)
                     │
                     ▼
  DAO Layer (ProductDAO, OrderDAO, UserDAO, ReviewDAO, AuditLogDAO)
                     │
                     ▼
  Connection Pool (HikariCP via DbContextListener at startup)
                     │
                     ▼
  Database (H2 Server / Embedded Mode with Flyway-style SQL migrations)
```

---

## 🗄️ Database ER Schema

```
+------------------+         +--------------------+         +-------------------+
|      USERS       |         |      PRODUCTS      |         |      ORDERS       |
+------------------+         +--------------------+         +-------------------+
| id (PK)          |<-------1| id (PK)            |    1<---| id (PK)          |
| name             |         | seller_id (FK)     |----     | buyer_id (FK)     |
| email (UNIQUE)   |         | name               |    │    | status            |
| password_hash    |         | description        |    │    | total_amount      |
| role (ENUM)      |         | price DECIMAL(10,2)|    │    | created_at        |
| created_at       |         | stock_qty          |    │    +-------------------+
+------------------+         | category           |    │              │ 1
                             | image_url          |    │              ▼
                             | created_at         |    │    +-------------------+
                             +--------------------+    │    |    ORDER_ITEMS    |
                                       │ 1             │    +-------------------+
                                       ▼               └───N| id (PK)          |
                             +--------------------+         | order_id (FK)     |
                             |      REVIEWS       |         | product_id (FK)   |
                             +--------------------+         | quantity          |
                             | id (PK)            |         | unit_price        |
                             | product_id (FK)    |         +-------------------+
                             | user_id (FK)       |
                             | rating INT         |
                             | comment TEXT       |
                             | created_at         |
                             +--------------------+
```

---

## 🛠️ Technology Stack Specification

| Component | Specification / Library |
| :--- | :--- |
| **JDK** | Java 17 (LTS) |
| **Servlet Container** | Apache Tomcat 9.0.x / Eclipse Jetty 9.4.x |
| **Build Tool** | Apache Maven 3.8+ |
| **Database** | H2 Database Engine (Server/Memory Mode) |
| **Connection Pool** | HikariCP (`com.zaxxer.HikariCP`) |
| **Password Security** | BCrypt (`org.mindrot.jbcrypt`) |
| **JSON Parser** | Google Gson (`com.google.code.gson`) |
| **Testing** | JUnit 5 + Mockito (18 automated tests passing) |
| **Logging** | SLF4J + Logback with MDC Request Tracking |

---

## 🔑 Pre-Configured Test Accounts

| Role | Email | Password | Access Rights |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@lathikamart.com` | `password123` | User role escalation, listing moderation, audit log inspection |
| **Buyer** | `john.buyer@gmail.com` | `password123` | Browsing, search & filter, cart, checkout, order tracker |
| **Seller** | `seller.tech@lathikamart.com` | `password123` | Inventory management, product creation, sales analytics |
| **Seller** | `seller.fashion@lathikamart.com` | `password123` | Fashion product catalog management |

---

## 🚀 How to Run Locally

### Option 1: Run with Maven Jetty Plugin (Dev Mode)
```bash
# Clone repository
git clone https://github.com/your-username/LathikaMart.git
cd LathikaMart

# Build and start dev server on http://localhost:9090/lathikamart/
mvn jetty:run
```

### Option 2: Build Production WAR File
```bash
# Clean, run test suite, and package WAR
mvn clean verify

# Built WAR artifact:
target/lathikamart.war
```
*Deploy `target/lathikamart.war` directly to Tomcat's `webapps/` folder.*

---

## 🧪 Running Automated Tests
```bash
mvn test
```
All 18 tests execute against an embedded H2 database (`jdbc:h2:mem:test`).

---

## 📄 License
Anna University R2025 Semester 3 Course Project Deliverable.

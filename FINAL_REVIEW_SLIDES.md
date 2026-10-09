# 📊 LathikaMart — Final Review Slide Deck Presentation
*Anna University R2025 Semester 3 Course Project Specification*

---

## Slide 1: Title & Overview
### ✨ LathikaMart — Multi-Seller E-Commerce Marketplace
- **Technology Stack**: Java 17, Java Servlets (JSP/JSTL), JDBC, HikariCP, H2 In-Memory DB, Apache Tomcat, BCrypt, SLF4J, JUnit 5.
- **Developer**: Solo Project (R2025 Specification)
- **Checkpoint Window**: Jul 27 – Oct 10, 2026
- **Status**: 100% Completed (Weeks 1 – 11)

---

## Slide 2: Problem Statement & Vision
- **Objective**: Build a robust, secure, production-grade e-commerce marketplace web application supporting multi-role interactions (Buyers, Sellers, Admin).
- **Key Requirements**:
  - Multi-seller inventory listing and seller analytics.
  - Multi-facet buyer search, category filtering, budget slider, dynamic sorting.
  - Real-time step-by-step Order Progress Tracker bar.
  - Role governance pipeline (`BUYER` ➔ `SELLER` ➔ `ADMIN`) and audit logging.
  - Embedded AI Shopping Assistant Chatbot for product & policy queries.

---

## Slide 3: System Architecture (Layered MVC)
```
Browser (HTML5 / Vanilla CSS / Modular JavaScript + AJAX Fetch)
                     │
                     ▼
  Filter Layer (AuthFilter, EncodingFilter, MdcLoggingFilter)
                     │
                     ▼
  Front Controller Layer (ProductServlet, OrderServlet, AdminServlet, ChatServlet)
                     │
                     ▼
  Service Layer (ProductService, OrderService, UserService, ChatService)
                     │
                     ▼
  DAO Layer (ProductDAO, OrderDAO, UserDAO, ReviewDAO, AuditLogDAO)
                     │
                     ▼
  HikariCP Connection Pool (Initialized at startup via DbContextListener)
                     │
                     ▼
  H2 Database Engine (Server/Memory mode with Flyway-style SQL migrations)
```

---

## Slide 4: Database Design & ER Schema
- **Normalised Relational Tables**:
  - `USERS` (id, name, email UNIQUE, password_hash, role ENUM)
  - `PRODUCTS` (id, seller_id FK, name, description, price DECIMAL(10,2), stock_qty, category, image_url)
  - `ORDERS` (id, buyer_id FK, status ENUM, total_amount DECIMAL(10,2))
  - `ORDER_ITEMS` (id, order_id FK, product_id FK, quantity, unit_price)
  - `REVIEWS` (id, product_id FK, user_id FK, rating INT, comment TEXT)
  - `AUDIT_LOGS` (id, admin_id FK, action, target_user_id FK, timestamp)
- **Data Integrity**: Foreign key indexing, monetary amounts as `DECIMAL(10,2)`, uniqueness constraints on `email`.

---

## Slide 5: Core Mandatory Features (F1 – F8)
- **F1 (Auth)**: BCrypt password hashing, session management, multi-role access control (`BUYER`, `SELLER`, `ADMIN`).
- **F2 (Seller Listings)**: Full CRUD item creation, price updates, stock management.
- **F3 (Browse & Search)**: Category filtering, search keywords, budget slider, dynamic sorting.
- **F4 (Cart System)**: Add/update/remove cart items with live running totals.
- **F5 (Mock Checkout)**: Instant transaction processing with stock auto-deduction.
- **F6 (Order Management)**: Dual-view order history for buyers and sellers.
- **F7 (Admin Panel)**: User role escalation, product listing moderation, audit logs.
- **F8 (Reviews & Ratings)**: Verified customer rating submission and review thread rendering.

---

## Slide 6: Optional & Advanced Features (O1 – O4)
- **O1**: Category multi-select & dynamic budget range slider ($0 – $500).
- **O2**: Step-by-Step 4-Stage Order Tracker Bar (**PLACED ➔ PROCESSING ➔ SHIPPED ➔ DELIVERED**).
- **O3**: Seller Analytics Dashboard (`totalRevenue`, `totalOrders`, `lowStockCount`).
- **O4**: AI Shopping Assistant Chatbot (`/api/chat` + floating glassmorphism widget with session rate limiting).

---

## Slide 7: Security Audit & Protection Standards
- **SQL Injection Defense**: 100% of database interactions enforce `PreparedStatement` try-with-resources. String concatenation in SQL is prohibited.
- **XSS Defense**: Output escaping via JSTL `<c:out>` and `escapeHtml()` client sanitization.
- **Password Protection**: BCrypt hashing (`org.mindrot.jbcrypt`). Plaintext password logging is blocked.
- **Security HTTP Headers**: Global filter injecting `X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, and `X-XSS-Protection`.

---

## Slide 8: Automated Test Coverage & Quality
- **Framework**: JUnit 5 + Mockito against an embedded H2 test database (`jdbc:h2:mem:test`).
- **Test Suite Results**: **23 / 23 Automated Tests PASSING** (0 Failures, 0 Errors).
- **Coverage Areas**:
  - `AuditLogServiceTest`: Administrative logging & action tracking.
  - `ChatServiceTest`: AI chatbot domain query matching, character caps, and rate limits.
  - `OrderServiceTest`: Cart checkout & stock isolation logic.
  - `ProductServiceTest`: Catalog search & price filtering.
  - `SecurityAuditTest`: Prepared statement query validation & password hashing.
  - `UserServiceTest` & `UserDAOTest`: Authentication & role transitions.

---

## Slide 9: Architectural Patterns Applied
- **Front Controller Pattern**: Centralized URL dispatching via Servlets.
- **Data Access Object (DAO) Pattern**: Decoupled database logic from business rules.
- **Factory & Singleton Patterns**: HikariCP DataSource lifecycle managed by single `ServletContextListener`.
- **Strategy Pattern**: Swappable AI providers (`MockChatProvider` vs `GeminiChatProvider`).

---

## Slide 10: Conclusion & Deployment Status
- **Local Dev Server**: Active at `http://localhost:9090/lathikamart/`
- **Production Package**: Executable WAR generated at `target/lathikamart.war`
- **Lab Deliverables**: `README.md`, `RETRO.md`, `CHANGELOG.md`, `.env.example`, `CONTRIBUTING.md`, `DEMO_SCRIPT.md`.
- **Status**: 100% Complete & Ready for Final Review Evaluation!

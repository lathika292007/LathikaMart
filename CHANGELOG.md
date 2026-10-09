# 📜 LathikaMart — Release Changelog (CHANGELOG.md)

All notable changes to the LathikaMart project are documented in this file.

---

## [1.1.0] - 2026-09-20 (AI Assistant & Polish Release - Weeks 9 & 10 Complete)
### Added
- Rule-based AI Shopping Assistant Chatbot (`/api/chat` endpoint + floating chat widget).
- Comprehensive `ChatServiceTest` unit test suite covering rate limiting, FAQ domain query matching, and length caps (23 total automated tests passing).
- Expanded seed product catalog to 12 rich items across 6 categories with Unsplash high-res imagery.
- Refactored Seller Dashboard client script into `js/seller.js`.

---

## [1.0.0] - 2026-09-20 (Full Build & Security Release)
### Added
- Interactive 4-step Order Progress Tracker bar (**PLACED ➔ PROCESSING ➔ SHIPPED ➔ DELIVERED**).
- Multi-facet product search, category filtering, budget slider, and dynamic sorting (`price_asc`, `price_desc`, `newest`).
- Global Security HTTP headers filter (`X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, `X-XSS-Protection`).
- Automated JUnit 5 + Mockito test suite (18/18 tests passing).
- Production WAR build packaging (`target/lathikamart.war`).

---

## [0.2.0] - 2026-09-10 (Seller & Admin Release)
### Added
- Seller Analytics Dashboard (`totalRevenue`, `totalOrders`, `lowStockCount`).
- Admin Panel with dynamic user role escalation (`BUYER` ➔ `SELLER` ➔ `ADMIN`).
- Product moderation and Audit Logging (`AuditLogDAO` & `AuditLogService`).

---

## [0.1.0] - 2026-08-10 (MVP Release)
### Added
- Initial HikariCP connection pool setup & H2 in-memory database.
- BCrypt password hashing & Servlet session authentication.
- Core Buyer workflow: Product catalog, cart management, mock checkout.

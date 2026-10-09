# 🔄 LathikaMart — Sprint Retrospectives (RETRO.md)

*Required by Section 16 of Project Execution Specification*

---

## Sprint 1: Core Setup & Authentication (Jul 27 – Aug 2)
- **What Worked**: Successfully configured HikariCP connection pooling and Flyway-style SQL migration listener (`DbContextListener`). BCrypt password hashing integrated.
- **What Didn't**: Initial session persistence required adjustments for role switching.
- **Sprint Change**: Enforced role validation at `AuthFilter` level before dispatching requests.

---

## Sprint 2: Product Catalog & Cart Checkout (Aug 3 – Aug 16)
- **What Worked**: Built complete buyer shopping cart flow with real-time running subtotal calculations and stock auto-deduction.
- **What Didn't**: Concurrent stock checks needed strict database transaction isolation.
- **Sprint Change**: Wrapped order placement logic inside DAO-level database transactions (`conn.setAutoCommit(false)`).

---

## Sprint 3: Seller Analytics & Admin Moderation (Aug 17 – Aug 30)
- **What Worked**: Implemented Seller Analytics Dashboard (`totalRevenue`, `totalOrders`, `lowStockCount`) and Admin user role escalation.
- **What Didn't**: Admin listing approval status logic required dedicated DAO updates.
- **Sprint Change**: Added `AuditLogDAO` to track administrative security actions.

---

## Sprint 4: Search, Filters & Order Progress Tracker (Aug 31 – Sep 13)
- **What Worked**: Added category multi-select, budget slider, dynamic sorting, and step-by-step 4-stage Order Tracker bar (**PLACED ➔ PROCESSING ➔ SHIPPED ➔ DELIVERED**).
- **What Didn't**: JSP EL parser conflicted with inline JS template literals containing `${...}`.
- **Sprint Change**: Refactored client-side script logic into dedicated modular JS files (`js/app.js`, `js/orders.js`, `js/admin.js`, `js/seller.js`).

---

## Sprint 5: Security Audit, Test Suite & AI Assistant (Sep 14 – Sep 20)
- **What Worked**: Completed 18/18 unit & security tests (`SecurityAuditTest`, `AuditLogServiceTest`, `OrderServiceTest`, `ProductServiceTest`, `UserServiceTest`, `UserDAOTest`). Built rule-based AI Shopping Assistant (`ChatServlet` + `js/chat.js`).
- **What Didn't**: Header injection required global filter level enforcement.
- **Sprint Change**: Added security headers (`X-Frame-Options: DENY`, `X-Content-Type-Options: nosniff`, `X-XSS-Protection`) in `EncodingFilter`.

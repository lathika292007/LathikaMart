# 🎬 LathikaMart — Final Review Live Demo Script & Rehearsal Guide
*Anna University R2025 Specification — Step-by-Step Presentation Script*

---

## ⏱️ Demo Time Allocation (Total: 8 Minutes)

| Time | Segment | Focus Area | Credentials / Action |
| :-: | :--- | :--- | :--- |
| **0:00 - 1:00** | **System Architecture & Build** | Tech stack, Layered MVC architecture, `mvn clean verify` test suite execution | Terminal (`mvn clean verify`) |
| **1:00 - 3:30** | **Buyer Experience & Order Tracker** | Browsing catalog, multi-facet price slider, cart checkout, step-by-step order tracking bar | `john.buyer@gmail.com` / `password123` |
| **3:30 - 5:00** | **Seller Hub & Inventory Manager** | Revenue analytics, low-stock warnings, creating & updating product listings | `seller.tech@lathikamart.com` / `password123` |
| **5:00 - 6:30** | **Admin Panel & Governance** | Role escalation (`BUYER` ➔ `SELLER` ➔ `ADMIN`), product moderation, audit logging | `admin@lathikamart.com` / `password123` |
| **6:30 - 7:30** | **AI Assistant Chatbot** | Storefront chatbot widget, FAQ query matching, session rate limiting guardrail | Storefront Chat Widget |
| **7:30 - 8:00** | **Wrap Up & Q&A** | WAR file generation, deployment readiness summary | Presentation Summary |

---

## 📝 Detailed Step-by-Step Rehearsal Script

### Step 1: Introduction & Architecture (1 Minute)
> *"Respected Evaluators, today I am presenting **LathikaMart**, a multi-seller e-commerce marketplace web application built following the Anna University R2025 specification using Java Servlets, HikariCP, H2 Database, and Apache Tomcat."*

- **Show Terminal**: Run `mvn -B clean verify` live.
- **Explain**: *"As you can see, our automated test suite executes 23 unit and security tests against an embedded H2 database with zero failures, generating our production WAR package at `target/lathikamart.war`."*

---

### Step 2: Buyer Journey Demo (2.5 Minutes)
- **Action**: Open `http://localhost:9090/lathikamart/` and log in as `john.buyer@gmail.com` / `password123`.
- **Explain**: *"I am logging in as a Buyer. Here is our storefront featuring multi-facet search."*
- **Demonstrate**:
  1. Filter products by category (select *Books* or *Electronics*).
  2. Drag the **Price Range Slider** to filter products under $50.
  3. Change dynamic sorting to **Price: Low to High**.
  4. Add a product to the shopping cart, open `cart.jsp`, adjust quantity, and click **Place Order (Mock Payment)**.
  5. Navigate to **My Orders** and click **Track Order**.
- **Highlight**: *"Here is our interactive 4-step Order Progress Tracker bar (**PLACED ➔ PROCESSING ➔ SHIPPED ➔ DELIVERED**). As order status changes, the active stage highlights dynamically with a status badge."*

---

### Step 3: Seller Hub Demo (1.5 Minutes)
- **Action**: Log in as `seller.tech@lathikamart.com` / `password123`.
- **Explain**: *"Now I am logging in as a Seller to access the Seller Dashboard."*
- **Demonstrate**:
  1. Show sales analytics cards (*Total Revenue*, *Total Orders*, *Active Listings*, *Low Stock Alerts*).
  2. Click **+ Create New Product Listing**, enter details, and save.
  3. Update stock quantity or price on an existing item.
- **Highlight**: *"All seller listing logic is modularized into clean external JavaScript files to guarantee security and performance."*

---

### Step 4: Admin Governance Demo (1.5 Minutes)
- **Action**: Log in as `admin@lathikamart.com` / `password123`.
- **Explain**: *"Logging in as System Administrator gives full governance over users, listings, and security audit logs."*
- **Demonstrate**:
  1. Open Admin Panel.
  2. Promote a `BUYER` user account to `SELLER` or `ADMIN`.
  3. Moderate newly submitted seller products (Approve / Reject).
  4. Review the **Audit Trail Table** showing logged administrative actions.

---

### Step 5: AI Assistant Chatbot Demo (1 Minute)
- **Action**: Click the floating chatbot widget at the bottom-right of the screen.
- **Explain**: *"LathikaMart includes an embedded AI Shopping Assistant Chatbot that uses a swappable provider interface."*
- **Demonstrate**:
  1. Ask: *"What is your return policy?"* -> Receive instant policy response.
  2. Ask: *"Recommend headphones"* -> Receive product recommendation response.
  3. Rapidly click send to demonstrate the **10 messages/minute session rate limiting guardrail**.

---

### Step 6: Q&A & Wrap Up (30 Seconds)
> *"In conclusion, LathikaMart fulfills all mandatory F1-F8 requirements, optional O1-O4 features, security standards, and automated test coverage across all 11 milestone weeks. Thank you!"*

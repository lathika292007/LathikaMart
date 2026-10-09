# 🤝 Contributing to LathikaMart

Thank you for contributing to **LathikaMart**! Follow these instructions to get your development environment set up and run tests.

---

## 💻 Local Setup Instructions

### Prerequisites
- **JDK 17** or higher (`java -version`)
- **Apache Maven 3.8+** (`mvn -version`)
- **Git** (`git -version`)

---

## 🛠️ Step-by-Step Quickstart

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/LathikaMart.git
   cd LathikaMart
   ```

2. **Run Unit Tests**:
   ```bash
   mvn test
   ```

3. **Start Local Development Server**:
   ```bash
   mvn jetty:run
   ```
   *Open your browser to [http://localhost:9090/lathikamart/](http://localhost:9090/lathikamart/)*.

4. **Package Production WAR**:
   ```bash
   mvn clean verify
   ```
   *Artifact generated at `target/lathikamart.war`*.

---

## 🧪 Coding & Security Standards
- **Parameterized SQL**: All database queries must use `PreparedStatement` try-with-resources. String-concatenated SQL queries are strictly prohibited.
- **Password Security**: Passwords must be hashed using BCrypt (`jBCrypt`). Plaintext password logging is forbidden.
- **API Response Envelope**: All API endpoints under `/api/v1/` must return JSON envelopes in the format `{ "success": true, "data": ..., "error": null }`.
- **Client Script Modularization**: Place client-side JavaScript inside `src/main/webapp/js/` files rather than inline JSP script tags to avoid JSP Expression Language conflicts.

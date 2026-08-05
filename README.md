# 💰 Expense Tracker

A full-stack personal finance management application built with **Spring Boot**, **MySQL**, and vanilla **HTML/CSS/JavaScript** — no frontend framework. Users can track accounts, categorize transactions, set monthly budgets, visualize spending analytics, and bulk-import transactions via CSV.

Built as a resume project to demonstrate backend architecture, security, and full-stack integration skills for software engineering internships.

---

## 🚀 Features

- **JWT Authentication** — secure registration/login with BCrypt password hashing and stateless token-based auth
- **Multi-Account Management** — track balances across multiple accounts (bank, cash, card)
- **Category-based Transactions** — income/expense tracking with automatic account balance updates
- **Server-side Filtering & Pagination** — query transactions by category and date range without loading the full dataset
- **Budget Tracking** — set monthly limits per category with live spend calculation (aggregated from real transaction data, not manually updated)
- **Analytics Dashboard** — visual spending breakdown (pie chart) and monthly trends (line chart) via Chart.js
- **CSV Bulk Import** — upload bank statement exports; invalid rows are skipped individually with detailed error reporting rather than failing the entire import
- **Centralized Error Handling** — consistent, clean JSON error responses across the entire API instead of raw stack traces

---

## 🏗️ Architecture

**Backend:** Layered architecture — `Controller → Service → Repository → Entity`, with DTOs controlling exactly what data enters and leaves the API (preventing internal fields like password hashes from ever being exposed in responses).

```
Browser (HTML/CSS/JS)
       │  fetch() + JWT Bearer token
       ▼
Spring Boot REST API
   ├── Controller layer   — HTTP routing, request/response shape
   ├── Service layer      — business logic (balance updates, budget calculations)
   ├── Repository layer   — Spring Data JPA + custom JPQL queries
   ├── Security layer     — Spring Security + JWT filter chain
   └── DTOs               — request validation & response shaping
       ▼
MySQL Database
```

### Key design decisions

- **JWT over sessions** — stateless authentication scales better and matches how most production REST APIs authenticate.
- **DTOs for every request/response** — entities are never returned directly from controllers, preventing accidental data leakage (e.g., password hashes, internal relationship objects).
- **Business logic lives in the Service layer**, not the Controller — e.g., creating a transaction automatically recalculates the linked account's balance in the same operation, keeping data consistent.
- **Partial-failure CSV import** — a single malformed row doesn't abort the whole batch; each row is processed independently and errors are collected and reported back to the user.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21, Spring Boot, Spring Security, Spring Data JPA (Hibernate) |
| Database | MySQL |
| Auth | JWT (jjwt), BCrypt |
| Frontend | HTML5, CSS3, Vanilla JavaScript (Fetch API) |
| Charts | Chart.js |
| Build Tool | Maven |
| Testing Tool | Postman |

---

## 📂 Project Structure

```
expense-tracker/
├── src/main/java/com/shreyas/expensetracker/
│   ├── config/         # Security & CORS configuration
│   ├── controller/     # REST endpoints
│   ├── service/        # Business logic
│   ├── repository/     # Spring Data JPA interfaces + custom queries
│   ├── entity/          # JPA entities
│   ├── dto/              # Request/response DTOs
│   ├── security/          # JWT generation & validation filter
│   └── exception/          # Global exception handling
├── src/main/resources/
│   ├── application.properties          # local config (gitignored)
│   └── application-example.properties  # template for setup
└── frontend/
    ├── *.html
    ├── css/style.css
    └── js/                # api.js (shared fetch wrapper) + per-page logic
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 21 (JDK)
- MySQL Server
- Maven (bundled via `mvnw`)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/shreyas-18-coder/expense-tracker.git
   cd expense-tracker
   ```

2. **Create the database**
   ```sql
   CREATE DATABASE expense_tracker_db;
   ```

3. **Configure your local environment**
   Copy the example config and fill in your own MySQL credentials:
   ```bash
   cp src/main/resources/application-example.properties src/main/resources/application.properties
   ```
   Edit `application.properties` with your MySQL username/password.

4. **Run the backend**
   ```bash
   ./mvnw spring-boot:run
   ```
   The API starts on `http://localhost:8080`.

5. **Run the frontend**
   Open `frontend/login.html` in a browser (e.g., via IntelliJ's "Open in Browser" or any static file server).

---

## 📡 API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Create a new user |
| POST | `/api/auth/login` | Authenticate and receive a JWT |
| GET/POST | `/api/accounts` | List / create accounts |
| GET/POST | `/api/categories` | List / create categories |
| GET/POST | `/api/transactions` | List (filterable + paginated) / create transactions |
| GET/POST | `/api/budgets` | List / create budgets, with live spend calculation |
| GET | `/api/analytics/summary` | Monthly income/expense/net summary |
| GET | `/api/analytics/category-breakdown` | Spending grouped by category |
| GET | `/api/analytics/trend` | Monthly spending trend |
| POST | `/api/import/csv` | Bulk import transactions from a CSV file |

All endpoints except `/api/auth/**` require a `Authorization: Bearer <token>` header.

---

## 🔒 Security Notes

- Passwords are hashed with BCrypt before storage — never stored or logged in plaintext.
- All protected endpoints require a valid JWT, verified on every request via a custom Spring Security filter.
- `application.properties` (containing real database credentials) is excluded from version control via `.gitignore`; only a placeholder template is committed.

---

## 🔮 Future Improvements

- Duplicate-transaction detection during CSV import
- Edit/delete functionality for transactions, accounts, and budgets
- One-budget-per-category-per-month constraint
- Swagger/OpenAPI documentation
- Automated test coverage (JUnit/Mockito)
- Deployment via Docker

---

## 👤 Author

**Shreyas** — 3rd-year CSE student
GitHub: [@shreyas-18-coder](https://github.com/shreyas-18-coder)

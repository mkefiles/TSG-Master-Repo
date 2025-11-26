# Project Overview

**Name:** Benefits Mini  
**Domain theme:** Lightweight health benefits & simple claims.  
**Goal:** Practice building an enterprise full‑stack app using React, Spring Boot, and Postgres. Business logic is intentionally thin.

## Core Use Cases (MVP)

1. **Member authentication** (email/password) and session handling.  
2. **View plans** (list + details).  
3. **Enroll a member** into a plan (one active enrollment at a time).  
4. **Submit a simple claim** (date of service, provider name, amount, description).  
5. **Track claim status** (Submitted → In Review → Approved|Denied).  
6. **Admin**: review & update claim status.

## High‑Level Architecture

- **Frontend:** React, React Router, Tailwind (or your choice) for styling. **NICETOHAVE:** use typescript instead of javascript.
- **Backend:** Spring Boot, Spring Web, Spring Data JPA, Spring Security (JWT), springdoc‑openapi for Swagger UI.  **NICETOHAVE:** use liquibase for data model migrations. Start getting familiar with unit testing with junit + mockito. Already familiar? Check out testNG. If you have a handle on ALL of these and want to look ahead, read about GraphQL.
- **Database:** Postgres.  
- **Local Dev:** **NICETOHAVE:** Docker Compose your postgres database. Dockerize your app. (A dockerfile in your frontend and backend.)
- **CI:** **NICETOHAVE:** GitHub Actions running unit + integration tests (Testcontainers) and frontend lint/build.  
- **Docs:** OpenAPI JSON/YAML auto‑generated; README with run scripts.

## Data Model (MVP)

**users** — app sign‑in accounts  
**members** — profile aligned to a person who consumes benefits (1:1 with user for MVP)  
**plans** — benefit plans (e.g., Basic, Plus) with simple attributes  
**enrollments** — member ↔ plan with effective dates  
**claims** — submitted claims tied to an enrollment

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    role TEXT NOT NULL CHECK (role IN ('MEMBER','ADMIN')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE members (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL REFERENCES users(id),
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    dob DATE NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE plans (
    id UUID PRIMARY KEY,
    code TEXT UNIQUE NOT NULL,
    name TEXT NOT NULL,
    premium_monthly NUMERIC(10,2) NOT NULL,
    deductible NUMERIC(10,2) NOT NULL,
    coinsurance_percent INT NOT NULL CHECK (coinsurance_percent BETWEEN 0 AND 100),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE enrollments (
    id UUID PRIMARY KEY,
    member_id UUID NOT NULL REFERENCES members(id),
    plan_id UUID NOT NULL REFERENCES plans(id),
    effective_start DATE NOT NULL,
    effective_end DATE
);

CREATE TABLE claims (
    id UUID PRIMARY KEY,
    enrollment_id UUID NOT NULL REFERENCES enrollments(id),
    dos DATE NOT NULL,
    provider_name TEXT NOT NULL,
    amount_cents INT NOT NULL,
    description TEXT,
    status TEXT NOT NULL CHECK (status IN ('SUBMITTED','IN_REVIEW','APPROVED','DENIED')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
```

## API Design (REST)
Base path: `/api/v1`

### Auth

- `POST /auth/register` — create MEMBER user + member profile.  
- `POST /auth/login` — returns JWT.  

### Plans

- `GET /plans` — list plans.  
- `GET /plans/{id}` — plan details.

### Enrollments (member‑scoped)

- `GET /me/enrollments/active` — current enrollment.  
- `POST /me/enrollments` — enroll in a plan (body: planId, effectiveStart). Ends any active enrollment.  

### Claims

- `GET /me/claims` — list my claims.  
- `POST /me/claims` — submit claim.  
- `GET /me/claims/{id}` — claim details.  

### Admin

- `GET /admin/claims?status=SUBMITTED` — queue.  
- `PATCH /admin/claims/{id}` — update status (IN_REVIEW → APPROVED|DENIED).  

**OpenAPI:** available at `/swagger-ui.html` and `/v3/api-docs`.

## Frontend Screens (MVP)

1. **Auth**: Register, Login.  
2. **Dashboard**: welcome banner + quick stats.  
3. **Plans**: list + detail; enroll CTA if not enrolled.  
4. **Enrollment**: show active enrollment card; history list.  
5. **Claims**: list + create (form); detail view with status chip.  
6. **Admin**: claims review table with status actions.

**UX Notes**

- Use protected routes (JWT in Authorization header).  
- Nice-to-have: Explore React Query for data fetching, caching, and optimistic updates on claim submit.  
- Use Tailwind components; optional UI kit.

## Security & Auth

- Spring Security stateless JWT.
- Password hashing with BCrypt or Argon2.  
- Roles: MEMBER vs ADMIN (method or endpoint security).  
- CORS: allow dev server origin.
- **NICETOHAVE:** OIDC login (e.g., Okta) behind a feature flag.

## Wireframes

### 1) Login

```
+-------------------------------------------------------+
| Benefits Mini                                         |
|-------------------------------------------------------|
|                       LOGIN                            |
|                                                       |
|  Email:    [_______________________________]          |
|  Password: [_______________________________] (••••)   |
|                                                       |
|               [  Sign In  ]   [  Register  ]          |
|                                                       |
|  Forgot password?                                     |
+-------------------------------------------------------+
```

### 2) Register

```
+-------------------------------------------------------+
| Benefits Mini                                         |
|-------------------------------------------------------|
|                     REGISTER                           |
|  First name: [_____________]  Last name: [__________]  |
|  DOB:        [MM/DD/YYYY]                              |
|  Email:      [_______________________________]         |
|  Password:   [_______________________________]         |
|  Confirm:    [_______________________________]         |
|                                                       |
|                 [  Create Account  ]                  |
|                                                       |
|  Have an account? [Login]                             |
+-------------------------------------------------------+
```

### 3) Member Dashboard

```
+--------------------------------------------------------------------------------+
| Benefits Mini                                  [Me ▾]  [Logout]                |
|--------------------------------------------------------------------------------|
| (Left Nav)  Dashboard | Plans | Enrollment | Claims                              |
|--------------------------------------------------------------------------------|
| Welcome, <FirstName>                                                              |
|--------------------------------------------------------------------------------|
| Cards:                                                                           |
| [ Active Plan ]      [ Claims Summary ]      [ Quick Actions ]                   |
| | Plan: PLUS   |     | Submitted: 2 |        | [ View Plans ] [ Submit Claim ]  |
| | Deductible $ |     | Approved:  1 |        | [ My Claims ]                    |
|--------------------------------------------------------------------------------|
| Recent Claims (table)                                                             |
| DOS        | Provider           | Amount | Status      | [ View ]               |
|------------+--------------------+--------+-------------+------------------------|
| 09/10/25   | River Clinic       | $120   | SUBMITTED   | [ ▸ ]                  |
| 09/02/25   | North Dental       | $75    | APPROVED    | [ ▸ ]                  |
+--------------------------------------------------------------------------------+
```

### 4) Plans List

```
+---------------------------------------------------------------------+
| Benefits Mini                              [Me ▾] [Logout]          |
|---------------------------------------------------------------------|
| Plans                                                                 |
| [ Search: __________________ ]   Sort: ⟂ Name ▾  Filter: ⟂           |
|---------------------------------------------------------------------|
| [ Plan Card ]   [ Plan Card ]   [ Plan Card ]                         |
| +-------------+ +-------------+ +-------------+                       |
| | Code: BASIC| | Code: PLUS  | | Code: PREM  |                       |
| | $200/mo    | | $350/mo     | | $500/mo     |                       |
| | Ded: 3000  | | Ded: 1500   | | Ded: 500    |                       |
| | Coins: 30% | | Coins: 20%  | | Coins: 10%  |                       |
| | [ View ]   | | [ View ]    | | [ View ]    |                       |
| +-------------+ +-------------+ +-------------+                       |
+---------------------------------------------------------------------+
```

### 5) Plan Detail

```
+---------------------------------------------------------------+
| Benefits Mini                                  [← Back]       |
|---------------------------------------------------------------|
| Plan: PLUS                                                    |
| Premium: $350/mo   Deductible: $1500   Coinsurance: 20%       |
|                                                             |
| [ Enroll in this Plan ]   (disabled if already active)        |
|                                                             |
| Key Details                                                  |
| • Office visit: N/A (out of scope)                           |
| • Notes: Sample details...                                   |
+---------------------------------------------------------------+
```

### 6) Enrollment (Active + History)

```
+------------------------------------------------------------------+
| Benefits Mini                                                     |
|------------------------------------------------------------------|
| Enrollment                                                        |
| [ Active Enrollment ]                                            |
|  Plan: PLUS     Effective: 09/01/2025 – (current)                |
|  [ Change Plan ] → navigates to Plans                            |
|------------------------------------------------------------------|
| History                                                           |
| Start       | End         | Plan |                               |
|-------------+-------------+------+-------------------------------|
| 06/01/2025  | 08/31/2025  | BASIC|                               |
+------------------------------------------------------------------+
```

### 7) My Claims (List)

```
+--------------------------------------------------------------------------------+
| Benefits Mini                                                                   |
|--------------------------------------------------------------------------------|
| Claims                                                    [ Submit Claim ]      |
| Filter: Status ⟂ All  | Search: _________________________                       |
|--------------------------------------------------------------------------------|
| DOS       | Provider            | Amount   | Status      | Actions              |
|-----------+---------------------+----------+-------------+---------------------|
| 09/10/25  | River Clinic        | $120.00  | SUBMITTED   | [ View ]            |
| 09/02/25  | North Dental        | $75.00   | APPROVED    | [ View ]            |
|--------------------------------------------------------------------------------|
| Pagination:  « Prev   1  2  3   Next »                                         |
+--------------------------------------------------------------------------------+
```

### 8) Submit Claim (Form)

```
+---------------------------------------------------------------+
| Benefits Mini                                                 |
|---------------------------------------------------------------|
| Submit Claim                                                  |
| Enrollment: [ Active Enrollment ▾ ]                           |
| Date of Service: [MM/DD/YYYY]                                 |
| Provider Name:   [___________________________]                |
| Amount (USD):    [________]                                   |
| Description:     [_______________________________]            |
| Attach Receipt:  [ Choose File ] (stretch)                    |
|---------------------------------------------------------------|
| [  Submit  ]   [  Cancel  ]                                   |
| Success / Error banner appears here                           |
+---------------------------------------------------------------+
```

### 9) Claim Detail

```
+--------------------------------------------------------------------+
| Benefits Mini                                                       |
|--------------------------------------------------------------------|
| Claim #C-2025-001                                  Status: SUBMITTED |
|--------------------------------------------------------------------|
| Summary                                                             |
| DOS: 09/10/2025      Provider: River Clinic                         |
| Amount: $120.00      Enrollment: PLUS (effective 09/01/2025)        |
| Description: Office visit                                           |
|                                                                     |
| Timeline                                                            |
| [Submitted] → [In Review] → [Approved/Denied]                       |
|                                                                     |
| Actions                                                             |
| [ Back to Claims ]                                                  |
+--------------------------------------------------------------------+
```

### 10) Admin — Claims Review Queue

```
+--------------------------------------------------------------------------------+
| Benefits Mini Admin                                   [admin@…] [Logout]       |
|--------------------------------------------------------------------------------|
| Claims Review                                                                |
| Filter: Status ⟂ SUBMITTED | IN_REVIEW | APPROVED | DENIED                    |
|--------------------------------------------------------------------------------|
| DOS       | Member              | Provider          | Amount  | Status  | View |
|-----------+---------------------+-------------------+---------+---------+------|
| 09/10/25  | Jane Doe            | River Clinic      | $120.00 | SUBMITTED| [ ▸ ]|
| 09/09/25  | John Smith          | Midtown Ortho     | $240.00 | IN_REVIEW|[ ▸ ]|
|--------------------------------------------------------------------------------|
| Detail Drawer (on select)                                                     |
| +-----------------------------------+--------------------------------------+   |
| | Claim Summary                      | Actions                              |   |
| | DOS: 09/10/25                      | [ Move to In Review ]                |   |
| | Provider: River Clinic             | [ Approve ]  [ Deny ] (with reason)  |   |
| | Amount: $120.00                    |                                      |   |
| | Description: Office visit          |                                      |   |
| +-----------------------------------+--------------------------------------+   |
+--------------------------------------------------------------------------------+
```

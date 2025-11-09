/**
* Create all applicable tables
*/
-- users - app sign-in accounts
CREATE TABLE
    users (
        id UUID PRIMARY KEY,
        email TEXT UNIQUE NOT NULL,
        password_hash TEXT NOT NULL,
        role TEXT NOT NULL CHECK (role IN ('MEMBER', 'ADMIN')),
        created_at TIMESTAMPTZ NOT NULL DEFAULT now ()
);

-- members - profile aligned to a person who consumes benefits (1:1 with user for MVP)
CREATE TABLE
    members (
        id UUID PRIMARY KEY,
        user_id UUID NOT NULL REFERENCES users (id),
        first_name TEXT NOT NULL,
        last_name TEXT NOT NULL,
        dob DATE NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT now ()
);

-- plans - benefit plans (e.g., Basic, Plus, etc) with simple attributes
CREATE TABLE
    plans (
        id UUID PRIMARY KEY,
        code TEXT UNIQUE NOT NULL,
        name TEXT NOT NULL,
        premium_monthly NUMERIC(10, 2) NOT NULL,
        deductible NUMERIC(10, 2) NOT NULL,
        coinsurance_percent INT NOT NULL CHECK (coinsurance_percent BETWEEN 0 AND 100),
        created_at TIMESTAMPTZ NOT NULL DEFAULT now ()
);

-- enrollments - member <--> plan (with effective dates)
CREATE TABLE
    enrollments (
        id UUID PRIMARY KEY,
        member_id UUID NOT NULL REFERENCES members (id),
        plan_id UUID NOT NULL REFERENCES plans (id),
        effective_start DATE NOT NULL,
        effective_end DATE
);

-- claims - submitted claims tied to an enrollment
CREATE TABLE
    claims (
        id UUID PRIMARY KEY,
        enrollment_id UUID NOT NULL REFERENCES enrollments (id),
        dos DATE NOT NULL,
        provider_name TEXT NOT NULL,
        amount_cents INT NOT NULL,
        description TEXT,
        status TEXT NOT NULL CHECK (
        status IN ('SUBMITTED', 'IN_REVIEW', 'APPROVED', 'DENIED')),
        created_at TIMESTAMPTZ NOT NULL DEFAULT now (),
        updated_at TIMESTAMPTZ NOT NULL DEFAULT now ()
);
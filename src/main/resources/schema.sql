CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'CLIENT', -- 'Admin', 'Client', 'Agent'
    locked BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    complement VARCHAR(100),
    zip_code VARCHAR(10) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    main_address BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS phones (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phone_number VARCHAR(100) NOT NULL,
    main_phone BOOLEAN NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(50) NOT NULL,
    "year" INTEGER NOT NULL,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS insurance_policies (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    policy_number VARCHAR(50) UNIQUE NOT NULL,
    coverage_type VARCHAR(50) NOT NULL, -- e.g., 'Liability', 'Comprehensive'
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    annual_premium DECIMAL(10, 2) NOT NULL,
    vehicle_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS claims (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    claim_number VARCHAR(50) UNIQUE NOT NULL,
    accident_date DATE NOT NULL,
    description TEXT,
    amount_claimed DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL, -- 'Pending', 'Approved', 'Rejected'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    policy_id BIGINT NOT NULL,
    FOREIGN KEY (policy_id) REFERENCES insurance_policies(id)
);
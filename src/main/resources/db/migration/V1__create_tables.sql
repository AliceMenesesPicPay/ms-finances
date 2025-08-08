CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(255),
    digit VARCHAR(255),
    agency VARCHAR(255),
    balance NUMERIC(19,2),
    account_type VARCHAR(50),
    status VARCHAR(25),
    customer_id BIGINT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    from_account_id BIGINT REFERENCES accounts(id),
    to_account_id BIGINT REFERENCES accounts(id),
    amount NUMERIC(19,2),
    transaction_type VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE financial_transactions (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255),
    amount NUMERIC(19,2),
    balance NUMERIC(19,2),
    financial_transaction_type VARCHAR(50),
    account_id INTEGER REFERENCES accounts(id),
    transaction_id INTEGER REFERENCES transactions(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
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

CREATE TABLE financial_transactions (
    id BIGSERIAL PRIMARY KEY,
    financial_transaction_type VARCHAR(50),
    amount NUMERIC(19,2),
    account_id BIGINT REFERENCES accounts(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    from_account_id BIGINT REFERENCES accounts(id),
    to_account_id BIGINT REFERENCES accounts(id),
    amount NUMERIC(19,2),
    transaction_type VARCHAR(50),
    financial_transaction_origin_id BIGINT REFERENCES financial_transactions(id),
    financial_transaction_destination_id BIGINT REFERENCES financial_transactions(id),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);
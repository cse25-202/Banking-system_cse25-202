-- Banking System schema (aligned with the class diagram)
CREATE TABLE IF NOT EXISTS customers (
    customer_id VARCHAR(20) PRIMARY KEY,
    customer_type VARCHAR(12) NOT NULL,
    first_name VARCHAR(50),
    surname VARCHAR(50),
    company_name VARCHAR(80),
    registration_name VARCHAR(80),
    address VARCHAR(120),
    phone VARCHAR(30),
    email VARCHAR(80),
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL
    );

CREATE TABLE IF NOT EXISTS accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    account_type VARCHAR(12) NOT NULL,
    balance DECIMAL(14,2) NOT NULL,
    branch VARCHAR(60),
    employer_name VARCHAR(80),
    employer_address VARCHAR(120),
    customer_id VARCHAR(20) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    transaction_type VARCHAR(60),
    amount DECIMAL(14,2),
    txn_timestamp TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
    );
-- Drop tables in reverse order of dependencies to avoid errors
DROP TABLE repayment CASCADE CONSTRAINTS;
DROP TABLE credit_payment CASCADE CONSTRAINTS;
DROP TABLE purchase CASCADE CONSTRAINTS;
DROP TABLE deposit CASCADE CONSTRAINTS;
DROP TABLE withdrawal CASCADE CONSTRAINTS;
DROP TABLE acc_transaction CASCADE CONSTRAINTS;

DROP TABLE debit_card CASCADE CONSTRAINTS;
DROP TABLE credit_card CASCADE CONSTRAINTS;

DROP TABLE asset CASCADE CONSTRAINTS;

DROP TABLE transfer CASCADE CONSTRAINTS;

DROP TABLE savings_acc CASCADE CONSTRAINTS;
DROP TABLE checking_acc CASCADE CONSTRAINTS;
DROP TABLE investment_acc CASCADE CONSTRAINTS;

DROP TABLE loan CASCADE CONSTRAINTS;

DROP TABLE vendor CASCADE CONSTRAINTS;
DROP TABLE atm CASCADE CONSTRAINTS;

DROP TABLE account CASCADE CONSTRAINTS;

DROP TABLE customer CASCADE CONSTRAINTS;

-- Create customer table
CREATE TABLE customer (
    customer_id NUMBER(6) GENERATED ALWAYS AS IDENTITY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) CHECK (REGEXP_LIKE(email, '^.+@.+\..+$')),
    phone_number VARCHAR(12) CHECK (REGEXP_LIKE(phone_number, '^\d{3}-\d{3}-\d{4}$')),
    PRIMARY KEY (customer_id)
);

-- Create account table
CREATE TABLE account (
    acc_id NUMBER(8) GENERATED ALWAYS AS IDENTITY,
    customer_id NUMBER(6) NOT NULL,
    ownership_type VARCHAR(50) CHECK (ownership_type IN ('joint', 'single')),
    balance NUMBER(15),
    pin NUMBER(4),
    PRIMARY KEY (acc_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create savings account table
CREATE TABLE savings_acc (
    acc_id NUMBER(8) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    ownership_type VARCHAR(50) CHECK (ownership_type IN ('joint', 'single')),
    balance NUMBER(15),
    pin NUMBER(4),
    min_balance NUMBER(15),
    interest_rate NUMBER(5, 2),
    penalty_fee NUMBER(15),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create checking account table
CREATE TABLE checking_acc (
    acc_id NUMBER(8) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    ownership_type VARCHAR(50) CHECK (ownership_type IN ('joint', 'single')),
    balance NUMBER(15),
    pin NUMBER(4),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create investment account table
CREATE TABLE investment_acc (
    acc_id NUMBER(8) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    ownership_type VARCHAR(50) CHECK (ownership_type IN ('joint', 'single')),
    balance NUMBER(15),
    pin NUMBER(4),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create asset table
CREATE TABLE asset (
    asset_id NUMBER(3) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    value NUMBER(15),
    FOREIGN KEY (acc_id) REFERENCES investment_acc(acc_id)
);

-- Create credit card table
CREATE TABLE credit_card (
    card_number NUMBER(16) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    credit_limit NUMBER(15),
    interest_rate NUMBER(5, 2),
    balance_due NUMBER(15),
    outstanding_balance NUMBER(15),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create debit card table
CREATE TABLE debit_card (
    card_number NUMBER(16) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    acc_id NUMBER(8) NOT NULL,
    balance NUMBER(15),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (acc_id) REFERENCES checking_acc(acc_id)
);

-- vendor 
CREATE TABLE vendor (
    vendor_id NUMBER(4),
    vendor_name CHAR(50),
    PRIMARY KEY (vendor_id)
);

CREATE TABLE atm (
    atm_id NUMBER(4),
    PRIMARY KEY (atm_id)
);

-- Create loan table
CREATE TABLE loan (
    loan_id NUMBER(5) PRIMARY KEY,
    customer_id NUMBER(6) NOT NULL,
    loan_type VARCHAR(50) CHECK (loan_type IN ('secure', 'unsecure')),
    amount NUMBER(15),
    interest_rate NUMBER(5, 2),
    monthly_payment NUMBER(15),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create transaction table
CREATE TABLE acc_transaction (
    transaction_id NUMBER(12) GENERATED ALWAYS AS IDENTITY,
    acc_id NUMBER(8) NOT NULL,
    amount NUMBER(15,2),
    time_of DATE,
    PRIMARY KEY (transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id)
);

-- trigger to validate accounts - make sure it is in either savings or checking
CREATE OR REPLACE TRIGGER validate_transaction_acc
BEFORE INSERT OR UPDATE ON acc_transaction
FOR EACH ROW
DECLARE
    is_valid_acc NUMBER := 0; -- variable to store validation 
BEGIN
    -- checks if acc_id exists in savings_acc or checking_acc!!
    SELECT COUNT(*)
    INTO is_valid_acc
    FROM (
        SELECT acc_id FROM savings_acc WHERE acc_id = :NEW.acc_id
        UNION ALL
        SELECT acc_id FROM checking_acc WHERE acc_id = :NEW.acc_id
    );

    -- if no matching acc_id is found, raise an error
    IF is_valid_acc = 0 THEN
        RAISE_APPLICATION_ERROR(-20001, 'acc_id must reference either savings_acc or checking_acc.');
    END IF;
END;
/

-- Update repayment table to reference acc_transaction
CREATE TABLE repayment (
    transaction_id NUMBER(12) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    loan_id NUMBER(5) NOT NULL,
    amount NUMBER(15,2),
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (loan_id) REFERENCES loan(loan_id)
);

-- Update credit_payment table to reference acc_transaction
CREATE TABLE credit_payment (
    transaction_id NUMBER(12) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    card_number NUMBER(16) NOT NULL,
    amount NUMBER(15,2),
    time_of DATE,
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (card_number) REFERENCES credit_card(card_number)
);

-- Update purchase table to reference acc_transaction
CREATE TABLE purchase (
    transaction_id NUMBER(12) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    vendor_id NUMBER(4) NOT NULL,
    amount NUMBER(15,2) NOT NULL,
    item_name CHAR(50) NOT NULL,
    time_of DATE,
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id)
);

-- Update deposit table to reference acc_transaction
CREATE TABLE deposit (
    transaction_id NUMBER(12) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    amount NUMBER(15,2),
    time_of DATE,
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id)
);

-- Update withdrawal table to reference acc_transaction
CREATE TABLE withdrawal (
    transaction_id NUMBER(12) NOT NULL,
    acc_id NUMBER(8) NOT NULL,
    card_number NUMBER(16) NOT NULL,
    atm_id NUMBER(4) NOT NULL,
    amount NUMBER(15,2),
    time_of DATE,
    PRIMARY KEY (transaction_id, atm_id),
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (card_number) REFERENCES debit_card(card_number),
    FOREIGN KEY (atm_id) REFERENCES atm(atm_id)
);

-- Create transfer table
CREATE TABLE transfer (
    transaction_id NUMBER(12) PRIMARY KEY,
    acc_id NUMBER(8) NOT NULL,
    destination_acc_id NUMBER(8) NOT NULL,
    amount NUMBER(15),
    time_of DATE,
    FOREIGN KEY (transaction_id) REFERENCES acc_transaction(transaction_id),
    FOREIGN KEY (acc_id) REFERENCES account(acc_id),
    FOREIGN KEY (destination_acc_id) REFERENCES account(acc_id),
    CONSTRAINT chk_transfer CHECK (acc_id != destination_acc_id)
);

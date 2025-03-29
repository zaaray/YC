-- Populate customer table (12 customers)
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('John', 'Doe', 'john.doe@example.com', '123-456-7890');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Jane', 'Smith', 'jane.smith@example.com', '234-567-8901');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Alice', 'Johnson', 'alice.johnson@example.com', '345-678-9012');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Bob', 'Brown', 'bob.brown@example.com', '456-789-0123');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Charlie', 'Davis', 'charlie.davis@example.com', '567-890-1234');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Diana', 'Evans', 'diana.evans@example.com', '678-901-2345');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Eve', 'Foster', 'eve.foster@example.com', '789-012-3456');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Frank', 'Green', 'frank.green@example.com', '890-123-4567');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Grace', 'Harris', 'grace.harris@example.com', '901-234-5678');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Hank', 'Irwin', 'hank.irwin@example.com', '012-345-6789');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Ivy', 'Jones', 'ivy.jones@example.com', '123-456-7800');
INSERT INTO customer (first_name, last_name, email, phone_number) VALUES ('Jack', 'King', 'jack.king@example.com', '234-567-8902');

-- Populate account table (14 accounts)
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (1, 'single', 1500.00, 1222); --1 
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (1, 'single', 1000.00, 2080);--2
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (2, 'joint', 2500.00, 1031);--3
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (2, 'single', 500.00, 2327);--4
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (3, 'single', 2000.00, 1789);--5
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (3, 'joint', 3000.00, 8921);--6
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (4, 'single', 4000.00, 6754);--7
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (5, 'joint', 5000.00, 8998);--8
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (7, 'single', 600.00, 3443);--9
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (8, 'joint', 1800.00, 4563);--10
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (9, 'single', 700.00, 2129);--11
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (10, 'single', 1200.00, 5380);--12
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (11, 'single', 900.00, 7935);--13
INSERT INTO account (customer_id, ownership_type, balance, pin) VALUES (12, 'joint', 300.00, 4672);--14

-- Populate savings_acc table
INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, pin, min_balance, interest_rate, penalty_fee) VALUES (1, 1, 'single', 1500.00, 1222,  500.00, 2.5, 20.00);
INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, pin, min_balance, interest_rate, penalty_fee) VALUES (3, 2, 'joint', 2500.00, 1031, 1000.00, 2.0, 25.00);
INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, pin, min_balance, interest_rate, penalty_fee) VALUES (6, 3, 'single', 2000.00, 8921, 1000.00, 2.0, 30.00);
INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, pin, min_balance, interest_rate, penalty_fee) VALUES (8, 5, 'joint', 5000.00, 8998, 2500.00, 2.5, 20.00);
INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, pin, min_balance, interest_rate, penalty_fee) VALUES (10, 6, 'single', 2500.00, 5380, 1200.00, 3.0, 15.00);

-- Populate checking_acc table
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (2, 1, 'single', 1000.00, 2080);
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (4, 3, 'joint', 3000.0, 2327);
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (5, 2, 'single', 500.00, 1789);
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (7, 4, 'single', 4000.00, 6754);
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (11, 10, 'single', 1200.00, 2129);
INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (12, 11, 'single', 92.10, 5380);

-- Populate investment_acc table
INSERT INTO investment_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (9, 8, 'joint', 1800.00, 3443);
INSERT INTO investment_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (13, 9, 'single', 700.00, 7935);
INSERT INTO investment_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (14, 12, 'joint', 300.00, 4672);

-- Populate asset table
INSERT INTO asset (asset_id, acc_id, value) VALUES (1, 9, 1000.00);
INSERT INTO asset (asset_id, acc_id, value) VALUES (2, 13, 500.00);
INSERT INTO asset (asset_id, acc_id, value) VALUES (3, 14, 300.00);

-- Populate vendor table
INSERT INTO vendor (vendor_id, vendor_name) VALUES (1, 'Shopright');
INSERT INTO vendor (vendor_id, vendor_name) VALUES (2, 'Amazon');
INSERT INTO vendor (vendor_id, vendor_name) VALUES (3, 'Cafe');
INSERT INTO vendor (vendor_id, vendor_name) VALUES (4, 'Diner');
INSERT INTO vendor (vendor_id, vendor_name) VALUES (5, 'Nails');

-- Populate atm table
INSERT INTO atm (atm_id) VALUES (1001);
INSERT INTO atm (atm_id) VALUES (1002);
INSERT INTO atm (atm_id) VALUES (1003);
INSERT INTO atm (atm_id) VALUES (1004);
INSERT INTO atm (atm_id) VALUES (1005);

-- Populate credit_card table
INSERT INTO credit_card (card_number, customer_id, credit_limit, interest_rate, balance_due, outstanding_balance) VALUES (1111222233334444, 1, 5000.00, 18.0, 100.00, 1000.00);
INSERT INTO credit_card (card_number, customer_id, credit_limit, interest_rate, balance_due, outstanding_balance) VALUES (2222333344445555, 2, 7000.00, 20.0, 200.00, 1500.00);
INSERT INTO credit_card (card_number, customer_id, credit_limit, interest_rate, balance_due, outstanding_balance) VALUES (3333444455556666, 4, 3000.00, 15.0, 50.00, 500.00);
INSERT INTO credit_card (card_number, customer_id, credit_limit, interest_rate, balance_due, outstanding_balance) VALUES (4444555566667777, 6, 10000.00, 22.0, 0.00, 8000.00);

-- Populate debit_card table
INSERT INTO debit_card (card_number, customer_id, acc_id, balance) VALUES (5555666677778888, 1, 2, 1000.00);
INSERT INTO debit_card (card_number, customer_id, acc_id, balance) VALUES (6666777788889999, 2, 5, 500.00);
INSERT INTO debit_card (card_number, customer_id, acc_id, balance) VALUES (7777888899990000, 4, 7, 4000.00);

-- Populate loan table
INSERT INTO loan (loan_id, customer_id, loan_type, amount, interest_rate, monthly_payment) VALUES (1, 1, 'secure', 100000.00, 3.5, 1200.00);
INSERT INTO loan (loan_id, customer_id, loan_type, amount, interest_rate, monthly_payment) VALUES (2, 4, 'unsecure', 15000.00, 5.0, 500.00);
INSERT INTO loan (loan_id, customer_id, loan_type, amount, interest_rate, monthly_payment) VALUES (3, 6, 'secure', 250000.00, 3.0, 2000.00);

-- Populate acc_transaction table (ensures transaction IDs exist for purchases, deposits, withdrawals, and transfers)
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (2, 50.00, SYSDATE - 3); -- transaction_id = 1
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (5, 6.23, SYSDATE - 4); -- transaction_id = 2
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (1, 1000.00, SYSDATE - 2); -- transaction_id = 3
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (3, 2000.00, SYSDATE - 3); -- transaction_id = 4
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (2, 500.00, SYSDATE - 1); -- transaction_id = 5
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (5, 200.00, SYSDATE - 2); -- transaction_id = 6
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (1, 100.00, SYSDATE - 1); -- transaction_id = 7
INSERT INTO acc_transaction (acc_id, amount, time_of) VALUES (4, 300.00, SYSDATE - 2); -- transaction_id = 8

-- Populate purchase table
INSERT INTO purchase (transaction_id, acc_id, vendor_id, amount, item_name, time_of) VALUES (1, 2, 1, 50.00, 'groceries', SYSDATE - 3);
INSERT INTO purchase (transaction_id, acc_id, vendor_id, amount, item_name, time_of) VALUES (2, 5, 3, 6.23, 'latte', SYSDATE - 4);

-- Populate deposit table
INSERT INTO deposit (transaction_id, acc_id, amount, time_of) VALUES (4, 1, 1000.00, SYSDATE - 3);
INSERT INTO deposit (transaction_id, acc_id, amount, time_of) VALUES (8, 3, 2000.00, SYSDATE - 4);

-- Populate withdrawal table
INSERT INTO withdrawal (transaction_id, acc_id, card_number, atm_id, amount, time_of) VALUES (5, 2, 5555666677778888, 1001, 500.00, SYSDATE - 1);
INSERT INTO withdrawal (transaction_id, acc_id, card_number, atm_id, amount, time_of) VALUES (6, 5, 6666777788889999, 1002, 200.00, SYSDATE - 2);

-- Populate transfer table
INSERT INTO transfer (transaction_id, acc_id, destination_acc_id, amount, time_of) VALUES (7, 1, 3, 100.00, SYSDATE - 1);
INSERT INTO transfer (transaction_id, acc_id, destination_acc_id, amount, time_of) VALUES (8, 4, 5, 300.00, SYSDATE - 2);

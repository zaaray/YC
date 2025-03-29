To compile, you can paste these commands into the terminal:

    javac -cp ojdbc11.jar zay225/*.java

    jar cfm zay225.jar zay225/Manifest.txt -C zay225/ .

    java -cp zay225.jar:ojdbc11.jar Bank

zay225 includes:

    Bank.java - this is the main class 
    DepositWithdrawal.java - this handles deposit withdrawal interface
    Payment.java - this handles the loan and credit card payment interface
    NewAccount.java - this handles creating a new account for an existing customer 



Database Structure
The following tables are part of the banking system database:

customer:

    Stores customer information such as first name, last name, email, and phone number.
    Example: John Doe (Customer ID: 1)
account:

    Stores account-related data including account ID, customer ID, ownership type, and balance.
    Linked to specific account types such as savings, checking, or investment.
    savings_acc, checking_acc, investment_acc:

    Specific account tables storing additional attributes such as interest rates or penalties.
credit_card, debit_card:

    Card information for purchases.
    Credit card fields include outstanding balance and credit limit.
loan:

    Loan details including amount, interest rate, and monthly payment.
acc_transaction:

    Records account transactions, including deposits, withdrawals, transfers, repayment, credit payment.
atm, vendor:    
    ATM and vendor information for transactions.


Testing Input 

1.Open a new account
Enter customer ID: 1
Enter account type (savings/checking/investment): savings
Enter initial balance: 200

this raises an input conversion error unfortunately. I was not able to get it to work fully.

2. Deposit/Withdrawal

Enter account PIN: 1222 (pin number associated with savings account)
Account ID: 1
Enter operation (deposit/withdrawal): deposit
Enter amount: 300

Enter account PIN: 2080 (pin number associated with checking account)
Account ID: 1
Enter operation (deposit/withdrawal): deposit
Enter amount: 300

Enter account PIN: 3443 (pin number associated with investment account)
- will get an error message saying investment accounts won't allow deposit/withdrawal


3. Purchase

MENU:
1. Deposit/Withdrawal
2. Purchase
3. Loan or Credit Card Payment
4. Open a New Account
5. Exit

2

Enter card number: 5555666677778888
Card Type: debit
Customer Name: John Doe
Balance: $1000.0
Enter purchase amount: 200
Purchase successful using debit card.
Updated Balance: $800.0


4. Payment

Credit Payment

MENU:
1. Deposit/Withdrawal
2. Purchase
3. Loan or Credit Card Payment
4. Open a New Account
5. Exit
3
Enter payment type (loan/credit): cdjv
Invalid payment type. Please enter 'loan' or 'credit'.
Enter payment type (loan/credit): credit
Enter account ID: 2
Account Balance: $940.0
Enter amount: 40
Enter card number: 1111222233334444
Credit Card Details:
Customer Name: John Doe
Interest Rate: 18.0%
Balance Due: $100.0
Outstanding Balance: $1000.0
Credit card payment successful.
Updated Outstanding Balance: $960.0
Account Balance: $900.0

Loan Payment


MENU:
1. Deposit/Withdrawal
2. Purchase
3. Loan or Credit Card Payment
4. Open a New Account
5. Exit
3
Enter payment type (loan/credit): loan
Enter account ID: 1
Account Balance: $1500.0
Enter amount: 200
Enter loan ID: 2
Loan Details:
Customer Name: Bob Brown
Loan Amount: $15000.0
Interest Rate: 5.0%
Loan Type: unsecure
Monthly Payment: $500.0
Loan payment successful.
Updated Loan Balance: $14800.0
Account Balance: $1300.0


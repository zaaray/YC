import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Payment {
    public static void run(Connection conn, Scanner scanner) {
        try {
            String type = "";
            int accId = 0;
            double amount = 0;

            // Prompt for payment type
            while (true) {
                System.out.print("Enter payment type (loan/credit): ");
                type = scanner.nextLine().toLowerCase();
                if (type.equals("loan") || type.equals("credit")) {
                    break;
                } else {
                    System.out.println("Invalid payment type. Please enter 'loan' or 'credit'.");
                }
            }

            // Prompt for account ID
            while (true) {
                try {
                    System.out.print("Enter account ID: ");
                    accId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (accId > 0) {
                        printAccountBalance(conn, accId); // Display account balance
                        break;
                    } else {
                        System.out.println("Account ID must be a positive integer. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid account ID.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            // Prompt for payment amount
            while (true) {
                try {
                    System.out.print("Enter amount: ");
                    amount = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (amount > 0) {
                        break;
                    } else {
                        System.out.println("Amount must be greater than 0. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid numeric amount.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            // Process loan payment
            if (type.equals("loan")) {
                int loanId = 0;
                while (true) {
                    try {
                        System.out.print("Enter loan ID: ");
                        loanId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        if (loanId > 0) {
                            printLoanDetails(conn, loanId); // Display loan details
                            break;
                        } else {
                            System.out.println("Loan ID must be a positive integer. Please try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid loan ID.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }

                String sql = "UPDATE loan SET amount = amount - ? WHERE loan_id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDouble(1, amount);
                    pstmt.setInt(2, loanId);
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Loan payment successful.");
                        printUpdatedLoanBalance(conn, loanId);
                        updateAccountBalance(conn, accId, amount); // Update and print account balance
                    } else {
                        System.out.println("Loan not found.");
                    }
                }
            }
            // Process credit card payment
            else if (type.equals("credit")) {
                long cardNumber = 0;
                while (true) {
                    try {
                        System.out.print("Enter card number: ");
                        cardNumber = scanner.nextLong();
                        scanner.nextLine(); // Consume newline
                        if (String.valueOf(cardNumber).length() == 16) {
                            printCreditCardDetails(conn, cardNumber); // Display credit card details
                            break;
                        } else {
                            System.out.println("Card number must be a 16-digit number. Please try again.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid 16-digit card number.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }

                String sql = "UPDATE credit_card SET outstanding_balance = outstanding_balance - ? WHERE card_number = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setDouble(1, amount);
                    pstmt.setLong(2, cardNumber);
                    int rows = pstmt.executeUpdate();
                    if (rows > 0) {
                        System.out.println("Credit card payment successful.");
                        printUpdatedCreditBalance(conn, cardNumber);
                        updateAccountBalance(conn, accId, amount); // Update and print account balance
                    } else {
                        System.out.println("Card not found.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void printAccountBalance(Connection conn, int accId) throws SQLException {
        String sql = "SELECT balance FROM account WHERE acc_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Account Balance: $" + balance);
            }
        }
    }

    private static void printLoanDetails(Connection conn, int loanId) throws SQLException {
        String sql = "SELECT l.amount, l.interest_rate, l.loan_type, l.monthly_payment, " +
                     "c.first_name || ' ' || c.last_name AS customer_name " +
                     "FROM loan l " +
                     "JOIN customer c ON l.customer_id = c.customer_id " +
                     "WHERE l.loan_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double amount = rs.getDouble("amount");
                double interestRate = rs.getDouble("interest_rate");
                String loanType = rs.getString("loan_type");
                double monthlyPayment = rs.getDouble("monthly_payment");
                String customerName = rs.getString("customer_name");

                System.out.println("Loan Details:");
                System.out.println("Customer Name: " + customerName);
                System.out.println("Loan Amount: $" + amount);
                System.out.println("Interest Rate: " + interestRate + "%");
                System.out.println("Loan Type: " + loanType);
                System.out.println("Monthly Payment: $" + monthlyPayment);
            }
        }
    }

    private static void printCreditCardDetails(Connection conn, long cardNumber) throws SQLException {
        String sql = "SELECT c.first_name || ' ' || c.last_name AS customer_name, " +
                     "cc.interest_rate, cc.balance_due, cc.outstanding_balance " +
                     "FROM credit_card cc " +
                     "JOIN customer c ON cc.customer_id = c.customer_id " +
                     "WHERE cc.card_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String customerName = rs.getString("customer_name");
                double interestRate = rs.getDouble("interest_rate");
                double balanceDue = rs.getDouble("balance_due");
                double outstandingBalance = rs.getDouble("outstanding_balance");

                System.out.println("Credit Card Details:");
                System.out.println("Customer Name: " + customerName);
                System.out.println("Interest Rate: " + interestRate + "%");
                System.out.println("Balance Due: $" + balanceDue);
                System.out.println("Outstanding Balance: $" + outstandingBalance);
            }
        }
    }

    private static void printUpdatedLoanBalance(Connection conn, int loanId) throws SQLException {
        String sql = "SELECT amount FROM loan WHERE loan_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, loanId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double updatedAmount = rs.getDouble("amount");
                System.out.println("Updated Loan Balance: $" + updatedAmount);
            }
        }
    }

    private static void printUpdatedCreditBalance(Connection conn, long cardNumber) throws SQLException {
        String sql = "SELECT outstanding_balance FROM credit_card WHERE card_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, cardNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double updatedBalance = rs.getDouble("outstanding_balance");
                System.out.println("Updated Outstanding Balance: $" + updatedBalance);
            }
        }
    }

    private static void updateAccountBalance(Connection conn, int accId, double amount) throws SQLException {
        String sql = "UPDATE account SET balance = balance - ? WHERE acc_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accId);
            pstmt.executeUpdate();
        }
        printAccountBalance(conn, accId); // Print updated balance
    }
}

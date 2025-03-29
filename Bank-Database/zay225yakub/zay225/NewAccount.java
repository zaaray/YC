import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class NewAccount {
    public static void run(Connection conn, Scanner scanner) {
        try {
            int customerId = 0;
            String accountType = "";
            double initialBalance = 0.0;
            int defaultPin = 1234;

            // Prompt for customer ID
            while (true) {
                try {
                    System.out.print("Enter customer ID: ");
                    customerId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    if (customerId > 0) {
                        break;
                    } else {
                        System.out.println("Customer ID must be a positive integer. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid customer ID.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            // Prompt for account type
            while (true) {
                System.out.print("Enter account type (savings/checking/investment): ");
                accountType = scanner.nextLine().toLowerCase();
                if (accountType.equals("savings") || accountType.equals("checking") || accountType.equals("investment")) {
                    break;
                } else {
                    System.out.println("Invalid account type. Please enter 'savings', 'checking', or 'investment'.");
                }
            }

            // Prompt for initial balance
            while (true) {
                try {
                    System.out.print("Enter initial balance: ");
                    initialBalance = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    if (initialBalance >= 0) {
                        break;
                    } else {
                        System.out.println("Balance must be a non-negative number. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid numeric balance.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            // Insert into the account table with initial balance as 0.00
            String accountInsertSql = "INSERT INTO account (customer_id, ownership_type, balance) VALUES (?, 'single', 0.00)";
            try (PreparedStatement pstmt = conn.prepareStatement(accountInsertSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, customerId);
                pstmt.executeUpdate();

                // Retrieve the generated account ID
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int accId = rs.getInt(1);

                    // Update the account balance by adding the initial balance
                    String updateBalanceSql = "UPDATE account SET balance = balance + ? WHERE acc_id = ?";
                    try (PreparedStatement updateBalancePstmt = conn.prepareStatement(updateBalanceSql)) {
                        updateBalancePstmt.setDouble(1, initialBalance);
                        updateBalancePstmt.setInt(2, accId);
                        updateBalancePstmt.executeUpdate();
                    }

                    // Insert into the respective account type table
                    String accountTypeSql = null;
                    if (accountType.equals("savings")) {
                        accountTypeSql = "INSERT INTO savings_acc (acc_id, customer_id, ownership_type, balance, min_balance, interest_rate, penalty_fee) VALUES (?, ?, 'single', ?, 500.00, 2.5, 25.00)";
                    } else if (accountType.equals("checking")) {
                        accountTypeSql = "INSERT INTO checking_acc (acc_id, customer_id, ownership_type, balance, pin) VALUES (?, ?, 'single', ?, ?)";
                    } else if (accountType.equals("investment")) {
                        accountTypeSql = "INSERT INTO investment_acc (acc_id, customer_id, ownership_type, balance) VALUES (?, ?, 'single', ?)";
                    }

                    try (PreparedStatement accountTypePstmt = conn.prepareStatement(accountTypeSql)) {
                        accountTypePstmt.setInt(1, accId);
                        accountTypePstmt.setInt(2, customerId);
                        accountTypePstmt.setDouble(3, initialBalance); // Use setDouble for compatibility
                        if (accountType.equals("checking")) {
                            accountTypePstmt.setInt(4, defaultPin); // Add default PIN for checking accounts
                        }
                        accountTypePstmt.executeUpdate();

                        System.out.println("Account created successfully with Account ID: " + accId);
                        System.out.println("Initial Balance: $" + initialBalance);
                        if (accountType.equals("checking")) {
                            System.out.println("Default PIN for your checking account: " + defaultPin);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}

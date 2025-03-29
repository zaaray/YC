import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DepositWithdrawal {
    public static void run(Connection conn, Scanner scanner) {
        try {
            int pin = 0;
            int accId = 0;
            int customerId = 0;
            String customerName = "";
            String accountType = "";
            double currentBalance = 0.0;

            // Prompt user for account PIN and fetch associated account details
            while (true) {
                System.out.print("Enter account PIN: ");
                try {
                    pin = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    String detailsQuery = "SELECT a.acc_id, a.customer_id, c.first_name || ' ' || c.last_name AS full_name, " +
                            "a.balance, CASE WHEN EXISTS (SELECT 1 FROM savings_acc sa WHERE sa.acc_id = a.acc_id) THEN 'savings' " +
                            "WHEN EXISTS (SELECT 1 FROM checking_acc ca WHERE ca.acc_id = a.acc_id) THEN 'checking' ELSE 'investment' END AS account_type " +
                            "FROM account a " +
                            "JOIN customer c ON a.customer_id = c.customer_id " +
                            "WHERE a.pin = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(detailsQuery)) {
                        pstmt.setInt(1, pin);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            accId = rs.getInt("acc_id");
                            customerId = rs.getInt("customer_id");
                            customerName = rs.getString("full_name");
                            currentBalance = rs.getDouble("balance");
                            accountType = rs.getString("account_type");

                            if ("investment".equalsIgnoreCase(accountType)) {
                                System.out.println("Deposits and withdrawals are not allowed for investment accounts.");
                                return;
                            }

                            System.out.println("Account ID: " + accId);
                            System.out.println("Customer ID: " + customerId);
                            System.out.println("Customer Name: " + customerName);
                            System.out.println("Account Type: " + accountType);
                            System.out.println("Current Balance: $" + currentBalance);
                            break;
                        } else {
                            System.out.println("Invalid PIN. Please try again.");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a numeric PIN.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            // Prompt user for operation type
            String operation = "";
            while (true) {
                System.out.print("Enter operation (deposit/withdrawal): ");
                operation = scanner.nextLine().toLowerCase();
                if (operation.equals("deposit") || operation.equals("withdrawal")) {
                    // Ensure withdrawal is only allowed for checking accounts
                    if (operation.equals("withdrawal") && !"checking".equalsIgnoreCase(accountType)) {
                        System.out.println("Withdrawals are only allowed for checking accounts.");
                        return;
                    }
                    break; // Exit loop on valid input
                } else {
                    System.out.println("Invalid operation. Only 'deposit' or 'withdrawal' allowed.");
                }
            }

            // Prompt user for amount
            double amount = 0;
            while (true) {
                System.out.print("Enter amount: ");
                try {
                    amount = scanner.nextDouble();
                    if (amount > 0) {
                        break; // Exit loop on valid input
                    } else {
                        System.out.println("Amount must be greater than 0. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid numeric amount.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            if (operation.equals("deposit")) {
                // Perform deposit
                updateAccountBalance(conn, accId, amount, true);
                System.out.println("Deposit successful.");
            } else if (operation.equals("withdrawal")) {
                // Perform withdrawal
                if (currentBalance >= amount) {
                    int atmId = promptForAtmId(scanner);
                    insertATMIfNotExists(conn, atmId);
                    updateAccountBalance(conn, accId, amount, false);
                    System.out.println("Withdrawal successful.");
                } else {
                    System.out.println("Insufficient funds for withdrawal.");
                }
            }

            // Print updated balance
            printUpdatedBalance(conn, accId);
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void updateAccountBalance(Connection conn, int accId, double amount, boolean isDeposit) throws SQLException {
        String updateSql = isDeposit
                ? "UPDATE account SET balance = balance + ? WHERE acc_id = ?"
                : "UPDATE account SET balance = balance - ? WHERE acc_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accId);
            pstmt.executeUpdate();
        }
    }

    private static int promptForAtmId(Scanner scanner) {
        while (true) {
            System.out.print("Enter ATM ID (4-digit integer): ");
            try {
                int atmId = scanner.nextInt();
                scanner.nextLine();
                if (atmId >= 1000 && atmId <= 9999) {
                    return atmId;
                } else {
                    System.out.println("ATM ID must be a 4-digit integer. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a 4-digit integer for ATM ID.");
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    private static void insertATMIfNotExists(Connection conn, int atmId) throws SQLException {
        String atmSql = "INSERT INTO atm (atm_id) SELECT ? FROM dual WHERE NOT EXISTS (SELECT 1 FROM atm WHERE atm_id = ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(atmSql)) {
            pstmt.setInt(1, atmId);
            pstmt.setInt(2, atmId);
            pstmt.executeUpdate();
            System.out.println("ATM ID " + atmId + " ensured in ATM table.");
        }
    }

    private static void printUpdatedBalance(Connection conn, int accId) throws SQLException {
        String balanceSql = "SELECT balance FROM account WHERE acc_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(balanceSql)) {
            pstmt.setInt(1, accId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double updatedBalance = rs.getDouble("balance");
                System.out.println("Updated balance: $" + String.format("%.2f", updatedBalance));
            } else {
                System.out.println("Error retrieving updated balance. Account not found.");
            }
        }
    }
}

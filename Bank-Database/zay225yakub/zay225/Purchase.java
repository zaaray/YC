import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Purchase {
    public static void run(Connection conn, Scanner scanner) {
        try {
            long cardNumber = 0;

            // Get Card Number
            while (true) {
                try {
                    System.out.print("Enter card number: ");
                    cardNumber = scanner.nextLong();
                    scanner.nextLine(); // Consume newline
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid card number.");
                    scanner.nextLine(); // Clear invalid input
                }
            }

            String cardType = null;
            String customerName = null;
            double balance = 0;
            double creditLimit = 0;
            double outstandingBalance = 0;

            // Determine Card Type and Get Initial Details
            String cardTypeQuery = 
                "SELECT 'debit' AS card_type, c.first_name || ' ' || c.last_name AS customer_name, " +
                "d.balance, NULL AS credit_limit, NULL AS outstanding_balance " +
                "FROM customer c " +
                "JOIN debit_card d ON c.customer_id = d.customer_id " +
                "WHERE d.card_number = ? " +
                "UNION ALL " +
                "SELECT 'credit' AS card_type, c.first_name || ' ' || c.last_name AS customer_name, " +
                "NULL AS balance, ccc.credit_limit, ccc.outstanding_balance " +
                "FROM customer c " +
                "JOIN credit_card ccc ON c.customer_id = ccc.customer_id " +
                "WHERE ccc.card_number = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(cardTypeQuery)) {
                pstmt.setLong(1, cardNumber);
                pstmt.setLong(2, cardNumber);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        cardType = rs.getString("card_type");
                        customerName = rs.getString("customer_name");
                        balance = rs.getDouble("balance");
                        creditLimit = rs.getDouble("credit_limit");
                        outstandingBalance = rs.getDouble("outstanding_balance");

                        System.out.println("Card Type: " + cardType);
                        System.out.println("Customer Name: " + customerName);
                        if ("debit".equals(cardType)) {
                            System.out.println("Balance: $" + balance);
                        } else if ("credit".equals(cardType)) {
                            System.out.println("Outstanding Balance: $" + outstandingBalance);
                            System.out.println("Credit Limit: $" + creditLimit);
                        }
                    } else {
                        System.out.println("Card not found. Please try again.");
                        return;
                    }
                }
            }

            // Get Purchase Amount
            double amount = 0;
            while (true) {
                try {
                    System.out.print("Enter purchase amount: ");
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

            // Handle Purchase Logic Based on Card Type
            if ("debit".equals(cardType)) {
                if (balance >= amount) {
                    // Update Balance in Debit Card
                    String updateSql = "UPDATE debit_card SET balance = balance - ? WHERE card_number = ?";
                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                        updatePstmt.setDouble(1, amount);
                        updatePstmt.setLong(2, cardNumber);
                        updatePstmt.executeUpdate();
                        System.out.println("Purchase successful using debit card.");
                        System.out.println("Updated Balance: $" + (balance - amount));
                    }
                } else {
                    System.out.println("Purchase declined due to insufficient funds.");
                }
            } else if ("credit".equals(cardType)) {
                if (outstandingBalance + amount <= creditLimit) {
                    // Update Outstanding Balance in Credit Card
                    String updateSql = "UPDATE credit_card SET outstanding_balance = outstanding_balance + ? WHERE card_number = ?";
                    try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                        updatePstmt.setDouble(1, amount);
                        updatePstmt.setLong(2, cardNumber);
                        updatePstmt.executeUpdate();
                        System.out.println("Purchase successful using credit card.");
                        System.out.println("Updated Outstanding Balance: $" + (outstandingBalance + amount));
                    }
                } else {
                    System.out.println("Purchase declined due to exceeding the credit limit.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }
}

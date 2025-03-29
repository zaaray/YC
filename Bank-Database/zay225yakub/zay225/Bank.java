import java.sql.*;
import java.util.Scanner;

public class Bank {
    private static final String url = "jdbc:oracle:thin:@edgar1.cse.lehigh.edu:1521:cse241";
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        // gets user input for user ID and password for the database
        System.out.print("Enter Oracle user id: ");
        String username = scanner.nextLine();
        System.out.print("Enter Oracle password for " + username + ": ");
        String password = scanner.nextLine();

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to database successfully!");

            while (true) {
                System.out.println("\n-----Welcome to LU Bank-----");
                
                System.out.println("\nMENU:");
                System.out.println("1. Deposit/Withdrawal");
                System.out.println("2. Purchase");
                System.out.println("3. Loan or Credit Card Payment");
                System.out.println("4. Open a New Account");
                System.out.println("5. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // newline

                switch (choice) {
                    case 1:
                        DepositWithdrawal.run(conn, scanner);
                        break;
                    case 2:
                        Purchase.run(conn, scanner);
                        break;
                    case 3:
                        Payment.run(conn, scanner);
                        break;
                    case 4:
                        NewAccount.run(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting application. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}

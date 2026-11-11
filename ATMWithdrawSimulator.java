import java.security.SecureRandom;
import java.util.Scanner;

public class ATMWithdrawSimulator {

    // A simple bank account.
    static class Account {
        String name;
        double balance;
        boolean isCredit;

        Account(String name, double balance, boolean isCredit) {
            this.name = name;
            this.balance = balance;
            this.isCredit = isCredit;
        }

        String balanceLabel() {
            return isCredit ? "Available credit" : "Balance";
        }
    }

    static final int OTP_ATTEMPTS = 3;
    static final long OTP_VALID_MILLIS = 60_000;

    static Scanner scanner = new Scanner(System.in);
    static SecureRandom random = new SecureRandom();

    static Account[] accounts = {
        new Account("Savings", 1000.0, false),
        new Account("Cheque", 500.0, false),
        new Account("Credit", 2000.0, true)
    };

    public static void main(String[] args) {
        System.out.println("Welcome to the ATM Withdrawal Simulator");

        boolean again = true;
        while (again) {
            System.out.println("\n1. Withdraw with card");
            System.out.println("2. Cardless withdrawal (OTP)");
            System.out.println("3. Exit");
            int choice = readInt("Choose an option:");

            if (choice == 1) {
                withdraw();
            } else if (choice == 2) {
                if (verifyOTP()) {
                    withdraw();
                }
            } else if (choice == 3) {
                again = false;
                continue;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }

            System.out.print("\nAnother transaction? (y/n): ");
            again = scanner.nextLine().trim().equalsIgnoreCase("y");
        }

        System.out.println("Thank you for using the ATM Withdrawal Simulator. Goodbye!");
        scanner.close();
    }

    // Cardless: send a code to the phone and check it.
    static boolean verifyOTP() {
        String phone;
        while (true) {
            System.out.print("Enter your phone number (10 digits): ");
            phone = scanner.nextLine().trim();
            if (phone.matches("\\d{10}")) {
                break;
            }
            System.out.println("Invalid phone number. Please try again.");
        }

        String otp = String.format("%06d", random.nextInt(1_000_000));
        long sentAt = System.currentTimeMillis();

        // In a real ATM this is sent by SMS; here it is displayed for simulation.
        System.out.println("[SMS to " + phone + "] Your ATM code is: " + otp
                + ". Valid for 60 seconds. Do not share this code with anyone.");

        for (int attempt = 1; attempt <= OTP_ATTEMPTS; attempt++) {
            System.out.print("Enter the OTP: ");
            String entered = scanner.nextLine().trim();

            if (System.currentTimeMillis() - sentAt > OTP_VALID_MILLIS) {
                System.out.println("OTP has expired. Please request a new one.");
                return false;
            }
            if (entered.equals(otp)) {
                System.out.println("OTP verified successfully.");
                return true;
            }

            int left = OTP_ATTEMPTS - attempt;
            if (left > 0) {
                System.out.println("Incorrect OTP. You have " + left + " attempt(s) left.");
            }
        }

        System.out.println("Too many failed attempts. Please request a new OTP.");
        return false;
    }

    // Pick an account and withdraw.
    static void withdraw() {
        System.out.println("\nSelect account:");
        for (int i = 0; i < accounts.length; i++) {
            System.out.printf("%d. %s (%s: R%.2f)%n", i + 1, accounts[i].name,
                    accounts[i].balanceLabel(), accounts[i].balance);
        }

        int pick = readInt("Choose an account:");
        if (pick < 1 || pick > accounts.length) {
            System.out.println("Invalid account selection.");
            return;
        }

        Account acc = accounts[pick - 1];
        System.out.printf("%s %s: R%.2f%n", acc.name,
                acc.balanceLabel().toLowerCase(), acc.balance);
        double amount = readDouble("Enter amount to withdraw: R");

        if (amount <= 0) {
            System.out.println("Invalid amount. You must withdraw more than R0.00.");
        } else if (amount <= acc.balance) {
            acc.balance -= amount;
            System.out.printf("Withdrawal successful. Remaining %s: R%.2f%n",
                    acc.balanceLabel().toLowerCase(), acc.balance);
        } else {
            System.out.println("Insufficient funds. Withdrawal failed.");
        }
    }

    // Safe input helpers.
    static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    static double readDouble(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
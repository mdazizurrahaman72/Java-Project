import java.util.Scanner;

public class SimpleATM {

    static double balance = 1000.0;
    static final int PIN = 1234;
    public static void checkBalance() {
        System.out.println("Your current balance: ৳" + balance);
    }

    public static void depositMoney(Scanner sc) {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited ৳" + amount);
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public static void withdrawMoney(Scanner sc) {
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn ৳" + amount);
        } else {
            System.out.println("Invalid amount or Insufficient Balance.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter your 4-digit PIN: ");
        int userPin = sc.nextInt();

        if (userPin != PIN) {
            System.out.println("Invalid PIN. Access Denied.");
            return;
        }

        int choice;
        do {
            System.out.println("\n===== ATM MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    checkBalance();
                    break;
                case 2:
                    depositMoney(sc);
                    break;
                case 3:
                    withdrawMoney(sc);
                    break;
                case 4:
                    System.out.println("Thanks for using the ATM. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }

        } while (choice != 4);

        sc.close();
    }
}

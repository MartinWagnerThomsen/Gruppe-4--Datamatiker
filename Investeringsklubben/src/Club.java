import DataObjects.Transaction;
import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Club {
    static String adminUserName = "president@gmail.com";
    static String password = "Sauron";
    private final CsvHandler handler = new CsvHandler();



    public static void login() {
        System.out.println("Velkommen. Tast brugernavn og kodeord.");

        boolean isLoggedIn = false;

        while (!isLoggedIn) {

            // checker om brugeren er presidenten
            if (adminUserName.equals(adminUserName)) {
                System.out.println("Logger ind som president");
                isLoggedIn = true;

            } else {
                System.out.println("Prøver at logge ind som medlem");
            }
            System.out.println("1. Se oversigt over brugernes porteføljeværdi \n" +
                    "2. Vis rangliste\n " +
                    "3. Vis fordelinger på aktier og sektorer\n" +
                    "4. Tilføj ny bruger\n" +
                    "5. Fjern bruger\n" +
                    "6. Log ud");
        }}

        public void logout () {

        }
        public void switchUser () {

        }

    public Transaction createTransaction() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter transaction ID: ");
        int transactionId = sc.nextInt();
        sc.nextLine(); // consume newline

        System.out.print("Enter user ID: ");
        int userId = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateInput = sc.nextLine();
        LocalDate date = LocalDate.parse(dateInput);

        System.out.print("Enter stock ticker: ");
        String ticker = sc.nextLine();

        System.out.print("Enter price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        System.out.print("Enter currency (e.g., DKK): ");
        String currency = sc.nextLine();

        System.out.print("Enter order type (buy/sell): ");
        String orderType = sc.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = sc.nextInt();

        // Create the transaction
        Transaction transaction = new Transaction(
                transactionId,
                userId,
                date,
                ticker,
                price,
                currency,
                orderType,
                quantity
        );

        return transaction;



    }
    public static void main(String[] args){
        Club investmentClub = new Club();
        DataManager dataManager = new DataManager();
        Transaction action = investmentClub.createTransaction();
        dataManager.registerNewTransaction(action);

    }
        }


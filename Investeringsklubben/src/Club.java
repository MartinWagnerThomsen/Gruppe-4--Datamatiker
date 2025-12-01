import DataObjects.Currency;
import DataObjects.Stock;
import DataObjects.Transaction;
import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Club {
    static String adminUserName = "president@gmail.com";
    static String password = "Sauron";
    private DataManager dataManager;


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
        }
    }

    public void logout() {

    }

    public void switchUser() {

    }

    /**
     * Finder vores medlem ved at bruge userId
     */
    public void findMember() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter user ID for the user which you want to find transactions from: ");
        int userId = sc.nextInt();
        Member foundMember;
        List<Member> members = dataManager.getMembers();
        Optional<Member> memberOptional = members.stream()
                .filter(member -> member.getUserId() == userId)
                .findFirst();
        if (memberOptional.isPresent()) {
            foundMember = memberOptional.get();
            foundMember.printMember();
        }
    }

    public void createTransaction() {
        Scanner sc = new Scanner(System.in);
        Transaction lastTransaction = dataManager.getMembers().getLast().getPortfolio().getTransactions().getLast(); // Tag den sidste transaction
        int transactionId = lastTransaction.getTransactionId() + 1;
        System.out.print("Enter user ID: ");
        int userId = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter date (dd-MM-yyyy): ");
        String dateInput = sc.nextLine();
        LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
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
        dataManager.registerNewTransaction(transaction);
    }

    public void mainLoop() {
        DataManager manager = new DataManager();
        this.dataManager = manager;

        try {
            if (manager.getMembers().size() < 0) {
                System.out.println("Tom liste af medlemmer");
            }
        } catch (Exception e) {
            System.out.println("Fortsæt loop");
            e.printStackTrace();
        }

        //createTransaction();
      //  findMember();
      List<Currency> currencies =  manager.getCurrencies();
        System.out.println(currencies);


    }

    /**
     * Tager en aktie som input
     * Derefter hentes vores valutakurser fra DataManager
     * Hvis aktiens valuta matcher en af vores kurser så hentes raten
     * Prisen af aktien ganges derefter med raten for at finde prisen i DKK
     * Resultatet bliver gemt på aktien samt dens aktie valuta gemmes som DKK
     * @param stock
     */
    public void convertToDkk (Stock stock) {
        String danishkrone = "DKK";
        // Vi har brug for at få vores rater
        DataManager manager = new DataManager();
        List<Currency> listOfCurrenciesAndRates = manager.getCurrencies();

        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stock.getCurrency();
        double stockPrice = stock.getPrice();
        System.out.println(stockCurrency);

        if(!stockCurrency.equalsIgnoreCase("DKK")) {
            for (Currency currency : listOfCurrenciesAndRates){
                if (stockCurrency.equalsIgnoreCase(currency.getBaseCurr())) {
                    System.out.println("Found the currency in our list: " + currency);
                    System.out.println("Converting " + stockPrice + stockCurrency + " the found value to DKK at " +
                            "our current rate: " + currency.getRate());

                    double totalPriceAfterConversion = stockPrice * currency.getRate();
                    System.out.println("Result of the conversion: " + totalPriceAfterConversion + danishkrone);

                    // Sæt aktiens priser til at reflektere dens reele dkk pris
                    stock.setCurrency(danishkrone);
                    stock.setPrice(totalPriceAfterConversion);

                }
            }

        }




    }

    public static void main(String[] args) {
        Club investmentClub = new Club();
        //investmentClub.mainLoop();
        DataManager manager = new DataManager();
        List<Stock> testList = manager.getStocks();
        investmentClub.convertToDkk(testList.getFirst());

    }
}


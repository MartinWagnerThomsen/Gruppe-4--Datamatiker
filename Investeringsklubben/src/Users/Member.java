package Users;

import DataObjects.Currency;
import DataObjects.Portfolio;
import DataObjects.Stock;
import DataObjects.Transaction;
import Filehandling.DataManager;
import UI.Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Member extends User {
    private final Scanner sc = new Scanner(System.in);
    private String password;
    private String userType;
    private double cashBalance;

    /**
     * Konstruktør til login-flowet. Bliver overskrevet af den størrere konstruktør efter succesfuldt login
     * @param email
     * @param password
     * @param userType
     */
    public Member(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    /**
     * Constructor with Portfolio added
      */
    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash ,createdAt,lastUpdated, portfolio);
        this.cashBalance = initialCash;
    }




    @Override
    public void viewTransactionHistory() {
        List<Transaction> memberTransactions = getPortfolio().getTransactions();
        System.out.println("Transaction History of: " + this.getFullName());
        System.out.println("================================================");
        for(Transaction transactions : memberTransactions) {
            System.out.println(transactions.toString(this));
        }
        System.out.println("================================================");
    }


    @Override
    public Menu getMenu() {

    }

    public void printMemberMenu() {
        System.out.println(
                "\n====================  📈  MEDLEM MENU  ====================\n" +
                        " 1. 📊 Se aktiemarkedet og aktuel kurs\n" +
                        " 2. 💼 Registrer køb og salg af aktier\n" +
                        " 3. 🧾 Se portefølje\n" +
                        " 4. 🕘 Se transaktionshistorik\n" +
                        " 5. 🔒 Log ud\n" +
                        " 6. 🔄 Skift bruger\n" +
                        "===========================================================\n");
    }

    public void printMember(DataManager dataManager, Member foundMember) {
        getPortfolio().calculateTotalValue(foundMember, dataManager);
        getPortfolio().calculateCashBalance(foundMember);
        System.out.println("Member profile for: " + this.getFullName());
        System.out.println("Portfolio: ");
        System.out.println(this.getPortfolio());
        getPortfolio().printInvestedStocks(foundMember, dataManager);
        System.out.println("Cash balance: " + this.getCashBalance());
        getPortfolio().showDifference(foundMember, dataManager);
    }


    public void printMarketAndRates() {
        List<Stock> stockMarket = dataManager.getStocks();
        for (Stock stock : stockMarket) {
            System.out.println(stock.getName());
            System.out.println(stock.getPrice());
        }
    }


    @Override
    public void createUser() {
    }


    public String getPassword() {
        return this.password;
    }

    public String getUserType() {
        return userType;
    }

    @Override
    public double getInitialCash() {
        return 0;
    }

    public void setLastUpdated (LocalDate update) {
        this.lastUpdated = update;
    }

    public void setCash (double cash) {
        this.initialCash = cash;
    }

    @Override
    public  int getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public LocalDate getCreationLocalDate() {
        return createdAt;
    }

    @Override
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public Portfolio getPortfolio() {
        return portfolio;
    }

    public double getCashBalance() {return cashBalance;}

    //setter
    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    @Override
    public String getFullName() {
        return fullName;
    }
    public void setUserType(String role) {
        this.userType = role;
    }

    @Override
    public String toString() {
        return
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", initialCash='" + initialCash +
                "', createdAt='" + createdAt + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                    ", portfolio='" + portfolio + '\''
                ;
    }


    public void registerStock() {
        int transactionId = dataManager.getTransactions().getLast().getTransactionId() + 1;
        LocalDate date = null;
        String ticker = "";
        double price = 0;
        String currency = "";
        String orderType = "";
        int quantity = 0;
        System.out.println("Vil du registrere et køb eller salg?");
        orderType = sc.nextLine();
        if (orderType.equalsIgnoreCase("køb")) {
            System.out.println("Hvilken aktie købte du?"); //ticker
            ticker = sc.nextLine();

            System.out.println("Hvor meget betalte du for aktien"); // price
            price = Integer.parseInt(sc.nextLine());

            System.out.println("Hvor mange aktier købte du?"); // quantity
            quantity = Integer.parseInt(sc.nextLine());

            System.out.println("Hvilken valuta var aktien i?"); // currency
            List<Currency> currencies = dataManager.getCurrencies();
            for (Currency curr : currencies) {
                System.out.println(curr);
            }
            switch (sc.nextLine().toLowerCase()) {
                case "dkk" -> currency = "DKK";
                case "eur" -> currency = "EUR";
                case "usd" -> currency = "USD";
            }
            System.out.println("Hvilen dato købte du?"); // date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(sc.nextLine()).format(formatter);

        } else if (orderType.equalsIgnoreCase("salg")) {
            System.out.println("Hvilken aktie solgte du?"); //ticker
            ticker = sc.nextLine();
            // pris
            System.out.println("Hvor meget fik du for aktien"); // price
            price = Integer.parseInt(sc.nextLine());
            // mængde
            System.out.println("Hvor mange aktier solgte du?"); // quantity
            quantity = Integer.parseInt(sc.nextLine());
            // valuta
            System.out.println("Hvilken valuta var aktien i?"); // currency
            List<Currency> currencies = dataManager.getCurrencies();
            for (Currency curr : currencies) {
                System.out.println(curr);
            }
            switch (sc.nextLine().toLowerCase()) {
                case "dkk" -> currency = "DKK";
                case "eur" -> currency = "EUR";
                case "usd" -> currency = "USD";
            }
            // dato
            System.out.println("Hvilen dato solgte du?"); // date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate.parse(sc.nextLine()).format(formatter);

        } else {
            System.out.println("Du kan kun vælge mellem 'køb' og 'salg', prøv venligst igen.");
            registerStock();
        }
        Transaction transaction = new Transaction(transactionId,this.getUserId(),date,ticker,price,currency,orderType,quantity);
        dataManager.registerNewTransaction(transaction);
    }
}

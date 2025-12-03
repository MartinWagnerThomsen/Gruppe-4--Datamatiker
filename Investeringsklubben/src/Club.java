import DataObjects.Currency;
import DataObjects.Stock;
import DataObjects.Transaction;

import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Club {
    public Scanner sc = new Scanner(System.in);
    private CsvHandler csvhandler = new CsvHandler();
    private DataManager dataManager = new DataManager();
    private Member currentMember;

    public static void main(String[] args) {
        Club investmentClub = new Club();
        System.out.println(investmentClub.getSector());
    }

    public void login() {
        /*
        tempMemberList bruger en mindre konstruktør og sørger for at det som brugeren logger ind som passer
        der bliver senere retuneret en Member med den fulde konstruktør
         */
        List<Member> tempMemberList = csvhandler.parseloginCredentials();
        System.out.println("Velkommen. Tast brugernavn og adgangskode.");

        System.out.print("Brugernavn: ");
        String username = sc.nextLine();
        for (Member member : tempMemberList) {
            if (username.equals(member.getEmail())) {
                System.out.print("Adgangskode: ");
                String password = sc.nextLine();
                if (password.equals(member.getPassword())) {
                    System.out.println("Logger dig ind som " + member.getUserType());
                    currentMember = dataManager.getMember(username);
                    switch (member.getUserType().toLowerCase()) {
                        case "member":
                            try {
                                membersMenu();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "president":
                            presidentMenu();
                            break;
                    }
                }
            }
        }
    }

    private void presidentMenu() throws IllegalArgumentException {
        System.out.println(
                "1. Se oversigt over brugernes porteføljeværdi\n" +
                        "2. Vis rangliste\n" +
                        "3. Vis fordelinger på aktier og sektorer\n" +
                        "4. Tilføj ny bruger\n" + "5. Fjern bruger\n" +
                        "6. Log ud");

        switch (sc.nextLine()) {
            case "1":
                //getPortfolio(user);
                break;
            case "2":
                //getRankings();
                break;
            case "3":
                getSector();
                break;
            case "4":
                //addUser(getUserInfo());
                break;
            case "5":
                //removeUser(getUserInfo());
                break;
            case "6":
                //saveProgress();
                login();
                break;
            default:
                throw new IllegalArgumentException("Forket input");
        }
    }

    private Map<String, Double> getSector() {
        // Deklarer variablerne
        List<Transaction> history = dataManager.getTransactions();
        List<Stock> stockHistory = dataManager.getStocks();
        // Vi skal bruge et Hashmap hvor vi har Sector som Key og Price som Value
        Map<String, Double> sectorAnalysis = new HashMap<>();
        // Prisen bliver initialiseret uden for loopet. Den
        double price = 0;
        // Først skal vi iterere over aktierne og dernæst transaktions historikken
        for (Stock investment : stockHistory) {
            for(Transaction transaction : history) {
                if (investment.getTicker().equalsIgnoreCase(transaction.getTicker())) {
                    if(transaction.getOrderType().equalsIgnoreCase("buy"))
                        price = transaction.getPrice() * transaction.getQuantity();
                    String sector = investment.getSector();
                    // Her tager vi og kalder en summariserings funktion over alle vores values
                    sectorAnalysis.merge(sector, price, Double::sum);
        }}}
        return sectorAnalysis;
    }


    public void membersMenu() throws IllegalArgumentException {
        boolean quit = false;
        while (!quit) {
            System.out.println(
                    "1. Se aktiemarkedet og aktuel kurs\n" +
                            "2. Registrer køb og salg af aktier\n" +
                            "3. Se portefølje\n" +
                            "4. Se transaktionshistorik\n" +
                            "5. Log ud");

            switch (sc.nextLine()) {
                case "1":
                    //getStockMarket()
                    List<Stock> stockMarket = dataManager.getStocks();
                    for (Stock stock : stockMarket) {
                        System.out.println(stock.getName());
                        System.out.println(stock.getPrice());
                    }
                    break;
                case "2":
                    createTransaction();
                    break;
                case "3":
                    //getPortfolio()
                    findMember(currentMember.getUserId());
                    break;
                case "4":
                    //getTransactions()

                /*

                List<Transaction> transactions = dataManager.getTransactions();

                for (Transaction transaction : transactions) {
                    System.out.println(transaction.getDate());
                    System.out.println(transaction.getTicker());
                    System.out.println(transaction.getPrice());
                    System.out.println(transaction.getQuantity());
                }

                 */
                    break;
                case "5":
                    //logOut
                    break;
                default:
                    throw new IllegalArgumentException("Forket input");
            }
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


    public void registerStock() {
        int transactionId = -1;
        LocalDate date;
        String ticker;
        double price;
        String currency;
        String orderType;
        int quantity;

        System.out.println("Vil du registrere et køb eller salg?");
        orderType = sc.nextLine();
        if (orderType.equalsIgnoreCase("køb")) {
            System.out.println("Hvilken aktie købte du?"); //ticker
            System.out.println("Hvor meget betalte du for aktien"); // price
            System.out.println("Hvor mange aktier købte du?"); // quantity
            System.out.println("Hvilken valuta var aktien i?"); // currency
            System.out.println("Hvilen dato købte du?"); // date
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

        //  Transaction transaction = new Transaction(transactionId,currentMember.getUserId(),date,ticker,price,currency,orderType,quantity);
        //  dataManager.registerNewTransaction(transaction);
    }

    /**
     * Finder vores medlem ved at bruge userId
     */
    public void findMember(int userId) {
        //Scanner sc = new Scanner(System.in);
        //System.out.print("Enter user ID for the user which you want to find transactions from: ");
        //int userId = sc.nextInt();
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

}

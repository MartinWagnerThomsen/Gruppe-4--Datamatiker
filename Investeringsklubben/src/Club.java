import DataObjects.Currency;
import DataObjects.Portfolio;
import DataObjects.Stock;
import DataObjects.Transaction;

import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Club {
    private final Scanner sc = new Scanner(System.in);
    private CsvHandler csvhandler = new CsvHandler();
    private DataManager dataManager;
    private Member currentMember;

    Club() {
        this.dataManager = new DataManager();
    }

    public static void main(String[] args) throws IOException {
        Club investmentClub = new Club();
        investmentClub.login();
    }
    // ---------------------------------------------------------------------------------------
    // Menu flows

    private void presidentMenu() throws IllegalArgumentException, IOException {
        boolean quit = false;
        while (!quit) {
            printPresidentMenu();
            switch (sc.nextLine()) {
                case "1":
                    clubPortfolioOverview();
                    break;
                case "2":
                    clubRankingInTotalValue();
                    break;
                case "3":
                    printSectorInvestmentDistribution();
                    break;
                case "4":
                    addUser();
                    break;
                case "5":
                    removeUser();
                    break;
                case "6":
                    logOut();
                    quit = true;
                    break;
                case "7":
                    switchUser();
                default:
                    throw new IllegalArgumentException("Forket input");
            }
            saveProgram();

    }
        }

    public void membersMenu() throws IllegalArgumentException, IOException {
        System.out.println("Logget ind som medlem: " + currentMember);
        boolean quit = false;
        while (!quit) {
            printMemberMenu();
            switch (sc.nextLine()) {
                case "1":
                    printMarketAndRates();
                    break;
                case "2":
                    registerStock();
                    break;
                case "3":
                    findMember(currentMember.getUserId());
                    break;
                case "4":
                    currentMember.viewTransactionHistory(currentMember);
                    break;
                case "5":
                    logOut();
                    quit = true;
                    break;
                case "6":
                    switchUser();
                default:
                    throw new IllegalArgumentException("Forkert input");
            }
        }
    }

    // ---------------------------------------------------------------------------------------
    // Metoder omhandlende login / logud
        /**
        tempMemberList bruger en mindre konstruktør og sørger for at det som brugeren logger ind som passer
        der bliver senere retuneret en Member med den fulde konstruktør
         */
    public void login() throws IOException {
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
    public void logOut() {
        System.out.println("Logger ud som bruger: " + currentMember.getFullName());
        System.out.println("Farvel og tak.");
    }
    public void switchUser() {
        try {
            System.out.println("Logger ud som bruger: " + currentMember.getFullName());
            System.out.println("Farvel og tak.");
            login();
        } catch (Exception e) {
            System.out.println("Problem med at logge ind.");
            e.printStackTrace();
        }
    }

    public void saveProgram() {
        dataManager.saveMembers();
    }

    private void addUser() {
        try {
            int userId = dataManager.getMembers().getLast().getUserId() + 1;
            Portfolio portfolio = new Portfolio();
            LocalDate birthday = LocalDate.now();
            String fullName = "Dave";
            String email = "dave@gmail.com";
            double initialCash = 100000.0;
            LocalDate createdAt = LocalDate.now();
            LocalDate lastUpdated = LocalDate.now();
            Member member = new Member(
                    userId,
                    fullName,
                    email,
                    birthday,
                    initialCash,
                    createdAt,
                    lastUpdated,
                    portfolio
            );

            if (dataManager.getMembers().contains(member)) {
                System.out.println(member.getFullName() + " er allerede i listen. Man kan ikke have flere kontier noob.");
                return;
            }
            System.out.println("Tilføjer medlem: " + member.getFullName() + " til klubben.");
            dataManager.addMember(member);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeUser() {
        try {
            System.out.println("Printer liste af alle brugere:");
            System.out.println("==================================");
            System.out.println(dataManager.getMembers());
            System.out.println("==================================");
            System.out.println("Brug user ID nr'et til at fjerne den bruger du gerne vil have fjernet.");
            int userInput = sc.nextInt();
            dataManager.removeMember(userInput);
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Problem med at fjerne bruger. Tilbage til menu.");
            throw new RuntimeException(e);
        }
    }
    // ---------------------------------------------------------------------------------------
    // Private hjælpemetoder til alt fra at printe sektorer til at finde medlemmer og registrere aktier
    private void printMarketAndRates() {
        List<Stock> stockMarket = dataManager.getStocks();
        for (Stock stock : stockMarket) {
            System.out.println(stock.getName());
            System.out.println(stock.getPrice());
        }
    }

    private Map<String, Double> printSectorInvestmentDistribution() {
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
        printSectors(sectorAnalysis);
        return sectorAnalysis;
    }

    private void refreshPortfolioValue() {
        for (Member member : dataManager.getMembers() ) {
            if (member != null && member.getPortfolio() != null) {
            member.getPortfolio().calculateTotalValue(member, dataManager);
        }}

    }
    private void printSectors (Map<String, Double> sectorAnalysis) {
        Comparator<Map.Entry<String, Double>> byValueComparator =
                Map.Entry.comparingByValue(Comparator.reverseOrder()); // Kan være natural order hvis man gerne vil have det fra mindst til højest
        Map<String, Double> sortedMap = sectorAnalysis.entrySet().stream()
                .sorted(byValueComparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        System.out.println("--- Sektor investeringer (Højest til Lavest) ---");
        sortedMap.forEach((sector, investment) ->
                System.out.println("Investeret: " + String.format("%,.2f", investment) + " DKK (Sektor: " + sector + ")")
        );
    }

    /**
     * Finder vores medlem ved at bruge userId
     */
    public void findMember(int userId) {
        Member foundMember;
        List<Member> members = dataManager.getMembers();
        Optional<Member> memberOptional = members.stream()
                .filter(member -> member.getUserId() == userId)
                .findFirst();
        if (memberOptional.isPresent()) {
            foundMember = memberOptional.get();
            foundMember.printMember(dataManager, foundMember);
        }
    }

    public void registerStock() {
        int transactionId = -1;
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
          Transaction transaction = new Transaction(transactionId,currentMember.getUserId(),date,ticker,price,currency,orderType,quantity);
        dataManager.registerNewTransaction(transaction);
    }


    private void clubRankingInTotalValue() {
        refreshPortfolioValue();
        List<Member> members = dataManager.getMembers();
        members.sort((m1, m2) -> Double.compare(m2.getPortfolio().getTotalValue(), m1.getPortfolio().getTotalValue()));
        System.out.println();
        System.out.println("Klub rangering baseret på portfølje værdi:");
        System.out.println("================================================");
        for (int i = 1; i < members.size(); i++) {
            System.out.println("Nr: " + i + " Medlem: " + members.get(i).getFullName());
            System.out.println("Total værdi af portføljen: " + members.get(i).getPortfolio().getTotalValue() + " DKK");
        }
        System.out.println("================================================");
    }


    /**
     * Printer portføljen for alle medlemmer.
     */
    private void clubPortfolioOverview() {
        System.out.println("Klub portfølje oversigt\n ");
        System.out.println("================================================");
        for(Member element : dataManager.getMembers()) {
            System.out.println();
            System.out.println("Portfølje for medlem: " + element.getFullName());
            System.out.println("--------------------------------------------");
            element.printMember(dataManager, element);
            System.out.println("--------------------------------------------");
        }
        System.out.println("================================================");
    }

    private void printPresidentMenu() {
        System.out.println(
                "1. Se oversigt over brugernes porteføljeværdi\n" +
                        "2. Vis rangliste\n" +
                        "3. Vis fordelinger på aktier og sektorer\n" +
                        "4. Tilføj ny bruger\n" + "5. Fjern bruger\n" +
                        "6. Log ud\n" + "7. Skift bruger");
    }

    private void printMemberMenu() {
        System.out.println(
                "1. Se aktiemarkedet og aktuel kurs\n" +
                        "2. Registrer køb og salg af aktier\n" +
                        "3. Se portefølje\n" +
                        "4. Se transaktionshistorik\n" +
                        "5. Log ud\n" + "6. Skift bruger" );
    }



}

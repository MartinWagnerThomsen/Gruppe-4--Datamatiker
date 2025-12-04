package Filehandling;

import Api.CurrencyFetcher;
import DataObjects.Currency;
import DataObjects.Portfolio;
import Exceptions.CsvParsingException;
import DataObjects.Stock;
import DataObjects.Transaction;
import Users.Member;
import Users.President;

import javax.xml.xpath.*;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static final String MEMBERS_FILE = "Investeringsklubben/src/Files/users.csv";
    private static final String STOCKS_FILE = "Investeringsklubben/src/Files/stockMarket.csv";
    private static final String TRANSACTIONS_FILE = "Investeringsklubben/src/Files/transactions.csv";
    private static final String CURRENCY_FILE = "Investeringsklubben/src/Files/currency.csv";
    private static final String LOGIN_FILE = "Investeringsklubben/src/Files/loginCredentials.csv";
    private static final String danishkrone = "DKK";

    private List<Member> members;
    private List<Stock> stocks;
    private List<Transaction> transactions;
    private static List<Currency> currencies;
    private final CsvHandler csvHandler;
    private static List<Member> login;
    public DecimalFormat numberFormat = new DecimalFormat("#.00");

    /**
     * Konstruktøren initialiserer vores lister i starten for at undgå
     * NullPointerExceptions
     */
    public DataManager() {
        this.csvHandler = new CsvHandler();
        this.members = new ArrayList<>();
        this.stocks = new ArrayList<>();
        loadAllData();
    }

    /**
     * Indlæser al data fra CSV-filer og bygger den interne datamodel.
     * Denne metode orkestrerer hele processen.
     */
    public void loadAllData() {
        System.out.println("Indlæser data fra filer...");
        try {
            // 1. Indlæs rådata fra filer
            List<Member> loadedMembers = csvHandler.readMembers(MEMBERS_FILE);
            List<Stock> loadedStocks = csvHandler.readStocks(STOCKS_FILE);
            List<Transaction> allTransactions = csvHandler.readTransactions(TRANSACTIONS_FILE);
            List<Currency> loadedCurrencies = csvHandler.readCurrency(CURRENCY_FILE);
            List<Member> loadedLogins = csvHandler.parseloginCredentials();

            // 2. Gem primær data i DataManager's state
            this.members = loadedMembers;
            this.stocks = loadedStocks;
            currencies = loadedCurrencies;
            this.transactions = allTransactions;
            login = loadedLogins;
            // 3. Prøv at opdatere vores currencies fra vores API kald
            System.out.println("Opdaterer valutakurser fra Nationalbanken...");
            updateCurrencies();
            // 4. Link transaktioner til medlemmer
            linkTransactionsToMembers(allTransactions);
            System.out.println("Data indlæst successfuldt.");
        } catch (CsvParsingException e) {
            System.err.println("KRITISK FEJL under indlæsning af data: " + e.getMessage());
            System.err.println("Applikationen kan ikke starte korrekt. Tjek filformater.");
        }
    }


   public Member loadUserByUsername(String username) {
       // 1. Find the user's assigned role from the login credentials file.
       List<Member> loginDetailsList = csvHandler.parseloginCredentials(); // You already have this
       String userRole = loginDetailsList.stream()
               .filter(loginUser -> username.equalsIgnoreCase(loginUser.getEmail()))
               .map(Member::getUserType) // Extracts the userType string ("president" or "member")
               .findFirst()
               .orElse("member"); // Default to "member" if something goes wrong
       // 2. Find the user's full data record from the main users file.
       Member fullUserData = this.members.stream()
               .filter(user -> username.equalsIgnoreCase(user.getEmail()))
               .findFirst()
               .orElse(null);

       if (fullUserData == null) {
               System.out.println("FATAL ERROR: User exists in login file but not in main user data file.");
               return null;
           }
       // 3. Now, build the correct object based on the role.
       Portfolio userPortfolio = fullUserData.getPortfolio();
       if ("president".equalsIgnoreCase(userRole)) {
               // If the role is "president", we create a new President object.
               return new President(
                           this, // The DataManager itself
                           fullUserData.getUserId(),
                           fullUserData.getFullName(),
                           fullUserData.getEmail(),
                           fullUserData.getBirthday(),
                           fullUserData.getInitialCash(),
                           fullUserData.getCreationLocalDate(),
                           fullUserData.getLastUpdated(),
                          userPortfolio // You would load the user's specific portfolio here
               );
           } else {
               return new Member(
                           this, // The DataManager itself
                           fullUserData.getUserId(),
                           fullUserData.getFullName(),
                           fullUserData.getEmail(),
                          fullUserData.getBirthday(),
                           fullUserData.getInitialCash(),
                           fullUserData.getCreationLocalDate(),
                           fullUserData.getLastUpdated(),
                           userPortfolio // You would load the user's specific portfolio here
              );
           }
   }
    /**
     * Hjælpemetode til at forbinde transaktioner med den korrekte bruger.
     */
    private void linkTransactionsToMembers(List<Transaction> transactions) {
        Map<Integer, Member> memberMap = this.members.stream()
                .collect(Collectors.toMap(Member::getUserId, member -> member));
        for (Transaction transaction : transactions) {
            Member member = memberMap.get(transaction.getUserId());
            if (member != null) {
                member.getPortfolio().addTransaction(transaction);
            }
        }
    }

    /**
     * Registrerer en ny transaktion og sikrer, at alt data gemmes korrekt.
     */
    public void registerNewTransaction(Transaction transaction) {

        // Find det relevante medlem
        Member memberToUpdate = members.stream()
                .filter(m -> m.getUserId() == transaction.getUserId())
                .findFirst()
                .orElse(null); // Overvej at tilføj NA?

        if (memberToUpdate == null) {
            System.err.println("Kunne ikke registrere transaktion: Medlem med ID " + transaction.getUserId() + " ikke fundet.");
            return;
        }
        // 1. Tilføj transaktion til porteføljen i hukommelsen
        memberToUpdate.getPortfolio().addTransaction(transaction);
        // 1.2
        String currency = transaction.getCurrency();

        // Her kunne vi faktisk sætte dens valuta til at være DKK fra det som den var før?
        if (!currency.equalsIgnoreCase("DKK")) {
            convertToDkk(transaction);
        //    transaction.setCurrency("DKK");
        }
        // 2. Opdater medlemmets 'cash' (skal implementeres)
        if (transaction.getOrderType().equals("buy")) {

        }
        // 3. Sæt 'lastUpdated' dato
        memberToUpdate.setLastUpdated(LocalDate.now());

        try {
            // 4. Gem den nye transaktion til filen
            csvHandler.appendTransaction(TRANSACTIONS_FILE, transaction);

            // 5. Gem HELE medlemslisten for at opdatere cash og lastUpdated
            csvHandler.writeAllMembers(MEMBERS_FILE, this.members);

            csvHandler.writeCurrencies(CURRENCY_FILE, currencies);

            System.out.println("Transaktion registreret og gemt for " + memberToUpdate.getFullName());

        } catch (IOException e) {
            System.err.println("KRITISK FEJL under gemning af transaktion: " + e.getMessage());
            // Her bør man overveje en strategi for at håndtere fejl under skrivning.
        }
    }


    /**
     * Tager en aktie som input
     * Derefter hentes vores valutakurser fra DataManager
     * Hvis aktiens valuta matcher en af vores kurser så hentes raten
     * Prisen af aktien ganges derefter med raten for at finde prisen i DKK
     * Resultatet bliver gemt på aktien samt dens aktie valuta gemmes som DKK
     *
     * @param stockTransaction
     */
    public void convertToDkk(Transaction stockTransaction) {
        // Vi har brug for at få vores rater
        List<Currency> listOfCurrenciesAndRates = getCurrencies();
        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stockTransaction.getCurrency();
        double stockPricePrStock = stockTransaction.getPrice();
        double stockQuantity = stockTransaction.getQuantity();
        System.out.println(stockCurrency);
        if (!stockCurrency.equalsIgnoreCase("DKK")) {
            for (Currency currency : listOfCurrenciesAndRates) {
                if (stockCurrency.equalsIgnoreCase(currency.getBaseCurr())) {
                    System.out.println("Found the currency in our list: " + currency);
                    System.out.println("Converting " + stockTransaction.getTicker() + " at the current price pr stock: " + stockPricePrStock + " " + stockCurrency + " to " + currency.getRate());
                    double totalStockPrice = stockPricePrStock * stockQuantity;
                    double totalPriceAfterConversion = totalStockPrice * currency.getRate();
                    System.out.println("Result of the conversion of " + stockQuantity + " " + stockCurrency + " to " + numberFormat.format(totalPriceAfterConversion) + danishkrone);
                }
            }
        }
    }


   public void saveMembers() {
       try {
           csvHandler.writeAllMembers(MEMBERS_FILE, this.members);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
       }
   public void saveLogin() {
        try {
            csvHandler.writeLogins(LOGIN_FILE, this.getLogin());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
   }

    /**
     * Kalder vores CurrencyFetcher klasses metode for at få
     * valutakurser fra Nationalbanken
     */
    private void updateCurrencies() {
        CurrencyFetcher fetcher = new CurrencyFetcher();
        String xmlData = fetcher.fetchCurrencyData();
        if (xmlData != null && !xmlData.isEmpty()) {
            List<Currency> fetchedCurrencies = fetcher.parseCurrencyXml(xmlData);
            // Opdater eksisterende kurser eller tilføj nye
            for (Currency fetchedCurrency : fetchedCurrencies) {
                // Find om vi allerede har denne valuta
                Optional<Currency> existingCurrencyOpt = currencies.stream()
                        .filter(currency -> currency.getBaseCurr().equalsIgnoreCase(fetchedCurrency.getBaseCurr()))
                        .findFirst();
                if (existingCurrencyOpt.isPresent()) {
                    // Opdater rate og dato for eksisterende valuta
                    Currency existingCurrency = existingCurrencyOpt.get();
                    existingCurrency.setRate(fetchedCurrency.getRate());
                    existingCurrency.setLastUpdated(fetchedCurrency.getLastUpdated());
                    //      System.out.println("Opdateret kurs for " + existingCurrency.getBaseCurr() + " til " + existingCurrency.getRate());
                } else {
                    // Tilføj ny valuta, hvis den ikke findes
                    currencies.add(fetchedCurrency);
                    //    System.out.println("Tilføjet ny kurs for " + fetchedCurrency.getBaseCurr());
                }
            }
            try {
                csvHandler.writeCurrencies(CURRENCY_FILE, currencies);
                System.out.println("Valutakurser gemt til fil.");
            } catch (IOException e) {
                System.err.println("Fejl under skrivning af opdaterede valutakurser til fil: " + e.getMessage());
            }
        }
    }


    // --- Getters til UI-laget ---
    public Member getMember(String email) {
        for (Member member : members) {
            if (email.equals(member.getEmail())) {
                return member;
            }
        }
        return null;
    }

    public List<Member> getLogin () {
        return login;
    }

    public List<Member> getMembers() {
        return members;
    }
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void addMember(Member member) throws IOException {
        members.add(member);
        csvHandler.writeAllMembers(MEMBERS_FILE, members);
    }

    public void removeMember(int userId) throws IOException {
        members.removeIf(member -> member.getUserId() == userId);
        csvHandler.writeAllMembers(MEMBERS_FILE, members);

        }

}
    
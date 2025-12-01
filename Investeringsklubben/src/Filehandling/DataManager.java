package Filehandling;

import DataObjects.Currency;
import Exceptions.CsvParsingException;
import DataObjects.Stock;
import DataObjects.Transaction;
import Users.Member;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DataManager {
    private static final String MEMBERS_FILE = "Investeringsklubben/src/Files/users.csv";
    private static final String STOCKS_FILE = "Investeringsklubben/src/Files/stockMarket.csv";
    private static final String TRANSACTIONS_FILE = "Investeringsklubben/src/Files/transactions.csv";
    private static final String CURRENCY_FILE = "Investeringsklubben/src/Files/currency.csv";

    private List<Member> members;
    private List<Stock> stocks;
    private List<Currency> currencies;
    private final CsvHandler csvHandler;

    DecimalFormat numberFormat = new DecimalFormat("#.00");



    // Initialiser lister for at undgå NullPointerException, selv hvis indlæsning fejler
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

            // 2. Gem primær data i DataManager's state
            this.members = loadedMembers;
            this.stocks = loadedStocks;
            this.currencies = loadedCurrencies;

            // 3. Link transaktioner til medlemmer
            linkTransactionsToMembers(allTransactions);

            System.out.println("Data indlæst successfuldt.");

        } catch (CsvParsingException e) {
            System.err.println("KRITISK FEJL under indlæsning af data: " + e.getMessage());
            System.err.println("Applikationen kan ikke starte korrekt. Tjek filformater.");
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

        if (!currency.equals("DKK")) {
            convertToDkk(transaction);
        }

        // 2. Opdater medlemmets 'cash' (skal implementeres)

        if (transaction.getOrderType().equals("buy")) {


        }
        double cost = transaction.getPrice() * transaction.getQuantity();
         memberToUpdate.setCash(memberToUpdate.getInitialCash() - cost);

        // 3. Sæt 'lastUpdated' dato
        memberToUpdate.setLastUpdated(LocalDate.now());

        try {
            // 4. Gem den nye transaktion til filen
            csvHandler.appendTransaction(TRANSACTIONS_FILE, transaction);

            // 5. Gem HELE medlemslisten for at opdatere cash og lastUpdated
            csvHandler.writeAllMembers(MEMBERS_FILE, this.members);

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
     * @param stockTransaction
     */
    public void convertToDkk (Transaction stockTransaction) {
        String danishkrone = "DKK";
        // Vi har brug for at få vores rater
        DataManager manager = new DataManager();
        List<Currency> listOfCurrenciesAndRates = manager.getCurrencies();

        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stockTransaction.getCurrency();
        double stockPricePrStock = stockTransaction.getPrice();
        double stockQuantity = stockTransaction.getQuantity();
        System.out.println(stockCurrency);

        if(!stockCurrency.equalsIgnoreCase("DKK")) {
            for (Currency currency : listOfCurrenciesAndRates){
                if (stockCurrency.equalsIgnoreCase(currency.getBaseCurr())) {
                    System.out.println("Found the currency in our list: " + currency);
                    System.out.println("Converting " + stockTransaction.getTicker() + " at the current price pr stock: " +
                            "" + stockPricePrStock + " " + stockCurrency + " to " + currency.getRate());

                    double totalStockPrice = stockPricePrStock * stockQuantity;
                    double totalPriceAfterConversion = totalStockPrice * currency.getRate();
                    System.out.println("Result of the conversion of " + stockQuantity + " " + stockCurrency + " to " + numberFormat.format(totalPriceAfterConversion) + danishkrone);


                }
            }

        }




    }

    // --- Getters til UI-laget ---
    public List<Member> getMembers() {
        return members;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
    public List<Currency> getCurrencies() {
        return currencies;
    }
}
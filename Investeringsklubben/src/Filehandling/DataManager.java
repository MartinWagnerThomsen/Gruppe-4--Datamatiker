package Filehandling;

import DataObjects.Stock;
import DataObjects.Transaction;
import Users.Member;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    // Centraliser filstier
    private static final String MEMBERS_FILE = "Investeringsklubben/src/Files/users.csv";
    private static final String STOCKS_FILE = "Investeringsklubben/src/Files/stockMarket.csv";
    private static final String TRANSACTIONS_FILE = "Investeringsklubben/src/Files/transactions.csv";

    private List<Member> members;
    private List<Stock> stocks;
    private final CsvHandler csvHandler;

    public DataManager() {
        this.csvHandler = new CsvHandler();
        // Initialiser lister for at undgå NullPointerException, selv hvis indlæsning fejler
        this.members = new ArrayList<>();
        this.stocks = new ArrayList<>();
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

            // 2. Gem primær data i DataManager's state
            this.members = loadedMembers;
            this.stocks = loadedStocks;

            // 3. Udfør forretningslogik: Link transaktioner til medlemmer
            linkTransactionsToMembers(allTransactions);

            System.out.println("Data indlæst successfuldt.");

        } catch (CsvParsingException e) {
            System.err.println("KRITISK FEJL under indlæsning af data: " + e.getMessage());
            System.err.println("Applikationen kan ikke starte korrekt. Tjek filformater.");
            // I en rigtig applikation ville man måske lukke programmet her
            // System.exit(1);
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
                // Her antager vi at Member har en getPortfolio() metode
                // der returnerer et Portfolio objekt, som har en addTransaction() metode.
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
                .orElse(null);

        if (memberToUpdate == null) {
            System.err.println("Kunne ikke registrere transaktion: Medlem med ID " + transaction.getUserId() + " ikke fundet.");
            return;
        }

        // 1. Tilføj transaktion til porteføljen i hukommelsen
        memberToUpdate.getPortfolio().addTransaction(transaction);

        // 2. Opdater medlemmets 'cash' (skal implementeres)
        // double cost = transaction.getPrice() * transaction.getQuantity();
        // memberToUpdate.setCash(memberToUpdate.getCash() - cost);

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

    // --- Getters til UI-laget ---
    public List<Member> getMembers() {
        return members;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
}
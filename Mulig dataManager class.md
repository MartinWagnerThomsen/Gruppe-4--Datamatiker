---
title: Mulig implementering af DataManager og CsvHandler
tags:
  - gemini-review
  - refactoring
  - java
  - csv-handling
  - datamanagement
  - code-example
---

# Mulig implementering af DataManager og CsvHandler

Her er et forslag til en fuld implementering af de nye klasser, skræddersyet til jeres eksisterende projekt. Koden er mere komplet og inkluderer logik til at konvertere jeres objekter til og fra CSV-format.

**Anbefalet Pakkestruktur:**
For at forbedre organiseringen kan I overveje følgende pakker:
- `dk.investeringsklubben` (eller lignende rod-pakke)
  - `model` (for `Member`, `Stock`, `Portfolio` etc.)
  - `data` (for `CsvHandler`, `DataManager`, `CsvParsingException`)
  - `ui` (for `Main`, `ClubMenu` etc.)

---
## `CsvParsingException.java`
Dette er en simpel, men vigtig klasse for robust fejlhåndtering.

```java
package dk.investeringsklubben.data;

/**
 * En custom exception, der kastes, når der sker en fejl under parsing af en CSV-fil.
 * Dette kan skyldes I/O fejl eller formatfejl i dataen.
 */
public class CsvParsingException extends Exception {
    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

---
## `CsvHandler.java` (Refactored)
Denne version er "stateless" og har kun ansvaret for at læse og skrive til filer.

```java
package dk.investeringsklubben.data;

import dk.investeringsklubben.model.*; // Erstat med jeres faktiske model-pakke

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CsvHandler {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String SEPARATOR = ";";

    /**
     * Læser en fil med brugere og returnerer en liste af Member-objekter.
     */
    public List<Member> readMembers(String filePath) throws CsvParsingException {
        List<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Spring header over
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 7) {
                    int userId = Integer.parseInt(parts[0]);
                    String fullName = parts[1];
                    String email = parts[2];
                    LocalDate birthday = LocalDate.parse(parts[3], FORMATTER);
                    double initialCash = Double.parseDouble(parts[4]);
                    LocalDate createdAt = LocalDate.parse(parts[5], FORMATTER);
                    LocalDate lastUpdated = LocalDate.parse(parts[6], FORMATTER);
                    // Opret et tomt portfolio; det fyldes senere af DataManager
                    Portfolio portfolio = new Portfolio(); 
                    members.add(new Member(userId, fullName, email, birthday, initialCash, createdAt, lastUpdated, portfolio));
                } else {
                     throw new CsvParsingException("Forkert antal kolonner i medlemsfil: " + line, null);
                }
            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new CsvParsingException("Kunne ikke læse eller parse medlemsfil: " + filePath, e);
        }
        return members;
    }

    /**
     * Læser en fil med transaktioner.
     */
    public List<Transaction> readTransactions(String filePath) throws CsvParsingException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Spring header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 8) {
                    int transactionId = Integer.parseInt(parts[0]);
                    int userId = Integer.parseInt(parts[1]);
                    LocalDate date = LocalDate.parse(parts[2], FORMATTER);
                    String ticker = parts[3];
                    double price = Double.parseDouble(parts[4].replace(',', '.'));
                    String currency = parts[5];
                    String orderType = parts[6];
                    int quantity = Integer.parseInt(parts[7]);
                    transactions.add(new Transaction(transactionId, userId, date, ticker, price, currency, orderType, quantity));
                } else {
                     throw new CsvParsingException("Forkert antal kolonner i transaktionsfil: " + line, null);
                }
            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new CsvParsingException("Kunne ikke læse eller parse transaktionsfil: " + filePath, e);
        }
        return transactions;
    }
    
    // Antager at parse-metoderne i Currency og StockExchange er statiske, som i den oprindelige kode
    public List<Stock> readStocks(String filePath) throws CsvParsingException {
        List<Stock> stocks = new ArrayList<>();
         try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // Spring header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                if (parts.length == 9) {
                    String ticker = parts[0];
                    String name = parts[1];
                    String sector = parts[2];
                    double price = Double.parseDouble(parts[3].replace(',', '.'));
                    Currency currency = Currency.parseCurrency(parts[4]);
                    String rating = parts[5];
                    double dividendYield = Double.parseDouble(parts[6].replace(',', '.'));
                    StockExchange market = StockExchange.parseStockExchange(parts[7]);
                    LocalDate lastUpdated = LocalDate.parse(parts[8], FORMATTER);
                    stocks.add(new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated));
                } else {
                    throw new CsvParsingException("Forkert antal kolonner i aktiefil: " + line, null);
                }
            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new CsvParsingException("Kunne ikke læse eller parse aktiefil: " + filePath, e);
        }
        return stocks;
    }


    /**
     * Tilføjer en enkelt transaktion til slutningen af en CSV-fil.
     */
    public void appendTransaction(String filePath, Transaction transaction) throws IOException {
        String csvLine = convertToCsvLine(transaction);
        // StandardOpenOption.APPEND: tilføj til filen.
        // StandardOpenOption.CREATE: opret filen hvis den ikke eksisterer.
        Files.write(Paths.get(filePath), (csvLine + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }

    /**
     * Overskriver hele bruger-filen med en ny liste af medlemmer.
     */
    public void writeAllMembers(String filePath, List<Member> members) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("userId;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated"); // Header
        
        List<String> memberLines = members.stream()
                .map(this::convertToCsvLine)
                .collect(Collectors.toList());
        lines.addAll(memberLines);

        // StandardOpenOption.TRUNCATE_EXISTING: slet alt eksisterende indhold før skrivning.
        Files.write(Paths.get(filePath), lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    // --- Private konverterings-hjælpemetoder ---

    private String convertToCsvLine(Member m) {
        return String.join(SEPARATOR,
                String.valueOf(m.getUserId()),
                m.getFullName(),
                m.getEmail(),
                m.getBirthday().format(FORMATTER),
                String.valueOf(m.getInitialCash()).replace('.', ','), // Konverter tilbage til komma for CSV
                m.getCreationLocalDate().format(FORMATTER),
                m.getLastUpdated().format(FORMATTER)
        );
    }

    private String convertToCsvLine(Transaction t) {
        return String.join(SEPARATOR,
                String.valueOf(t.getTransactionId()),
                String.valueOf(t.getUserId()),
                t.getDate().format(FORMATTER),
                t.getTicker(),
                String.valueOf(t.getPrice()).replace('.',','), // Konverter tilbage til komma for CSV
                t.getCurrency(),
                t.getOrderType(),
                String.valueOf(t.getQuantity())
        );
    }
}
```

---
## `DataManager.java`
Denne klasse er nu "hjernen", der styrer dataflowet.

```java
package dk.investeringsklubben.data;

import dk.investeringsklubben.model.*; // Erstat med jeres faktiske model-pakke

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate; // Importer LocalDate

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
```
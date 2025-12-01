package Filehandling;

import DataObjects.*;
import DataObjects.Currency;
import Exceptions.CsvParsingException;
import Users.Member;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
                    double initialCash = Double.parseDouble(parts[4].replace(',', '.'));
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

    public List<Currency> readCurrency(String filePath) throws CsvParsingException {
        List<Currency> currencies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(SEPARATOR);
                String baseCurr = parts[0];
                String quote = parts[1];
                double rate = Double.parseDouble(parts[2].replace(',', '.'));
                LocalDate lastUpdated = LocalDate.parse(parts[3],FORMATTER);
                System.out.println(lastUpdated);
                Currency currency = new Currency(baseCurr, quote, rate, lastUpdated);
                currencies.add(currency);

            }
        } catch (IOException | NumberFormatException | java.time.format.DateTimeParseException e) {
            throw new CsvParsingException("Kunne ikke læse eller parse currency fil: " + filePath, e);
        }
        System.out.println(currencies);
        return currencies;
    }

    ;

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
                    String currency = parts[4];
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

        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(csvLine + "\n");
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + e.getMessage());
        }


    }

    /**
     * Overskriver hele bruger-filen med en ny liste af medlemmer.
     */
    public void writeAllMembers(String filePath, List<Member> members) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("user_Id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated"); // Header

        List<String> memberLines = members.stream()
                .map(this::convertToCsvLine)
                .collect(Collectors.toList());
        lines.addAll(memberLines);

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + e.getMessage());
        }


    }


    /**
     * Overskriver hele bruger-filen med en ny liste af medlemmer.
     */
    public void writeCurrencies(String filePath, List<Currency> currencies) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("base_currency;quote_currency;rate;last_updated"); // Header

        List<String> currencyLines = currencies.stream()
                .map(this::convertToCsvLine)
                .collect(Collectors.toList());
        lines.addAll(currencyLines);

        try (FileWriter writer = new FileWriter(filePath)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println("Fejl ved skrivning til fil: " + e.getMessage());
        }


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
                String.valueOf(t.getPrice()).replace('.', ','), // Konverter tilbage til komma for CSV
                t.getCurrency(),
                t.getOrderType(),
                String.valueOf(t.getQuantity())
        );
    }

    private String convertToCsvLine(Currency c) {
        return String.join(SEPARATOR,
                String.valueOf(c.getBaseCurr()),
                String.valueOf(c.getQuote()),
                String.valueOf(c.getRate()),
                String.valueOf(c.getLastUpdated()));
    }
}

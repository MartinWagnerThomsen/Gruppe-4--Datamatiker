package Filehandling;

import DataObjects.*;
import DataObjects.Currency;
import Users.Member;
import Users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
public class CsvHandler implements FileHandler {

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    private final static String memberData = "Investeringsklubben/src/Files/users.csv";
    private final static String stockmarket = "Investeringsklubben/src/Files/stockMarket.csv";
    private final static String transactions = "Investeringsklubben/src/Files/transactions.csv";

    private static ArrayList<Member> userList = new ArrayList<>();
    private static ArrayList<Transaction> transactionList = new ArrayList<>();
    private static ArrayList<Stock> listOfStocks = new ArrayList<>();



    public void readFile(String fileName, String dataObjectName) {
        switch(dataObjectName.toLowerCase()) {
            case "user":
                userList = parseUser(fileName);
                break;
            case "portfolio":
                System.out.println("Portfolio");
                parsePortfolio(fileName);
                break;
            case "stocks":
                listOfStocks = parseStock(fileName);
                for (Stock stock : listOfStocks) {
                    System.out.println(stock.toString());
                }
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    /**
     * Header for the parseUser
     * @params userId;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated
     */
    private ArrayList<Member> parseUser(String fileName) {
        ArrayList<Member> members = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // Den her linje den spiser Headeren (den første linje i arrayet)
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                    int userId = Integer.parseInt(parts[0]);
                    String fullName = parts[1];
                    String email = parts[2];
                    LocalDate birthday = LocalDate.parse(parts[3], formatter);
                    double initialCash = Double.parseDouble(parts[4]);
                    LocalDate createdAt = LocalDate.parse(parts[5], formatter);
                    LocalDate lastUpdated = LocalDate.parse(parts[6], formatter);
                    Portfolio portfolio = new Portfolio();
                    Member newMember = new Member(userId, fullName, email, birthday, initialCash, createdAt, lastUpdated, portfolio);
                    members.add(newMember);
    } } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    /**
     * It imports the transactions and creates Transaction
     * objects and appends them to a list of transactions (transactionList).
     * For every transactionId it then matches them on their userId.
     * Then adds the transactions associated with each unique user to their own
     * portfolio attribute.
     * @param fileName
     */
    private void parsePortfolio(String fileName) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 8) {
                        int transactionId = Integer.parseInt(parts[0]);
                        int userId = Integer.parseInt(parts[1]);
                        LocalDate date = LocalDate.parse(parts[2], formatter);
                        String ticker = parts[3];
                        double price = Double.parseDouble(parts[4].replace(',', '.'));
                        String currency = parts[5];
                        String orderType = parts[6];
                        int quantity = Integer.parseInt(parts[7]);
                        Transaction transaction = new Transaction(transactionId, userId, date,
                                ticker, price, currency, orderType, quantity);
                        transactionList.add(transaction);
                    }
                }
            } catch (IOException e) {
                System.out.println("Fejl ved læsning: " + e.getMessage());
            }
        for (Member member : userList) {
            for (Transaction transaction : transactionList) {
                if (member.getUserId() == transaction.getUserId()) {
                    member.addTransaction(transaction);
                }
            }
        }

        // Match userID med de respektive transactions ved at løbe igennem listen af users
            // Beregn derefter værdien af alle deres stocks UNIT TEST
            // Beregn difference uden % (dv.s hvor meget du har tjent/tabt fra baseline 100.000 (nuværende værdi - 100.000 for at finde forskellen) UNIT TEST
            // Beregn så deres difference (målt i % hvor meget de har vundet/tabt fra deres baseline som er 100.000) UNIT TEST

        }


    /**
     * Reads and parses stock data from a semicolon-separated values (CSV) file
     * and populates the {@code listOfStocks} field with {@link Stock} objects.
     *
     * <p>The file is expected to have a header row followed by data rows.
     * Each data row must contain nine semicolon-separated fields in the following order:
     * <ul>
     * <li><strong>ticker</strong> (String)</li>
     * <li><strong>name</strong> (String)</li>
     * <li><strong>sector</strong> (String)</li>
     * <li><strong>price</strong> (double, uses ',' as decimal separator)</li>
     * <li><strong>currency</strong> (String, parsed by {@link Currency#parseCurrency(String)})</li>
     * <li><strong>rating</strong> (String)</li>
     * <li><strong>dividend_yield</strong> (double, uses ',' as decimal separator)</li>
     * <li><strong>market</strong> (String, parsed by {@link StockExchange#parseStockExchange(String)})</li>
     * <li><strong>last_updated</strong> (String, parsed using the class's {@code formatter} field)</li>
     * </ul>
     *
     * @param fileName The path and name of the CSV file containing the stock data.
     * @return The {@code ArrayList} of {@link Stock} objects, which is the class's {@code listOfStocks} field,
     * populated with the data read from the file. Returns the current state of {@code listOfStocks}
     */
    private ArrayList<Stock> parseStock(String fileName) {
        System.out.println("I got so far");

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // eat the line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                System.out.println(line);
                if (parts.length == 9) {
                    String ticker = parts[0];
                    String name = parts[1];
                    String sector = parts[2];
                    double price = Double.parseDouble(parts[3].replace(',', '.'));
                    Currency currency = Currency.parseCurrency(parts[4]);
                    String rating = parts[5];
                    double dividendYield = Double.parseDouble(parts[6].replace(',', '.'));
                    StockExchange market = StockExchange.parseStockExchange(parts[7]);
                    LocalDate lastUpdated = LocalDate.parse(parts[8], formatter);
                    Stock stock = new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated);
                    listOfStocks.add(stock);
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
     return listOfStocks;
    }

    private String prettyPrint() {
        String result = userList.stream().map(User::getFullName).collect(Collectors.joining("  "));
        return result;
    }


    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void getUserData() {

    }
    public static ArrayList<Stock> getStockData() {
        return listOfStocks;
    }

    public static void main(String[] args) {
        CsvHandler handler = new CsvHandler();
        handler.readFile(stockmarket, "stocks");
         handler.readFile(memberData, "user");
        handler.parsePortfolio(transactions);
        userList.get(8).printMember();

    }
}

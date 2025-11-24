package Filehandling;

import DataObjects.Currency;
import DataObjects.Portfolio;
import DataObjects.Stock;
import DataObjects.StockExchange;
import Users.Member;
import Users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class CsvHandler implements FileHandler {


    private final static String Users = "Investeringsklubben/src/Files/users.csv";
    private final static String stockmarket = "Investeringsklubben/src/Files/stockMarket.csv";
    ArrayList<User> userList;
    ArrayList<Portfolio> portfolio;
    public static ArrayList<Stock> listOfStocks = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void readFile(String fileName, String dataObjectName) {

        switch(dataObjectName.toLowerCase()) {
            case "user":
            //    System.out.println("Something");
                parseUser(fileName);
                break;
            case "portfolio":
              //  System.out.println("Portfolio");
                parsePortifolio();
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

    private void parseUser(String fileName) {
    }

    private void parsePortifolio() {
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

    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void main(String[] args) {
        CsvHandler handle = new CsvHandler();
        handle.readFile(stockmarket, "stocks");
    }
}

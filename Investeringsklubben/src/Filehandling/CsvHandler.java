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
import java.time.LocalDate;
import java.util.ArrayList;

public class CsvHandler implements FileHandler {


    private final static String Users = "Investeringsklubben/src/Files/users.csv";
    ArrayList<User> userList;
    ArrayList<Portfolio> portfolio;
    ArrayList<Stock> stocks;

    public void readFile(String fileName, String dataObjectName) {

        switch(dataObjectName.toLowerCase()) {
            case "user":
                System.out.println("Something");
                parseUser(fileName);
                break;
            case "portfolio":
                System.out.println("Portfolio");
                parsePortifolio();
                break;
            case "stocks":
                System.out.println("stocks");
                parseStock(fileName);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    private void parseUser(String fileName) {
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
            return;
        }
        User[] users = new User[count];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 7) {
                    int userId = Integer.parseInt(parts[0]);
                    String full_name = parts[1];
                    String email = parts[2];
                    LocalDate birthday = LocalDate.parse(parts[3]);
                    double initialCash = Double.parseDouble(parts[4]);
                    LocalDate createdAt = LocalDate.parse(parts[5]);
                    LocalDate lastUpdated = LocalDate.parse(parts[6]);
                    // Portfolio portfolio = parts[7];
                    // user_id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated
                    users[i] = new Member(userId, full_name, email, birthday, initialCash, createdAt, lastUpdated);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
    }

    private void parsePortifolio() {
    }

    private void parseStock(String fileName) {
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
            return;
        }
        Stock[] stocks = new Stock[count];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 9) {
                    String ticker = parts[0];
                    String name = parts[1];
                    String sector = parts[2];
                    double price = Integer.parseInt(parts[3]);
                    Currency currency = Currency.parseCurrency(parts[4]);
                    String rating = parts[5];
                    double dividendYield = Double.parseDouble(parts[6]);
                    StockExchange market = StockExchange.parseStockExchange(parts[7]);
                    LocalDate lastUpdated = LocalDate.parse(parts[8]);
                    // stockMarket.csv ticker;name;sector;price;currency;rating;dividend_yield;market;last_updated
                    stocks[i] = new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated);
                    System.out.println(stocks[i]);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
        for (Stock stock : stocks) {
            System.out.println(stock.toString());
        }
    }

    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void main(String[] args) {
        CsvHandler handle = new CsvHandler();
        handle.parseStock("Investeringsklubben/src/Files/currency.csv");
    }
}

package Filehandling;
package Users;

import DataObjects.Portfolio;
import DataObjects.Stock;
import Users.Member;
import Users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

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
                parsePortifolio(fileName);
                break;
            case "stocks":
                System.out.println("stocks");
                parseStock();
                break;
            default:
                System.out.println("Invalid input");
                break;
        }


        // Først tæller vi linjer i filen
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
            return;
        }
        String[] files = new String[count];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 1) {
                    String userId = parts[0];
                    String full_name = parts[1];
                    String email = parts[2];
                    String birthdate = parts[3];
                    String initialCash = parts[4];
                    String createdAt = parts[5];
                    String lastUpdated = parts[6];
                    // user_id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated
                    files[i] = new String(userId, full_name, email, birthdate, initialCash, createdAt, lastUpdated);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
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

    private void parsePortifolio(String fileName){
        int count = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
            return;
        }
        Portfolio[] portfolios = new Portfolio[count];

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 2) {
                    double totalValue = Double.parseDouble(parts[0]);
                    double totalDifference = Double.parseDouble(parts[1]);
                    portfolios[i] = new Portfolio(totalValue, totalDifference);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
    }

    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void main(String[] args) {
        CsvHandler handler = new CsvHandler();
        handler.readFile(users,);
    }
}

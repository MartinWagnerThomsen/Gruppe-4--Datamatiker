package Filehandling;

import DataObjects.Portfolio;
import DataObjects.Stock;
import Users.Member;
import Users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class CsvHandler implements FileHandler {


    private final static String Users = "Investeringsklubben/src/Files/users.csv";
    ArrayList<User> userList;
    ArrayList<Portfolio> portfolio;
    ArrayList<Stock> stocks;

    public void readFile(String fileName, String dataObjectName) {

        switch(dataObjectName.toLowerCase()) {
            case "user":
                userList = parseUser(fileName);
                System.out.println("Something");
                System.out.println(userList);
                break;
            case "portfolio":
                System.out.println("Portfolio");
               // parsePortfolio();
                break;
            case "stocks":
                System.out.println("stocks");
               // parseStock();
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
    private ArrayList<User> parseUser(String fileName) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while (br.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
        User[] users = new User[count];
        System.out.println("What is the length of the user array: " + users.length);
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int i = 0;
            br.readLine(); // Den her linje den spiser Headeren (den første linje i arrayet)

            while ((line = br.readLine()) != null) {
                System.out.println("Line:" + line);
                String[] parts = line.split(";");
                if (parts.length == 8) {
                    int userId = Integer.parseInt(parts[0]);
                    String fullName = parts[1];
                    String email = parts[2];
                    LocalDate birthday = LocalDate.parse(parts[3]);
                    double initialCash = Double.parseDouble(parts[4]);
                    LocalDate createdAt = LocalDate.parse(parts[5]);
                    LocalDate lastUpdated = LocalDate.parse(parts[6]);
                    // Portfolio portfolio = parts[7];
                    users[i] = new Member(userId, fullName, email, birthday, initialCash, createdAt, lastUpdated);
                    System.out.println(users[i]);
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
        // Konverter vores Array til en Arraylist
        ArrayList<User> convertedUserList = new ArrayList<User>(Arrays.asList(users));
        for(User element : convertedUserList ) {
            System.out.println("Printing the convertedList:");
            System.out.println(element);
        }

            return convertedUserList;
    }

    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void main(String[] args) {
        CsvHandler handler = new CsvHandler();
        handler.readFile(Users, "user");
    }
}

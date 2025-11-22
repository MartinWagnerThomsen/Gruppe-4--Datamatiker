package Filehandling;

import DataObjects.Portfolio;
import DataObjects.Stock;
import Users.Member;
import Users.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CsvHandler implements FileHandler {


    private final static String memberData = "Investeringsklubben/src/Files/users.csv";
    ArrayList<Member> userList;
    ArrayList<Portfolio> portfolio;
    ArrayList<Stock> stocks;
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public void readFile(String fileName, String dataObjectName) {

        switch(dataObjectName.toLowerCase()) {
            case "user":
                userList = parseUser(fileName);
                System.out.println(userList.get(1).getBirthday());
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
                    Member newMember = new Member(userId, fullName, email, birthday, initialCash, createdAt, lastUpdated);
                    members.add(newMember);
            }
        } catch (IOException e) {
            System.out.println("Fejl ved læsning: " + e.getMessage());
        }
        return members;
    }

    public void writeFile(String fileName, String objectDataName) {

    };

    static void convertToString() {

    }

    static void convertToDataTypes() {

    }

    public static void main(String[] args) {
        CsvHandler handler = new CsvHandler();
        handler.readFile(memberData, "user");

    }
}

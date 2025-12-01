import DataObjects.Stock;
import DataObjects.Transaction;
import Exceptions.CsvParsingException;
import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;


public class Club {
    Scanner sc = new Scanner(System.in);
    CsvHandler csvhandler = new CsvHandler();
    DataManager dataManager = new DataManager();
    Member currentMember;

    public static void main(String[] args) {
        Club investmentClub = new Club();
        investmentClub.mainLoop();
        investmentClub.login();
    }

    public void login() {
        /*
        tempMemberList bruger en mindre konstruktør og sørger for at det som brugeren logger ind som passer
        der bliver senere retuneret en Member med den fulde konstruktør
         */
        List<Member> tempMemberList = csvhandler.parseloginCredentials();
        System.out.println("Velkommen. Tast brugernavn og adgangskode.");

        System.out.print("Brugernavn: ");
        String username = sc.nextLine();
        for (Member member : tempMemberList) {
            if (username.equals(member.getEmail())) {
                System.out.print("Adgangskode: ");
                String password = sc.nextLine();
                if (password.equals(member.getPassword())) {
                    System.out.println("Logger dig ind som " + member.getUserType());
                    switch (member.getUserType().toLowerCase()) {
                        case "member":
                            try {
                                membersMenu(username);
                            } catch (CsvParsingException e) {
                                throw new RuntimeException(e);
                            }
                            break;
                        case "president":
                            presidentMenu();
                            break;
                    }
                }
            }
        }
    }

    private void presidentMenu() throws IllegalArgumentException {
        System.out.println(
                "1. Se oversigt over brugernes porteføljeværdi\n" +
                        "2. Vis rangliste\n" +
                        "3. Vis fordelinger på aktier og sektorer\n" +
                        "4. Tilføj ny bruger\n" + "5. Fjern bruger\n" +
                        "6. Log ud");

        switch (sc.nextLine()) {
            case "1":
                //getPortfolio(user);
                break;
            case "2":
                //getRankings();
                break;
            case "3":
                //getSector(getPortfolio(user));
                break;
            case "4":
                //addUser(getUserInfo());
                break;
            case "5":
                //removeUser(getUserInfo());
                break;
            case "6":
                //saveProgress();
                login();
                break;
            default:
                throw new IllegalArgumentException("Forket input");
        }
    }


    public void membersMenu() throws IllegalArgumentException {
        // Hvad medlemmerne kan
        System.out.println(
                "1. Se aktiemarkedet og aktuel kurs\n" +
                        "2. Registrer køb og salg af aktier\n" +
                        "3. Se portefølje\n" +
                        "4. Se transaktionshistorik\n" +
                        "5. Log ud");

        switch (sc.nextLine()) {
            case "1":
                //getStockMarket()
                List<Stock> stockMarket = dataManager.getStocks();
                for (Stock stock : stockMarket) {
                    System.out.println(stock.getName());
                    System.out.println(stock.getPrice());
                }
                break;
            case "2":
                //registerStock()
                break;
            case "3":
                //getPortfolio()
                break;
            case "4":
                //getTransactions()

                /*

                List<Transaction> transactions = dataManager.getTransactions();

                for (Transaction transaction : transactions) {
                    System.out.println(transaction.getDate());
                    System.out.println(transaction.getTicker());
                    System.out.println(transaction.getPrice());
                    System.out.println(transaction.getQuantity());
                }

                 */
                break;
            case "5":
                //logOut
                break;
            default:
                throw new IllegalArgumentException("Forket input");
        }
    }
}
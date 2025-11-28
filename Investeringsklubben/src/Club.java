import java.util.Scanner;

public class Club {
    private static final String adminUserName = "president@gmail.com";
    private static final String admingPassword = "Sauron";

    Scanner sc = new Scanner(System.in);

    String username;
    String password;

    public void login() {
        System.out.println("Velkommen. Tast brugernavn og kodeord.");

        System.out.print("Brugernavn:");
        username = sc.nextLine();
        System.out.print("Adgangskode:");
        password = sc.nextLine();

        // checker om brugeren er presidenten
        if (username.equals(adminUserName) && password.equals(admingPassword)) {
            System.out.println("Logger ind som president");
            presidentMenu();
        } else if (InvestmentClubFacade.credentialsValidation(username, password)) {
            System.out.println("Logger ind som member");
            membersMenu();
        }
    }

    private Portfolio getPortfolio(int userId) {
        ArrayList<Member> memberList = CsvHandler.getUserData();
        for (Member member : memberList) {
            if (userId == member.getUserId()){
                return member.getPortfolio();
            }
        }
        return null;
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
                ArrayList<Stock> stockMarket = InvestmentClubFacade.fetchStockData();
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
                ArrayList<Transaction> transactions = InvestmentClubFacade.fetchStockData();
                for (Transaction transaction : transactions) {
                    System.out.println(transaction.getDate());
                    System.out.println(transaction.getTicker());
                    System.out.println(transaction.getPrice());
                    System.out.println(transaction.getQuantity());
                }
                break;
            case "5":
                //logOut
                menu();
                break;
            default:
                throw new IllegalArgumentException("Forket input");
        }
    }
}
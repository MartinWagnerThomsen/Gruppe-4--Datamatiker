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

    private static void presidentMenu() {
        // Hvad end presidenten kan
    }

    public void membersMenu() {

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

    public void logout() {

    }

    public void switchUser() {

    }
}



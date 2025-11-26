import Filehandling.CsvHandler;
import Filehandling.FileHandler;
import Users.Member;

import java.util.ArrayList;

import com.sun.tools.javac.Main;

public class Club {
    static String adminUserName = "president@gmail.com";
    String password = "Sauron";


    public static void login() {
        System.out.println("Velkommen. Tast brugernavn og kodeord.");

        boolean isLoggedIn = false;

        while (!isLoggedIn) {

            // checker om brugeren er presidenten
            if (adminUserName.equals(adminUserName)) {
                System.out.println("Logger ind som president");
                isLoggedIn = true;

            } else {
                System.out.println("Prøver at logge ind som medlem");
    private final CsvHandler handler = new CsvHandler();
    public void login(){

            }
            // checker om brugeren ikke er presidenten
            if (!adminUserName.equals(adminUserName)) {
                System.out.println("Logger ind som medlem");
                isLoggedIn = true;

            } else {
                System.out.println("Forkert brugernavn eller kodeord.");
            }
        }


        System.out.println("1. Se oversigt over brugernes porteføljeværdi \n" +
                "2. Vis rangliste\n " +
                "3. Vis fordelinger på aktier og sektorer\n" +
                "4. Tilføj ny bruger\n" +
                "5. Fjern bruger\n" +
                "6. Log ud");
    }

    public void logout() {

    }

    public void switchUser() {

    }
    public static void main(String[] args) {
        Club investmentClub = new Club();
        InvestmentClubFacade facade = new InvestmentClubFacade();
        ArrayList<Member> activeMembers = facade.fetchUserData();


    }

    public static void main(String[] args) {
        login();
    }
}

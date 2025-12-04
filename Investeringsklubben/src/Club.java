import Filehandling.CsvHandler;
import Filehandling.DataManager;
import UI.Menu;
import Users.Member;
import Users.President;

import java.io.IOException;
import java.util.*;


public class Club {
    private final Scanner sc = new Scanner(System.in);
    private CsvHandler csvhandler = new CsvHandler();
    private DataManager dataManager;

    public Club(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public static void main(String[] args) throws IOException {
        DataManager dataManager = new DataManager();
        Club investmentClub = new Club(dataManager);

        while (true) {
            investmentClub.login();
            System.out.println("\n--- Du har logget ud. Klar til næste bruger. ---");

        }

    }
    // ---------------------------------------------------------------------------------------
    // Menu flows

//    private void presidentMenu(President president) throws IllegalArgumentException, IOException {
//        boolean quit = false;
//        while (!quit) {
//            president.printPresidentMenu();
//            switch (sc.nextLine()) {
//                case "1":
//                    president.clubPortfolioOverview();
//                    break;
//                case "2":
//                    president.clubRankingInTotalValue();
//                    break;
//                case "3":
//                    president.printSectorInvestmentDistribution();
//                    break;
//                case "4":
//                    president.addMember();
//                    break;
//                case "5":
//                    president.removeMember();
//                    break;
//                case "6":
//                    logOut();
//                    quit = true;
//                    break;
//                case "7":
//                    switchUser();
//                case "8":
//                    president.promoteToPresident();
//                default:
//                    throw new IllegalArgumentException("Forkert input");
//            }
//            saveProgram();
//            // Husk og gem programmet
//        }
//    }

//    public void membersMenu(Member member) throws IllegalArgumentException, IOException {
//        System.out.println("Logget ind som medlem: " + member);
//        boolean quit = false;
//        while (!quit) {
//            member.printMemberMenu();
//            switch (sc.nextLine()) {
//                case "1":
//                    member.printMarketAndRates();
//                    break;
//                case "2":
//                    member.registerStock();
//                    break;
//                case "3":
//                    findMember(member.getUserId());
//                    break;
//                case "4":
//                    member.viewTransactionHistory();
//                    break;
//                case "5":
//                    logOut();
//                    quit = true;
//                    break;
//                case "6":
//                    switchUser();
//                default:
//                    throw new IllegalArgumentException("Forkert input");
//            }
//            saveProgram(); // Husk og gem programmet
//        }
//    }

    /**
     tempMemberList bruger en mindre konstruktør og sørger for at det som brugeren logger ind som passer
     der bliver senere retuneret en Member med den fulde konstruktør
     */
    public void login() throws IOException {
        List<Member> tempMemberList = csvhandler.parseloginCredentials();
        System.out.println("Velkommen. Tast brugernavn og adgangskode.");
        System.out.print("Brugernavn: ");
        String username = sc.nextLine();
        for (Member tempMember : tempMemberList) {
                     if (username.equals(tempMember.getEmail())) {
                             System.out.print("Adgangskode: ");
                             String password = sc.nextLine();
                             if (password.equals(tempMember.getPassword())) {
                                     System.out.println("Logger dig ind som " + tempMember.getUserType());
                                     Member currentUser = dataManager.loadUserByUsername(username);
                                     Menu userMenu = currentUser.getMenu(this.sc);
                                     userMenu.run();
                                     logOut();
                                     return;
                                 }
                         }
                 }
         }

    public void logOut() {
        System.out.println("Logger ud som bruger...");
        System.out.println("Farvel og tak.");
    }
    public void switchUser() {
        try {
            System.out.println("Skifter bruger.... ");
            System.out.println("Farvel og tak.");
            login();
        } catch (Exception e) {
            System.out.println("Problem med at logge ind.");
            e.printStackTrace();
        }
    }

    public void saveProgram() {
        dataManager.saveMembers();
    }

    public void findMember(int userId) {
        Member foundMember;
        List<Member> members = dataManager.getMembers();
        Optional<Member> memberOptional = members.stream()
                .filter(member -> member.getUserId() == userId)
                .findFirst();
        if (memberOptional.isPresent()) {
            foundMember = memberOptional.get();
            foundMember.printMember(dataManager, foundMember);
        }
    }
}
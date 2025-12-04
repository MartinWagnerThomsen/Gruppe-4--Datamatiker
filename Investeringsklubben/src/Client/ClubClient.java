package Client;

import java.util.Optional;

public class ClubClient {
    private final ClubManager manager;
    private boolean isRunning = true;
    private boolean isLoggedIn = false;

    public ClubClient() {
        this.manager = new ClubManager();
    }

    public void run() {
        while (isRunning) {
            IClubAccessor accessor = login();
            printMenu(accessor);
        }
    }

    private IClubAccessor login() {
        while (true) {
            System.out.print("Brugernavn: ");
            String username = Parser.parseString();
            System.out.print("Adgangskode: ");
            String password = Parser.parseString();

            Optional<IClubAccessor> accessor = manager.login(username, password);
            if (accessor.isEmpty()) {
                System.out.println("Adgangskode eller brugernavn er forkert, prøv venligst igen.");
                continue;
            }

            isLoggedIn = true;
            return accessor.get();
        }
    }

    private void printMenu(IClubAccessor accessor) {
        if (accessor instanceof PresidentAccessor presidentAccessor) {
            printPresidentMenu(presidentAccessor);
        } else if (accessor instanceof MemberAccessor memberAccessor) {
            printMemberMenu(memberAccessor);
        } else {
            System.out.println("Denne Accessor er ikke implementeret endnu");
        }
    }

    private void printPresidentMenu(PresidentAccessor accessor) {
        while (isLoggedIn) {
            System.out.println(
                    "1. Se oversigt over brugernes porteføljeværdi\n" +
                            "2. Vis rangliste\n" +
                            "3. Vis fordelinger på aktier og sektorer\n" +
                            "4. Tilføj ny bruger\n" +
                            "5. Fjern bruger\n" +
                            "6. Skift bruger\n" +
                            "7. Log ud");

            switch (Parser.parseString()) {
                case "1":
                    accessor.clubPortfolioOverview();
                    break;
                case "2":
                    accessor.clubRankingInTotalValue();
                    break;
                case "3":
                    accessor.printSectorInvestmentDistribution();
                    break;
                case "4":
                    accessor.addUser();
                    break;
                case "5":
                    accessor.removeUser();
                    break;
                case "6":
                    isLoggedIn = false;
                    break;
                case "7":
                    isLoggedIn = false;
                    isRunning = false;
                    break;
            }
        }
    }

    private void printMemberMenu(MemberAccessor accessor) {
        while (isLoggedIn) {
            System.out.println(
                    "1. Se aktiemarkedet og aktuel kurs\n" +
                            "2. Registrer køb og salg af aktier\n" +
                            "3. Se portefølje\n" +
                            "4. Se transaktionshistorik\n" +
                            "5. Skift bruger\n" +
                            "6. Log ud ");

            switch (Parser.parseString()) {
                case "1":
                    accessor.printMarketAndRates();
                    break;
                case "2":
                    accessor.createTransaction();
                    break;
                case "3":
                    accessor.printPortfolio();
                    break;
                case "4":
                    accessor.viewTransactionHistory();
                    break;
                case "5":
                    isLoggedIn = false;
                    break;
                case "6":
                    isLoggedIn = false;
                    isRunning = false;
                    break;
            }
        }
    }
}

package Users;

import DataObjects.Portfolio;
import DataObjects.Stock;
import DataObjects.Transaction;
import Filehandling.DataManager;
import UI.Menu;
import UI.MenuItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class President extends Member implements Administrator {

    public President(DataManager dataManager, int userId, String fullName, String email, LocalDate birthday, double initialCash, LocalDate createdAt,
                     LocalDate lastUpdated, Portfolio portfolio) {
        super(dataManager,userId, fullName, email, birthday, initialCash, createdAt, lastUpdated, portfolio);
    }

     public Menu getMenu(Scanner scanner) {
                 String menuTitle = "====================  🏛️  PRÆSIDENT MENU  ====================";
                 List<MenuItem> menuItems = Arrays.asList(
                             new MenuItem("Se oversigt over brugernes porteføljeværdi", this::clubPortfolioOverview),
                             new MenuItem("Vis rangliste", this::clubRankingInTotalValue),
                             new MenuItem("Vis fordelinger på aktier og sektorer", this::printSectorInvestmentDistribution),
                             new MenuItem("Tilføj ny bruger", this::addMember),
                             new MenuItem("Fjern bruger", this::removeMember),
                             new MenuItem("Gør bruger til præsident", this::promoteToPresident)
                 );
                 return new Menu(menuTitle, menuItems, scanner);
             }

    @Override
    public void addMember() {

        int count = 0;
        for (Member member : dataManager.getMembers()) {
            if (member.getUserId() > count) {
                count = member.getUserId();
            }
        }
        try { int userId = count + 1;
            Portfolio portfolio = new Portfolio();
            System.out.print("Hvad er dit fulde navn? ");
            String fullName = sc.nextLine();
            System.out.print("Hvad er din mail? ");
            String email = sc.nextLine();
            System.out.print("Hvad er din fødselsdag? (dd-mm-yyyy): ");
            String birthdayInput = sc.nextLine();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate birthday = LocalDate.parse(birthdayInput, formatter);
            double initialCash = 100000;
            LocalDate createdAt = LocalDate.now();
            LocalDate lastUpdated = LocalDate.now();
            Member member = new Member(
                    userId,
                    fullName,
                    email,
                    birthday,
                    initialCash,
                    createdAt,
                    lastUpdated,
                    portfolio
            );

            if (dataManager.getMembers().contains(member)) {
                System.out.println(member.getFullName() + " er allerede i listen. Man kan ikke have flere kontier noob.");
                return;
            }
            System.out.println("Tilføjer medlem: " + member.getFullName() + " til klubben.");
            dataManager.addMember(member);
            dataManager.saveLogin();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMember() {
        try {
            System.out.println("Printer liste af alle brugere:");
            System.out.println("==================================");
            System.out.println(dataManager.getMembers());
            System.out.println("==================================");
            System.out.println("Brug user ID nr'et til at fjerne den bruger du gerne vil have fjernet.");
            int userInput = sc.nextInt();
            dataManager.removeMember(userInput);
            dataManager.saveLogin();
            sc.nextLine();
        } catch (Exception e) {
            System.out.println("Problem med at fjerne bruger. Tilbage til menu.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void promoteToPresident() {
        String president = "president";
        List<Member> userLogin = dataManager.getLogin();
        System.out.println("Promote user to president by writing the username (email) of the user.");
        System.out.println("Printing users:");
        for (Member member : userLogin) {
            System.out.println(member.getEmail());
        }
        try {
            String userInput = sc.next();
            for (Member member : userLogin) {
                if (member.getEmail().equalsIgnoreCase(userInput)) {
                    member.setUserType(president);
                    dataManager.saveLogin();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshPortfolioValue() {
        for (Member member : dataManager.getMembers() ) {
            if (member != null && member.getPortfolio() != null) {
                member.getPortfolio().calculateTotalValue(member, dataManager);
            }}
    }

    private void printSectors (Map<String, Double> sectorAnalysis) {
        Comparator<Map.Entry<String, Double>> byValueComparator =
                Map.Entry.comparingByValue(Comparator.reverseOrder()); // Kan være natural order hvis man gerne vil have det fra mindst til højest
        Map<String, Double> sortedMap = sectorAnalysis.entrySet().stream()
                .sorted(byValueComparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));
        System.out.println("--- Sektor investeringer (Højest til Lavest) ---");
        sortedMap.forEach((sector, investment) ->
                System.out.println("Investeret: " + String.format("%,.2f", investment) + " DKK (Sektor: " + sector + ")")
        );
    }

    public Map<String, Double> printSectorInvestmentDistribution() {
        // Deklarer variablerne
        List<Transaction> history = dataManager.getTransactions();
        List<Stock> stockHistory = dataManager.getStocks();
        // Vi skal bruge et Hashmap hvor vi har Sector som Key og Price som Value
        Map<String, Double> sectorAnalysis = new HashMap<>();
        // Prisen bliver initialiseret uden for loopet. Den
        double price = 0;
        // Først skal vi iterere over aktierne og dernæst transaktions historikken
        for (Stock investment : stockHistory) {
            for(Transaction transaction : history) {
                if (investment.getTicker().equalsIgnoreCase(transaction.getTicker())) {
                    if(transaction.getOrderType().equalsIgnoreCase("buy"))
                        price = transaction.getPrice() * transaction.getQuantity();
                    String sector = investment.getSector();
                    // Her tager vi og kalder en summariserings funktion over alle vores values
                    sectorAnalysis.merge(sector, price, Double::sum);
                }}}
        printSectors(sectorAnalysis);
        return sectorAnalysis;
    }



    public void clubRankingInTotalValue() {
        refreshPortfolioValue();
        List<Member> members = dataManager.getMembers();
        members.sort((m1, m2) -> Double.compare(m2.getPortfolio().getTotalValue(), m1.getPortfolio().getTotalValue()));
        System.out.println();
        System.out.println("Klub rangering baseret på portfølje værdi:");
        System.out.println("================================================");
        for (int i = 1; i < members.size(); i++) {
            System.out.println("Nr: " + i + " Medlem: " + members.get(i).getFullName());
            System.out.println("Total værdi af portføljen: " + members.get(i).getPortfolio().getTotalValue() + " DKK");
        }
        System.out.println("================================================");
    }

    /**
     * Printer portføljen for alle medlemmer.
     */
    public void clubPortfolioOverview() {
        System.out.println("Klub portfølje oversigt\n ");
        for(Member element : dataManager.getMembers()) {
            System.out.println();
            System.out.println("================================================");
            System.out.println("Portfølje for medlem: " + element.getFullName());
            element.printMember(dataManager, element);
            System.out.println("================================================");
        }
        System.out.println("================================================");
    }

}

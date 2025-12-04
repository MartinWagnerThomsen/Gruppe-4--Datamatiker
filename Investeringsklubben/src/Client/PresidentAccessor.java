package Client;

import DataObjects.Stock;
import DataObjects.Transaction;
import Users.Member;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PresidentAccessor implements IClubAccessor {
    private final ClubManager manager;

    public PresidentAccessor(ClubManager manager) {
        this.manager = manager;
    }

    void clubPortfolioOverview() {
        System.out.println("Klub portfølje oversigt ");
        System.out.println("================================================");
        for (Member member : manager.getMembersList()) {
            System.out.println();
            System.out.println("Portfølje for medlem: " + member.getFullName());
            System.out.println("--------------------------------------------");
            member.getPortfolio().printPortfolio();
            System.out.println("--------------------------------------------");
        }
        System.out.println("================================================");
    }

    void clubRankingInTotalValue() {
        List<Member> members = manager.getMembersList();
        members.sort((m1, m2) -> Double.compare(m2.getPortfolio().getTotalValue(), m1.getPortfolio().getTotalValue()));
        System.out.println(); // lille mellemrum
        System.out.println("Klub rangering baseret på portfølje værdi:");
        System.out.println("================================================");
        for (int i = 1; i < members.size(); i++) {
            System.out.println("Nr: " + i + " Medlem: " + members.get(i).getFullName());
            System.out.println("Total værdi af portføljen: " + members.get(i).getPortfolio().getTotalValue() + " DKK");
        }
        System.out.println("================================================");
    }

    Map<String, Double> printSectorInvestmentDistribution() {
        // Deklarer variablerne
        List<Transaction> history = manager.getTransactions();
        List<Stock> stockHistory = manager.getStocks();

        // Vi skal bruge et Hashmap hvor vi har Sector som Key og Price som Value
        Map<String, Double> sectorAnalysis = new HashMap<>();

        // Prisen bliver initialiseret uden for loopet. Den
        double price = 0;

        // Først skal vi iterere over aktierne og dernæst transaktions historikken
        for (Stock investment : stockHistory) {
            for (Transaction transaction : history) {
                if (investment.getTicker().equalsIgnoreCase(transaction.getTicker())) {
                    if (transaction.getOrderType().equalsIgnoreCase("buy"))
                        price = transaction.getPrice() * transaction.getQuantity();
                    String sector = investment.getSector();

                    // Her tager vi og kalder en summariserings funktion over alle vores values
                    sectorAnalysis.merge(sector, price, Double::sum);
                }
            }
        }
        manager.printSectors(sectorAnalysis);
        return sectorAnalysis;
    }

    void addUser() {
        System.out.print("Hvad er dit fulde navn? ");
        String fullName = Parser.parseString();

        System.out.print("Hvad er din mail? ");
        String email = Parser.parseString();

        System.out.print("Hvad er din fødselsdag? (dd-mm-yyyy): ");
        Optional<LocalDate> birthday = Parser.parseDate();
        if (birthday.isEmpty()) {
            System.out.println("Fødselsdag kan ikke være tom!");
            return;
        }

        manager.createMember(fullName, email, birthday.get());
    }

    void removeUser() {
        System.out.println("Printer liste af alle brugere:");
        System.out.println("==================================");
        System.out.println(manager.getMembersList());
        System.out.println("==================================");
        while (true) {
            System.out.println("Indtast bruger ID'et på det medlem du gerne vil fjerne.");
            Optional<Integer> userInput = Parser.parseInt();
            if (userInput.isEmpty()) {
                System.out.println("Felt må ikke være tomt!");
                continue;
            }
            manager.removeMember(userInput.get());
            break;
        }
    }
}

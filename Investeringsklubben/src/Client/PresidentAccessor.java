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
            System.out.println("========== " + member.getFullName() + "s Portefølje ==========");
            member.getPortfolio().printPortfolio();
            System.out.println("============================================");
        }
    }

    void clubRankingInTotalValue() {
        List<Member> members = manager.getMembersList();
        for (Member member : members) {
            member.getPortfolio().calculateTotalValue();
        }
        members.sort((m1, m2) -> Double.compare(m2.getPortfolio().getTotalValue(), m1.getPortfolio().getTotalValue()));
        System.out.println(); // lille mellemrum
        System.out.println("Klub rangering baseret på portfølje værdi:");
        System.out.println("================================================");
        for (int i = 0; i < members.size(); i++) {
            System.out.println("Nr: " + (i + 1) + " Medlem: " + members.get(i).getFullName());
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


        // Først skal vi iterere over aktierne og dernæst transaktions historikken
        for (Stock investment : stockHistory) {
            for (Transaction transaction : history) {
                if (investment.getTicker().equalsIgnoreCase(transaction.getTicker())) {
                    // istedet for at vi laver prisen uden for loopet laver vi den kun hvis vi finder den rigtige ticker
                    double price = transaction.getPrice() * transaction.getQuantity();

                    if (transaction.getOrderType().equalsIgnoreCase("buy")) {
                        sectorAnalysis.merge(investment.getSector(), price, Double::sum);
                    }
                    // fordi vi går igennem hele transaktion historikken skal vi også huske at trække fra når der er salg
                    if (transaction.getOrderType().equalsIgnoreCase("sell")) {
                        sectorAnalysis.merge(investment.getSector(), -price, Double::sum);
                    }
                    // pga vi også skal trække fra, var det nemmere bare at smide merge ind i if-statement
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

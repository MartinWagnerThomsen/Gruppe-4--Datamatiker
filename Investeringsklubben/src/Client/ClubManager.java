package Client;

import DataObjects.Portfolio;
import DataObjects.Stock;
import DataObjects.Transaction;
import Filehandling.CsvHandler;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ClubManager {
    private final CsvHandler csvHandler = new CsvHandler();
    private final DataManager dataManager = new DataManager();
    private int nextTransactionId = 0;
    private int nextUserId = 0;

    ClubManager() {
        // bestemmer det næste højeste transaktionsId
        List<Transaction> transactionIdHistory = dataManager.getTransactions();
        for (Transaction transaction : transactionIdHistory) {
            if (transaction.getTransactionId() < nextTransactionId)
                continue;

            nextTransactionId = transaction.getTransactionId() + 1;
        }
        // bestemmer det næste højeste userId
        List<Member> memberIdHistory = dataManager.getMembers();
        for (Member member : memberIdHistory) {
            if (member.getUserId() < nextUserId)
                continue;

            nextUserId = member.getUserId() + 1;
        }
    }

    public void saveProgram() {
        dataManager.saveMembers();
    }

    /**
     * Optional fordi den endte returner en PresidentAccessor, MemberAccessor eller ingenting
     *
     * @param username
     * @param password
     * @return
     */
    public Optional<IClubAccessor> login(String username, String password) {
        List<Member> tempMemberList = csvHandler.parseloginCredentials();
        return tempMemberList.stream()
                // filtrere for members email/username og adgangskode
                .filter(member -> member.getEmail().equals(username) && member.getPassword().equals(password))
                .findFirst()
                // returner den tilsvarende Accessor an på userType eller returner null hvis ingen er fundet
                .map(member -> {
                    if (member.getUserType().equalsIgnoreCase("president"))
                        return new PresidentAccessor(this);

                    if (member.getUserType().equalsIgnoreCase("member"))
                        return new MemberAccessor(this, dataManager.getMember(username));

                    return null;
                });
    }

    /**
     * @param userId
     * @param date
     * @param ticker
     * @param price
     * @param currency
     * @param orderType
     * @param quantity
     */
    public void createTransaction(int userId, LocalDate date, String ticker, double price, String currency, String orderType, int quantity) {
        Transaction transaction = new Transaction(nextTransactionId++, userId, date, ticker, price, currency, orderType, quantity);
        dataManager.registerNewTransaction(transaction);
    }

    /**
     * @return
     */
    public List<Stock> getStocks() {
        return dataManager.getStocks();
    }


    /**
     * @return
     */
    public List<Member> getMembersList() {
        return dataManager.getMembers();
    }

    /**
     * Kalder DataManagers addMember
     *
     * @param fullName
     * @param email
     * @param birthday
     */
    public void createMember(String fullName, String email, LocalDate birthday) {
        try {
            dataManager.addMember(new Member(nextUserId++, fullName, email, birthday, 100000, LocalDate.now(), LocalDate.now(), new Portfolio()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Printer en sorteret liste af de sektorer investerings klubben er investeret i
     *
     * @param sectorAnalysis
     */
    public void printSectors(Map<String, Double> sectorAnalysis) {
        Comparator<Map.Entry<String, Double>> byValueComparator =
                Map.Entry.comparingByValue(Comparator.reverseOrder()); // Kan være natural order hvis man gerne vil have det fra mindst til højest
        Map<String, Double> sortedMap = sectorAnalysis.entrySet().stream()
                .filter(entry -> entry.getValue() > 0) // transaktion filen er rodet så vi filtrere alt under nul ud
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

    /**
     * Kalder på DataManagers removeMember metode med userId
     *
     * @param userId
     */
    public void removeMember(Integer userId) {
        try {
            dataManager.removeMember(userId);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Transaction> getTransactions() {
        return dataManager.getTransactions();
    }

    /**
     * Retunere en liste transaktioner for medlemmet (userId)
     *
     * @param userId
     * @return
     */
    public List<Transaction> getTransactions(int userId) {
        return findMember(userId)
                .map(member -> member.getPortfolio().getTransactions())
                .stream()
                .flatMap(List::stream)
                .toList();
    }

    /**
     * Retunere endten en member eller intet
     *
     * @param userId
     * @return
     */
    public Optional<Member> findMember(int userId) {
        List<Member> members = dataManager.getMembers();
        return members.stream()
                .filter(member -> member.getUserId() == userId)
                .findFirst();
    }
}

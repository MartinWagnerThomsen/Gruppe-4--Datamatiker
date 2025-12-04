package DataObjects;

import Filehandling.DataManager;
import Users.Member;
import java.util.ArrayList;
import java.util.List;


public class Portfolio implements  Comparable<Portfolio> {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private List<Stock> investedStocks = new ArrayList<>();
    private double totalValue;
    private double totalDifference;


    public Portfolio() {
    }

    public Portfolio(double totalValue, double totalDifference){
        this.totalValue = totalValue;
        this.totalDifference = totalDifference;
    }
    public void showStockPrice(){
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction ) {
        this.transactions.add(transaction);
    }

    public void addTransactions(Transaction transaction ) {
        this.transactions.add(transaction);
    }

    /*
    * Lav en variabel "sum" som holder quantity af den pågældende aktie
    * Check om sum er lig med 0 - hvis det er den så skal den fjernes fra listen af akier
    * for det pågældende medlem
    * Hvis ikke den er nul skal quantity være resultatet af quantity - sum (sell) / quantity + sum (buy)
     Bagefter skal man regne ud hvad den reele pris af aktien er baseret på stockmarket.csv prisen som er vores
     * baseline pris (snapshot).
     * */
    public void calculateTotalValue(Member member, DataManager dataManager){
        //System.out.println("Calculating total value...");
        double balance = calculateCashBalance(member);
        double stocksValue = calculateInvestedStocks(member, dataManager);
        totalValue = balance + stocksValue;
        //System.out.println("Total value is: " + totalValue);
    }

    public double calculateCashBalance(Member member) {
        double cashBalance = member.getCashBalance(); // Start from the ORIGINAL gold reserves!

        for (Transaction transaction : transactions) {
            if (transaction.getOrderType().equalsIgnoreCase("buy")){
                cashBalance -= transaction.getQuantity() * transaction.getPrice();
            }
            else if (transaction.getOrderType().equalsIgnoreCase("sell")){
                cashBalance += transaction.getQuantity() * transaction.getPrice();
            }
        }

        return cashBalance; // Simply return! No writing to the member's records!
    }


    public double calculateInvestedStocks(Member member, DataManager dataManager) {
        List<Stock> listOfStocks = dataManager.getStocks();
        List<Transaction> memberTransactions = member.getPortfolio().transactions;
        double sum = 0;
        for (Transaction transaction : memberTransactions) {
            if (transaction.getOrderType().equalsIgnoreCase("buy")){
                for (Stock stocks : listOfStocks){
                    if (stocks.getTicker().equalsIgnoreCase(transaction.getTicker())){
                        investedStocks.add(stocks);
                        sum += stocks.getPrice() * transaction.getQuantity();
                    }
                }
            }
            if (transaction.getOrderType().equalsIgnoreCase("sell")){
                for (Stock stocks : listOfStocks){
                    if (stocks.getTicker().equalsIgnoreCase(transaction.getTicker())){
                        investedStocks.remove(stocks);
                        sum -= stocks.getPrice() * transaction.getQuantity();
                    }
                }

            }
            // Nu bør vi have en liste af de aktier vi har tilbage
        }

        return sum;
    }

    public void printInvestedStocks(Member member, DataManager dataManager) {
        System.out.println("\n" + member.getFullName() + " is currently invested in:");
        System.out.println("——————————————————————————————————————————————————");
        for(Stock stocks : investedStocks){
            System.out.println(stocks);
        }
        System.out.println("——————————————————————————————————————————————————");
        System.out.println("Sum value of invested stocks: " + calculateInvestedStocks(member, dataManager) + "\n");
    }

    public void registerStock(){

    }
    public void viewPortfolio(){

    }
    public void showPortfolioValue(){

    }

    //gevinst/tab = nuværende totalværdi - initial cash
    public void showDifference(Member member, DataManager dataManager){
        calculateTotalValue(member, dataManager);
        String symbol = "";
        double difference = totalValue - member.getInitialCash();
        if (difference >= 0){
            symbol = " ↗";
        } else {
            symbol = " ↘";
        }
        System.out.println("Gevinst/Tab: " + difference + symbol);
    }

    public void printPortfolio() {
        for (Transaction element : transactions) {
            System.out.println(element);
        }
    }
    public double getTotalValue(){
        return totalValue;
    }

    public int compareTo(Portfolio o) {
        // Stigende sortering efter værdi (laveste værdi først)
        return Double.compare(this.totalValue, o.totalValue);
    }

    @Override
    public String toString() {
        int portfolioNumber = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("--- Portfolio Transactions ---\n");
        for (Transaction t : transactions) {
            sb.append(portfolioNumber).append(": ");
            portfolioNumber++;
            sb.append(t.toString());
            sb.append("\n");
        }
        sb.append("Portfolio total value: ").append(totalValue).append(" DKK"); // Hardcoded Currency for now
        return sb.toString();
    }

}

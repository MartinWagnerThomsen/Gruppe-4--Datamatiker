package DataObjects;

import Filehandling.DataManager;
import Users.Member;

import java.util.ArrayList;
import java.util.List;


public class Portfolio implements Comparable<Portfolio> {
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private List<Stock> investedStocks = new ArrayList<>();
    private double totalValue;
    private double totalDifference;
    private double balance;
    private double stocksValue;
    private Member member;
    private DataManager dataManager;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public Portfolio() {
    }

    public Portfolio(double totalValue) {
        this.totalValue = totalValue;
        this.totalDifference = totalDifference;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        totalValue += transaction.getPrice();
        totalDifference += transaction.getPrice();
    }

    /*
    * Lav en variabel "sum" som holder quantity af den pågældende aktie
    * Check om sum er lig med 0 - hvis det er den så skal den fjernes fra listen af akier
    * for det pågældende medlem
    * Hvis ikke den er nul skal quantity være resultatet af quantity - sum (sell) / quantity + sum (buy)
     Bagefter skal man regne ud hvad den reele pris af aktien er baseret på stockmarket.csv prisen som er vores
     * baseline pris (snapshot).
     * */
    public void calculateTotalValue() {
        balance = calculateCashBalance();
        stocksValue = calculateInvestedStocks();
        totalValue = balance + stocksValue;
    }

    public double calculateCashBalance() {
        double cashBalance = member.getInitialCash();

        for (Transaction transaction : transactions) {

            if (transaction.getOrderType().equalsIgnoreCase("buy")) {
                cashBalance -= transaction.getQuantity() * transaction.getPrice();
            }
            if (transaction.getOrderType().equalsIgnoreCase("sell")) {
                cashBalance += transaction.getQuantity() * transaction.getPrice();
            }
        }
        member.setCashBalance(cashBalance);
        return member.getCashBalance();
    }


    public double calculateInvestedStocks() {
        List<Stock> listOfStocks = dataManager.getStocks();
        double totalInvestedInStocks = 0;
        investedStocks.clear(); // så vi ikke tilføjer oven på listen når vi tilføjer stocks

        for (Stock stock : listOfStocks) {
            int quantity = getQuantity(stock); // får fat i mængden af køb for aktien

            // sanity check, vi burde ikke have solgt flere aktier end vi har købt
            if (quantity > 0) {
                investedStocks.add(stock);
                totalInvestedInStocks += stock.getPrice() * quantity;
            }
        }
        return totalInvestedInStocks;
    }

    private int getQuantity(Stock stock) {
        int quantity = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getTicker().equalsIgnoreCase(stock.getTicker())) {
                if (transaction.getOrderType().equalsIgnoreCase("buy")) {
                    quantity += transaction.getQuantity();
                }
                if (transaction.getOrderType().equalsIgnoreCase("sell")) {
                    quantity -= transaction.getQuantity();
                }
            }
        }
        return quantity;
    }

    public void printInvestedStocks(Member member) {
        System.out.println("\n" + member.getFullName() + " is currently invested in:");
        System.out.println("——————————————————————————————————————————————————");
        for (Stock stocks : investedStocks) {
            System.out.println(stocks);
        }
        System.out.println("——————————————————————————————————————————————————");
        System.out.println("Sum value of invested stocks: " + calculateInvestedStocks() + "\n");
    }

    //gevinst/tab = nuværende totalværdi - initial cash
    public void showDifference() {
        String symbol = "";
        double difference = totalValue - member.getInitialCash();
        if (difference >= 0) {
            symbol = " ↗";
        } else {
            symbol = " ↘";
        }
        System.out.println("Gevinst/Tab: " + difference + " DKK" + symbol);
    }

    public void printPortfolio() {
        calculateTotalValue();
        System.out.println("Transaktioner:");
        System.out.println("--------------------------------------------");
        for (Transaction element : transactions) {
            System.out.println(element);
        }
        System.out.println("--------------------------------------------");
        System.out.println("Kontant beholdning: " + balance + " DKK");
        System.out.println("Investeret i aktier: " + stocksValue + " DKK");
        System.out.println("Total porteføljeværdi: " + totalValue + " DKK");
        showDifference();
    }

    public double getTotalValue() {
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
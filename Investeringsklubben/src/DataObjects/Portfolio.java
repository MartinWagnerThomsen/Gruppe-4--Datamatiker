package DataObjects;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Portfolio {
    private ArrayList<Transaction> transactions = new ArrayList<>();
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


    public void setTransactions(Transaction transaction ) {
        this.transactions.add(transaction);
        setTotalValue();
    }

    public void setTotalValue() {
        double sum = 0;
        for (Transaction transaction : transactions) {
            sum+= transaction.getQuantity() * transaction.getPrice();
        }
        totalValue = sum;
    }
    public void registerStock(){

    }
    public void viewPortfolio(){

    }
    public void showPortfolioValue(){

    }
    public void showLosses(){
    }

    public void printPortfolio() {
        for (Transaction element : transactions) {
            System.out.println(element);
        }
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
        sb.append("Portfolio total value: ").append(totalValue).append(" DKK").append("\n"); // Hardcoded Currency for now
        return sb.toString();
    }

}

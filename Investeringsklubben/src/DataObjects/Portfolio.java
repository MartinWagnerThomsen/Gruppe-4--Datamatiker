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
        System.out.println("Remove this when done testing");
        for(Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        return transactions;
    }


    public void setTransactions(Transaction transaction ) {
        this.transactions.add(transaction);
    }
    public void registerStock(){

    }
    public void viewPortfolio(){

    }
    public void showPortfolioValue(){

    }
    public void showLosses(){

    }
}

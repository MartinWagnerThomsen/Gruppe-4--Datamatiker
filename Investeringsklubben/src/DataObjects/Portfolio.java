package DataObjects;

import Users.Member;

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


    public void addTransactions(Transaction transaction ) {
        this.transactions.add(transaction);
    }

    public void calculateTotalValue(Member member) {
        double sum = 0;
        double totalStocksValue = 0;
        double stocksBuyValue = 0;
        double stocksSellValue = 0;
        double cashBalance = member.getInitialCash();
        for (Transaction transaction : transactions) {
            if (transaction.getOrderType().equalsIgnoreCase("buy")){
                stocksBuyValue += transaction.getQuantity() * transaction.getPrice();
                totalStocksValue += stocksBuyValue;
                cashBalance -= totalStocksValue;
                sum = cashBalance + totalStocksValue;
            }
            if (transaction.getOrderType().equalsIgnoreCase("sell")){
                stocksSellValue += transaction.getQuantity() * transaction.getPrice();
                totalStocksValue -= stocksBuyValue;
                cashBalance += stocksSellValue;
                sum = cashBalance + totalStocksValue;

            }
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
    public double getTotalValue(){
        return totalValue;
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

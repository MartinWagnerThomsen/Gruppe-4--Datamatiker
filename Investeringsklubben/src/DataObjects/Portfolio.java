package DataObjects;

import Filehandling.InvestmentClubFacade;
import Users.Member;
import java.util.ArrayList;
import java.util.List;


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

    /*
    * Lav en variabel "sum" som holder quantity af den pågældende aktie
    * Check om sum er lig med 0 - hvis det er den så skal den fjernes fra listen af akier
    * for det pågældende medlem
    * Hvis ikke den er nul skal quantity være resultatet af quantity - sum (sell) / quantity + sum (buy)
     Bagefter skal man regne ud hvad den reele pris af aktien er baseret på stockmarket.csv prisen som er vores
     * baseline pris (snapshot).
     * */
    public void calculateTotalValue(Member member) {
        InvestmentClubFacade facade = new InvestmentClubFacade();
        List<Stock> listOfStocks = facade.fetchStockData();

        double sum = 0;
        double totalStocksValue = 0;
        double cashBalance = member.getInitialCash();
        for (Transaction transaction : transactions) {

            if (transaction.getOrderType().equalsIgnoreCase("buy")){
                double stocksBuyValue = 0;

                stocksBuyValue += transaction.getQuantity() * transaction.getPrice();
            //    totalStocksValue += stocksCurrentValue * transaction.getQuan62tity();
                cashBalance -= stocksBuyValue;
            //    sum = cashBalance + stocksCurrentValue;
            }
            if (transaction.getOrderType().equalsIgnoreCase("sell")){
                double stocksSellValue = 0;
                stocksSellValue += transaction.getQuantity() * transaction.getPrice();
            //    totalStocksValue -= stocksBuyValue;
                cashBalance += stocksSellValue;
            //    sum = cashBalance + totalStocksValue;

            }


        }
        totalValue = cashBalance; //temporary. should be totalValue = sum;
    }


    public void calculateInvestedStocks () {
        List<Transaction> investedStocks = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getOrderType().equalsIgnoreCase("buy")){
                investedStocks.add(transaction);
            }
            if (transaction.getOrderType().equalsIgnoreCase("sell")){
                //if (transaction.getQuantity())
                investedStocks.remove(transaction);
            }
            // Nu bør vi have en liste af de aktier vi har tilbage


        }


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

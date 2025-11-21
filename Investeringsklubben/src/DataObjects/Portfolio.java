package DataObjects;

public class Portfolio {
    private double totalValue;
    private double totalDifference;

    public Portfolio(double totalValue, double totalDifference){
        this.totalValue = totalValue;
        this.totalDifference = totalDifference;
    }

    //prisen på enkelte stock
    public void showStockPrice(){

    }

    //registrer køb eller salg af beholdninger
    public void registerStock(){

    }

    //vis hele portefoljen: beholdning, samlet værdi, gevinst/tab.
    public void viewPortfolio(){

    }

    //samlet værdi
    public void showPortfolioValue(){

    }

    // tab
    public void showLosses(){

    }
    // gevinst
    public void showProfits(){

    }

    public double getTotalDifference(){
        return this.totalDifference;
    }
}

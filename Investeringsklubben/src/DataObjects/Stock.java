package DataObjects;

import java.util.Date;

public class Stock {
   private String ticker;
    private String name;
    private  String sector;
    private  double price;
    private  double transactionCost;
    private Currency currency;
    private String rating;
    private double dividentYield;
    private StockExchange stockExchange;
    Date lastUpdated;

    // getters
    public String getTicker(){return this.ticker;}
    public String getName(){return this.name;}
    public String getSector(){return this.sector;}
    public double getPrice(){return this.price;}
    public double getTransactionCost(){return this.transactionCost;}
    public Currency getCurrency(){return this.currency;}
    public String getRating(){return this.rating;}
    public double getDividentYield(){return this.dividentYield;}
    public StockExchange getStockExchange(){return this.stockExchange;}
    public Date getLastUpdate(){return this.lastUpdated;}

    // setters
    public void setTicker(String ticker){this.ticker = ticker;}
    public void setName(String name){this.name = name;}
    public void setSector(String sector){this.sector = sector;}
    public void setPrice(double price){this.price = price;}
    public void setTransactionCost(double transactionCost){this.transactionCost = transactionCost;}
    public void setCurrency(Currency currency){this.currency = currency;}
    public void setRating(String rating){this.rating = rating;}
    public void setDividentYield(double dividentYield){this.dividentYield = dividentYield;}
    public void setMarket(StockExchange stockExchange){this.stockExchange = stockExchange;}
    public void setLastUpdated(Date lastUpdated){this.lastUpdated = lastUpdated;}

    // constructor
    Stock(String ticker,
    String name,
    String sector,
    double price,
    double transactionCost,
    Currency currency,
    String rating,
    double dividentYield,
    StockExchange stockExchange,
    Date lastUpdated
    ){
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.transactionCost = transactionCost;
        this.currency = currency;
        this.rating = rating;
        this.dividentYield = dividentYield;
        this.stockExchange = stockExchange;
        this.lastUpdated = lastUpdated;
    }
}

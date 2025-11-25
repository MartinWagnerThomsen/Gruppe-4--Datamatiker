package DataObjects;

import java.time.LocalDate;

public class Transaction {

  private int transactionId;
  private  int userId;
  private  LocalDate date;
  private  String ticker;
  private  double price;
  private  String currency;
  private String orderType;
   private int quantity;


   /**
    * Main constructor for Transaction
    */
   public Transaction(int transactionId,int userId, LocalDate date, String ticker, double price,
                String currency,
   String orderType, int quantity) {
       this.transactionId = transactionId;
       this.userId = userId;
       this.date = date;
       this.ticker = ticker;
       this.price = price;
       this.currency = currency;
       this.orderType = orderType;
       this.quantity = quantity;
   }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    // Der skal konverteres fra int til String samt localdate til en string værdi
    @Override
    public String toString() {
        return "Transaction Id: " + transactionId + " | Userid: " + userId + " | Date of the transaction: " + date + " | Ticker: " + ticker + " | Price: " + price + " | Currency: " + currency +  " | Order type: " + orderType;
     }
}

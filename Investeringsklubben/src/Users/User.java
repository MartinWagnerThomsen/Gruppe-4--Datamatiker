package Users;

import DataObjects.Portfolio;

import java.time.LocalDate;

public abstract class User {
    /**
     * Mandatory attributes for Users
     */
    int userId;
     String email;
     LocalDate birthday;
     double initialCash;
     LocalDate createdAt;
     LocalDate lastUpdated;
     Portfolio portfolio;
     String fullName;

     public User(int userId, String fullName, String email, LocalDate birthday, double initialCash,
                 LocalDate createdAt, LocalDate lastUpdated,
                 Portfolio portfolio
          ){
         this.userId = userId;
         this.email = email;
         this.birthday = birthday;
         this.initialCash = initialCash;
         this.createdAt = createdAt;
         this.lastUpdated = lastUpdated;
         this.portfolio = portfolio;
         this.fullName = fullName;        
     }

     public User(int userId, String fullName, String email, LocalDate birthday, double initialCash,
                 LocalDate createdAt, LocalDate lastUpdated){
         this.userId = userId;
         this.email = email;
         this.birthday = birthday;
         this.initialCash = initialCash;
         this.createdAt = createdAt;
         this.lastUpdated = lastUpdated;
         this.fullName = fullName;
     }

     User(){
         int userId = 0;
         String email = null;
         LocalDate birthday = null;
         double initialCash = 0;
         LocalDate createdAt = null;
         LocalDate lastUpdated = null;
         Portfolio portfolio = null;
         String fullName = null;
     }

    /**
     * Mandatory methods for the user
     */
    public abstract void viewPortfolio();
    public abstract void viewTransactionHistory();
    public abstract void createUser();

    public abstract double getInitialCash();

    /**
     * Getters & Setters for the user class
     */
    abstract int getUserId();
     abstract String getEmail();
    abstract  LocalDate getBirthday();
    abstract public LocalDate getCreationLocalDate();

    public abstract LocalDate getLastUpdated();

    abstract public Portfolio getPortfolio();
    abstract public String getFullName();


}

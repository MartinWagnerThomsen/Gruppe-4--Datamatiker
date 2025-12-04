package Users;

import DataObjects.Portfolio;
import UI.Menu;

import java.time.LocalDate;
import java.util.Scanner;

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
    public User() {
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
    public abstract Menu getMenu(Scanner scanner);
    public abstract void viewTransactionHistory();
    public abstract double getInitialCash();

    /**
     * Getters & Setters for the user class
     */
    public abstract int getUserId();
    public abstract String getEmail();
    public abstract LocalDate getBirthday();
    public abstract LocalDate getCreationLocalDate();
    public abstract LocalDate getLastUpdated();
    public abstract Portfolio getPortfolio();
    public abstract String getFullName();


}

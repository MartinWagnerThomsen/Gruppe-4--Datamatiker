package Users;

import DataObjects.Portfolio;
import DataObjects.Transaction;

import java.time.LocalDate;

public class Member extends User {

    /**
     * Constructor with Portfolio added
      */

    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash,createdAt,lastUpdated, portfolio);
    }
    @Override
    public void viewPortfolio() {
        getPortfolio().getTransactions();

    }

    @Override
    public void viewTransactionHistory() {

    }

    @Override
    public void createUser() {

    }

    public void setLastUpdated (LocalDate update) {
        this.lastUpdated = update;
    }

    public void setCash (double cash) {
        this.initialCash = cash;
    }



    @Override
    public double getInitialCash() {
        return initialCash;
    }


    @Override
   public  int getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public LocalDate getCreationLocalDate() {
        return createdAt;
    }

    @Override
    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    @Override
    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void addTransaction(Transaction transaction) {
        this.portfolio.setTransactions(transaction);
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public void printMember() {
        System.out.println("Member profile for: " + this.getFullName());
        System.out.println("Portfolio: ");
        System.out.println(this.getPortfolio());
    }

    @Override
    public String toString() {
        return
                "userId='" + userId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", birthday='" + birthday + '\'' +
                ", initialCash='" + initialCash +
                "', createdAt='" + createdAt + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                    ", portfolio='" + portfolio + '\''
                ;
    }

    public void add(Transaction transaction) {
        portfolio.registerStock();
    }
}

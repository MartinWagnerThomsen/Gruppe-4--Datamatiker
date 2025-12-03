package Users;

import DataObjects.Portfolio;
import DataObjects.Transaction;

import java.time.LocalDate;
import java.util.List;

public class Member extends User {
    String password;
    String userType;

    // meget hacky
    public Member(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUserType() {
        return this.userType;
    }

    public Member(int userId) {

    }
    private double cashBalance;

    /**
     * Constructor with Portfolio added
      */

    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash,createdAt,lastUpdated, portfolio);
        this.cashBalance = initialCash;
    }
    @Override
    public void viewPortfolio() {

        getPortfolio().getTransactions();

    }

    @Override
    public void viewTransactionHistory(Member member) {
        List<Transaction> memberTransactions = getPortfolio().getTransactions();
        for(Transaction transactions : memberTransactions) {
            System.out.println(transactions.toString(member));
        }
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

    public double getInitialCash(){
        return initialCash;
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

    public double getCashBalance() {return cashBalance;}

    //setter
    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public void addTransaction(Transaction transaction) {
   //     this.portfolio.setTransactions(transaction);
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    public void printMember(Member foundMember) {
        getPortfolio().calculateTotalValue(foundMember);
        getPortfolio().calculateCashBalance(foundMember);
        System.out.println("Member profile for: " + this.getFullName());
        System.out.println("Portfolio: ");
        System.out.println(this.getPortfolio());
        System.out.println("Cash balance: " + this.getCashBalance());
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

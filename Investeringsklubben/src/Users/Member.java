package Users;

import DataObjects.Portfolio;
import DataObjects.Transaction;
import Filehandling.DataManager;

import java.time.LocalDate;
import java.util.List;

public class Member extends User {
    private String password;
    private String userType;
    private double cashBalance;

    /**
     * Konstruktør til login-flowet. Bliver overskrevet af den størrere konstruktør efter succesfuldt login
     * @param email
     * @param password
     * @param userType
     */
    public Member(String email, String password, String userType) {
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    /**
     * Constructor with Portfolio added
      */
    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash ,createdAt,lastUpdated, portfolio);
        this.cashBalance = initialCash;
    }
    @Override
    public void viewPortfolio() {

    }
    @Override
    public void viewTransactionHistory(Member member) {
        List<Transaction> memberTransactions = getPortfolio().getTransactions();
        System.out.println("Transaction History of: " + member.getFullName());
        System.out.println("================================================");
        for(Transaction transactions : memberTransactions) {
            System.out.println(transactions.toString(member));
        }
        System.out.println("================================================");
    }
    @Override
    public void createUser() {
    }

    //getters
    @Override
    public String getFullName() {return fullName;}

    public String getUserType() {return this.userType;}

    public String getPassword() {return this.password;}

    @Override
    public double getInitialCash() {return initialCash;}

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
    public Portfolio getPortfolio() {return portfolio;}

    public double getCashBalance() {return cashBalance;}

    //setters
    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public void setLastUpdated (LocalDate update) {
        this.lastUpdated = update;
    }

    public void setCash (double cash) {
        this.initialCash = cash;
    }

    public void printMember(DataManager dataManager, Member foundMember) {
        System.out.println("Member profile for: " + this.getFullName());
        System.out.println("Portfolio: ");
        System.out.println(this.getPortfolio());
        getPortfolio().printInvestedStocks(foundMember);
        System.out.println("Cash balance: " + this.getCashBalance());
        getPortfolio().showDifference();
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
}

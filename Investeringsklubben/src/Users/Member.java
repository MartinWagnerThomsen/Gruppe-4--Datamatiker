package Users;

import DataObjects.Portfolio;

import java.time.LocalDate;

public class Member extends User {

    /**
     * Constructor with Portfolio added
      */

    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash,createdAt,lastUpdated);
    }

    // Constructor without portfolio for testing purposes
    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash, LocalDate createdAt, LocalDate lastUpdated) {
        super(userId,
                fullName,
                email,
                birthday,
                initialCash,
                createdAt,
                lastUpdated);


    }

    @Override
    public void viewPortfolio() {
        
    }

    @Override
    public void viewTransactionHistory() {

    }

    @Override
    public void createUser() {

    }

    @Override
    int getUserId() {
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

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String toString() {
        return userId + fullName + email + birthday +  initialCash + createdAt + lastUpdated;
    }
}

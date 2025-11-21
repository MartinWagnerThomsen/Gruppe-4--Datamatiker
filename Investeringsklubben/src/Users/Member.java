package Users;

import DataObjects.Portfolio;

import java.time.LocalDate;

public class Member extends User {


    // Constructor with Portfolio added
    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash,
           LocalDate createdAt, LocalDate lastUpdated,
           Portfolio portfolio){
        super(userId, fullName,email,birthday, initialCash,createdAt,lastUpdated);
    }

    // Constructor without portfolio for testing purposes
    public Member(int userId, String fullName, String email, LocalDate birthday, double initialCash, LocalDate createdAt, LocalDate lastUpdated) {
        super(userId, fullName, email, birthday, initialCash, createdAt, lastUpdated);
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
        return 0;
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public LocalDate getBirthday() {
        return null;
    }

    @Override
    public LocalDate getCreationLocalDate() {
        return null;
    }

    @Override
    public LocalDate getLastUpLocalDated() {
        return null;
    }

    @Override
    public Portfolio getPortfolio() {
        return null;
    }

    @Override
    public String getFullName() {
        return "";
    }

    @Override
    public String toString() {
        return userId + fullName + email + birthday +  initialCash + createdAt + lastUpdated;
    }
}

package Filehandling;

import DataObjects.Stock;
import Users.Member;

import java.util.ArrayList;

public class InvestmentClubFacade {


    public void fetchAllData() {
        fetchUserData();
        fetchStockData();
    }


//    public void searchUser() {
//        Scanner sc = new Scanner(System.in);
//        System.out.println("Write the name of the member you're looking for: ");
//        try {
//            String userSearchInput = sc.next();
//            ArrayList<Member> members = fetchUserData();
//            for (Member member : members) {
//                if (member.getFullName().equalsIgnoreCase(userSearchInput)) {
//                    System.out.println("User found: " + member.getFullName());
//                    System.out.println("Printing user information: ");
//                    System.out.println(member);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("User not found");
//        }
//    }

    public ArrayList<Member> fetchUserData () {
       ArrayList<Member> members = new ArrayList<>();
               try {
            members = CsvHandler.getUserData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return members;
    }

    public ArrayList<Stock> fetchStockData () {
        ArrayList<Stock> stocks = new ArrayList<>();
        try {
            stocks = CsvHandler.getStockData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return stocks;
    }

    public void saveUserData() {}
    public void saveStockData() {}
}

import Filehandling.CsvHandler;
import Filehandling.FileHandler;
import Users.Member;

import java.util.ArrayList;

public class Club {
    private final CsvHandler handler = new CsvHandler();
    public void login(){

    }
    public void logout(){

    }
    public void switchUser(){

    }
    public static void main(String[] args) {
        Club investmentClub = new Club();
        InvestmentClubFacade facade = new InvestmentClubFacade();
        ArrayList<Member> activeMembers = facade.fetchUserData();


    }
}

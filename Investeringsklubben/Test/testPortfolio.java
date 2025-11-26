import DataObjects.*;
import Filehandling.CsvHandler;
import Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class testPortfolio {
    @Test
    public void testShowStockPrice() {

    }

    @Test
    public void testRegisterStock() {

    }

    //beregn difference (gevindst/tab) uden %
    //nuværende totalværdi - startværdigen = difference
    // (startværdigen er 100.000 DKK)
    Portfolio p1 = new Portfolio(170200);
    Portfolio p2 = new Portfolio(89630);

    @Test
    public void testDifferenceForP1() {
        Assertions.assertEquals(70200, p1.getTotalDifference());
    }

    @Test
    public void testDifferenceForP2() {
        Assertions.assertEquals(-10370, p2.getTotalDifference());
    }

    //setTotalValue
    //udregnes ved: initialCash er kontante værdi i starten. hver gang en stock købes:
    //kontantVærdi - pris af stocks købt = ny kontantVærdi
    //kontant værdi = kontant værdi + stock beholdninger
    Portfolio portfolio = new Portfolio(0,0);
    Member fakeMember = new Member(
            101,
            "Torben Christensen",
            "torbenchris@gmail.com",
            LocalDate.of(1995, 5, 20),
            100000,
            LocalDate.of(2025, 11, 20),
            LocalDate.of(2025, 11, 26),
            portfolio);
    Transaction transaction1 = new Transaction(
            1, 101,
            LocalDate.of(2025, 11, 21),
            "ORSTED", 456, "DKK", "buy", 10);
    Transaction transaction2 = new Transaction(
            1, 101,
            LocalDate.of(2025, 11, 22),
            "ORSTED", 500, "DKK", "sell", 10);
    portfolio.setTransactions(transaction1);
    portfolio.setTransactions(transaction2);

    @Test
    public void testTotalValueMalthe(){
        Assertions.assertEquals(100000, CsvHandler.userList.get(8).totalValue);
    }

    @Test
    public void testTotalValue(){
        Assertions.assertEquals(102168, fakeMember.Portfolio);
    }


}



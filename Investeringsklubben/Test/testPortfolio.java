import DataObjects.*;
import Filehandling.CsvHandler;
import Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;



public class testPortfolio {
    Portfolio portfolio = new Portfolio(0,0);

    //;2;02-03-2025;VWS;197;DKK;buy;50
    Transaction transaction1 = new Transaction(
            1, 101,
            LocalDate.of(2025, 3, 2),
            "VWS", 197, "DKK", "buy", 50);
    //;2;02-03-2025;VWS;200;DKK;sell;50
    Transaction transaction2 = new Transaction(
            2, 101,
            LocalDate.of(2025, 3, 2),
            "VWS", 200, "DKK", "sell", 50);
    //;2;08-03-2025;NETC;356;DKK;buy;22
    Transaction transaction3 = new Transaction(
            3, 101,
            LocalDate.of(2025, 3, 8),
            "NETC", 356, "DKK", "buy", 22);
    Member fakeMember = new Member(
            101,
            "Torben Christensen",
            "torbenchris@gmail.com",
            LocalDate.of(1995, 5, 20),
            100000,
            LocalDate.of(2025, 2, 20),
            LocalDate.of(2025, 11, 26), portfolio);

    @Test
    public void testShowStockPrice() {

    }

    @Test
    public void testRegisterStock() {

    }

    //beregn difference (gevindst/tab) uden %
    //nuværende totalværdi - startværdigen = difference
    // (startværdigen er 100.000 DKK)
/*    Portfolio p1 = new Portfolio(170200);
    Portfolio p2 = new Portfolio(89630);

    @Test
    public void testDifferenceForP1() {
        Assertions.assertEquals(70200, p1.getTotalDifference());
    }

    @Test
    public void testDifferenceForP2() {
        Assertions.assertEquals(-10370, p2.getTotalDifference());
    }
*/
    //setTotalValue
    //udregnes ved: initialCash er kontante værdi i starten. hver gang en stock købes:
    //kontantVærdi - pris af stocks købt = ny kontantVærdi
    //total værdi = kontant værdi + stock beholdninger
    @BeforeEach
    void init(){
        System.out.println("Test startup");
    //    Portfolio portfolio = new Portfolio(0,0);
//        Transaction transaction1 = new Transaction(
//                01, 101,
//                LocalDate.of(2025, 11, 21),
//                "ORSTED", 456, "DKK", "buy", 10);
//        Transaction transaction2 = new Transaction(
//                02, 101,
//                LocalDate.of(2025, 11, 22),
//                "ORSTED", 500, "DKK", "sell", 10);
//        portfolio.addTransactions(transaction1);
//        portfolio.calculateTotalValue();
//        portfolio.addTransactions(transaction2);
//        portfolio.calculateTotalValue();
//        Member fakeMember = new Member(
//                101,
//                "Torben Christensen",
//                "torbenchris@gmail.com",
//                LocalDate.of(1995, 5, 20),
//                100000,
//                LocalDate.of(2025, 11, 20),
//                LocalDate.of(2025, 11, 26),
//                portfolio);

    }

    @Test
    void testName(){
        Assertions.assertEquals("Torben Christensen", fakeMember.getFullName());
    }

    @Test
    public void testTotalValueNotZero(){
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertNotEquals(0, portfolio.getTotalValue());
    }

    //test that the starting value is 100000 DKK
    @Test
    public void testStartTotalValue(){
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100000, portfolio.getTotalValue());
    }

    @Test
    public void testTotalValueAfterTransaction1(){
        portfolio.addTransactions(transaction1); // buy
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100050, portfolio.getTotalValue());

    }

    @Test
    public void testTotalValueAfterTransaction2(){
        portfolio.addTransactions(transaction1); //buy
        portfolio.addTransactions(transaction2); //sell
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100150, portfolio.getTotalValue());
    }

    @Test
    public void testTotalValueAfterTransaction3(){
        portfolio.addTransactions(transaction1); //buy
        portfolio.addTransactions(transaction2); //sell
        portfolio.addTransactions(transaction3); //buy
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100238, portfolio.getTotalValue());
    }

//    public static void main(String[] args) {
//        System.out.println();
//    }
}



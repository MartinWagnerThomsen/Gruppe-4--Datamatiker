import DataObjects.*;
import Filehandling.CsvHandler;
import Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;



public class testPortfolio {
    Portfolio portfolio = new Portfolio(0,0);
    Transaction transaction1 = new Transaction(
            1, 101,
            LocalDate.of(2025, 11, 21),
            "ORSTED", 456, "DKK", "buy", 10);
    Transaction transaction2 = new Transaction(
            2, 101,
            LocalDate.of(2025, 11, 22),
            "ORSTED", 500, "DKK", "sell", 10);

    Member fakeMember = new Member(
            101,
            "Torben Christensen",
            "torbenchris@gmail.com",
            LocalDate.of(1995, 5, 20),
            100000,
            LocalDate.of(2025, 11, 20),
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
        System.out.println("startup");
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
        Assertions.assertNotEquals(0, portfolio.getTotalValue());
    }

    @Test
    public void testTotalValueAfterTransaction1(){
        portfolio.addTransactions(transaction1);
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100000.00, portfolio.getTotalValue());

    }

    @Test
    public void testTotalValueAfterTransaction2(){
        portfolio.addTransactions(transaction1);
        portfolio.addTransactions(transaction2);
        portfolio.calculateTotalValue(fakeMember);
        Assertions.assertEquals(100440.00, portfolio.getTotalValue());
    }

//    public static void main(String[] args) {
//        System.out.println();
//    }
}



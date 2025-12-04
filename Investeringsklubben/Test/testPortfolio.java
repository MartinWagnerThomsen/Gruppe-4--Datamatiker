import DataObjects.*;
import Filehandling.DataManager;
import Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class testPortfolio {
    DataManager dataManager = new DataManager();
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
    Transaction transaction4 = new Transaction(
            4, 101,
            LocalDate.of(2025, 3, 3),
            "PANDORA", 770.5, "DKK", "buy", 20);
    Member fakeMember = new Member(
            101,
            "Torben Christensen",
            "torbenchris@gmail.com",
            LocalDate.of(1995, 5, 20),
            100000,
            LocalDate.of(2025, 2, 20),
            LocalDate.of(2025, 11, 26), portfolio);


    //beregn difference (gevindst/tab) uden %
    //nuværende totalværdi - startværdigen = difference
    // (startværdigen er 100.000 DKK)

    @Test
    public void testDifferenceOne(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        portfolio.showDifference(fakeMember, dataManager);
    }

    @Test
    public void testDifferenceTwo(){
        portfolio.addTransaction(transaction1); //buy VWS
        //portfolio.addTransactions(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        portfolio.showDifference(fakeMember, dataManager);
    }

    @Test
    public void testDifferenceThree(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        //portfolio.addTransactions(transaction3); //buy NETC
        portfolio.showDifference(fakeMember, dataManager);
    }
    @Test
    public void testDifference(){
        portfolio.addTransaction(transaction4); //buy 20 PANDORA
        portfolio.showDifference(fakeMember, dataManager);
    }

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
    //calculateTotalValue
    //udregnes ved: initialCash er kontante værdi i starten. hver gang en stock købes:
    //kontantVærdi - pris af stocks købt = ny kontantVærdi
    //total værdi = kontant værdi + kurs for aktier der er investeret i
    @BeforeEach
    void init(){
        System.out.println("Test startup");
    }

    @Test
    void testName(){
        Assertions.assertEquals("Torben Christensen", fakeMember.getFullName());
    }

    @Test
    public void testStartCashBalanceNotZero(){
        Assertions.assertNotEquals(0, fakeMember.getCashBalance());
    }

    @Test
    public void testInitialCashNotZero(){
        Assertions.assertNotEquals(0, fakeMember.getCashBalance());
    }

    @Test
    public void testStartTotalValueNotZero(){
        Assertions.assertNotEquals(0, portfolio.calculateCashBalance(fakeMember));
    }

    //test that the starting value is 100000 DKK
    @Test
    public void testStartCashBalance(){
        portfolio.calculateCashBalance(fakeMember);
        Assertions.assertEquals(100000, portfolio.calculateCashBalance(fakeMember));
    }

    @Test
    public void testCashBalanceAfterTransaction1(){
        portfolio.addTransaction(transaction1); // buy
        Assertions.assertEquals(90150, portfolio.calculateCashBalance(fakeMember));

    }

    @Test
    public void testCashBalanceAfterTransaction2(){
        portfolio.addTransaction(transaction1); //buy
        portfolio.addTransaction(transaction2); //sell
        Assertions.assertEquals(100150, portfolio.calculateCashBalance(fakeMember));
    }

    @Test
    public void testCashBalanceAfterTransaction3(){
        portfolio.addTransaction(transaction1); //buy
        portfolio.addTransaction(transaction2); //sell
        portfolio.addTransaction(transaction3); //buy
        Assertions.assertEquals(92318, portfolio.calculateCashBalance(fakeMember));
    }

    @Test
    public void testListOfInvestedStocksNETC(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        Assertions.assertEquals(7920, portfolio.calculateInvestedStocks(fakeMember, dataManager));
    }

    @Test
    public void testListOfInvestedStocksTwo(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction3); //buy NETC
        Assertions.assertEquals(17820, portfolio.calculateInvestedStocks(fakeMember, dataManager));
    }

    //beregn total value for en mestemt member
    @Test
    public void testTotalValue100238(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        portfolio.calculateTotalValue(fakeMember, dataManager);
        Assertions.assertEquals(100238, portfolio.getTotalValue());
    }

    @Test
    public void testViewTransactionHistory(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        fakeMember.viewTransactionHistory(fakeMember);
    }

    @Test
    public void testPrintMemberThreeTransactions(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction2); //sell VWS
        portfolio.addTransaction(transaction3); //buy NETC
        fakeMember.printMember(dataManager, fakeMember);
    }

    @Test
    public void testPrintMemberTwoTransactions(){
        portfolio.addTransaction(transaction1); //buy VWS
        portfolio.addTransaction(transaction3); //buy NETC
        fakeMember.printMember(dataManager, fakeMember);
    }

}



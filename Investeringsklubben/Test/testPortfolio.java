import DataObjects.Portfolio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testPortfolio {
    @Test
    public void testShowStockPrice() {

    }

    @Test
    public void testRegisterStock() {

    }

    Portfolio p1 = new Portfolio(170200);
    Portfolio p2 = new Portfolio(89630);
    //beregn difference (gevindst/tab) uden %
    //nuværende totalværdi - startværdigen = difference
    // (startværdigen er 100.000 DKK)
    @Test
    public void testDifference170000() {
        Assertions.assertEquals(70200, p1.getTotalDifference());
    }

    @Test
    public void testDifference89630() {
        Assertions.assertEquals(-10370, p2.getTotalDifference());
    }
}



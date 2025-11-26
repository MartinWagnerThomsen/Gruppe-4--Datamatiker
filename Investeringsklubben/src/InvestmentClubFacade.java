import DataObjects.Stock;
import Filehandling.CsvHandler;

import java.util.ArrayList;

public class InvestmentClubFacade {

    public void fetchUserData () {
        try {
            CsvHandler.getUserData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

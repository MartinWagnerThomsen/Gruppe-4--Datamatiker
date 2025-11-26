import Filehandling.CsvHandler;

public class InvestmentClubFacade {

    public void fetchUserData () {
        try {
            CsvHandler.getUserData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchStockData () {
        try {
            CsvHandler.getStockData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUserData() {}
    public void saveStockData() {}
}

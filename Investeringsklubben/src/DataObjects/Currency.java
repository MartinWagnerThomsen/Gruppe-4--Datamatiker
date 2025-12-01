package DataObjects;


import Filehandling.DataManager;

import java.time.LocalDate;
import java.util.List;


public class Currency {
    private String baseCurr;
    private String quote;
    private double rate;
    private LocalDate lastUpdated;

    public Currency(String baseCurr, String quote, double rate, LocalDate lastUpdated) {
        this.baseCurr = baseCurr;
        this.quote = quote;
        this.rate = rate;
        this.lastUpdated = lastUpdated;
    }


    public double getRate() {
        return rate;
    }

    public String getBaseCurr() {
        return baseCurr;
    }


    /**
     * Find beløbet
     * Find den relevante kurs pris  (fx. Euro)
     * Multiplicer beløbet med kursen
     * Rund det af til nærmeste decimal
     * Overvej gebyrer
     * @return
     */
    public void convertToDkk (Stock stock) {
        // Vi har brug for at få vores rater
        DataManager manager = new DataManager();
        List<Currency> listOfCurrenciesAndRates = manager.getCurrencies();

        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stock.getCurrency();
        System.out.println(stockCurrency);


        for (Currency currency : listOfCurrenciesAndRates) {
        }

    }

    public static void main(String[] args) {
    }


    @Override
    public String toString() {
        return "Currency: " + baseCurr + " Rate: " + rate;
    }
}

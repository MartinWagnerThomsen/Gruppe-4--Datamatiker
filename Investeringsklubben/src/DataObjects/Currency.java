package DataObjects;


import Filehandling.DataManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Currency {
    private String baseCurr;
    private String quote;
    private double rate;
    private LocalDate lastUpdated;
    private List<Currency> currencies = new ArrayList<>();

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

    public double convertToDkk () {
        DataManager manager = new DataManager();
        this.currencies = manager.getCurrencies();

        for (Currency element : currencies) {
            if(element.getBaseCurr() == this.baseCurr) {
                this.getRate() = element.getRate();
            }
        }

    }


    @Override
    public String toString() {
        return "Currency: " + baseCurr + " Rate: " + rate;
    }
}

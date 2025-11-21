package DataObjects;

import com.sun.source.tree.BreakTree;

public class Currency {
    private String currencyName;

    private static final Currency DKK = new Currency("DKK");
    private static final Currency EUR = new Currency("EUR");
    private static final Currency USD = new Currency("USD");

    Currency(String currencyName){
        this.currencyName = currencyName;
    }

    // getters
    public String getCurrencyName(){return currencyName;}

    // setters
    public void setCurrencyName(String currencyName){this.currencyName = currencyName;}

    public static void findRightCurrency() {
    }
    public static void calculateCurrencyPrice() {
    }

    public static Currency parseCurrency(String currency) throws InvalidCurrencyException {
        if(currency.length() > 3){
            throw new InvalidCurrencyException("The currency you're trying to parse exceeds the expected limit!");
        }
        return switch (currency) {
            case "DKK" -> DKK;
            case "EUR" -> EUR;
            case "USD" -> USD;
            default -> DKK;
        };
    }

    @Override
    public String toString() {
        return "Currency{" +
                "currencyName='" + currencyName + '\'' +
                '}';
    }
}

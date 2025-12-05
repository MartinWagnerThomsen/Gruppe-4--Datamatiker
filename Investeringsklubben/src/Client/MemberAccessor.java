package Client;

import DataObjects.Stock;
import DataObjects.Transaction;
import Filehandling.DataManager;
import Users.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MemberAccessor implements IClubAccessor {
    private final ClubManager manager;
    private final Member member;

    public MemberAccessor(ClubManager manager, Member member) {
        this.manager = manager;
        this.member = member;
    }

    public void printMarketAndRates() {
        List<Stock> stockMarket = manager.getStocks();
        for (Stock stock : stockMarket) {
            System.out.println(stock.getName());
            System.out.println(stock.getPrice());
        }
    }

    public void createTransaction() {
        int userId = member.getUserId();
        LocalDate date = promptDate();
        String ticker = promptTicker();
        double price = promptPrice();
        String currency = promptCurrency();
        String orderType = promptOrderType();
        int quantity = promptQuantity();

        manager.createTransaction(userId, date, ticker, price, currency, orderType, quantity);
        System.out.println("Transaktion oprettet!");
    }


    public void printPortfolio() {
        member.getPortfolio().printPortfolio();
    }

    public void viewTransactionHistory() {
        System.out.println("Transaktion historik af: " + member.getFullName());
        System.out.println("================================================");
        for (Transaction transaction : manager.getTransactions(member.getUserId())) {
            System.out.println(transaction.toString(member));
        }
        System.out.println("================================================");
    }

    // ---------------------------------------------------------------------------------------
    // Private hjælpe metoder til createTransaction

    /**
     * Spørger brugeren for en dato og validerer formatet (dd-MM-yyyy)
     */
    private LocalDate promptDate() {
        System.out.print("Indtast dato i format (dd-MM-yyyy): ");
        Optional<LocalDate> date = Parser.parseDate();

        while (date.isEmpty()) {
            System.out.println("Ugyldig dato!");
            System.out.print("Prøv igen: ");
            date = Parser.parseDate();
        }

        return date.get();
    }

    /**
     * Spørger brugeren for en aktie ticker, og sørger for at den er 1-5 bogstaver lang
     * Midlertidig løsning, en bedre implementering ville være at tjekke op imod de tickers vi har i stockMarket
     */
    private String promptTicker() {
        System.out.print("Indtast aktie ticker: ");
        String input = Parser.parseString().trim();

        while (input.isEmpty() || input.length() > 5 || !input.matches("[a-zA-Z]+")) {
            System.out.println("Ugyldig ticker: '" + input + "'. Ticker må ikke være tom og skal være 1-5 bogstaver.");
            System.out.print("Prøv igen: ");
            input = Parser.parseString();
        }

        return input.toUpperCase();
    }

    /**
     * Spørger brugeren for aktieprisen
     */
    private double promptPrice() {
        System.out.print("Indtast aktie pris: ");
        String input = Parser.parseString().trim().replace(",", ". "); // Konvertere komma til punktum for parse metoder

        while (!isPositiveNumber(input)) {
            System.out.println("Ugyldig pris: '" + input + "'. Prisen skal være et positivt tal.");
            System.out.print("Prøv igen: ");
            input = Parser.parseString().replace(",", ".");
        }

        return Double.parseDouble(input);
    }

    /**
     * Tjekker om input er et positivt tal
     */
    private boolean isPositiveNumber(String input) {
        try {
            return Double.parseDouble(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Spørger brugeren for valuta og validerer at det er en gyldig valuta
     */
    private String promptCurrency() {
        // der er nok en bedre måde end den her at gøre det på, så optimer den endelig :)
        String[] validCurrencies = {"DKK", "EUR", "USD", "SEK", "NOK", "GBP", "JPY", "CHF", "AUD", "CAD"};

        System.out.print("Indtast valutaen (" + String.join(", ", validCurrencies) + "): ");
        String input = Parser.parseString().toUpperCase();

        while (!isInArray(input, validCurrencies)) {
            System.out.println("Ugyldig valuta: '" + input + "'");
            System.out.print("Vælg en af (" + String.join(", ", validCurrencies) + "): ");
            input = Parser.parseString().toUpperCase();
        }

        return input;
    }

    /**
     * Spørger brugeren for ordretype køb/salg og konvertere til sell/buy
     */
    private String promptOrderType() {
        System.out.print("Indtast ordretypen (køb/salg): ");
        String input = Parser.parseString().trim().toLowerCase();

        while (!input.equals("køb") && !input.equals("salg")) {
            System.out.println("Ugyldig ordretype: '" + input + "'");
            System.out.print("Vælg enten 'køb' eller 'salg': ");
            input = Parser.parseString().toLowerCase();
        }

        if (input.equals("køb")) return "buy";
        if (input.equals("salg")) return "sell";
        return input;
    }

    /**
     * Spørger brugeren for antal af aktier
     */
    private int promptQuantity() {
        System.out.print("Indtast mængde af aktier købt/solgt: ");
        String input = Parser.parseString();

        while (!isPositiveInteger(input)) {
            System.out.println("Ugyldig mængde: '" + input + "'. Skal være et positivt heltal.");
            System.out.print("Prøv igen: ");
            input = Parser.parseString();
        }

        return Integer.parseInt(input);
    }

    /**
     * Tjekker om input er et positivt tal
     */
    private boolean isPositiveInteger(String input) {
        try {
            return Integer.parseInt(input) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tjekker om en værdi findes i et array
     */
    private boolean isInArray(String value, String[] array) {
        for (String item : array) {
            if (item.equals(value)) return true;
        }
        return false;
    }

}

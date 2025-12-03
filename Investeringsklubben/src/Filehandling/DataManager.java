package Filehandling;

import DataObjects.Currency;
import Exceptions.CsvParsingException;
import DataObjects.Stock;
import DataObjects.Transaction;
import Users.Member;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataManager {
    private static final String MEMBERS_FILE = "Investeringsklubben/src/Files/users.csv";
    private static final String STOCKS_FILE = "Investeringsklubben/src/Files/stockMarket.csv";
    private static final String TRANSACTIONS_FILE = "Investeringsklubben/src/Files/transactions.csv";
    private static final String CURRENCY_FILE = "Investeringsklubben/src/Files/currency.csv";

    private List<Member> members;
    private List<Stock> stocks;
    private List<Transaction> transactions;
    private static List<Currency> currencies;
    private final CsvHandler csvHandler;
    public DecimalFormat numberFormat = new DecimalFormat("#.00");


    // Initialiser lister for at undgå NullPointerException, selv hvis indlæsning fejler
    public DataManager() {
        this.csvHandler = new CsvHandler();
        this.members = new ArrayList<>();
        this.stocks = new ArrayList<>();
        loadAllData();
    }

    /**
     * Indlæser al data fra CSV-filer og bygger den interne datamodel.
     * Denne metode orkestrerer hele processen.
     */
    public void loadAllData() {
        System.out.println("Indlæser data fra filer...");
        try {
            // 1. Indlæs rådata fra filer
            List<Member> loadedMembers = csvHandler.readMembers(MEMBERS_FILE);
            List<Stock> loadedStocks = csvHandler.readStocks(STOCKS_FILE);
            List<Transaction> allTransactions = csvHandler.readTransactions(TRANSACTIONS_FILE);
            List<Currency> loadedCurrencies = csvHandler.readCurrency(CURRENCY_FILE);

            // 2. Gem primær data i DataManager's state
            this.members = loadedMembers;
            this.stocks = loadedStocks;
            currencies = loadedCurrencies;
            this.transactions = allTransactions;

            // 3. Prøv at opdatere vores currencies fra vores API kald
            System.out.println("Opdaterer valutakurser fra Nationalbanken...");
            updateCurrencies();

            // 4. Link transaktioner til medlemmer
            linkTransactionsToMembers(allTransactions);

            System.out.println("Data indlæst successfuldt.");

        } catch (CsvParsingException e) {
            System.err.println("KRITISK FEJL under indlæsning af data: " + e.getMessage());
            System.err.println("Applikationen kan ikke starte korrekt. Tjek filformater.");
        }
    }

    /**
     * Hjælpemetode til at forbinde transaktioner med den korrekte bruger.
     */
    private void linkTransactionsToMembers(List<Transaction> transactions) {
        Map<Integer, Member> memberMap = this.members.stream()
                .collect(Collectors.toMap(Member::getUserId, member -> member));
        for (Transaction transaction : transactions) {
            Member member = memberMap.get(transaction.getUserId());
            if (member != null) {
                member.getPortfolio().addTransaction(transaction);
            }
        }
    }

    /**
     * Registrerer en ny transaktion og sikrer, at alt data gemmes korrekt.
     */
    public void registerNewTransaction(Transaction transaction) {

        // Find det relevante medlem
        Member memberToUpdate = members.stream()
                .filter(m -> m.getUserId() == transaction.getUserId())
                .findFirst()
                .orElse(null); // Overvej at tilføj NA?

        if (memberToUpdate == null) {
            System.err.println("Kunne ikke registrere transaktion: Medlem med ID " + transaction.getUserId() + " ikke fundet.");
            return;
        }


        // 1. Tilføj transaktion til porteføljen i hukommelsen
        memberToUpdate.getPortfolio().addTransaction(transaction);

        // 1.2

        String currency = transaction.getCurrency();

        if (!currency.equals("DKK")) {
            convertToDkk(transaction);
        }

        // 2. Opdater medlemmets 'cash' (skal implementeres)

        if (transaction.getOrderType().equals("buy")) {


        }
        double cost = transaction.getPrice() * transaction.getQuantity();
        memberToUpdate.setCash(memberToUpdate.getInitialCash() - cost);

        // 3. Sæt 'lastUpdated' dato
        memberToUpdate.setLastUpdated(LocalDate.now());

        try {
            // 4. Gem den nye transaktion til filen
            csvHandler.appendTransaction(TRANSACTIONS_FILE, transaction);

            // 5. Gem HELE medlemslisten for at opdatere cash og lastUpdated
            csvHandler.writeAllMembers(MEMBERS_FILE, this.members);

            csvHandler.writeCurrencies(CURRENCY_FILE, currencies);

            System.out.println("Transaktion registreret og gemt for " + memberToUpdate.getFullName());

        } catch (IOException e) {
            System.err.println("KRITISK FEJL under gemning af transaktion: " + e.getMessage());
            // Her bør man overveje en strategi for at håndtere fejl under skrivning.
        }
    }


    /**
     * Tager en aktie som input
     * Derefter hentes vores valutakurser fra DataManager
     * Hvis aktiens valuta matcher en af vores kurser så hentes raten
     * Prisen af aktien ganges derefter med raten for at finde prisen i DKK
     * Resultatet bliver gemt på aktien samt dens aktie valuta gemmes som DKK
     *
     * @param stockTransaction
     */
    public void convertToDkk(Transaction stockTransaction) {
        String danishkrone = "DKK";
        // Vi har brug for at få vores rater
        List<Currency> listOfCurrenciesAndRates = getCurrencies();

        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stockTransaction.getCurrency();
        double stockPricePrStock = stockTransaction.getPrice();
        double stockQuantity = stockTransaction.getQuantity();
        System.out.println(stockCurrency);

        if (!stockCurrency.equalsIgnoreCase("DKK")) {
            for (Currency currency : listOfCurrenciesAndRates) {
                if (stockCurrency.equalsIgnoreCase(currency.getBaseCurr())) {
                    System.out.println("Found the currency in our list: " + currency);
                    System.out.println("Converting " + stockTransaction.getTicker() + " at the current price pr stock: " + stockPricePrStock + " " + stockCurrency + " to " + currency.getRate());

                    double totalStockPrice = stockPricePrStock * stockQuantity;
                    double totalPriceAfterConversion = totalStockPrice * currency.getRate();
                    System.out.println("Result of the conversion of " + stockQuantity + " " + stockCurrency + " to " + numberFormat.format(totalPriceAfterConversion) + danishkrone);

                }
            }

        }


    }

    // --- Getters til UI-laget ---
    public Member getMember(String email) {
        for (Member member : members) {
            if (email.equals(member.getEmail())) {
                return member;
            }
        }
        return null;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }


    public List<Stock> getStocks() {
        return stocks;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void updateCurrencies() {
        CurrencyFetcher fetcher = new CurrencyFetcher();
        String xmlData = fetcher.fetchCurrencyData();
        if (xmlData != null && !xmlData.isEmpty()) {
            List<Currency> fetchedCurrencies = fetcher.parseCurrencyXml(xmlData);

            // Opdater eksisterende kurser eller tilføj nye
            for (Currency fetchedCurrency : fetchedCurrencies) {
                // Find om vi allerede har denne valuta
                Optional<Currency> existingCurrencyOpt = currencies.stream()
                        .filter(c -> c.getBaseCurr().equalsIgnoreCase(fetchedCurrency.getBaseCurr()))
                        .findFirst();

                if (existingCurrencyOpt.isPresent()) {
                    // Opdater rate og dato for eksisterende valuta
                    Currency existingCurrency = existingCurrencyOpt.get();
                    existingCurrency.setRate(fetchedCurrency.getRate());
                    existingCurrency.setLastUpdated(fetchedCurrency.getLastUpdated());
                    System.out.println("Opdateret kurs for " + existingCurrency.getBaseCurr() + " til " + existingCurrency.getRate());
                } else {
                    // Tilføj ny valuta, hvis den ikke findes
                    currencies.add(fetchedCurrency);
                    System.out.println("Tilføjet ny kurs for " + fetchedCurrency.getBaseCurr());
                }
            }
            try {
                csvHandler.writeCurrencies(CURRENCY_FILE, currencies);
                System.out.println("Valutakurser gemt til fil.");
            } catch (IOException e) {
                System.err.println("Fejl under skrivning af opdaterede valutakurser til fil: " + e.getMessage());
            }
        }
    }

    private static class CurrencyFetcher {
        private static final String API_URL = "https://www.nationalbanken.dk/api/currencyrates?format=rss&lang=da&isocodes=eur,usd,sek,nok,gbp,jpy,aud,cad,chf";

        public String fetchCurrencyData() {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();
            try {
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    return response.body();
                } else {
                    System.err.println("Fejl ved hentning af valutakurser. Statuskode: " + response.statusCode());
                    return null;
                }
            } catch (Exception e) {
                System.err.println("Exception under HTTP-forespørgsel til Nationalbanken: " + e.getMessage());
                return null;
            }
        }

        public List<Currency> parseCurrencyXml(String xmlData) {
            List<Currency> fetchedCurrencies = new ArrayList<>();
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                InputSource is = new InputSource(new StringReader(xmlData.replace("\uFEFF", "").trim()));
                Document doc = builder.parse(is);

                XPath xpath = XPathFactory.newInstance().newXPath();
                XPathExpression expr = xpath.compile("//item");
                NodeList itemNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

                Map<String, String> nameToCode = createNameToCodeMap();
                Pattern pattern = Pattern.compile("^(.+)\\s*\\(([\\d,]+)\\)$");

                String firstDate = (itemNodes.getLength() > 0)
                        ? xpath.compile("./pubDate").evaluate(itemNodes.item(0), XPathConstants.STRING).toString().trim()
                        : null;

                for (int i = 0; i < itemNodes.getLength(); i++) {
                    Node itemNode = itemNodes.item(i);
                    String pubDate = xpath.compile("./pubDate").evaluate(itemNode, XPathConstants.STRING).toString().trim();

                    if (firstDate != null && !pubDate.equals(firstDate)) {
                        break; // Stop hvis vi er nået til gårsdagens kurser
                    }

                    String titleString = xpath.compile("./title").evaluate(itemNode, XPathConstants.STRING).toString();
                    Matcher matcher = pattern.matcher(titleString.trim());

                    if (matcher.find()) {
                        String rawName = matcher.group(1).trim();
                        String rateString = matcher.group(2).trim();
                        String baseCurrency = nameToCode.get(rawName);

                        if (baseCurrency == null && rawName.contains("Schweiziske franc")) {
                            baseCurrency = "CHF";
                        }

                        if (baseCurrency != null) {
                            try {
                                double rawRateValue = Double.parseDouble(rateString.replace(',', '.'));
                                double unitAmount = 100.0;
                                // Japanske Yen er per 1 enhed, ikke 100
                                if (baseCurrency.equals("JPY")) {
                                    unitAmount = 1.0;
                                }
                                double actualRate = rawRateValue / unitAmount;
                                actualRate = Math.round(actualRate * 100.0) / 100.0; // nærmeste 2 decimaller

                                fetchedCurrencies.add(new Currency(baseCurrency, "DKK", actualRate, LocalDate.now()));
                            } catch (NumberFormatException e) {
                                System.err.println("Advarsel: Kunne ikke parse kurs for '" + rawName + "'. Værdi: '" + rateString + "'");
                            }
                        } else {
                            System.err.println("Advarsel: Kunne ikke finde ISO-kode for: " + rawName);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Fejl under XML-parsing: " + e.getMessage());
            }
            return fetchedCurrencies;
        }

        private Map<String, String> createNameToCodeMap() {
            Map<String, String> nameToCode = new HashMap<>();
            nameToCode.put("Amerikanske dollar", "USD");
            nameToCode.put("Euro", "EUR");
            nameToCode.put("Svenske kroner", "SEK");
            nameToCode.put("Australske dollar", "AUD");
            nameToCode.put("Norske kroner", "NOK");
            nameToCode.put("Canadiske dollar", "CAD");
            nameToCode.put("Britiske pund", "GBP");
            nameToCode.put("Japanske yen", "JPY");
            nameToCode.put("Schweiziske franc", "CHF");
            return nameToCode;
        }
    }
}
    
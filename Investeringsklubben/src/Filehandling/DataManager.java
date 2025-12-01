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

import javax.net.ssl.ManagerFactoryParameters;
import javax.xml.crypto.Data;
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
    private static List<Currency> currencies;
    private final CsvHandler csvHandler;

    DecimalFormat numberFormat = new DecimalFormat("#.00");



    // Initialiser lister for at undgå NullPointerException, selv hvis indlæsning fejler
    public DataManager()  {
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

            // 3. Link transaktioner til medlemmer
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

            csvHandler.writeCurrencies(CURRENCY_FILE, this.currencies);

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
     * @param stockTransaction
     */
    public void convertToDkk (Transaction stockTransaction)  {
        String danishkrone = "DKK";
        // Vi har brug for at få vores rater
        DataManager manager = new DataManager();
        List<Currency> listOfCurrenciesAndRates = manager.getCurrencies();

        // Så har vi brug for at gemme aktie kursen
        String stockCurrency = stockTransaction.getCurrency();
        double stockPricePrStock = stockTransaction.getPrice();
        double stockQuantity = stockTransaction.getQuantity();
        System.out.println(stockCurrency);

        if(!stockCurrency.equalsIgnoreCase("DKK")) {
            for (Currency currency : listOfCurrenciesAndRates){
                if (stockCurrency.equalsIgnoreCase(currency.getBaseCurr())) {
                    System.out.println("Found the currency in our list: " + currency);
                    System.out.println("Converting " + stockTransaction.getTicker() + " at the current price pr stock: " +
                            "" + stockPricePrStock + " " + stockCurrency + " to " + currency.getRate());

                    double totalStockPrice = stockPricePrStock * stockQuantity;
                    double totalPriceAfterConversion = totalStockPrice * currency.getRate();
                    System.out.println("Result of the conversion of " + stockQuantity + " " + stockCurrency + " to " + numberFormat.format(totalPriceAfterConversion) + danishkrone);


                }
            }

        }




    }

    // --- Getters til UI-laget ---
    public List<Member> getMembers() {
        return members;
    }

    public List<Stock> getStocks() {
        return stocks;
    }
    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void updateCurrencies() {
        CurrencyFetcher fetcher = new CurrencyFetcher();
        fetcher.fetchCurrencyData();

    }




    public static class CurrencyFetcher {
        private static final String API_URL =
                "https://www.nationalbanken.dk/api/currencyrates?format=rss&lang=da&isocodes=eur,usd,sek,nok,gbp,jpy,aud,cad";

        public String fetchCurrencyData() {
            // 1. Opret HttpClient instans (bruges ofte som Singleton/globalt)
            HttpClient client = HttpClient.newHttpClient();
            // 2. Opret HttpRequest objekt
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .GET()
                    .build();
            try {
                // 3. Send forespørgslen og modtag svaret synkront
                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString() // Venter at få svaret som en String (tekst/XML)
                );
                // 4. Tjek om forespørgslen var succesfuld (Statuskode 200)
                if (response.statusCode() == 200) {
                    return response.body();
                } else {
                    System.err.println("Fejl ved hentning. Statuskode: " + response.statusCode());
                    return null;
                }
            } catch (Exception e) {
                System.err.println("Exception under HTTP-forespørgsel: " + e.getMessage());
                return null;
            }
        }

        public List<Currency> parseCurrencyXml(String xmlData) {
            if (xmlData == null || xmlData.isEmpty()) return new ArrayList<>();

            try {
                // 1. Opret DocumentBuilder
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();

                // Fjern BOM (Byte Object Marker i XMLen)
                xmlData = xmlData.replace("\uFEFF", "").trim();

                // Konverter String til InputSource
                InputSource is = new InputSource(new StringReader(xmlData));

                // Opret DOM-dokumentet (træet)
                Document doc = builder.parse(is);


                // 2. Opret XPath instans
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();

                // 3. Brug XPath til at trække data ud
                return extractDataWithXPath(doc, xpath);

            } catch (Exception e) {
                System.err.println("Fejl under XML-parsing: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        private List<Currency> extractDataWithXPath(Document doc, XPath xpath)
                throws XPathExpressionException {

            DataManager manager = new DataManager();
            List<Currency> updatedCurrencies = new ArrayList<>();
            List<Currency> currencies = manager.getCurrencies();

            //Hashmap til at konvertere navn til kode
            Map<String, String> nameToCode = new HashMap<>();
            nameToCode.put("Amerikanske dollar", "USD");
            nameToCode.put("Euro", "EUR");
            nameToCode.put("Svenske kroner", "SEK");
            nameToCode.put("Australske dollar", "AUD");
            nameToCode.put("Norske kroner", "NOK");
            nameToCode.put("Canadiske dollar", "CAD");
            nameToCode.put("Britiske pund", "GBP");
            nameToCode.put("Japanske yen", "JPY");
            nameToCode.put("Schweiziske franc ", "CHF");

            // Tager navn og valuta
            Pattern pattern = Pattern.compile("^(.+)\\s*\\(([\\d,]+)\\)$");

            // XPath udtrykket: //item
            // Dette finder ALLE <item> elementer, uanset hvor de er i dokumentet.
            XPathExpression expr = xpath.compile("//item");

            // Resultatet er en NodeList af alle <item> noderne.
            NodeList itemNodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
            String firstDate = null;

            for (int i = 0; i < itemNodes.getLength(); i++) {
                org.w3c.dom.Node itemNode = itemNodes.item(i);

                // Læs pubDate
                String pubDate = xpath.compile("./pubDate")
                        .evaluate(itemNode, XPathConstants.STRING)
                        .toString()
                        .trim();

                if (firstDate == null)
                    firstDate = pubDate;
                // Hvis datoen ikke længere matcher → stop the loop
                if (!pubDate.equals(firstDate))
                    break;
                // Udtræk indholdet af <title>
                XPathExpression titleExpr = xpath.compile("./title");
                String titleString = (String) titleExpr.evaluate(itemNode, XPathConstants.STRING);
                if (titleString != null) {
                    Matcher matcher = pattern.matcher(titleString.trim());
                    if (matcher.find()) {
                        // 1. Extract navn (Matcher Group 1)
                        String rawName = matcher.group(1).trim();

                        // 2. Extract String (Matcher Group 2)
                        String rateString = matcher.group(2).trim();

                        // 3. Konverter navn til ISO-kode
                        String baseCurrency = nameToCode.get(rawName);

                        // 4. Rens strengen og konverter til double
                        double rawRateValue = Double.parseDouble(rateString.replace(',', '.'));

                        // Nationalbankens kurser er næsten altid for 100 enheder.
                        double unitAmount = 100.0;

                        // 5. Beregn kursen for 1 enhed (den faktiske rate)
                        double actualRate = rawRateValue / unitAmount;

                        if (baseCurrency != null) {
                            updatedCurrencies.add(new Currency(baseCurrency, "DKK", actualRate, LocalDate.now()));
                        } else {
                            System.err.println("Advarsel: Kunne ikke finde ISO-kode for: " + rawName);
                        }
                        for (Currency element : currencies) {
                            for (Currency updatedElement : updatedCurrencies) {
                                if (element.getBaseCurr().equalsIgnoreCase(updatedElement.getBaseCurr())) {
                                    element.setRate(updatedElement.getRate());
                                }
                            }
                       }
                        }
                }
            }

            return currencies;
        }


    }
    public static void main(String[] args) throws IOException {

    }}
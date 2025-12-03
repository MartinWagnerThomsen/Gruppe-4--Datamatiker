package Api;

import DataObjects.Currency;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyFetcher {
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

                    if (baseCurrency != null) {
                        try {
                            double rawRateValue = Double.parseDouble(rateString.replace(',', '.'));
                            double unitAmount = 100.0;
                            double actualRate = rawRateValue / unitAmount;
                            actualRate = Math.round(actualRate* 100.0) / 100.0; // nærmeste 2 decimaller
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

package DataObjects;

public class StockExchange {
    private String marketName;
    private double totalMarketValue;

    private static final StockExchange nasdaqCopenhagen = new StockExchange("Nasdaq Copenhagen", 1);
    private static final StockExchange nasdaqStockholm = new StockExchange("Nasdaq Stockholm", 2);
    private static final StockExchange nasdaqNewYork = new StockExchange("Nasdaq New York", 3);

    StockExchange(String marketName, double totalMarketValue){
        this.marketName = marketName;
        this.totalMarketValue = totalMarketValue;
    }

    // getters
    public String getMarketName(){return marketName;}
    public double getTotalMarketValue(){return totalMarketValue;}

    // setters
    public void setMarketName(String marketName){this.marketName = marketName;}
    public void setTotalMarketValue(double totalMarketValue){this.totalMarketValue = totalMarketValue;}

    public static StockExchange parseStockExchange(String part) {

        return switch (part.toLowerCase()){
            case "nasdaq copenhagen" -> nasdaqCopenhagen;
            case "nasdaq stockholm" -> nasdaqStockholm;
            case "nasdaq new york" -> nasdaqNewYork;
            default -> nasdaqCopenhagen;
        };
    }

    @Override
    public String toString() {
        return "StockExchange{" +
                "marketName='" + marketName + '\'' +
                ", totalMarketValue=" + totalMarketValue +
                '}';
    }
}

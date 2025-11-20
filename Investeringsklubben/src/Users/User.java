package Users;

public abstract User {
    /**
     * Mandatory attributes for Users
     */
    private int userId;
    private String email;
    private Date birthday;
    private double initialCash;
    private Date createdAt;
    private Date lastUpdated;
    private Portfolio portfolio;
    private String fullName;

    /**
     * Mandatory methods for the user
     */
    public void viewPortfolio();
    public void viewTransactionHistory();
    public void createUser();

    /**
     * Getters & Setters for the user class
     */
    public int getUserId();
    public String getEmail();
    public Date getBirthday();
    public Date getCreationDate();
    public Date getLastUpdated();
    public Portfolio getPortfolio();
    public String getFullName();




}

package UI;

public class MenuItem {
    private String description;
    private MenuAction action;


    public MenuItem (String description, MenuAction action) {
        this.description = description;
        this.action = action;
    }
    public String getDescription() {
        return this.description;
    }

    public MenuAction getAction() {
        return this.action;
    }
}

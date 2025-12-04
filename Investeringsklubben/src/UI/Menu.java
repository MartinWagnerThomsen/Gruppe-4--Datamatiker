package UI;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private final String title;
    private final List<MenuItem> items;
    private final Scanner scanner;
    private boolean shouldExit = false;


    public Menu(String title, List<MenuItem> items, Scanner scanner) {
        this.title = title;
        this.items = items;
        this.scanner = scanner;
    }


    public void run() {
        shouldExit = false;
        while (!shouldExit) {
            printMenu();
            executeChosenAction();
        }
    }

    private void printMenu() {
        System.out.println("\n" + title);
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + items.get(i).getDescription());
        }
        System.out.println(" 0. Luk program");
        System.out.println("=============================================");
    }

    private void executeChosenAction() {
        System.out.print("Vælg handling: ");
        String input = scanner.nextLine();
        try {
            int choice = Integer.parseInt(input);

            if (choice == 0) {
                stop();
                return;
            }
            if (choice > 0 && choice <= items.size()) {
                items.get(choice - 1).getAction().execute();
            } else {
                System.out.println("Forkert valg. Prøv igen.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Forkert input. Indtast et tal.");
        }
    }

    public void stop() {
        this.shouldExit = true;
    }
}


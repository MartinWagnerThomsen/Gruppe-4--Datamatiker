---
title: Mulig implementering af en simpel While Loop Menu
tags:
  - gemini-review
  - java
  - ui-example
  - code-example
---

# Mulig implementering af en simpel While Loop Menu

Her er et forslag til, hvordan I kan bygge en simpel, hardcoded menu i jeres `main`-metode. Denne menu bruger `DataManager` til at indlæse og tilgå data, og den giver et funktionelt skelet, I kan bygge videre på.

**Fil:** `Club.java` (eller en ny `Main.java` i en `ui`-pakke)

```java
// Anbefalet placering: dk.investeringsklubben.ui.Main.java

import dk.investeringsklubben.data.DataManager; // Importer jeres nye DataManager
import dk.investeringsklubben.model.Member;     // Importer jeres model-klasser
import dk.investeringsklubben.model.Stock;

import java.util.Scanner;

public class Club { // Eller Main

    // Gør DataManager og Scanner tilgængelige i hele klassen
    private static DataManager dataManager;

    public static void main(String[] args) {
        // 1. Initialiser DataManager og indlæs al data ved opstart
        dataManager = new DataManager();
        dataManager.loadAllData();

        // Tjek om data blev indlæst korrekt. Hvis member-listen er tom, er der nok sket en fejl.
        if (dataManager.getMembers().isEmpty()) {
            System.out.println("Ingen data kunne indlæses. Programmet afsluttes.");
            return; // Afslut programmet hvis ingen data kan læses
        }

        // 2. Start hovedmenuen
        showMainMenu();
    }

    private static void showMainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\n--- Hovedmenu for Investeringsklubben ---");
            System.out.println("1. Vis alle medlemmer");
            System.out.println("2. Vis alle tilgængelige aktier");
            System.out.println("3. Login som specifik bruger (ikke implementeret)");
            System.out.println("4. Afslut program");
            System.out.print("Vælg en mulighed: ");

            String input = scanner.nextLine();

            switch (input) {
                case "1":
                    displayAllMembers();
                    break;
                case "2":
                    displayAllStocks();
                    break;
                case "3":
                    System.out.println("Denne funktionalitet er ikke implementeret endnu.");
                    break;
                case "4":
                    keepRunning = false; // Stopper while-loopet
                    break;
                default:
                    System.out.println("Ugyldigt valg. Prøv igen.");
                    break;
            }
        }
        scanner.close();
        System.out.println("Programmet er afsluttet. Farvel!");
    }

    /**
     * Henter alle medlemmer fra DataManager og udskriver deres navne.
     */
    private static void displayAllMembers() {
        List<Member> members = dataManager.getMembers();
        System.out.println("\n--- Alle Medlemmer ---");
        if (members.isEmpty()) {
            System.out.println("Der er ingen medlemmer i systemet.");
        } else {
            for (Member member : members) {
                // Bruger den toString() metode I allerede har i Member-klassen
                System.out.println(member.toString());
            }
        }
    }

    /**
     * Henter alle aktier fra DataManager og udskriver dem.
     */
    private static void displayAllStocks() {
        List<Stock> stocks = dataManager.getStocks();
        System.out.println("\n--- Tilgængelige Aktier på Markedet ---");
        if (stocks.isEmpty()) {
            System.out.println("Der er ingen aktier i systemet.");
        } else {
            for (Stock stock : stocks) {
                 // Bruger den toString() metode I allerede har i Stock-klassen
                System.out.println(stock.toString());
            }
        }
    }
}
```

### Hvordan I kommer videre herfra:

1.  **Login-system:**
    *   I `showMainMenu`, når brugeren vælger "3", kan I spørge om et `userId`.
    *   Find det pågældende `Member`-objekt i `dataManager.getMembers()`.
    *   Hvis medlemmet findes, kan I kalde en ny metode, `showMemberMenu(Member loggedInMember)`, som viser en menu med medlems-specifikke valg (f.eks. "Vis min portefølje", "Køb aktie").

2.  **Køb af Aktie:**
    *   Fra `showMemberMenu`, hvis brugeren vil købe, kan I:
        1.  Spørge om `ticker` og `antal`.
        2.  Finde den korrekte `Stock` i `dataManager.getStocks()`.
        3.  Beregne prisen og tjekke om medlemmet har nok penge.
        4.  Oprette et nyt `Transaction`-objekt med de korrekte data.
        5.  Kalde `dataManager.registerNewTransaction(newTransaction)`.
    *   `DataManager` vil herefter sørge for at opdatere hukommelsen OG skrive til filerne. Næste gang I viser porteføljen, vil den nye aktie være med.

Dette simple loop giver jer en funktionel base at bygge alle de krævede features ovenpå.
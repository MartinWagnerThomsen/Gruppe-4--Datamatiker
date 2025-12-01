---
title: Sprint Review og Vejledning - Investeringsklubben
tags:
  - gemini-review
  - projektvejledning
  - sprint-review
---

# Sprint Review og Vejledning - Investeringsklubben

Hej gruppe,

Her er mit review af jeres projekt "Investeringsklubben" med en uge til aflevering. Formålet er at give jer et klart overblik over jeres nuværende status og en konkret handlingsplan for at nå i mål med alle kravene.

## Overordnet Status

I har etableret en grundlæggende projektstruktur og har en velfungerende datalæsning fra de påkrævede CSV-filer. Datamodellerne for brugere, aktier og transaktioner er på plads. Dog mangler I den centrale applikationslogik, brugergrænsefladen, og muligheden for at gemme data. Projektet er i en tilstand, hvor fundamentet er lagt, men selve "huset" mangler at blive bygget.

## Hvad der virker godt (Styrker)

*   **Projektstruktur:** I har en logisk opdeling af klasser i pakker (`DataObjects`, `Users`, `Filehandling`), hvilket er en god start for et vedligeholdelsesvenligt system.
*   **Data-læsning:** `CsvHandler` er i stand til at læse og parse data fra `users.csv`, `stockMarket.csv` og `transactions.csv`, inklusiv håndtering af dato- og talformater.
*   **Datamodellering:** Klasser som `Member`, `Stock`, og `Transaction` er fornuftigt opbygget og afspejler de data, de skal repræsentere.

## Områder til Forbedring (Svagheder og mangler)

*   **Kritisk - Manglende kerne-logik:** Der er ingen funktionel brugergrænseflade eller applikations-loop. `Club.java` er i sin nuværende form ikke funktionel og kan ikke håndtere bruger-login eller menuvalg.
*   **Kritisk - Data kan ikke gemmes:** `writeFile` metoden i `CsvHandler` er ikke implementeret. Uden denne kan systemet ikke opdatere `transactions.csv` eller `users.csv`, hvilket er et kernekrav.
*   **Beregninger mangler:** Der er ingen logik til at beregne porteføljeværdi, gevinst/tab, eller den rangliste, som lederen skal bruge.
*   **Dårlig praksis i `CsvHandler`:** Klassen benytter `static` lister til at holde data. Dette fører til ineffektiv datalæsning (filerne læses igen og igen) og kan skabe svære at finde fejl. Ansvaret for datahåndtering bør adskilles fra fil-læsning.
*   **Facade-laget er tomt:** `InvestmentClubFacade` er tænkt som et bindeled, men indeholder i praksis ingen logik endnu.
*   **Unit Tests mangler:** Et krav i opgaven er "flere unit tests", og disse er endnu ikke påbegyndt. Tests er essentielle for at validere jeres beregninger.

## Konkrete Næste Skridt (Handlingsplan for den sidste uge)

Følg denne prioriterede liste for at sikre, I når i mål.

---

### **Prioritet 1: Få et funktionelt skelet (Skal løses hurtigst muligt)**

1.  **Refaktorér `CsvHandler`:**
    *   Fjern alle `static` lister (`userList`, `listOfStocks` etc.).
    *   Opret en ny klasse, f.eks. `DataManager`, som har ansvaret for at holde på data. Denne klasse kan have en metode som `loadAllData()`, der kaldes én gang ved opstart. `CsvHandler` skal kun indeholde logik til at læse/skrive filer og returnere data.

2.  **Implementér `writeFile`:**
    *   Implementer en metode i `CsvHandler`, der kan tilføje en ny linje til en CSV-fil. Start med at kunne gemme en ny transaktion til `transactions.csv`.

3.  **Byg en simpel UI-loop:**
    *   Omskriv `Club.java` (eller lav en ny `UI` klasse).
    *   Lav en `while`-løkke, der præsenterer en simpel menu (f.eks. "1. Login som Medlem", "2. Login som Leder").
    *   Baseret på valget, vis den relevante menu for rollen (start bare med hardcodede brugere). Få menuen til at reagere på input, selvom funktionerne bag ikke er implementeret endnu.

---

### **Prioritet 2: Implementer Medlems-funktionalitet (Kerne-features)**

4.  **Registrer en Transaktion:**
    *   Implementer funktionen, hvor et medlem kan "købe" en aktie.
    *   Dette skal:
        *   Oprette et nyt `Transaction` objekt.
        *   Opdatere medlemmets kontantbeholdning (`cash`).
        *   Kalde `writeFile` for at gemme den nye transaktion i `transactions.csv`.

5.  **Vis Portefølje:**
    *   Implementer `viewPortfolio` for en `Member`.
    *   Metoden skal iterere over medlemmets transaktioner for at beregne, hvilke aktier de ejer og i hvilket antal.
    *   Vis en liste af disse aktier sammen med antal. Næste skridt er at tilføje værdi.

---

### **Prioritet 3: Leder-funktionalitet og Beregninger**

6.  **Beregn Porteføljeværdi:**
    *   Skriv en metode (f.eks. i `Portfolio` eller `Member` klassen), der kan beregne den samlede værdi af en portefølje i DKK.
    *   Dette kræver, at I slår den nuværende kurs op for hver aktie i porteføljen (fra de indlæste `stockMarket` data) og ganger med antallet.
    *   Husk at implementere **valutaomregning** (`currency.csv`) for udenlandske aktier, som er et "EKSTRA" krav, men som vil imponere.

7.  **Implementer Rangliste:**
    *   Når porteføljeværdien kan beregnes for alle medlemmer, kan I implementere lederens rangliste.
    *   Brug en `Comparator` til at sortere listen af medlemmer baseret på deres samlede porteføljeværdi.

---

### **Prioritet 4: Kvalitetssikring og Aflevering**

8.  **Skriv Unit Tests:**
    *   Opret test-filer for jeres vigtigste klasser.
    *   Skriv tests, der verificerer jeres beregninger: porteføljeværdi, gevinst/tab, og valutaomregning. Dette fanger fejl og beviser, at jeres logik er korrekt.

9.  **Udarbejd Systemudviklingsdokumentation:**
    *   **Klassediagram:** Generer et klassediagram fra jeres kode i IntelliJ. Det giver et godt overblik.
    *   **Use Cases:** Skriv de vigtigste Use Cases ned (f.eks. "Registrer aktiekøb", "Vis rangliste").
    *   **Sekvensdiagrammer:** Tegn sekvensdiagrammer for 2-3 af de mest centrale Use Cases for at vise, hvordan jeres objekter interagerer.

## Forberedelse til Demo

Til jeres sprint review skal I fokusere på at demonstrere **ét fuldt gennemløb**. Det bedste vil være:

1.  Start programmet.
2.  Login som et specifikt medlem.
3.  Vis medlemmets (i starten tomme eller simple) portefølje.
4.  Gennemfør et køb af en aktie.
5.  Vis, at den nye transaktion er gemt i `transactions.csv`.
6.  Vis den opdaterede portefølje, hvor den nye aktie nu indgår, og medlemmets kontantbeholdning er reduceret.

Hvis I kan vise dette, demonstrerer I, at jeres kerne-loop virker, at I kan håndtere data, og at I kan persistere (gemme) ændringer.

## Afsluttende Bemærkninger

I har en travl uge foran jer, men opgaven er absolut overkommelig, hvis I arbejder struktureret og følger den prioriterede plan. Fokuser på at få kernefunktionaliteten på plads først, og byg derefter ovenpå.

Held og lykke med sprinten!
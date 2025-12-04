package Client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Scanner;

public class Parser {
    private static final Scanner scanner = new Scanner(System.in);

    public static String parseString() {
        return scanner.nextLine().trim();
    }

    /**
     * Indlæser wrapper klassen Int og spytter enten en int eller ingenting ud
     * @return
     */
    public static Optional<Integer> parseInt() {
        try {
            return Optional.of(Integer.parseInt(scanner.nextLine().trim()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }


    /**
     * Indlæser LocalDate og returner enten ingenting eller LocalDate hvis det er formateret korrekt
     * @return
     */
    public static Optional<LocalDate> parseDate() {
        try {
            String dateStr = parseString();
            return Optional.of(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }
}

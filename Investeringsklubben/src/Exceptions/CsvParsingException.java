package Exceptions;

/**
 * En custom exception, der kastes, når der sker en fejl under parsing af en CSV-fil.
 * Dette kan skyldes I/O fejl eller formatfejl i dataen.
 */
public class CsvParsingException extends Exception {
    public CsvParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
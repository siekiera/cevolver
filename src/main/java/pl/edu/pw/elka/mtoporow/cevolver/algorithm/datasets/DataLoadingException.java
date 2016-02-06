package pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets;

/**
 * Wyjątek podczas ładowania danych
 * Data utworzenia: 06.02.16, 12:12
 *
 * @author Michał Toporowski
 */
public class DataLoadingException extends RuntimeException {

    public DataLoadingException(String message) {
        super(message);
    }

    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataLoadingException(Throwable cause) {
        super(cause);
    }
}

package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

/**
 * Typ modelu
 * Data utworzenia: 29.02.16, 11:45
 *
 * @author Michał Toporowski
 */
public enum ModelType {
    /**
     * Model z reprezentacją nieciągłości jako bardzo krótkich mikropasków (dla N nieciągłości 2N+1 elementów)
     */
    SHORTBREAK,
    /**
     * Model z reprezentacją nieciągłości jako zmiany szerokości mikropaska (dla N nieciągłości N+1 elementów)
     */
    LONGBREAK
}

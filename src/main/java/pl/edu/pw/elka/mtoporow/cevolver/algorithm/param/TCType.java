package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

/**
 * Kryterium zakończenia algorytmu
 * Data utworzenia: 27.05.15, 16:01
 *
 * @author Michał Toporowski
 */
public enum TCType implements AlgorithmPartType {
    /**
     * Przekroczenie określonej liczby pokoleń
     */
    GENERATION_COUNT,
    /**
     * Brak poprawy funkcji celu w ciągu określonej liczbie pokoleń
     */
    STAGNATION,
    /**
     * Uzyskanie określonej wartości funkcji celu
     */
    TARGET_FITNESS
}

package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

/**
 * Typ operatora ewolucyjnego
 * Data utworzenia: 27.05.15, 16:00
 *
 * @author Michał Toporowski
 */
public enum EOType implements AlgorithmPartType {
    /**
     * Mutacja polegająca na dodawaniu wartości losowej
     */
    SIMPLE_MUTATION,
    /**
     * Krzyżowanie polegające na wymianie parametrów
     */
    DIST_ARRAY_CROSSOVER,
    /**
     * Inwersja
     */
    INVERSION,
    /**
     * Mutacja polegająca na przemnożeniu wartości przez Gaussian
     */
    STANDARD_GAUSSIAN_MUTATION,
    /**
     * Krzyżowanie uśredniające
     */
    AVERAGE_VALUE_CROSSOVER,
    /**
     * Krzyżowanie uśredniające po parametrach
     */
    PARAMETER_AVG_VAL_CROSSOVER
}

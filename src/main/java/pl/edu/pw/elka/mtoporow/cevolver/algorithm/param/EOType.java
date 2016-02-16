package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

/**
 * Typ operatora ewolucyjnego
 * Data utworzenia: 27.05.15, 16:00
 *
 * @author Michał Toporowski
 */
public enum EOType implements AlgorithmPartType {
    SIMPLE_MUTATION,
    DIST_ARRAY_CROSSOVER,
    INVERSION,
    STANDARD_GAUSSIAN_MUTATION,
    AVERAGE_VALUE_CROSSOVER,
    PARAMETER_AVG_VAL_CROSSOVER
}

package pl.edu.pw.elka.mtoporow.cevolver.algorithm.param;

/**
 * Typ strategii selekcji, bądź strategii ewolucyjnej
 * Data utworzenia: 27.05.15, 16:00
 *
 * @author Michał Toporowski
 */
public enum SSType implements AlgorithmPartType {
    /**
     * Selekcja rankingowa
     */
    RANK,
    /**
     * Uniwersalne próbkowanie stochastyczne
     */
    SUS,
    /**
     * Selekcja ruletkowa
     */
    ROULETTE_WHEEL,
    /**
     * Selekcja turniejowa
     */
    TOURNAMENT,
    /**
     * Strategia ewolucyjna (μ+λ)
     */
    ES_PLUS,
    /**
     * Strategia ewolucyjna (μ,λ)
     */
    ES_COMMA
}

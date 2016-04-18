package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import org.uncommons.maths.random.Probability;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;

import java.util.Random;

/**
 * Mutacja samo-adoptująca stworzona na podstawie artykułu:
 * Evolution strategies. A comprehensive introduction. (Hans-Georg Beyer, Hans-Paul Schwefel)
 * <p>
 * Data utworzenia: 17.02.16, 15:27
 *
 * @author Michał Toporowski
 */
public class SelfAdaptatingMutation extends BaseMutation {
    private static final double C = 1.0;
    private static final double DEFAULT_INITIAL_DEVIATION = 0.1;
    private final double tau0;
    private final double tau;

    public SelfAdaptatingMutation(final int breakCount, final Probability probability) {
        super(probability);
        tau0 = C / Math.sqrt(2 * breakCount);
        tau = C / Math.sqrt(2 * Math.sqrt(breakCount));
    }

    @Override
    protected AbstractCanalModel mutate(final AbstractCanalModel candidate, final Random rng) {
        RealVector deviations = candidate.algorithmTempVector();
        if (deviations == null) {
            // Jeśli jeszcze nie ma odchyleń, bierzemy jakieś losowe proporcjonalne do odległości
            deviations = candidate.distances().distances().map(d -> (d * DEFAULT_INITIAL_DEVIATION * rng.nextGaussian()));
        }
        // Mutacja odchyleń standardowych
        RealVector mutatedDeviations = deviations.map(s -> s * expNormal(tau0, rng) * expNormal(tau, rng));
        // Właściwa mutacja osobnika
        RealVector newDists = candidate.distances().distances().add(mutatedDeviations);
        return candidate.createNew(newDists).withTempVector(mutatedDeviations);
    }

    /**
     * Zwraca exp(tau * N(0,1))
     *
     * @param tau tau
     * @param rng rng
     * @return wynik działania
     */
    private double expNormal(final double tau, final Random rng) {
        return Math.exp(tau * rng.nextGaussian());
    }
}

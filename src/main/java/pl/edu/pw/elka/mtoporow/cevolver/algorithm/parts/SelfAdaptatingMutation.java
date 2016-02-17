package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.uncommons.maths.random.Probability;
import pl.edu.pw.elka.mtoporow.cevolver.algorithm.datasets.DataHolder;
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
    /* To jest inicjalizowane raz na uruchomienie algorytmu */
    private final int n = DataHolder.getCurrent().measurementParams().getDiscontinuitiesCount();
    private final double tau0 = C / Math.sqrt(2 * n);
    private final double tau = C / Math.sqrt(2 * Math.sqrt(n));
    private final double randomCoeffScalar = 0.01 * DataHolder.getCurrent().measurementParams().getTotalLength();
    private final RealVector initialDeviations = new ArrayRealVector(n, randomCoeffScalar);

    public SelfAdaptatingMutation(Probability probability) {
        super(probability);
    }

    @Override
    protected AbstractCanalModel mutate(final AbstractCanalModel candidate, final Random rng) {
        RealVector deviations = candidate.algorithmTempVector();
        if (deviations == null) {
            deviations = initialDeviations;
        }
        // Mutacja odchyleń standardowych
        RealVector mutatedDeviations = deviations.map(s -> s * expNormal(tau0, rng) * expNormal(tau, rng));
        candidate.algorithmTempVector_$eq(mutatedDeviations);
        // Właściwa mutacja osobnika
        RealVector newDists = candidate.distances().distances().add(mutatedDeviations);
        return candidate.createNew(newDists);
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

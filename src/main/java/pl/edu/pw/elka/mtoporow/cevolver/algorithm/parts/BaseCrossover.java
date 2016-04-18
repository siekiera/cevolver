package pl.edu.pw.elka.mtoporow.cevolver.algorithm.parts;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.util.Pair;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;
import pl.edu.pw.elka.mtoporow.cevolver.lib.model.AbstractCanalModel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Klasa bazowa dla operatora krzyżowania
 * Data utworzenia: 09.01.16, 16:48
 *
 * @author Michał Toporowski
 */
abstract class BaseCrossover extends AbstractCrossover<AbstractCanalModel> {

    public BaseCrossover(final Probability crossoverProbability) {
        super(1, crossoverProbability);
    }

    @Override
    protected List<AbstractCanalModel> mate(AbstractCanalModel parent1, AbstractCanalModel parent2, int numberOfCrossoverPoints, Random rng) {
        final Pair<RealVector, RealVector> childVectors = mate(parent1.distances().distances(), parent2.distances().distances(), numberOfCrossoverPoints, rng);
        return Arrays.asList(parent1.createNew(childVectors.getFirst()), parent2.createNew(childVectors.getSecond()));
    }

    /**
     * Krzyżuje wektory dwóch osobników
     *
     * @param parent1                 wektor osobnika 1
     * @param parent2                 wektor osobnika 2
     * @param numberOfCrossoverPoints liczba p. krzyżowania
     * @param rng                     rng
     * @return para wektoróœ dzieci
     */
    protected abstract Pair<RealVector, RealVector> mate(RealVector parent1, RealVector parent2, int numberOfCrossoverPoints, Random rng);

}
